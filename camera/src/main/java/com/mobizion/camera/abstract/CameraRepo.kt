package com.mobizion.camera.abstract

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface CameraRepo {
    fun cameraCaptureImageUri(fileUri: Uri)
}