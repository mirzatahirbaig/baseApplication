package com.xdevelopers.xfilemanager.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.*
import com.xdevelopers.xfilemanager.model.ProjectItemModel
import com.xdevelopers.xfilemanager.abstract.FileManagerRepo
import kotlinx.coroutines.launch
import java.util.*

class FileManagerViewModel(private val repository: FileManagerRepo): ViewModel() {

    private val _files: MutableLiveData<List<ProjectItemModel>> by lazy {
        MutableLiveData()
    }
    val projects: LiveData<List<ProjectItemModel>> get() = _files

    fun getAllProjects(folderName: String) = viewModelScope.launch {
        _files.value = repository.createAllProjects(folderName)
    }
    fun createNewProject(path: String = "") = viewModelScope.launch {
        repository.createProject("$path/"+UUID.randomUUID().toString())
        getAllProjects("$path/")
    }

    fun createFileWithProject() = repository.createFileWithProject().asLiveData()

    fun createNewFolder(path: String = "", name: String) = viewModelScope.launch {
        repository.createNewFolder("$path/$name")
    }

    private val _fileUri: MutableLiveData<Uri> by lazy {
        MutableLiveData()
    }
    val fileUri: LiveData<Uri?> get() = _fileUri

    fun createFile(fileName: String?, uri: Uri) = viewModelScope.launch {
        _fileUri.value = repository.createFile(fileName, uri)
    }
    private val _fileUpdatedUri: MutableLiveData<Uri> by lazy {
        MutableLiveData()
    }
    val fileUpdatedUri: LiveData<Uri?> get() = _fileUri

    fun updateFile(bitmap: Bitmap, uri: Uri) = viewModelScope.launch {
        _fileUpdatedUri.value = repository.updateFile(bitmap, uri)
    }


}
