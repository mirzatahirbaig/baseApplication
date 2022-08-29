package com.mobizion.camera

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CameraViewModel: ViewModel() {

    private val _captureImageUri:MutableLiveData<Uri> by lazy {
        MutableLiveData()
    }

    val imageUri:LiveData<Uri>
    get() = _captureImageUri

    fun capturedImageUri(path: Uri = Uri.EMPTY) = viewModelScope.launch {
        _captureImageUri.value = path
    }
}