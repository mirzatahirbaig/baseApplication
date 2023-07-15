package com.mobizion.xutils

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

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
fun Context.getResFontId(name: String): Int {
    return resources.getIdentifier(
        name, "font",
        packageName
    )
}
fun Context.getResDrawableId(name: String): Int {
    return resources.getIdentifier(
        name, "drawable",
        packageName
    )
}

fun Context.getXResDrawableId(name: String): Int {
    return resources.getIdentifier(
        name, "drawable",
        "com.xdevelopers.xresources"
    )
}
fun Drawable.setImageColor(color: Int){
    val wrappedDrawable: Drawable = DrawableCompat.wrap(this)
    DrawableCompat.setTint(wrappedDrawable, color)
}

fun ImageView.setXImage(@DrawableRes drawableId: Int, color: Int? = null, colorId: Int? = null){
    setImageDrawable(ContextCompat.getDrawable(context, drawableId)?.also { draw ->
        color?.let {
            draw.setColorFilter(it, PorterDuff.Mode.SRC_ATOP)
        }
        colorId?.let {
            draw.setColorFilter(ContextCompat.getColor(context, it), PorterDuff.Mode.SRC_ATOP)
        }
    })
}