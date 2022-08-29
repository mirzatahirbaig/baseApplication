package com.mobizion.gallary.view.holders

import android.provider.MediaStore
import com.bumptech.glide.Glide
import com.mobizion.base.extension.enabled
import com.mobizion.base.extension.visible
import com.mobizion.base.view.holder.BaseViewHolder
import com.mobizion.gallary.databinding.LayoutGalleryItemBinding
import com.mobizion.gallary.listeners.CheckedChangedListener
import com.mobizion.gallary.models.GalleryMedia

class MultiItemSelectionViewHolder(val binding: LayoutGalleryItemBinding, val checkedChangedListener: CheckedChangedListener):
    BaseViewHolder<LayoutGalleryItemBinding, GalleryMedia>(binding) {
    override fun bind(item: GalleryMedia, position: Int) {
        val media = GalleryMedia(
            item.media_id,
            item.type,
            item.contentUri,
            item.duration,
            item.isSelected
        )
        Glide.with(binding.imgImage)
            .load(media.contentUri)
            .thumbnail(0.33f)
            .centerCrop()
            .into(binding.imgImage)
        binding.txtVideoDuration.visible(media.type == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
        item.duration?.let {
            binding.txtVideoDuration.text = it
        }
        binding.cbSelect.isChecked = media.isSelected
        binding.cbSelect.enabled(false)
        binding.imgImage.setOnClickListener {
            media.isSelected = !media.isSelected
            checkedChangedListener.isChecked(media.isSelected,media,position)
        }
    }
}