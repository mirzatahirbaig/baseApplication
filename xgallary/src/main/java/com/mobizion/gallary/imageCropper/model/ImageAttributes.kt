package com.mobizion.gallary.imageCropper.model

import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.Matrix

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 11/10/2022
 */

data class ImageAttributes(
    val width: Int,
    val height: Int,
    val config: Config,
    val bitmap: Bitmap,
    val centerX: Float,
    val centerY: Float,
    val matrix: Matrix
)
