package com.mobizion.camera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobizion.base.activity.BaseActivity
import com.mobizion.camera.databinding.ActivityXcameraBinding

class XCameraActivity :  BaseActivity<ActivityXcameraBinding>(ActivityXcameraBinding::inflate) {

    override fun shouldHideKeyboard(): Boolean {
       return false
    }

    override fun initViews() {

    }
}