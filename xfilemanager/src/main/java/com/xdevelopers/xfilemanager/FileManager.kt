package com.xdevelopers.xfilemanager

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.xdevelopers.xfilemanager.abstract.FileManagerRepo
import com.xdevelopers.xfilemanager.model.ProjectItemModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.*
import java.util.*

class FileManager(private val context: Context): FileManagerRepo {

    fun fileExists(path: String):Boolean{
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
    fun getPath(filename: String): Uri? {
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

    fun getDocumentDirectory(): String {
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

    override suspend fun createAllProjects(folderName: String): List<ProjectItemModel> {
        var file = File(getDocumentDirectory())
        if (folderName.isNotEmpty()){
            file = File(getDocumentDirectory()+"/$folderName")
        }
        val directories = mutableListOf<ProjectItemModel>()
        file.listFiles()?.let {
            for (f in it){
                directories.add(ProjectItemModel(f.name, f.isFile))
            }
        }
        return directories
    }

    override fun createFileWithProject(): Flow<Uri> {
       val newProjectPath = createDirectory(UUID.randomUUID().toString())
        val file = File("$newProjectPath/${System.currentTimeMillis()}.png")
        return flow { emit(Uri.fromFile(file)) }
    }
}