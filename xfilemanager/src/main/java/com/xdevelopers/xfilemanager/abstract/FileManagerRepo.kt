package com.xdevelopers.xfilemanager.abstract

import android.net.Uri
import com.xdevelopers.xfilemanager.model.ProjectItemModel
import kotlinx.coroutines.flow.Flow

interface FileManagerRepo {

    suspend fun createProject(name: String?)

    suspend fun createNewFolder(name: String?)

    suspend fun createAllProjects(folderName: String):List<ProjectItemModel>

    fun createFileWithProject():Flow<Uri>
}