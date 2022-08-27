package com.mobizion.gallary.listeners

import com.mobizion.gallary.models.GalleryMedia

fun interface CheckedChangedListener {
    fun isChecked(isChecked:Boolean, galleryMedia: GalleryMedia, position:Int)
}