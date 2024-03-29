package com.xdevelopers.xfilemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mobizion.xbaseadapter.adapter.BaseListAdapter
import com.mobizion.xbaseadapter.listeners.OnItemClickedListener
import com.mobizion.xbaseadapter.viewholder.BaseViewHolder
import com.xdevelopers.xfilemanager.ProjectDiffUtil
import com.xdevelopers.xfilemanager.databinding.ProjectItemBinding
import com.xdevelopers.xfilemanager.model.ProjectItemModel

class ProjectsAdapter(private val itemClickListener: OnItemClickedListener<ProjectItemModel>) :
    BaseListAdapter<ProjectItemModel, ProjectDiffUtil>(
        ProjectDiffUtil()
    ) {


    override fun onViewHolderCreate(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProjectViewHolder(
            ProjectItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), itemClickListener
        )
    }

    override fun onViewHolderBind(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ProjectViewHolder).bind(getItem(position), position)
    }

    class ProjectViewHolder(
        private val binding: ProjectItemBinding,
        private val itemClickListener: OnItemClickedListener<ProjectItemModel>
    ) :
        BaseViewHolder<ProjectItemBinding, ProjectItemModel>(binding) {
        override fun bind(item: ProjectItemModel, position: Int) {
            binding.projectNameTextView.text = item.title
            if (item.isFile){
                Glide.with(binding.root.context)
                    .asBitmap().load(item.filePath)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.imageView)

            }else{
                binding.imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.root.context,
                        com.xdevelopers.xfilemanager.R.drawable.folder_image
                    )
                )
            }
            binding.root.setOnClickListener {
                itemClickListener.onItemClicked(item, position)
            }
        }

    }
}