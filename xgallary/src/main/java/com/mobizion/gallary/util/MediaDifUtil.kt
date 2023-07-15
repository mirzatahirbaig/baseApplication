package com.mobizion.gallary.util

import com.mobizion.gallary.DeviceMedia
import com.mobizion.xbaseadapter.difutil.BaseDiffUtil

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 11/10/2022
 */
class MediaDifUtil:BaseDiffUtil<DeviceMedia>() {
    override fun areContentsTheSame(oldItem: DeviceMedia, newItem: DeviceMedia) =
        oldItem.media_id == newItem.media_id
}