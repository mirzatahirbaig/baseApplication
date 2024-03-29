package com.mobizion.xdrawable

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.RippleDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import com.mobizion.xutils.dp

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */

fun getDrawable(
    color: Int,
    borderColor: Int = Color.TRANSPARENT,
    borderWidth: Int = 0,
    topLeft: Float = 0f,
    topRight: Float = 0f,
    bottomRight: Float = 0f,
    bottomLeft: Float = 0f
): GradientDrawable =
    GradientDrawable().also {
        it.setColor(color)
        it.setStroke(borderWidth.dp, borderColor)
        it.cornerRadii = floatArrayOf(
            topLeft,
            topLeft,
            topRight,
            topRight,
            bottomRight,
            bottomRight,
            bottomLeft,
            bottomLeft
        )

    }

fun getFilledDrawable(
    color: Int,
    radius:  Float = 0f
): GradientDrawable = getDrawable(
    color,
    topLeft = radius,
    topRight = radius,
    bottomRight = radius,
    bottomLeft = radius
)

fun getBorderedDrawable(
    borderColor: Int,
    borderWidth: Int = 1,
    fillColor: Int = Color.WHITE,
    radius: Float = 0f
): GradientDrawable = getDrawable(
    fillColor,
    borderColor,
    borderWidth,
    radius,
    topRight = radius,
    bottomRight = radius,
    bottomLeft = radius
)
fun getDrawableWithRipple(
    borderColor: Int,
    borderWidth: Int = 0,
    fillColor: Int = Color.WHITE,
    radius: Float = 0f,
    rippleColor: Int = Color.WHITE,
): LayerDrawable{
    val bgLayer = getBorderedDrawable(borderColor, borderWidth, fillColor, radius)
 return LayerDrawable(arrayOf(
     bgLayer,
     RippleDrawable(ColorStateList.valueOf(rippleColor), bgLayer, null)
 ))
}

