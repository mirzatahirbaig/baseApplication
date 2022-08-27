package com.mobizion.gallary.view.holders

import android.graphics.Color
import com.mobizion.base.listeners.OnItemSelectedListener
import com.mobizion.base.view.holder.BaseViewHolder
import com.mobizion.gallary.databinding.LayoutPdfItemBinding
import com.mobizion.gallary.models.PdfFile

class PdfViewHolder(private val binding: LayoutPdfItemBinding, val onMediaSelectedListener: OnItemSelectedListener<PdfFile>):
    BaseViewHolder<LayoutPdfItemBinding, PdfFile>(binding) {
    override fun bind(item: PdfFile, position: Int) {
        binding.txtPdfName.text = item.media_name
        binding.txtPdfName.setOnClickListener {
            binding.root.setBackgroundColor(Color.parseColor("#ebebeb"))
            onMediaSelectedListener.onItemSelected(item,position)
        }
    }
}