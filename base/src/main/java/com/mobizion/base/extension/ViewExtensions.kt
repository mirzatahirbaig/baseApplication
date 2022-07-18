package com.mobizion.base.extension

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.View
import com.mobizion.base.R
import com.mobizion.base.response.NetworkResponse
import com.mobizion.base.utils.getFilledDrawable


fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.invisible(isInvisible: Boolean) {
    visibility = if (isInvisible) View.INVISIBLE else View.VISIBLE
}

fun View.enabled(enabled: Boolean) {
    isEnabled = enabled
}

fun View.alpha(alpha:Float) {
    this.alpha = alpha
}

fun View.setBackground(drawable: Drawable,states:Array<IntArray>?,colors:IntArray?){
    background = drawable
    backgroundTintList = ColorStateList(states,colors)
}