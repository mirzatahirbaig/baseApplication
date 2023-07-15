package com.mobizion.gallary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobizion.gallary.databinding.LayoutGalleryBinding
import com.mobizion.hiddlegallery.enum.Selection
import com.mobizion.gallary.util.MediaDifUtil
import com.mobizion.gallary.view.holder.GalleryViewHolder
import com.mobizion.xbaseadapter.adapter.BaseListAdapter
import com.mobizion.xbaseadapter.listeners.OnItemClickedListener
import com.mobizion.gallary.DeviceMedia

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 11/10/2022
 */
class GalleryAdapter(
    private val selection: Selection,
    private val onItemClickedListener: OnItemClickedListener<DeviceMedia>
) : BaseListAdapter<DeviceMedia, MediaDifUtil>(MediaDifUtil()) {

    override fun onViewHolderCreate(parent: ViewGroup, viewType: Int) =
        GalleryViewHolder(
            LayoutGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClickedListener, selection
        )

    override fun onViewHolderBind(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as GalleryViewHolder).bind(getItem(position),position)
    }

    override fun getItemViewType(position: Int) = position
}