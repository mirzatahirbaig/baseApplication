package com.mobizion.gallary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobizion.base.adapter.BaseListAdapter
import com.mobizion.base.listeners.OnItemSelectedListener
import com.mobizion.gallary.databinding.LayoutPdfItemBinding
import com.mobizion.gallary.models.PdfFile
import com.mobizion.gallary.utils.PdfDiffUtil
import com.mobizion.gallary.view.holders.PdfViewHolder

class PdfAdapter(
    private val onMediaSelectedListener: OnItemSelectedListener<PdfFile>,
) : BaseListAdapter<PdfFile, PdfDiffUtil>(PdfDiffUtil()) {

    override fun onViewHolderCreate(parent: ViewGroup, viewType: Int) = PdfViewHolder(
        LayoutPdfItemBinding.inflate(LayoutInflater.from(parent.context),parent,false),
        onMediaSelectedListener
    )

    override fun onViewHolderBind(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PdfViewHolder).bind(getItem(position),position)
    }

    override fun getItemViewType(position: Int) = position
}