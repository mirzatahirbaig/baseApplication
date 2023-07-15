package com.mobizion.gallary.view.holder

import com.bumptech.glide.Glide
import com.mobizion.gallary.DeviceMedia
import com.mobizion.gallary.MessageType
import com.mobizion.gallary.R
import com.mobizion.gallary.databinding.LayoutGalleryBinding
import com.mobizion.hiddlegallery.enum.Selection
import com.mobizion.xbaseadapter.listeners.OnItemClickedListener
import com.mobizion.xbaseadapter.viewholder.BaseViewHolder
import com.mobizion.xutils.visible

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 11/10/2022
 */

class GalleryViewHolder(
    private val binding: LayoutGalleryBinding,
    private val onItemClickedListener: OnItemClickedListener<DeviceMedia>,
    private val selection: Selection
    ):BaseViewHolder<LayoutGalleryBinding,DeviceMedia>(binding) {
    override fun bind(item: DeviceMedia, position: Int) {
        binding.imgCheck.visible(selection == Selection.Multiple)
        binding.txtDuration.visible(item.type == MessageType.VIDEO)
        binding.imgCheck.setImageResource(R.drawable.ic_check_selector)
        binding.imgCheck.isSelected = item.isSelected
        Glide.with(binding.imgMedia)
            .load(item.contentUri)
            .into(binding.imgMedia)
        binding.root.setOnClickListener {
            onItemClickedListener.onItemClicked(item,position)
        }
    }
}