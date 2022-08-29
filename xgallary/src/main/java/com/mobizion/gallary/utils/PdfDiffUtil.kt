package com.mobizion.gallary.utils

import com.mobizion.base.utils.BaseDiffUtil
import com.mobizion.gallary.models.PdfFile

class PdfDiffUtil : BaseDiffUtil<PdfFile>() {

    override fun areContentsTheSame(
        oldItem: PdfFile,
        newItem: PdfFile
    ) = oldItem.media_path == oldItem.media_path

}