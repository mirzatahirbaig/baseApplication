package com.mobizion.hiddlegallery.view.model

import android.app.Application
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizion.gallary.imageCropper.model.ImageAttributes
import com.mobizion.xutils.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.*

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 11/10/2022
 */
class BitmapViewModel(private val context: Application) : ViewModel() {

    private val _path: MutableLiveData<Uri> by lazy {
        MutableLiveData()
    }

    val path: LiveData<Uri>
        get() = _path

    fun createBitmapFromAttributes(attributes: ImageAttributes) = viewModelScope.launch {
        withContext(Dispatchers.Default) {
            val paint = Paint()
            val path = Path()
            val bitmap = Bitmap.createBitmap(attributes.width, attributes.height, attributes.config)
            val canvas = Canvas(bitmap)
            canvas.drawBitmap(attributes.bitmap,
                attributes.matrix, null
            )
            paint.style = Paint.Style.FILL
            paint.color = Color.TRANSPARENT
            paint.isAntiAlias = true
            path.fillType = Path.FillType.INVERSE_EVEN_ODD
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
            saveBitmap(bitmap)
        }
    }

    private fun saveBitmap(bitmap: Bitmap) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            try {
                val path = File(
                    context.getDir(Environment.DIRECTORY_PICTURES,Context.MODE_PRIVATE).absolutePath,
                    "IMG.png"
                )
                FileOutputStream(path).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, out) // bmp is your Bitmap instance
                }
                _path.postValue(path.toUri())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}