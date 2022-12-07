/*
 * Copyright (C) 2012 Lightbox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mobizion.xutils

import android.app.Activity
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.opengl.GLException
import android.opengl.GLSurfaceView
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.EditText
import java.io.*
import java.lang.Exception
import java.nio.IntBuffer
import javax.microedition.khronos.opengles.GL10
import kotlin.jvm.JvmOverloads
import kotlin.Throws

class BitmapUtils {
    class BitmapSize(var width: Int, var height: Int)

    fun printscreen_share(v: View?, context: Activity) {
        val view1 = context.window.decorView
        val display = context.windowManager.defaultDisplay
        view1.layout(0, 0, display.width, display.height)
        view1.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(view1.drawingCache)
    }

    companion object {
        var scaleSize = 1024
        fun resizeImageForImageView(bitmap: Bitmap): Bitmap? {
            var resizedBitmap: Bitmap? = null
            val originalWidth = bitmap.width
            val originalHeight = bitmap.height
            var newWidth = -1
            var newHeight = -1
            var multFactor = -1.0f
            if (originalHeight > originalWidth) {
                newHeight = scaleSize
                multFactor = originalWidth.toFloat() / originalHeight.toFloat()
                newWidth = (newHeight * multFactor).toInt()
            } else if (originalWidth > originalHeight) {
                newWidth = scaleSize
                multFactor = originalHeight.toFloat() / originalWidth.toFloat()
                newHeight = (newWidth * multFactor).toInt()
            } else if (originalHeight == originalWidth) {
                newHeight = scaleSize
                newWidth = scaleSize
            }
            resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false)
            return resizedBitmap
        }

        /**
         * Used to tag logs
         */
        private const val TAG = "BitmapUtils"
        const val MAX_SZIE = (1024 * 512 // 500KB
                ).toLong()

        fun getOrientation(imagePath: String?): Int {
            var rotate = 0
            try {
                val imageFile = File(imagePath)
                val exif = ExifInterface(imageFile.absolutePath)
                val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                    ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                    ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return rotate
        }

        fun getBitmapSize(filePath: String?): BitmapSize {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(filePath, options)
            return BitmapSize(options.outWidth, options.outHeight)
        }

        fun getScaledSize(
            originalWidth: Int,
            originalHeight: Int, numPixels: Int
        ): BitmapSize {
            val ratio = originalWidth.toFloat() / originalHeight
            val scaledHeight = Math.sqrt((numPixels.toFloat() / ratio).toDouble()).toInt()
            val scaledWidth = (ratio * Math.sqrt((numPixels.toFloat() / ratio).toDouble())).toInt()
            return BitmapSize(scaledWidth, scaledHeight)
        }

        fun rotateImage(source: Bitmap, angle: Float): Bitmap {
            val matrix = Matrix()
            matrix.postRotate(angle)
            return Bitmap.createBitmap(
                source, 0, 0, source.width, source.height,
                matrix, true
            )
        }

        fun bitmapTobytes(bitmap: Bitmap): ByteArray {
            val a = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 30, a)
            return a.toByteArray()
        }

        fun bitmapTobytesNoCompress(bitmap: Bitmap): ByteArray {
            val a = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, a)
            return a.toByteArray()
        }

        fun genRotateBitmap(data: ByteArray): Bitmap {
            var bMap = BitmapFactory.decodeByteArray(data, 0, data.size)
            val matrix = Matrix()
            matrix.reset()
            matrix.postRotate(90f)
            val bMapRotate = Bitmap.createBitmap(
                bMap!!, 0, 0, bMap.width,
                bMap.height, matrix, true
            )
            bMap.recycle()
            bMap = null
            System.gc()
            return bMapRotate
        }

        fun byteToBitmap(data: ByteArray): Bitmap {
            return BitmapFactory.decodeByteArray(data, 0, data.size)
        }

        fun getBitmapFromView(view: View): Bitmap {
            val returnedBitmap = Bitmap.createBitmap(
                view.width,
                view.height, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(returnedBitmap)
            val bgDrawable = view.background
            if (bgDrawable != null) bgDrawable.draw(canvas) else canvas.drawColor(Color.WHITE)
            view.draw(canvas)
            return returnedBitmap
        }

        fun getImageCompress(srcPath: String?): Bitmap? {
            val newOpts = BitmapFactory.Options()
            newOpts.inJustDecodeBounds = true
            var bitmap = BitmapFactory.decodeFile(srcPath, newOpts)
            newOpts.inJustDecodeBounds = false
            val w = newOpts.outWidth
            val h = newOpts.outHeight
            val hh = 800f
            val ww = 480f
            var be = 1
            if (w > h && w > ww) {
                be = (newOpts.outWidth / ww).toInt()
            } else if (w < h && h > hh) {
                be = (newOpts.outHeight / hh).toInt()
            }
            if (be <= 0) be = 1
            newOpts.inSampleSize = be
            bitmap = BitmapFactory.decodeFile(srcPath, newOpts)
            return compressImage(bitmap)
        }

        fun compress(image: Bitmap): Bitmap? {
            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            if (baos.toByteArray().size / 1024 > 1024) {
                baos.reset()
                image.compress(Bitmap.CompressFormat.JPEG, 50, baos)
            }
            var isBm = ByteArrayInputStream(baos.toByteArray())
            val newOpts = BitmapFactory.Options()
            newOpts.inJustDecodeBounds = true
            var bitmap = BitmapFactory.decodeStream(isBm, null, newOpts)
            newOpts.inJustDecodeBounds = false
            val w = newOpts.outWidth
            val h = newOpts.outHeight
            val hh = 800f
            val ww = 480f
            var be = 1
            if (w > h && w > ww) {
                be = (newOpts.outWidth / ww).toInt()
            } else if (w < h && h > hh) {
                be = (newOpts.outHeight / hh).toInt()
            }
            if (be <= 0) be = 1
            newOpts.inSampleSize = be
            isBm = ByteArrayInputStream(baos.toByteArray())
            bitmap = BitmapFactory.decodeStream(isBm, null, newOpts)
            return compressImage(bitmap)
        }

        private fun compressImage(image: Bitmap?): Bitmap? {
            val baos = ByteArrayOutputStream()
            image!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            var options = 100
            while (baos.toByteArray().size / 1024 > 100) {
                baos.reset()
                image.compress(Bitmap.CompressFormat.JPEG, options, baos)
                options -= 10
            }
            val isBm =
                ByteArrayInputStream(baos.toByteArray())
            return BitmapFactory.decodeStream(isBm, null, null)
        }

        // 图片转为文件
        fun saveBitmap2file(bmp: Bitmap, filepath: String): Boolean {
            val format = Bitmap.CompressFormat.PNG
            val quality = 100
            var stream: OutputStream? = null
            try {
                if (Environment.MEDIA_MOUNTED != Environment
                        .getExternalStorageState()
                ) {
                    return false
                }

                // 检查SDcard空间
                val SDCardRoot = Environment.getExternalStorageDirectory()
                if (SDCardRoot.freeSpace < 10000) {
                    Log.e("Utils", "存储空间不够")
                    return false
                }

                // 在SDcard创建文件夹及文件
                val bitmapFile = File(SDCardRoot.path + filepath)
                bitmapFile.parentFile.mkdirs()
                stream = FileOutputStream(SDCardRoot.path + filepath) // "/sdcard/"
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            return bmp.compress(format, quality, stream)
        }

        fun getScreenViewBitmap(activity: Activity): Bitmap {
            val view = activity.window.decorView
            view.isDrawingCacheEnabled = true
            view.buildDrawingCache()
            return view.drawingCache
        }

        /**
         * 一个 View的图像
         *
         * @param view
         * @return
         */
        fun getViewBitmap(view: View): Bitmap {
            view.isDrawingCacheEnabled = true
            view.buildDrawingCache()
            return view.drawingCache
        }
        /**
         * Resize a bitmap object to fit the passed width and height
         *
         * @param input      The bitmap to be resized
         * @param destWidth  Desired maximum width of the result bitmap
         * @param destHeight Desired maximum height of the result bitmap
         * @return A new resized bitmap
         * @throws OutOfMemoryError if the operation exceeds the available vm memory
         */
        /**
         * Resize a bitmap
         *
         * @param input
         * @param destWidth
         * @param destHeight
         * @return
         * @throws OutOfMemoryError
         */
        @JvmOverloads
        @Throws(OutOfMemoryError::class)
        fun resizeBitmap(
            input: Bitmap,
            destWidth: Int,
            destHeight: Int,
            rotation: Int = 0
        ): Bitmap {
            var dstWidth = destWidth
            var dstHeight = destHeight
            val srcWidth = input.width
            val srcHeight = input.height
            if (rotation == 90 || rotation == 270) {
                dstWidth = destHeight
                dstHeight = destWidth
            }
            var needsResize = false
            val p: Float
            if (srcWidth > dstWidth || srcHeight > dstHeight) {
                needsResize = true
                if (srcWidth > srcHeight && srcWidth > dstWidth) {
                    p = dstWidth.toFloat() / srcWidth.toFloat()
                    dstHeight = (srcHeight * p).toInt()
                } else {
                    p = dstHeight.toFloat() / srcHeight.toFloat()
                    dstWidth = (srcWidth * p).toInt()
                }
            } else {
                dstWidth = srcWidth
                dstHeight = srcHeight
            }
            return if (needsResize || rotation != 0) {
                val output: Bitmap
                output = if (rotation == 0) {
                    Bitmap.createScaledBitmap(input, dstWidth, dstHeight, true)
                } else {
                    val matrix = Matrix()
                    matrix.postScale(dstWidth.toFloat() / srcWidth, dstHeight.toFloat() / srcHeight)
                    matrix.postRotate(rotation.toFloat())
                    Bitmap.createBitmap(input, 0, 0, srcWidth, srcHeight, matrix, true)
                }
                output
            } else input
        }

        fun getSampledBitmap(filePath: String?, reqWidth: Int, reqHeight: Int): Bitmap {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(filePath, options)
            val inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
            options.inSampleSize = inSampleSize
            options.inPreferredConfig = Bitmap.Config.RGB_565
            options.inJustDecodeBounds = false
            return BitmapFactory.decodeFile(filePath, options)
        }

        fun calculateInSampleSize(
            options: BitmapFactory.Options,
            reqWidth: Int,
            reqHeight: Int
        ): Int {
            // Raw height and width of image
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1
            if (height > reqHeight || width > reqWidth) {
                val halfHeight = height / 2
                val halfWidth = width / 2

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while (halfHeight / inSampleSize >= reqHeight
                    && halfWidth / inSampleSize >= reqWidth
                ) {
                    inSampleSize *= 2
                }
            }
            return inSampleSize
        }

        fun saveBitmap(bm: Bitmap, filePath: String?): Boolean {
            val f = File(filePath)
            if (f.exists()) {
                f.delete()
            }
            return try {
                val out = FileOutputStream(f)
                bm.compress(Bitmap.CompressFormat.PNG, 90, out)
                out.flush()
                out.close()
                true
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                false
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }

        fun getResizedBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
            val background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val originalWidth = bitmap.width.toFloat()
            val originalHeight = bitmap.height.toFloat()
            val canvas = Canvas(background)
            val scale = width / originalWidth
            val xTranslation = 0.0f
            val yTranslation = (height - originalHeight * scale) / 2.0f
            val transformation = Matrix()
            transformation.postTranslate(xTranslation, yTranslation)
            transformation.preScale(scale, scale)
            val paint = Paint()
            paint.isFilterBitmap = true
            canvas.drawBitmap(bitmap, transformation, paint)
            return background
        }

        fun getBorderedBitmap(image: Bitmap, borderColor: Int, borderSize: Int): Bitmap {

            // Creating a canvas with an empty bitmap, this is the bitmap that gonna store the final canvas changes
            val finalImage = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(finalImage)

            // Make a smaller copy of the image to draw on top of original
            val imageCopy = Bitmap.createScaledBitmap(
                image,
                image.width - borderSize,
                image.height - borderSize,
                true
            )

            // Let's draw the bigger image using a white paint brush
            val paint = Paint()
            paint.colorFilter = PorterDuffColorFilter(
                borderColor,
                PorterDuff.Mode.SRC_ATOP
            )
            canvas.drawBitmap(image, 0f, 0f, paint)
            val width = image.width
            val height = image.height
            val centerX = (width - imageCopy.width) * 0.5f
            val centerY = (height - imageCopy.height) * 0.5f
            // Now let's draw the original image on top of the white image, passing a null paint because we want to keep it original
            canvas.drawBitmap(imageCopy, centerX, centerY, null)

            // Returning the image with the final results
            return finalImage
        }

        fun trim(source: Bitmap): Bitmap {
            var firstX = 0
            var firstY = 0
            var lastX = source.width
            var lastY = source.height
            val pixels = IntArray(source.width * source.height)
            source.getPixels(pixels, 0, source.width, 0, 0, source.width, source.height)
            loop@ for (x in 0 until source.width) {
                for (y in 0 until source.height) {
                    if (pixels[x + y * source.width] != Color.TRANSPARENT) {
                        firstX = x
                        break@loop
                    }
                }
            }
            loop@ for (y in 0 until source.height) {
                for (x in firstX until source.width) {
                    if (pixels[x + y * source.width] != Color.TRANSPARENT) {
                        firstY = y
                        break@loop
                    }
                }
            }
            loop@ for (x in source.width - 1 downTo firstX) {
                for (y in source.height - 1 downTo firstY) {
                    if (pixels[x + y * source.width] != Color.TRANSPARENT) {
                        lastX = x
                        break@loop
                    }
                }
            }
            loop@ for (y in source.height - 1 downTo firstY) {
                for (x in source.width - 1 downTo firstX) {
                    if (pixels[x + y * source.width] != Color.TRANSPARENT) {
                        lastY = y
                        break@loop
                    }
                }
            }
            return Bitmap.createBitmap(source, firstX, firstY, lastX - firstX, lastY - firstY)
        }

        fun createTrimmedBitmap(bmp: Bitmap): Bitmap {
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
            Log.d(TAG, "left:$left right:$right top:$top bottom:$bottom")
            bmp = Bitmap.createBitmap(
                bmp,
                left,
                top,
                imgWidth - left - right,
                imgHeight - top - bottom
            )
            return bmp
        }

        fun textAsBitmap(editText: EditText): Bitmap {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.textSize = editText.textSize
            paint.color = editText.currentTextColor
            paint.textAlign = Paint.Align.LEFT
            paint.setShadowLayer(editText.shadowRadius, 0f, 0f, editText.shadowColor)
            val baseline = -paint.ascent() // ascent() is negative
            val width = (paint.measureText(editText.text.toString()) + 0.5f).toInt() // round
            val height = (baseline + paint.descent() + 0.5f).toInt()
            val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(image)
            canvas.drawText(editText.text.toString(), 0f, baseline, paint)
            return image
        }

        /**
         * Remove transparency in edited bitmap
         *
         * @param source edited image
         * @return bitmap without any transparency
         */
        fun removeTransparency(source: Bitmap): Bitmap {
            var firstX = 0
            var firstY = 0
            var lastX = source.width
            var lastY = source.height
            val pixels = IntArray(source.width * source.height)
            source.getPixels(pixels, 0, source.width, 0, 0, source.width, source.height)
            loop@ for (x in 0 until source.width) {
                for (y in 0 until source.height) {
                    if (pixels[x + y * source.width] != Color.TRANSPARENT) {
                        firstX = x
                        break@loop
                    }
                }
            }
            loop@ for (y in 0 until source.height) {
                for (x in firstX until source.width) {
                    if (pixels[x + y * source.width] != Color.TRANSPARENT) {
                        firstY = y
                        break@loop
                    }
                }
            }
            loop@ for (x in source.width - 1 downTo firstX) {
                for (y in source.height - 1 downTo firstY) {
                    if (pixels[x + y * source.width] != Color.TRANSPARENT) {
                        lastX = x
                        break@loop
                    }
                }
            }
            loop@ for (y in source.height - 1 downTo firstY) {
                for (x in source.width - 1 downTo firstX) {
                    if (pixels[x + y * source.width] != Color.TRANSPARENT) {
                        lastY = y
                        break@loop
                    }
                }
            }
            return Bitmap.createBitmap(source, firstX, firstY, lastX - firstX, lastY - firstY)
        }

        /**
         * Save filter bitmap from [ImageFilterView]
         *
         * @param glSurfaceView surface view on which is image is drawn
         * @param gl            open gl source to read pixels from [GLSurfaceView]
         * @return save bitmap
         * @throws OutOfMemoryError error when system is out of memory to load and save bitmap
         */
        @Throws(OutOfMemoryError::class)
        fun createBitmapFromGLSurface(glSurfaceView: GLSurfaceView, gl: GL10): Bitmap? {
            val x = 0
            val y = 0
            val w = glSurfaceView.width
            val h = glSurfaceView.height
            val bitmapBuffer = IntArray(w * h)
            val bitmapSource = IntArray(w * h)
            val intBuffer = IntBuffer.wrap(bitmapBuffer)
            intBuffer.position(0)
            try {
                gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer)
                var offset1: Int
                var offset2: Int
                for (i in 0 until h) {
                    offset1 = i * w
                    offset2 = (h - i - 1) * w
                    for (j in 0 until w) {
                        val texturePixel = bitmapBuffer[offset1 + j]
                        val blue = texturePixel shr 16 and 0xff
                        val red = texturePixel shl 16 and 0x00ff0000
                        val pixel = texturePixel and -0xff0100 or red or blue
                        bitmapSource[offset2 + j] = pixel
                    }
                }
            } catch (e: GLException) {
                return null
            }
            return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888)
        }
    }

}