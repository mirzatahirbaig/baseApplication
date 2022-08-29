package com.mobizion.xdrawable

import android.graphics.Color
import android.graphics.drawable.GradientDrawable

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */

fun getGradientDrawable(
    intArray: IntArray,
    orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.LEFT_RIGHT,
    borderColor: Int = Color.TRANSPARENT,
    gradientType: GradientTypes = GradientTypes.RECTANGLE,
    borderWidth: Int = 0,
    topLeft: Float = 0f,
    topRight: Float = 0f,
    bottomRight: Float = 0f,
    bottomLeft: Float = 0f
): GradientDrawable = GradientDrawable(
    orientation,
    intArray
).also {
    it.setStroke(borderWidth, borderColor)
    it.gradientType = gradientType.gradientType
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

fun getFilledGradientDrawable(
    colors: IntArray,
    radius: Float = 0f,
    orientation: GradientDrawable.Orientation
): GradientDrawable = getGradientDrawable(
    colors,
    orientation,
    topLeft = radius,
    topRight = radius,
    bottomRight = radius,
    bottomLeft = radius
)

fun getStrokeGradientDrawable(
    colors: IntArray,
    radius: Float = 0f,
    borderWidth: Int = 1,
    borderColor: Int = Color.WHITE,
    orientation: GradientDrawable.Orientation
): GradientDrawable = getGradientDrawable(
    colors,
    orientation,
    borderColor = borderColor,
    borderWidth = borderWidth,
    topLeft = radius,
    topRight = radius,
    bottomRight = radius,
    bottomLeft = radius
)