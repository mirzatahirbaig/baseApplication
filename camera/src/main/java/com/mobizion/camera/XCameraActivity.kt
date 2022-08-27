package com.mobizion.camera

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mobizion.base.activity.BaseActivity
import com.mobizion.camera.abstract.CameraRepo
import com.mobizion.camera.databinding.ActivityXcameraBinding

class XCameraActivity:BaseActivity<ActivityXcameraBinding>(ActivityXcameraBinding::inflate), CameraRepo {

    override fun shouldHideKeyboard(): Boolean {
       return false
    }

    override fun initViews() {

    }

    override fun cameraCaptureImageUri(fileUri: Uri) {
        Log.d("XCameraActivity", fileUri.path ?: "")
    }
}