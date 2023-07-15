package com.mobizion.gallary.activity.gallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 11/10/2022
 */

const val HIDDEN = "hidden"
const val MEDIA = "media"

fun XGalleryActivity.setupClickListeners(){
    setupSendClickListener()
    setupBackClickListener()
    setupPermissionClickListener()
}

private fun XGalleryActivity.setupSendClickListener(){
    binding.txtSend.setOnClickListener {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelableArrayList(MEDIA, selectedMedia)
        bundle.putBoolean(HIDDEN,binding.toggleMessage.isChecked)
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}

private fun XGalleryActivity.setupBackClickListener(){
    binding.toolbar.imgBack.setOnClickListener {
        finish()
    }
}

private fun XGalleryActivity.setupPermissionClickListener(){
    launchPermissions()
}
