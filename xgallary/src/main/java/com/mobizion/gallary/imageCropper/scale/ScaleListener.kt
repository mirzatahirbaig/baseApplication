package com.mobizion.gallary.imageCropper.scale

import android.view.ScaleGestureDetector
import com.mobizion.gallary.imageCropper.CropImageView
import com.mobizion.gallary.imageCropper.enum.TouchMode
import com.mobizion.gallary.imageCropper.extensions.fixTrans

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 11/10/2022
 */

const val CLICK = 3
val CropImageView.ScaleListener:ScaleGestureDetector.SimpleOnScaleGestureListener
get() = object :ScaleGestureDetector.SimpleOnScaleGestureListener() {

    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
        touchMode = TouchMode.ZOOM
        return super.onScaleBegin(detector)
    }

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        var mScaleFactor: Float = detector.scaleFactor
        val origScale = saveScale
        saveScale *= mScaleFactor
        if (saveScale > maxScale) {
            saveScale = maxScale
            mScaleFactor = maxScale / origScale
        } else if (saveScale < minScale) {
            saveScale = minScale
            mScaleFactor = minScale / origScale
        }
        if (origWidth * saveScale <= viewWidth
            || origHeight * saveScale <= viewHeight
        ) cropMatrix.postScale(
            mScaleFactor, mScaleFactor, (viewWidth / 2).toFloat(),
            (viewHeight / 2).toFloat()
        ) else cropMatrix.postScale(
            mScaleFactor, mScaleFactor,
            detector.focusX, detector.focusY
        )
        fixTrans()
        return true
    }
}