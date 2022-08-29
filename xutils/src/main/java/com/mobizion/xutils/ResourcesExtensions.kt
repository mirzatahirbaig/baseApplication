package com.mobizion.xutils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */

fun Context.getStringRes(stringId: Int): String {
    return this.resources.getString(stringId)
}

fun Context.getStringArray(@ArrayRes arrayId: Int): Array<String> {
    return this.resources.getStringArray(arrayId)
}

fun Context.getImageDrawable(@DrawableRes drawableId: Int): Drawable {
    return ContextCompat.getDrawable(this, drawableId)!!
}

fun Context.getInteger(@IntegerRes integerId: Int): Int {
    return this.resources.getInteger(integerId)
}

fun Context.getColour(@ColorRes colorId: Int): Int {
    return ContextCompat.getColor(this, colorId)
}

fun Context.getIntArray(@ArrayRes arrayId: Int): IntArray {
    return resources.getIntArray(arrayId)
}