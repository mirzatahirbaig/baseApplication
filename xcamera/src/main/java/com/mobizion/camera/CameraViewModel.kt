package com.mobizion.camera

import android.net.Uri
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class CameraViewModel: ViewModel() {

    private val _captureImageUri:MutableLiveData<Uri> by lazy {
        MutableLiveData()
    }
    private val _captureImageDirectory:MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val imageUri:LiveData<Uri>
    get() = _captureImageUri

    val captureImageDirectory:LiveData<String>
        get() = _captureImageDirectory

    fun capturedImageUri(path: Uri = Uri.EMPTY) = viewModelScope.launch {
        _captureImageUri.value = path
    }

    fun setCaptureImageDirector(path: String = Environment.DIRECTORY_PICTURES) = viewModelScope.launch {
        _captureImageDirectory.value = path
    }
}