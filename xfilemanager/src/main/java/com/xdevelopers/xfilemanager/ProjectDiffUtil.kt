package com.xdevelopers.xfilemanager

import com.mobizion.xbaseadapter.difutil.BaseDiffUtil
import com.xdevelopers.xfilemanager.model.ProjectItemModel

class ProjectDiffUtil: BaseDiffUtil<ProjectItemModel>() {

    override fun areContentsTheSame(oldItem: ProjectItemModel, newItem: ProjectItemModel): Boolean {
        return true
    }

}