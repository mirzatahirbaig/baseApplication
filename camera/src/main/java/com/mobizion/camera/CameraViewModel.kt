package com.mobizion.camera

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizion.camera.abstract.CameraRepo
import kotlinx.coroutines.launch

class CameraViewModel(private val repository: CameraRepo): ViewModel() {
    fun capturedImageUri(path: Uri = Uri.EMPTY) = viewModelScope.launch {
        repository.cameraCaptureImageUri(path)
    }
}