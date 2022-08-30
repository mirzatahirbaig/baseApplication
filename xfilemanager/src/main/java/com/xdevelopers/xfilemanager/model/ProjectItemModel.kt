package com.xdevelopers.xfilemanager.model

import android.net.Uri

data class ProjectItemModel(var title: String, var isFile: Boolean = false, var filePath: Uri)