package com.mobizion.gallary.imageCropper.extensions

import android.graphics.PointF
import com.mobizion.gallary.imageCropper.CropImageView
import com.mobizion.gallary.imageCropper.enum.TouchMode
import com.mobizion.gallary.imageCropper.scale.CLICK
import kotlin.math.abs

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 11/10/2022
 */

fun CropImageView.handleActionMove(currentPoint: PointF){
    if (touchMode == TouchMode.DRAG) {
        val deltaX: Float = currentPoint.x - lastPoint.x
        val deltaY: Float = currentPoint.y - lastPoint.y
        val fixTransX = getFixDragTrans(
            deltaX, viewWidth.toFloat(),
            origWidth * saveScale
        )
        val fixTransY = getFixDragTrans(
            deltaY, viewHeight.toFloat(),
            origHeight * saveScale
        )
        cropMatrix.postTranslate(fixTransX, fixTransY)
        fixTrans()
        lastPoint.set(currentPoint.x, currentPoint.y)
    }
}

fun CropImageView.handleActionUp(currentPoint: PointF){
    touchMode = TouchMode.NONE
    val xDiff = abs(currentPoint.x - startPoint.x).toInt()
    val yDiff = abs(currentPoint.y - startPoint.y).toInt()
    if (xDiff < CLICK && yDiff < CLICK) performClick()
}