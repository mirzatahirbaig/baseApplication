package com.mobizion.gallary.repository.abstraction

import com.mobizion.gallary.enum.MediaType
import com.mobizion.hiddlegallery.enum.SortOrder
import com.mobizion.gallary.DeviceMedia
import kotlinx.coroutines.flow.Flow

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 27/08/2022
 */
interface GalleryRepository {

    fun load(type: MediaType, order: SortOrder): Flow<List<DeviceMedia>>

}