package com.mobizion.camera

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Environment
import com.mobizion.camera.databinding.ActivityXcameraBinding
import com.mobizion.xbase.activity.XBaseActivity
import com.mobizion.xbase.activity.extentions.X_FILE_DIRECTORY
import com.mobizion.xbase.activity.extentions.X_REQUEST_CODE
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class XCameraActivity : XBaseActivity<ActivityXcameraBinding>(ActivityXcameraBinding::inflate) {

    private val cameraViewModel: CameraViewModel by viewModel()

    override fun shouldHideKeyboard(): Boolean {
        return false
    }

    override fun initViews() {
        requestSinglePermission.launch(android.Manifest.permission.CAMERA)

        intent.extras?.let {
            it.getString(X_FILE_DIRECTORY)?.let {
                cameraViewModel.setCaptureImageDirector(it)
            }
        }
        cameraViewModel.imageUri.observe { uri ->
            val intent = Intent().also {
                it.data = uri
                it.putExtra(X_REQUEST_CODE, intent.getIntExtra(X_REQUEST_CODE, 0))
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}