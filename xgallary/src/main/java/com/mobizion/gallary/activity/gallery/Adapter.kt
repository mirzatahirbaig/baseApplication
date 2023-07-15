package com.mobizion.gallary.activity.gallery

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.mobizion.gallary.adapter.GalleryAdapter
import com.mobizion.hiddlegallery.enum.Selection
import com.mobizion.gallary.DeviceMedia
import com.mobizion.xutils.REQUEST_CODE
import com.mobizion.xutils.SINGLE_REQUEST_CODE

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 11/10/2022
 */

fun XGalleryActivity.setupAdapter(selection: Selection){
    adapter = GalleryAdapter(selection) { media, position ->
        when(selection){
            Selection.Single ->{
                val intent = Intent().also { it.data = media.contentUri
                    it.putExtra("media",media.path)
                }
                Log.d("single", intent.data.toString())
                intent.putExtra(REQUEST_CODE, SINGLE_REQUEST_CODE)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            else -> {
                if (selectedMedia.size < 10){
                    media.isSelected = !media.isSelected
                    notifyAdapter(media,position)
                }else{
                    if (media.isSelected){
                        media.isSelected = false
                        notifyAdapter(media,position)
                    }
                }
            }
        }
    }
}

private fun XGalleryActivity.notifyAdapter(media: DeviceMedia, position: Int){
    if (media.isSelected){
        selectedMedia.add(media)
    }else{
        selectedMedia.remove(media)
    }
    mediaItems[position] = media
    adapter.notifyItemChanged(position)
}