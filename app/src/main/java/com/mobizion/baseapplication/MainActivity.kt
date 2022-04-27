package com.mobizion.baseapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobizion.base.activity.BaseActivity
import com.mobizion.baseapplication.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun initViews() {
    }

    override fun shouldHideKeyboard(): Boolean {
        TODO("Not yet implemented")
    }
}