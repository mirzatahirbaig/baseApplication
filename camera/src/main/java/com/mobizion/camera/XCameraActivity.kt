package com.mobizion.camera

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import com.mobizion.base.activity.BaseActivity
import com.mobizion.camera.abstract.CameraRepo
import com.mobizion.camera.databinding.ActivityXcameraBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class XCameraActivity:BaseActivity<ActivityXcameraBinding>(ActivityXcameraBinding::inflate) {

    private val cameraViewModel:CameraViewModel by viewModel()

    override fun shouldHideKeyboard(): Boolean {
       return false
    }

    override fun initViews() {
        cameraViewModel.imageUri.observe { uri ->
            val intent = Intent().also {
                it.data = uri
                it.putExtra("REQUEST_CODE",intent.getIntExtra("REQUEST_CODE",0))
            }
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
    }

}