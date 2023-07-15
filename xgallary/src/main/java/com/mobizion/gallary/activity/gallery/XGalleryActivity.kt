package com.mobizion.gallary.activity.gallery

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import com.mobizion.gallary.databinding.ActivityGalleryBinding
import com.mobizion.gallary.adapter.GalleryAdapter
import com.mobizion.gallary.enum.MediaType
import com.mobizion.hiddlegallery.enum.Selection
import com.mobizion.hiddlegallery.enum.SortOrder
import com.mobizion.gallary.util.setDrawable
import com.mobizion.gallary.util.setMargin
import com.mobizion.hiddlegallery.view.model.GalleryViewModel
import com.mobizion.xbase.activity.XBaseActivity
import com.mobizion.gallary.DeviceMedia
import com.mobizion.gallary.R
import com.mobizion.xutils.IMAGE_REQUEST_CODE
import com.mobizion.xutils.IMAGE_VIDEO_REQUEST_CODE
import com.mobizion.xutils.MEDIA_TYPE_REQUEST
import com.mobizion.xutils.MULTIPLE_REQUEST_CODE
import com.mobizion.xutils.REQUEST_CODE
import com.mobizion.xutils.SINGLE_REQUEST_CODE
import com.mobizion.xutils.getColour
import com.mobizion.xutils.setGridManager
import com.mobizion.xutils.themImageView
import com.mobizion.xutils.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class XGalleryActivity : XBaseActivity<ActivityGalleryBinding>(ActivityGalleryBinding::inflate) {

    private val galleryViewModel: GalleryViewModel by viewModel()
    var mediaItems: MutableList<DeviceMedia> = mutableListOf()
    lateinit var adapter: GalleryAdapter
    val selectedMedia = arrayListOf<DeviceMedia>()

    override fun shouldHideKeyboard() = true
    override fun enableFullScreen() = true

    override fun initViews() {
        launchPermissions()
        initUI()
        if (intent.getIntExtra(REQUEST_CODE,-1) == MULTIPLE_REQUEST_CODE){
            setupAdapter(Selection.Multiple)
        }else if (intent.getIntExtra(REQUEST_CODE,-1) == SINGLE_REQUEST_CODE){
            setupAdapter(Selection.Single)
        }
        setupRecyclerView()
        setupClickListeners()
    }

    private fun initUI() {
        binding.toolbar.root.setMargin()
        binding.toolbar.txtHeading.text = "Your Photos"
        themImageView(binding.toolbar.imgBack, com.xdevelopers.xresources.R.drawable.back_icon)
        setupBackgrounds()
        binding.txtSend.visible(intent.getIntExtra(REQUEST_CODE,-1) == MULTIPLE_REQUEST_CODE)
        binding.toggleMessage.visible(intent.getIntExtra(REQUEST_CODE,-1) == MULTIPLE_REQUEST_CODE)
    }

    private fun setupBackgrounds() {
        binding.txtNoPermission.setDrawable(
            getColour(com.xdevelopers.xresources.R.color.red),
            resources.getDimension(com.intuit.sdp.R.dimen._10sdp)
        )
        binding.txtSend.setDrawable(
            Color.WHITE,
            resources.getDimension(com.intuit.sdp.R.dimen._20sdp)
        )
    }

    private fun setupRecyclerView() {
        binding.rvMedia.adapter = adapter
        binding.rvMedia.setGridManager(4)
    }

    private fun setupDataObserver(mediaType: MediaType) {
        galleryViewModel.load(mediaType, SortOrder.DESC).observe {
            mediaItems.clear()
            mediaItems.addAll(it)
            adapter.submitList(mediaItems)
        }
    }

    fun launchPermissions() {

        val permissionsList =  mutableListOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsList.add(android.Manifest.permission.READ_MEDIA_IMAGES)
        }
        permissionsLauncher.launch(
            permissionsList.toTypedArray()
        ){
            if (it[android.Manifest.permission.READ_EXTERNAL_STORAGE] == true || it[android.Manifest.permission.READ_MEDIA_IMAGES] == true) {
                if (intent.getIntExtra(MEDIA_TYPE_REQUEST,-1) == IMAGE_REQUEST_CODE){
                    setupDataObserver(MediaType.IMAGES)
                }else if (intent.getIntExtra(MEDIA_TYPE_REQUEST,-1) == IMAGE_VIDEO_REQUEST_CODE){
                    setupDataObserver(MediaType.IMAGES_AND_VIDEOS)
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                binding.txtNoPermission.visible(it[android.Manifest.permission.READ_MEDIA_IMAGES] != true)
            }else{
                binding.txtNoPermission.visible(it[android.Manifest.permission.READ_EXTERNAL_STORAGE] != true)
            }
        }
    }

}