package com.mobizion.xutils

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.View


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