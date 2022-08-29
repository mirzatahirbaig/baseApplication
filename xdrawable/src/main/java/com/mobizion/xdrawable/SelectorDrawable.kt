package com.mobizion.xdrawable

import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */

fun getEnableDisableDrawable(
    enableColor: Int,
    disableColor: Int,
    radius: Float,
    enableBorderColor: Int,
    disableBorderColor: Int,
    enableBorderWidth: Int,
    disableBorderWidth: Int,
): Drawable {
    val enableDrawable = getBorderedDrawable(
        enableBorderColor,
        enableBorderWidth,
        enableColor,
        radius
    )
    val disableDrawable = getBorderedDrawable(
        disableBorderColor,
        disableBorderWidth,
        disableColor,
        radius
    )
    return StateListDrawable().also {
        it.addState(intArrayOf(android.R.attr.state_enabled), enableDrawable)
        it.addState(intArrayOf(-android.R.attr.state_enabled), disableDrawable)
    }
}

fun getCheckUnCheckDrawable(
    checkedColor: Int,
    unCheckedColor: Int,
    radius: Float,
    checkedBorderColor: Int,
    unCheckedBorderColor: Int,
    checkedBorderWidth: Int,
    unCheckedBorderWidth: Int
): Drawable {
    val enableDrawable = getBorderedDrawable(
        checkedBorderColor,
        checkedBorderWidth,
        checkedColor,
        radius
    )
    val disableDrawable = getBorderedDrawable(
        unCheckedBorderColor,
        unCheckedBorderWidth,
        unCheckedColor,
        radius
    )
    return StateListDrawable().also {
        it.addState(intArrayOf(android.R.attr.state_checked), enableDrawable)
        it.addState(intArrayOf(-android.R.attr.state_checked), disableDrawable)
    }
}

fun getPressedUnPressedDrawable(
    pressedColor: Int,
    unPressedColor: Int,
    radius: Float,
    pressedBorderColor: Int,
    unPressedBorderColor: Int,
    pressedBorderWidth: Int,
    unPressedBorderWidth: Int
): Drawable {
    val pressedDrawable = getBorderedDrawable(
        pressedBorderColor,
        pressedBorderWidth,
        pressedColor,
        radius
    )
    val disableDrawable = getBorderedDrawable(
        unPressedBorderColor,
        unPressedBorderWidth,
        unPressedColor,
        radius
    )
    return StateListDrawable().also {
        it.addState(intArrayOf(android.R.attr.state_pressed), pressedDrawable)
        it.addState(intArrayOf(-android.R.attr.state_pressed), disableDrawable)
    }
}