package com.xdevelopers.xfilemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobizion.base.adapter.BaseListAdapter
import com.mobizion.base.listeners.OnItemSelectedListener
import com.mobizion.base.view.holder.BaseViewHolder
import com.xdevelopers.xfilemanager.ProjectDiffUtil
import com.xdevelopers.xfilemanager.databinding.ProjectItemBinding
import com.xdevelopers.xfilemanager.model.ProjectItemModel

class ProjectsAdapter(private val itemClickListener: OnItemSelectedListener<ProjectItemModel>):BaseListAdapter<ProjectItemModel, ProjectDiffUtil>(
    ProjectDiffUtil()
) {


    override fun onViewHolderCreate(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
         return ProjectViewHolder(ProjectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), itemClickListener)
    }

    override fun onViewHolderBind(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ProjectViewHolder).bind(getItem(position), position)
    }

    class ProjectViewHolder(private val binding: ProjectItemBinding, private val itemClickListener: OnItemSelectedListener<ProjectItemModel>):BaseViewHolder<ProjectItemBinding, ProjectItemModel>(binding) {
        override fun bind(item: ProjectItemModel, position: Int) {
            binding.projectNameTextView.text = item.title
            binding.root.setOnClickListener {
                itemClickListener.onItemSelected(item, position)
            }
        }

    }
}