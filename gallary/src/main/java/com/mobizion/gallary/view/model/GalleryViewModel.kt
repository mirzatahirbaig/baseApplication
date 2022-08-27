package com.mobizion.gallary.view.model

import android.app.Application
import android.database.ContentObserver
import android.provider.MediaStore
import androidx.lifecycle.*
import com.mobizion.gallary.enum.MediaType
import com.mobizion.gallary.enum.SortOrder
import com.mobizion.gallary.models.GalleryMedia
import com.mobizion.gallary.repository.abstraction.GalleryRepository
import kotlinx.coroutines.launch

class GalleryViewModel(private val repository: GalleryRepository):ViewModel() {


    /**
     * Performs a one shot load of media from [MediaStore.Images.Media.EXTERNAL_CONTENT_URI]
     */

    fun load(type: MediaType , order: SortOrder = SortOrder.DESC) = repository.load(type, order).asLiveData()

}