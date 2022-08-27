package com.mobizion.camera

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.display.DisplayManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.mobizion.base.extension.enabled
import com.mobizion.base.extension.visible
import com.mobizion.base.fragment.BaseFragment
import com.mobizion.camera.databinding.FragmentCameraBinding
import org.koin.android.ext.android.inject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import androidx.window.layout.WindowMetricsCalculator
import com.mobizion.camera.abstract.CameraRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.androidx.viewmodel.ext.android.viewModel

class CameraFragment : BaseFragment<FragmentCameraBinding>(FragmentCameraBinding::inflate) {

    private var lensFacing: Int = CameraSelector.LENS_FACING_FRONT
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    //    private lateinit var windowManager: WindowManager
    private var displayId: Int = -1
    private lateinit var broadcastManager: LocalBroadcastManager
    private lateinit var cameraSelector:CameraSelector
    private var flashMode = false
    var isSubmitted = false
    private val imageCaptureDoneViewModel: CameraViewModel by viewModel()
    private val displayManager by lazy {
        requireContext().getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }

    /** Blocking camera operations are performed using this executor */
    private lateinit var cameraExecutor: ExecutorService

    /** Volume down button receiver used to trigger shutter */
    private val volumeDownReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getIntExtra(KEY_EVENT_EXTRA, KeyEvent.KEYCODE_UNKNOWN)) {
                // When the volume down button is pressed, simulate a shutter button click
                KeyEvent.KEYCODE_VOLUME_DOWN -> {
//                    binding.btnCameraCapture.simulateClick()
                }
            }
        }
    }

    /**
     * We need a display listener for orientation changes that do not trigger a configuration
     * change, for example if we choose to override config change in manifest or for 180-degree
     * orientation changes.
     */
    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) = Unit
        override fun onDisplayRemoved(displayId: Int) = Unit
        override fun onDisplayChanged(displayId: Int) = view?.let { view ->
            if (displayId == this@CameraFragment.displayId) {
                Log.d(TAG, "Rotation changed: ${view.display.rotation}")
                imageCapture?.targetRotation = view.display.rotation
            }
        } ?: Unit
    }


    override fun onViewCreated() {

        // Initialize our background executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        broadcastManager = LocalBroadcastManager.getInstance(requireContext())

        // Set up the intent filter that will receive events from our main activity
        val filter = IntentFilter().apply { addAction(KEY_EVENT_ACTION) }
        broadcastManager.registerReceiver(volumeDownReceiver, filter)

        // Every time the orientation of device changes, update rotation for use cases
        displayManager.registerDisplayListener(displayListener, null)

        //Initialize WindowManager to retrieve display metrics
//        windowManager = WindowManager(view.context)

        // Wait for the views to be properly laid out
        binding.cameraPreview.post {

            // Keep track of the display in which this view is attached
            displayId = binding.cameraPreview.display.displayId

            // Build UI controls
            updateCameraUi()

            // Set up the camera and its use cases
            setUpCamera()
        }



    }
    //flash light function
    private fun flashOnButton(mood:Boolean) {
        if (camera != null) {
            try {
                val cameraControl = camera!!.cameraControl
                flashMode=mood
                if (mood) {
                    cameraControl.enableTorch(true) // enable torch
                    binding.btnFlashLight.setImageResource(R.drawable.ic_flash_on_inner)
                } else {
                    cameraControl.enableTorch(false) // disbale torch
                    binding.btnFlashLight.setImageResource(R.drawable.ic_flash_off)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Inflate camera controls and update the UI manually upon config changes to avoid removing
     * and re-adding the view finder from the view hierarchy; this provides a seamless rotation
     * transition on devices that support it.
     *
     * NOTE: The flag is supported starting in Android 8 but there still is a small flash on the
     * screen for devices that run Android 9 or below.
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Rebind the camera with the updated display metrics
        bindCameraUseCases()

        // Enable or disable switching between cameras
        updateCameraSwitchButton()
    }

    /** Initialize CameraX, and prepare to bind the camera use cases  */
    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({

            // CameraProvider
            cameraProvider = cameraProviderFuture.get()

            // Select lensFacing depending on the available cameras
            lensFacing = when {
                hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                else -> throw IllegalStateException("Back and front camera are unavailable")
            }

            // Enable or disable switching between cameras
            updateCameraSwitchButton()

            // Build and bind the camera use cases
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    /** Declare and bind preview, capture and analysis use cases */
    private fun bindCameraUseCases() {

        // Get screen metrics used to setup camera for full screen resolution
        val metrics = WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(requireActivity()).bounds
        Log.d(TAG, "Screen metrics: ${metrics.width()} x ${metrics.height()}")

        val screenAspectRatio = aspectRatio(metrics.width(), metrics.height())
        Log.d(TAG, "Preview aspect ratio: $screenAspectRatio")

        val rotation = binding.cameraPreview.display.rotation

        // CameraProvider
        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")

        // CameraSelector
        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        // Preview
        preview = Preview.Builder()
            // We request aspect ratio but no resolution
            .setTargetAspectRatio(screenAspectRatio)
            // Set initial target rotation
            .setTargetRotation(rotation)
            .build()

        // ImageCapture
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            // We request aspect ratio but no resolution to match preview config, but letting
            // CameraX optimize for whatever specific resolution best fits our use cases
            .setTargetAspectRatio(screenAspectRatio)
            // Set initial target rotation, we will have to call this again if rotation changes
            // during the lifecycle of this use case
            .setTargetRotation(rotation)
            .build()
        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()

        try {
            // A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture)

            // Attach the viewfinder's surface provider to preview use case
            preview?.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    /** Enabled or disabled a button to switch cameras depending on the available cameras */
    private fun updateCameraSwitchButton() {
        try {
            binding.btnSwitchCamera.isEnabled = hasBackCamera() && hasFrontCamera()
        } catch (exception: CameraInfoUnavailableException) {
            binding.btnSwitchCamera.isEnabled = false
        }
    }

    /** Method used to re-draw the camera UI controls, called every time configuration changes. */
    private fun updateCameraUi() {

        // Remove previous UI if any
        binding.root.let {
            binding.root.removeView(it)
        }

        binding = FragmentCameraBinding.inflate(
            LayoutInflater.from(requireContext()),
            binding.root,
            true
        )
        //flash light on
        binding.btnFlashLight.setOnClickListener {
            flashOnButton(!flashMode)
        }

//         Listener for button used to capture photo
        binding.btnCameraCapture.setOnClickListener {
            // fahad agr camera on ha tu usy band krna ha
            flashLightOffCameraOn()
            //finish work light
            binding.btnCameraCapture.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_shutter_pressed))
            cameraProvider?.unbind(preview)
            // Get a stable reference of the modifiable image capture use case
            imageCapture?.let { imageCapture ->

                // Create output file to hold the image
                val photoFile = createFile(File(requireContext().getDir(Environment.DIRECTORY_DOCUMENTS,Context.MODE_PRIVATE).absolutePath))

                // Setup image capture metadata
                val metadata = ImageCapture.Metadata().apply {

                    // Mirror image when using the front camera
                    isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
                }

                // Create output options object which contains file + metadata
                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
                    .setMetadata(metadata)
                    .build()

                // Setup image capture listener which is triggered after photo has been taken
                imageCapture.takePicture(
                    outputOptions, cameraExecutor, object : ImageCapture.OnImageSavedCallback {
                        override fun onError(exc: ImageCaptureException) {
                            Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                        }

                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                            Log.d(TAG, "Photo capture succeeded: $savedUri")
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                // Update the gallery thumbnail with latest picture taken
                                setGalleryThumbnail(photoFile)
                            }
                            // Implicit broadcasts will be ignored for devices running API level >= 24
                            // so if you only target API level 24+ you can remove this statement
//                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//                                requireActivity().sendBroadcast(
//                                    Intent(android.hardware.Camera.ACTION_NEW_PICTURE, savedUri)
//                                )
//                            }

//                            // If the folder selected is an external media directory, this is
//                            // unnecessary but otherwise other apps will not be able to access our
//                            // images unless we scan them using [MediaScannerConnection]
//                            val mimeType = MimeTypeMap.getSingleton()
//                                .getMimeTypeFromExtension(savedUri.toFile().extension)
//                            MediaScannerConnection.scanFile(
//                                context,
//                                arrayOf(savedUri.toFile().absolutePath),
//                                arrayOf(mimeType)
//                            ) { _, uri ->
//                                Log.d(TAG, "Image capture scanned into media store: $uri")
//                            }
                        }
                    })

                // We can only change the foreground Drawable using API level 23+ API
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    // Display flash animation to indicate that photo was captured
                    binding.root.postDelayed({
                        binding.root.foreground = ColorDrawable(Color.WHITE)
                        binding.root.postDelayed(
                            { binding.root.foreground = null }, 100)
                    }, 200)
                }
            }
        }

        // Setup for button used to switch cameras
        binding.btnSwitchCamera.let {

            // Disable the button until the camera is set up
            it.isEnabled = false

            // Listener for button used to switch cameras. Only called if the button is enabled
            it.setOnClickListener {
                flashLightOffCameraOn()
                lensFacing = if (CameraSelector.LENS_FACING_FRONT == lensFacing) {
                    binding.btnFlashLight.enabled(true)
                    CameraSelector.LENS_FACING_BACK
                } else {
                    binding.btnFlashLight.enabled(false)
                    CameraSelector.LENS_FACING_FRONT
                }
                // Re-bind use cases to update selected camera
                bindCameraUseCases()
            }
        }
        binding.btnBack.setOnClickListener {
            backPressed(it)
        }

    }
    private  fun flashLightOffCameraOn(){
        // fahad agr camera on ha tu usy band krna ha
        val cameraControl = camera!!.cameraControl
        if (flashMode) {
            flashMode=false
            cameraControl.enableTorch(false) // disbale torch
            binding.btnFlashLight.setImageResource(R.drawable.ic_flash_off)
        }
        //finish work light
    }
    private fun setGalleryThumbnail(file: File) {
        // Run the operations in the view's thread
        binding.btnCameraCapture.post {
            binding.llCamera.visible(false)
            binding.llPhoto.visible(true)
            binding.btnCameraCapture.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_shutter_normal))
            Glide.with(requireActivity()).asBitmap().load(file.path).into(binding.previewImage)
            binding.txtCancel.setOnClickListener {
                cameraProvider?.bindToLifecycle(this,cameraSelector,preview)
                binding.llPhoto.visible(false)
                binding.llCamera.visible(true)
            }

        }
        binding.txtSend.setOnClickListener {
            imageCaptureDoneViewModel.capturedImageUri(Uri.fromFile(file))
            requireActivity().finish()
        }
    }

    /** Returns true if the device has an available back camera. False otherwise */
    private fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    /** Returns true if the device has an available front camera. False otherwise */
    private fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }

    companion object {

        const val TAG = "CameraXBasic"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val KEY_EVENT_EXTRA = "Extra_Event_Key"
        const val KEY_EVENT_ACTION = "Key_Event_Action"
        private const val PHOTO_EXTENSION = ".jpeg"
        const val RATIO_4_3_VALUE = 4.0 / 3.0
        const val RATIO_16_9_VALUE = 16.0 / 9.0

        /** Helper function used to create a timestamped file */
        private fun createFile(baseFolder: File) =
            File(baseFolder, "IMG_${SimpleDateFormat(FILENAME, Locale.US)
                .format(System.currentTimeMillis())}" + PHOTO_EXTENSION)
    }

    override fun backPressed(view: View) {
        requireActivity().finish()
    }

    override fun enableBackPress() = true
}