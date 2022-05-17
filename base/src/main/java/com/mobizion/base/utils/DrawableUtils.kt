package com.mobizion.base.utils

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.Orientation
import android.graphics.drawable.LayerDrawable
import com.mobizion.base.enums.GradientTypes
import com.mobizion.base.extension.dp

fun getDrawable(
    color: Int,
    borderColor: Int = Color.TRANSPARENT,
    borderWidth: Int = 0,
    topLeft: Int = 0,
    topRight: Int = 0,
    bottomRight: Int = 0,
    bottomLeft: Int = 0
): GradientDrawable =
    GradientDrawable().also {
        it.setColor(color)
        it.setStroke(borderWidth.dp, borderColor)
        it.cornerRadii = floatArrayOf(
            topLeft.dp.toFloat(),
            topLeft.dp.toFloat(),
            topRight.dp.toFloat(),
            topRight.dp.toFloat(),
            bottomRight.dp.toFloat(),
            bottomRight.dp.toFloat(),
            bottomLeft.dp.toFloat(),
            bottomLeft.dp.toFloat()
        )
    }

fun getGradientDrawable(
    intArray: IntArray,
    orientation: Orientation = Orientation.LEFT_RIGHT,
    borderColor: Int = Color.TRANSPARENT,
    gradientType: GradientTypes = GradientTypes.RECTANGLE,
    borderWidth: Int = 0,
    topLeft: Int = 0,
    topRight: Int = 0,
    bottomRight: Int = 0,
    bottomLeft: Int = 0
): GradientDrawable = GradientDrawable(
    orientation,
    intArray
).also {
    it.setStroke(borderWidth, borderColor)
    it.gradientType = gradientType.gradientType
    it.cornerRadii = floatArrayOf(
        topLeft.dp.toFloat(),
        topLeft.dp.toFloat(),
        topRight.dp.toFloat(),
        topRight.dp.toFloat(),
        bottomRight.dp.toFloat(),
        bottomRight.dp.toFloat(),
        bottomLeft.dp.toFloat(),
        bottomLeft.dp.toFloat()
    )
}

fun getFilledDrawable(
    color: Int,
    radius: Int = 0
): GradientDrawable = getDrawable(
    color,
    topLeft = radius,
    topRight = radius,
    bottomRight = radius,
    bottomLeft = radius
)

fun getFilledGradientDrawable(
    colors: IntArray,
    radius: Int = 0,
    orientation: Orientation
): GradientDrawable = getGradientDrawable(
    colors,
    orientation,
    topLeft = radius,
    topRight = radius,
    bottomRight = radius,
    bottomLeft = radius
)

fun getBorderedDrawable(
    borderColor: Int,
    borderWidth: Int = 0,
    fillColor: Int = Color.TRANSPARENT,
    radius: Int = 0
): GradientDrawable = getDrawable(
    fillColor,
    borderColor,
    borderWidth,
    radius,
    topRight = radius,
    bottomRight = radius,
    bottomLeft = radius
)

fun getLayeredDrawable(drawables:Array<Drawable>) = LayerDrawable(drawables)
