package com.mobizion.gallary.activity.viewer

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import com.mobizion.gallary.databinding.ActivityPhotoViewerBinding
import com.mobizion.gallary.util.setDrawable
import com.mobizion.gallary.util.setMargin
import com.mobizion.hiddlegallery.view.model.BitmapViewModel
import com.mobizion.xbase.activity.XBaseActivity
import com.mobizion.xutils.REQUEST_CODE
import com.mobizion.xutils.VIEW_IMAGE_REQUEST_CODE
import com.mobizion.xutils.VIEW_REQUEST_CODE
import com.mobizion.xutils.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 11/10/2022
 */

class PhotoViewerActivity:XBaseActivity<ActivityPhotoViewerBinding>(ActivityPhotoViewerBinding::inflate) {

    private val bitmapViewModel:BitmapViewModel by viewModel()

    override fun enableFullScreen() = true

    override fun shouldHideKeyboard() = true

    override fun initViews() {
        initUI()
        setupClickListeners()
    }

    private fun initUI(){
        binding.txtConfirm.visible(intent.getIntExtra(REQUEST_CODE, -1) != VIEW_IMAGE_REQUEST_CODE)
        binding.imgAvatar.setImageURI(intent.data)
        binding.toolbar.root.setMargin()
        binding.toolbar.txtHeading.setTextColor(Color.WHITE)
        binding.toolbar.txtHeading.text = "Pull to adjust"
        binding.toolbar.imgBack.imageTintList = ColorStateList.valueOf(Color.WHITE)
        binding.txtConfirm.setDrawable(
            Color.WHITE,
            resources.getDimension(com.intuit.sdp.R.dimen._20sdp)
        )
    }

    private fun setupClickListeners(){
        binding.toolbar.imgBack.setOnClickListener {
            finish()
        }
        binding.txtConfirm.setOnClickListener {
            bitmapViewModel.createBitmapFromAttributes(binding.imgAvatar.getImageAttributes())
        }

        bitmapViewModel.path.observe {
            Intent().also { intent ->
                intent.putExtra(REQUEST_CODE, VIEW_REQUEST_CODE)
                intent.data = it
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
        }
    }
}