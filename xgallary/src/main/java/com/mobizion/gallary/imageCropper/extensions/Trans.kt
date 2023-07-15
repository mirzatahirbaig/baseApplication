package com.mobizion.gallary.imageCropper.extensions

import android.graphics.Matrix
import com.mobizion.gallary.imageCropper.CropImageView

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 11/10/2022
 */

fun CropImageView.fixTrans() {
    cropMatrix.getValues(matrixValues)
    val transX = matrixValues[Matrix.MTRANS_X]
    val transY = matrixValues[Matrix.MTRANS_Y]
    val fixTransX = getFixTrans(transX, viewWidth.toFloat(), origWidth * saveScale)
    val fixTransY = getFixTrans(
        transY, viewHeight.toFloat(), origHeight
                * saveScale
    )
    if (fixTransX != 0f || fixTransY != 0f) cropMatrix.postTranslate(fixTransX, fixTransY)
}

private fun getFixTrans(trans: Float, viewSize: Float, contentSize: Float): Float {
    val minTrans: Float
    val maxTrans: Float
    if (contentSize <= viewSize) {
        minTrans = 0f
        maxTrans = viewSize - contentSize
    } else {
        minTrans = viewSize - contentSize
        maxTrans = 0f
    }
    if (trans < minTrans) return -trans + minTrans
    return if (trans > maxTrans) -trans + maxTrans else 0f
}

fun getFixDragTrans(delta: Float, viewSize: Float, contentSize: Float): Float {
    return if (contentSize <= viewSize) {
        0f
    } else delta
}