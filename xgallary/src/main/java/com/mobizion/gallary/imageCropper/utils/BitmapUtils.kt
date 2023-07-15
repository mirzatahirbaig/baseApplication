package com.mobizion.gallary.imageCropper.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Environment
import java.io.File
import java.util.*

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 11/10/2022
 */

fun Drawable.toBitmap(): Bitmap {
    if (this is BitmapDrawable) {
        return this.bitmap
    }
    var bitmap = if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
        Bitmap.createBitmap(
            1,
            1,
            Bitmap.Config.ARGB_8888
        ) // Single color bitmap will be created of 1x1 pixel
    } else {
        Bitmap.createBitmap(
            intrinsicWidth,
            intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
    }
    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}

fun removeTransparent(bmp: Bitmap): Bitmap {
    var bmp = bmp
    val imgHeight = bmp.height
    val imgWidth = bmp.width
    val smallX = 0
    val smallY = 0
    var left = imgWidth
    var right = imgWidth
    var top = imgHeight
    var bottom = imgHeight
    for (i in 0 until imgWidth) {
        for (j in 0 until imgHeight) {
            if (bmp.getPixel(i, j) != Color.TRANSPARENT) {
                if (i - smallX < left) {
                    left = i - smallX
                }
                if (imgWidth - i < right) {
                    right = imgWidth - i
                }
                if (j - smallY < top) {
                    top = j - smallY
                }
                if (imgHeight - j < bottom) {
                    bottom = imgHeight - j
                }
            }
        }
    }
//        Log.d(TAG, "left:$left right:$right top:$top bottom:$bottom")
    bmp = Bitmap.createBitmap(bmp, left, top, imgWidth - left - right, imgHeight - top - bottom)
    return bmp
}


/***
 * Trim an image, removing transparent borders.
 * @param bitmap image to crop
 * @return square bitmap with the cropped image
 */
fun crop(bitmap: Bitmap): Bitmap {
    val height = bitmap.height
    val width = bitmap.width
    var empty = IntArray(width)
    var buffer = IntArray(width)
    Arrays.fill(empty, 0)
    var top = 0
    var left = 0
    var botton = height
    var right = width
    for (y in 0 until height) {
        bitmap.getPixels(buffer, 0, width, 0, y, width, 1)
        if (!empty.contentEquals(buffer)) {
            top = y
            break
        }
    }
    for (y in height - 1 downTo top + 1) {
        bitmap.getPixels(buffer, 0, width, 0, y, width, 1)
        if (!empty.contentEquals(buffer)) {
            botton = y
            break
        }
    }
    val bufferSize = botton - top + 1
    empty = IntArray(bufferSize)
    buffer = IntArray(bufferSize)
    Arrays.fill(empty, 0)
    for (x in 0 until width) {
        bitmap.getPixels(buffer, 0, 1, x, top + 1, 1, bufferSize)
        if (!empty.contentEquals(buffer)) {
            left = x
            break
        }
    }
    Arrays.fill(empty, 0)
    for (x in width - 1 downTo left + 1) {
        bitmap.getPixels(buffer, 0, 1, x, top + 1, 1, bufferSize)
        if (!empty.contentEquals(buffer)) {
            right = x
            break
        }
    }
    return Bitmap.createBitmap(bitmap, left, top, right - left, botton - top)
}

fun cropBitmapTransparency(sourceBitmap: Bitmap): Bitmap {
    var minX = sourceBitmap.width
    var minY = sourceBitmap.height
    var maxX = -1
    var maxY = -1
    for (y in 0 until sourceBitmap.height) {
        for (x in 0 until sourceBitmap.width) {
            val alpha = sourceBitmap.getPixel(x, y) shr 24 and 255
            if (alpha > 0) // pixel is not 100% transparent
            {
                if (x < minX) minX = x
                if (x > maxX) maxX = x
                if (y < minY) minY = y
                if (y > maxY) maxY = y
            }
        }
    }
    return if (maxX < minX || maxY < minY) sourceBitmap else Bitmap.createBitmap(
        sourceBitmap,
        minX,
        minY,
        maxX - minX + 1,
        maxY - minY + 1
    ) // Bitmap is entirely transparent

    // crop bitmap to non-transparent area and return:
}

fun getImageDirectory(context: Context): String {
    val dir = File(
        context.getDir(Environment.DIRECTORY_PICTURES, Context.MODE_PRIVATE).absolutePath
    )
    if (!dir.isDirectory) {
        dir.mkdir()
    }
    return dir.absolutePath
}