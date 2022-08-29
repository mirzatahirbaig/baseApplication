package com.mobizion.camera

import android.app.Activity
import android.content.Intent
import com.mobizion.camera.databinding.ActivityXcameraBinding
import com.mobizion.xbase.activity.XBaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class XCameraActivity : XBaseActivity<ActivityXcameraBinding>(ActivityXcameraBinding::inflate) {

    private val cameraViewModel: CameraViewModel by viewModel()

    override fun shouldHideKeyboard(): Boolean {
        return false
    }

    override fun initViews() {
        requestSinglePermission.launch(android.Manifest.permission.CAMERA)
        cameraViewModel.imageUri.observe { uri ->
            val intent = Intent().also {
                it.data = uri
                it.putExtra("REQUEST_CODE", intent.getIntExtra("REQUEST_CODE", 0))
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

}