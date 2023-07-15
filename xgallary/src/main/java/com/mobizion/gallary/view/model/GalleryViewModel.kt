package com.mobizion.hiddlegallery.view.model

import android.provider.MediaStore
import androidx.lifecycle.*
import com.mobizion.gallary.enum.MediaType
import com.mobizion.hiddlegallery.enum.SortOrder
import com.mobizion.gallary.repository.abstraction.GalleryRepository

class GalleryViewModel(private val repository: GalleryRepository) : ViewModel() {


    /**
     * Performs a one shot load of media from [MediaStore.Images.Media.EXTERNAL_CONTENT_URI]
     */

    fun load(type: MediaType, order: SortOrder = SortOrder.DESC) =
        repository.load(type, order).asLiveData()


}