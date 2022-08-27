package com.mobizion.gallary.models

import android.net.Uri
import androidx.recyclerview.widget.DiffUtil

/**
 * Simple data class to hold information about an image included in the device's MediaStore.
 */
data class GalleryMedia(
    var media_id:String,
    var type:Int,
    val contentUri: Uri,
    val duration:String?,
    var isSelected:Boolean = false
)