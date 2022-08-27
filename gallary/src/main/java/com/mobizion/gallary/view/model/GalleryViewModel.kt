package com.mobizion.gallary.view.model

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentUris
import android.database.ContentObserver
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mobizion.gallary.extensions.convertSecondsToHMmSs
import com.mobizion.gallary.extensions.registerObserver
import com.mobizion.gallary.models.GalleryMedia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GalleryViewModel(application:Application):AndroidViewModel(application) {
    private val _images = MutableLiveData<MutableList<GalleryMedia>>()
    val images: LiveData<MutableList<GalleryMedia>> get() = _images

    private var contentObserver: ContentObserver? = null

    /**
     * Performs a one shot load of images from [MediaStore.Images.Media.EXTERNAL_CONTENT_URI] into
     * the [_images] [LiveData] above.
     */
    fun loadImages(justImages: Boolean)  {
        viewModelScope.launch {
            val imageList = queryImages(justImages)
            _images.postValue(imageList)

            if (contentObserver == null) {
                contentObserver = getApplication<Application>().contentResolver.registerObserver(
                    MediaStore.Files.getContentUri("external")
                ) {
                    loadImages(justImages)
                }
            }
        }
    }

    @SuppressLint("Range")
    private suspend fun queryImages(justImages:Boolean): MutableList<GalleryMedia> {
        val images = mutableListOf<GalleryMedia>()
        withContext(Dispatchers.IO) {
            val projection = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Video.VideoColumns.DURATION,
                MediaStore.Video.VideoColumns.DATA,
            )
            val selection = if (justImages){
                (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                        + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
            }else {
                (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                        + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                        + " OR "
                        + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                        + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
            }
            val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"

            getApplication<Application>().contentResolver.query(
                MediaStore.Files.getContentUri("external"),
                projection,
                selection,
                null,
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val mediaTypeColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)
                while (cursor.moveToNext()) {
                    val id = cursor.getString(idColumn)
                    val mediaType = cursor.getInt(mediaTypeColumn)
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Files.getContentUri("external"),
                        id.toLong()
                    )
                    var duration:String? = null
                    if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO){
                        duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION)).convertSecondsToHMmSs()
                    }
                    val image = GalleryMedia(id,mediaType, contentUri,duration)
                    images += image
                }
            }
        }
        return images
    }

    /**
     * Since we register a [ContentObserver], we want to unregister this when the `ViewModel`
     * is being released.
     */
    override fun onCleared() {
        contentObserver?.let {
            getApplication<Application>().contentResolver.unregisterContentObserver(it)
        }
    }
}