package com.xdevelopers.xfilemanager.abstract

import android.graphics.Bitmap
import android.net.Uri
import com.xdevelopers.xfilemanager.model.ProjectItemModel
import kotlinx.coroutines.flow.Flow

interface FileManagerRepo {

    suspend fun createProject(name: String?)

    suspend fun createNewFolder(name: String?)

    suspend fun createFile(name: String?, uri: Uri):Uri?

    suspend fun createAllProjects(folderName: String):List<ProjectItemModel>

    suspend fun updateFile(bitmap: Bitmap, uri: Uri):Uri?

    fun createFileWithProject():Flow<Uri>

}