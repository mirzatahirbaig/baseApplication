/**
 * created by tahir baig
 * 3 march 2022
 */
package com.mobizion.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<T,D: DiffUtil.ItemCallback<T>>(utils:D):ListAdapter<T,RecyclerView.ViewHolder>(utils) {

    abstract fun onViewHolderCreate(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder

    abstract fun onViewHolderBind(holder: RecyclerView.ViewHolder, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = onViewHolderCreate(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onViewHolderBind(holder, position)
    }

}