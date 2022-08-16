/**
 * created by tahir baig
 * 3 march 2022
 */
package com.mobizion.base.activity

import android.Manifest
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding
import com.mobizion.base.view.model.PermissionsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseActivity<B : ViewBinding>(val bindingFactory: (LayoutInflater) -> B) :
    AppCompatActivity() {

    val permissionViewModel by viewModel<PermissionsViewModel>()

    private val contactPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { granted ->
            permissionViewModel.setContactPermissionStatus(granted[Manifest.permission.READ_CONTACTS] == true && granted[Manifest.permission.WRITE_CONTACTS] == true)
        }

    val requestMultiplePermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { granted ->
        permissionViewModel.setMultiplePermissionStatus(granted)
    }

    val launchActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        permissionViewModel.setActivityResult(result)
    }

    private val cameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            permissionViewModel.setCameraPermissionStatus(granted)
        }

    private val storagePermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { granted ->
            permissionViewModel.setStoragePermissionStatus(granted[Manifest.permission.READ_EXTERNAL_STORAGE] == true && granted[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true)
        }

    private val microphoneAccessPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            permissionViewModel.setMicrophonePermissionStatus(granted)
        }

    private val locationAccessPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { granted ->
            permissionViewModel.setLocationPermissionStatus(granted)
        }

    val binding: B by lazy { bindingFactory(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if(shouldHideStatusBar()){
                window.decorView.windowInsetsController!!.hide(
                    WindowInsets.Type.statusBars()
                )
            }
        }else{
            if(shouldHideStatusBar()){
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        }
        setContentView(binding.root)
        initViews()
    }

    abstract fun shouldHideKeyboard(): Boolean

    abstract fun initViews()

    open fun shouldHideStatusBar():Boolean{
        return false
    }


    fun launchStoragePermission() {
        storagePermission.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
    }

    fun launchMicroPhonePermission() {
        microphoneAccessPermission.launch(Manifest.permission.RECORD_AUDIO)
    }

    fun launchContactPermission() {
        contactPermission.launch(
            arrayOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS
            )
        )
    }

    fun launchCameraPermission() {
        cameraPermission.launch(Manifest.permission.CAMERA)
    }

    fun launchLocationPermission() {
        locationAccessPermission.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (shouldHideKeyboard()) {
            if (ev?.action == MotionEvent.ACTION_DOWN) {
                val v: View? = currentFocus
                if (v is EditText) {
                    val outRect = Rect()
                    v.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                        v.clearFocus()
                        val imm =
                            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    fun <T> LiveData<T>.observe(onChanged: (T) -> Unit) {
        observe(this@BaseActivity, onChanged)
    }
}