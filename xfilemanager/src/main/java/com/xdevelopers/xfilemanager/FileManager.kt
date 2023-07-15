package com.xdevelopers.xfilemanager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import com.blankj.utilcode.util.LogUtils
import com.xdevelopers.xfilemanager.abstract.FileManagerRepo
import com.xdevelopers.xfilemanager.model.ProjectItemModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class FileManager(private val context: Context): FileManagerRepo {

    private fun fileExists(path: String):Boolean{
        val file = File(path)
        return file.exists()
    }
    fun fileExistsAtPath(uri: Uri): Uri{
        val file = File(uri.path!!)
        if (file.exists())
        {
            return uri
        }
        return Uri.EMPTY
    }
    private fun getDirectoryPath(path: String):String{
       val split = path.split("/")
        return when(split.last().contains(".")){
            true -> {
                val pathArray = split.dropLast(1)
                pathArray.joinToString("/")
            }
            false -> {
                path
            }
        }
    }
    fun createDirectoryIfNotExisted(path: String) {
        val newPath = getDirectoryPath(path)
        if(!fileExists(newPath)){
            val file = File(newPath)
            file.mkdirs()
        }
    }
    private fun getPath(filename: String): Uri? {
        return  Uri.parse("${context.getDir(Environment.DIRECTORY_DOCUMENTS, Context.MODE_PRIVATE).absolutePath}/$filename")
    }
    fun removeFile(filename:String, context: Context){
        getPath(filename)?.let {
            val file = File(it.path)
            if (file.isDirectory) {
                val children = file.list()
                for (i in children.indices) {
                    File(file, children[i]).delete()
                }
            }
            file.delete()
        }
    }

    private fun getDocumentDirectory(): String {
     return context.getDir(Environment.DIRECTORY_DOCUMENTS,Context.MODE_PRIVATE).absolutePath
    }
    fun removeTemplates(){
        getPath("templates")?.let {
            val file = File(it.path)
            if (file.isDirectory) {
                val children = file.list()
                for (i in children.indices) {
                    File(file, children[i]).delete()
                }
            }
            file.delete()
        }
    }
    fun renameFile(sourceFileName: String, destFileName: String) {
        var bis: BufferedInputStream? = null
        var bos: BufferedOutputStream? = null

        try {
            bis = BufferedInputStream(FileInputStream(sourceFileName))
            bos = BufferedOutputStream(FileOutputStream(destFileName, false))
            val buf = ByteArray(1024)
            bis.read(buf)
            do {
                bos.write(buf)
            } while (bis.read(buf) !== -1)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bis?.close()
                bos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
    private fun createDirectory(name: String?): String{
        val newPath = getDocumentDirectory()
        val file = File("$newPath/$name")
        if(!fileExists(file.absolutePath)){
            file.mkdirs()
        }
        return file.absolutePath
    }
    override suspend fun createProject(name: String?) {
        val newPath = getDocumentDirectory()
        val file = File("$newPath/$name")
        if(!fileExists(file.absolutePath)){
            file.mkdirs()
        }
    }

    override suspend fun createNewFolder(name: String?) {
        val newPath = getDocumentDirectory()
        val file = File("$newPath/$name")
        if(!fileExists(file.absolutePath)){
            file.mkdirs()
        }
    }

    override suspend fun createFile(name: String?, uri: Uri): Uri? {
        var fileName = createFileName()
        name?.let { fileName = it }
        val filePath = File(getDocumentDirectory(), "$fileName.jpg")
        withContext(Dispatchers.IO) {
            var outputStream: OutputStream? = null
            try {
                outputStream = FileOutputStream(filePath)

               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source).compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                } else {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri).compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                }

                return@withContext Uri.fromFile(filePath)
            } catch (e: IOException) {
                LogUtils.e(e)
                return@withContext null
            } finally {
                try {
                    outputStream?.close()
                } catch (e: IOException) {
                    return@withContext null
                }
            }
        }
        return Uri.fromFile(filePath)
    }

    override suspend fun createAllProjects(folderName: String): List<ProjectItemModel> {
        var file = File(getDocumentDirectory())
        if (folderName.isNotEmpty()){
            file = File(getDocumentDirectory()+"/$folderName")
        }
        val directories = mutableListOf<ProjectItemModel>()
        file.listFiles()?.let {
            for (f in it){
                if (!f.isHidden){
                    directories.add(ProjectItemModel(f.name, f.isFile, Uri.fromFile(f)))
                }
            }
        }
        return directories
    }

    override suspend fun updateFile(bitmap: Bitmap, uri: Uri): Uri? {
        withContext(Dispatchers.IO) {
            try {
                val out = context.contentResolver.openOutputStream(uri)
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                out?.write(stream.toByteArray())
                out?.close()
                return@withContext uri
            } catch (e: IOException) {
                e.printStackTrace()
                return@withContext null
            }
        }
        return null
    }

    override fun createFileWithProject(): Flow<Uri> {
       val newProjectPath = createDirectory(UUID.randomUUID().toString())
        val file = File("$newProjectPath/${System.currentTimeMillis()}.png")
        return flow { emit(Uri.fromFile(file)) }
    }

    private fun createFileName(): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = context.contentResolver

        // Get the file type from the content resolver
        val mimeType = contentResolver.getType(uri)

        // Return the file extension from the mimeType
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
    }
}