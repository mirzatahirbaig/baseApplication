package com.mobizion.gallary.view.holders

import com.bumptech.glide.Glide
import com.mobizion.base.extension.visible
import com.mobizion.base.listeners.OnItemSelectedListener
import com.mobizion.base.view.holder.BaseViewHolder
import com.mobizion.gallary.databinding.LayoutGalleryItemBinding

import com.mobizion.gallary.models.GalleryMedia

class SingleItemSelectionViewHolder(private val binding: LayoutGalleryItemBinding, val onMediaSelectedListener: OnItemSelectedListener<GalleryMedia>):
    BaseViewHolder<LayoutGalleryItemBinding, GalleryMedia>(binding) {
    override fun bind(item: GalleryMedia, position: Int) {
        binding.cbSelect.visible(false)
        binding.txtVideoDuration.visible(false)
        Glide.with(binding.imgImage)
            .load(item.contentUri)
            .thumbnail(0.33f)
            .centerCrop()
            .into(binding.imgImage)
        binding.imgImage.setOnClickListener {
            onMediaSelectedListener.onItemSelected(item,position)
        }
    }
}