package com.mobizion.base.utils

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable

fun getDrawable(color: Int, radius: Float): Drawable =
    GradientDrawable().also {
        it.setColor(color)
        it.cornerRadius = radius
    }