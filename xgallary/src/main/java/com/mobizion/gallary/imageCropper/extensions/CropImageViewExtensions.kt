package com.mobizion.gallary.imageCropper.extensions

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import com.mobizion.gallary.imageCropper.CropImageView
import com.mobizion.gallary.imageCropper.utils.toBitmap

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 11/10/2022
 */

fun CropImageView.onCropMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    viewWidth = View.MeasureSpec.getSize(widthMeasureSpec)
    viewHeight = View.MeasureSpec.getSize(heightMeasureSpec)
    if (oldMeasuredHeight == viewWidth && oldMeasuredHeight == viewHeight || viewWidth == 0 || viewHeight == 0) return
    oldMeasuredHeight = viewHeight
    oldMeasuredWidth = viewWidth
    setCenterOfCircle(viewWidth, viewHeight)
    if (saveScale == 1f) {
        val scale: Float
        val drawable: Drawable = drawable
        if (drawable.intrinsicWidth == 0 || drawable.intrinsicHeight == 0) return
        cropImageBitmap = drawable.toBitmap()
        val bmWidth: Int = drawable.intrinsicWidth
        val bmHeight: Int = drawable.intrinsicHeight
        Log.d("bmSize", "bmWidth: $bmWidth bmHeight : $bmHeight")
        val scaleX = viewWidth.toFloat() / bmWidth.toFloat()
        val scaleY = viewHeight.toFloat() / bmHeight.toFloat()
        scale = scaleX.coerceAtMost(scaleY)
        cropMatrix.setScale(scale, scale)

        var redundantYSpace = (viewHeight.toFloat()
                - scale * bmHeight.toFloat())
        var redundantXSpace = (viewWidth.toFloat()
                - scale * bmWidth.toFloat())
        redundantYSpace /= 2.toFloat()
        redundantXSpace /= 2.toFloat()
        cropMatrix.postTranslate(redundantXSpace, redundantYSpace)
        origWidth = viewWidth - 2 * redundantXSpace
        origHeight = viewHeight - 2 * redundantYSpace
        imageMatrix = cropMatrix
    }
    fixTrans()
}

private fun CropImageView.setCenterOfCircle(width: Int, height: Int) {
    centreXCircleArea = (width / 2).toFloat()
    centreYCircleArea = (height / 2).toFloat()
}

fun getMinimumSideLength(width: Int, height: Int): Float {
    return ((width - (height / 2)) * 0.95).toFloat()
}