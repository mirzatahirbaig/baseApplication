package com.mobizion.base.utils

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.Orientation
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.StateListDrawable
import android.util.StateSet
import com.mobizion.base.enums.GradientTypes
import com.mobizion.base.extension.dp

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

fun getGradientDrawable(
    intArray: IntArray,
    orientation: Orientation = Orientation.LEFT_RIGHT,
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

fun getFilledGradientDrawable(
    colors: IntArray,
    radius: Float = 0f,
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

fun getLayeredDrawable(drawables:Array<Drawable>) = LayerDrawable(drawables)

fun getSelectorDrawable(pressedColor:Int,bgColor:Int,radius:Int=0): StateListDrawable {
    val drawable = StateListDrawable()
    drawable.addState(intArrayOf(android.R.attr.state_checked), getDrawable(pressedColor,radius))
    drawable.addState(StateSet.WILD_CARD, getDrawable(bgColor,radius))
    return drawable
}
