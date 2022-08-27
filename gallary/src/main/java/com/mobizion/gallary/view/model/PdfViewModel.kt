package com.mobizion.gallary.view.model

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentUris
import android.database.ContentObserver
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mobizion.gallary.extensions.registerObserver
import com.mobizion.gallary.models.PdfFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PdfViewModel(application: Application): AndroidViewModel(application) {

    private val _pdfs = MutableLiveData<MutableList<PdfFile>>()
    val pdfs: LiveData<MutableList<PdfFile>> get() = _pdfs

    private var contentObserver: ContentObserver? = null

    fun loadFiles()  {
        viewModelScope.launch {
            val imageList = queryPdfFiles()
            _pdfs.postValue(imageList)

            if (contentObserver == null) {
                contentObserver = getApplication<Application>().contentResolver.registerObserver(
                    MediaStore.Files.getContentUri("external")
                ) {
                    loadFiles()
                }
            }
        }
    }


    @SuppressLint("Range")
    private suspend fun queryPdfFiles(): MutableList<PdfFile> {
        val pdfs = mutableListOf<PdfFile>()
        withContext(Dispatchers.IO) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                val pdfMineType =
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf")
                val projection = arrayOf(
                    MediaStore.Files.FileColumns._ID,
                    MediaStore.Files.FileColumns.DATA,
                    MediaStore.Files.FileColumns.DISPLAY_NAME,
                    MediaStore.Files.FileColumns.MIME_TYPE
                )
                val selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                        + MediaStore.Files.FileColumns.MEDIA_TYPE_DOCUMENT
                        + " AND "
                        + MediaStore.Files.FileColumns.MIME_TYPE+ "='"
                        + pdfMineType + "'")

                getApplication<Application>().contentResolver.query(
                    MediaStore.Files.getContentUri("external"),
                    projection,
                    selection,
                    null,
                    null
                )?.use { cursor ->
                    val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                    val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                    val mediaTypeColumn =
                        cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
                    while (cursor.moveToNext()) {
                        val id = cursor.getString(idColumn)
                        val mediaType = cursor.getString(mediaTypeColumn)
                        val mediaName = cursor.getString(nameColumn)
                        val contentUri = ContentUris.withAppendedId(
                            MediaStore.Files.getContentUri("external"),
                            id.toLong()
                        )
                        val pdf = PdfFile(mediaName,mediaType,contentUri)
                        pdfs += pdf
                    }
                }
            }else{
                val ROOT_DIR = Environment.getExternalStorageDirectory().absolutePath
                val ANDROID_DIR = File("$ROOT_DIR/Android")
                val DATA_DIR = File("$ROOT_DIR/data")
                val filesList = File(ROOT_DIR).walk()
                    // befor entering this dir check if
                    .onEnter{ !it.isHidden // it is not hidden
                            && it != ANDROID_DIR // it is not Android directory
                            && it != DATA_DIR // it is not data directory
                            && !File(it, ".nomedia").exists() //there is no .nomedia file inside
                    }.filter { it.extension == "pdf"}.toMutableList()
                filesList.forEach { file ->
                    val pdf = PdfFile(file.name,file.path, Uri.fromFile(file))
                    pdfs += pdf
                }
            }
        }
        return pdfs
    }

    private val _result = MutableLiveData<Result<Boolean>>()
    val result: LiveData<Result<Boolean>> get() = _result

    fun signPdfFile(filePath:String,signature:Bitmap,xAxis:Float,yAxis:Float,pageIndex:Int,name:String){
        val renderer = PdfRenderer(
            ParcelFileDescriptor.open(
                File(filePath),
                ParcelFileDescriptor.MODE_READ_ONLY
            )
        )
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val page: PdfRenderer.Page = renderer.openPage(pageIndex)
                    val width: Int = getApplication<Application>().resources.displayMetrics.densityDpi / 72 * page.width
                    val height: Int = getApplication<Application>().resources.displayMetrics.densityDpi / 72 * page.height
                    val srcBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888)
                    val left = xAxis*srcBitmap.width
                    val top = yAxis*srcBitmap.height
                    page.render(srcBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    page.close()
                    val desBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(desBitmap)
                    canvas.drawColor(Color.WHITE)
                    canvas.drawBitmap(srcBitmap,0f,0f,null)
                    canvas.drawBitmap(signature,(left-signature.width*0.5).toFloat(),(top-signature.height*0.5).toFloat(),null)
                    val document = PdfDocument()
                    val pageCount: Int = renderer.pageCount
                    for (i in 0 until pageCount) {
                        if (i == pageIndex){
                            drawBitmapOnCanvas(desBitmap,document,i+1)
                        }else{
                            val renderPage: PdfRenderer.Page = renderer.openPage(i)
                            val renderWidth: Int = getApplication<Application>().resources.displayMetrics.densityDpi / 72 * renderPage.width
                            val renderHeight: Int = getApplication<Application>().resources.displayMetrics.densityDpi / 72 * renderPage.height
                            val renderBitmap = Bitmap.createBitmap(renderWidth, renderHeight, Bitmap.Config.ARGB_8888)
                            renderPage.render(renderBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                            renderPage.close()
                            drawBitmapOnCanvas(renderBitmap,document,i+1)
                        }
                    }
                    val directoryPath  = File(getApplication<Application>().filesDir.absolutePath+"/Documents",name)
                    directoryPath.deleteOnExit()
                    document.writeTo(FileOutputStream(directoryPath))
                    document.close()
                    renderer.close()
                    _result.postValue(Result.success(true))
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    _result.postValue(Result.failure(ex))
                }
            }
        }
    }

    private fun drawBitmapOnCanvas(bitmap: Bitmap, document: PdfDocument, index:Int){
        val pageInfo: PdfDocument.PageInfo  = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, index).create()
        val docPage: PdfDocument.Page  = document.startPage(pageInfo)
        val pdfCanvas: Canvas = docPage.canvas
        pdfCanvas.drawBitmap(bitmap, 0f, 0f, null)
        document.finishPage(docPage)
    }

    /**
     * Since we register a [ContentObserver], we want to unregister this when the `ViewModel`
     * is being released.
     */
    override fun onCleared() {
        contentObserver?.let {
            getApplication<Application>().contentResolver.unregisterContentObserver(it)
        }
    }
}