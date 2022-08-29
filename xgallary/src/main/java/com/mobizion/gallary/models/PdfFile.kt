package com.mobizion.gallary.models

import android.net.Uri

data class PdfFile(
    var media_name:String,
    var media_path:String,
    var contentUri:Uri
)
