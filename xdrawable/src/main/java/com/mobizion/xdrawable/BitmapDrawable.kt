package com.mobizion.xdrawable

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory


fun getCircleBitmapDrawable(
    bitmap: Bitmap,
    resources: Resources
): RoundedBitmapDrawable =
    RoundedBitmapDrawableFactory.create(resources, bitmap).also {
        it.isCircular = true
    }
fun getCircleBitmapDrawable(
    resId: Int,
    resources: Resources
): RoundedBitmapDrawable =
    RoundedBitmapDrawableFactory.create(resources, BitmapFactory.decodeResource(resources, resId)).also {
        it.isCircular = true
    }

fun getCircleBitmapDrawable(
    resources: Resources,
    path: String
): RoundedBitmapDrawable =
    RoundedBitmapDrawableFactory.create(resources, path).also {
        it.isCircular = true
    }

fun getRoundBitmapDrawable(
    cornerRadius: Float,
    bitmap: Bitmap,
    resources: Resources

): RoundedBitmapDrawable =
    RoundedBitmapDrawableFactory.create(resources, bitmap).also {
        it.isCircular = true
        it.cornerRadius = cornerRadius
    }
fun getRoundBitmapDrawable(
    resId: Int,
    cornerRadius: Float,
    resources: Resources

): RoundedBitmapDrawable =
    RoundedBitmapDrawableFactory.create(resources, BitmapFactory.decodeResource(resources, resId)).also {
        it.isCircular = true
        it.cornerRadius = cornerRadius
    }