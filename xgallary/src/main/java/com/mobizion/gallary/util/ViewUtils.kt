package com.mobizion.gallary.util

import android.graphics.Color
import android.view.View
import com.intuit.sdp.R
import com.mobizion.xdrawable.getBorderedDrawable
import com.mobizion.xutils.getStatusBarHeight
import com.mobizion.xutils.setMargins

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 11/10/2022
 */

fun View.setDrawable(
    borderColor:Int,
    radius:Float,
    width:Int = 2
){
    background = getBorderedDrawable(
        borderColor,
        width,
        Color.TRANSPARENT,
        radius
    )
}

fun View.setMargin(
){
    setMargins(
        0,
        (resources.getDimension(com.intuit.sdp.R.dimen._8sdp) + getStatusBarHeight()).toInt(),
        0,
        0
    )
}