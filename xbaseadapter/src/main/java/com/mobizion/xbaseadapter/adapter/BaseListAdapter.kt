package com.mobizion.xbaseadapter.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */

abstract class BaseListAdapter<T,D: DiffUtil.ItemCallback<T>>(utils:D):
    ListAdapter<T, RecyclerView.ViewHolder>(utils) {

    abstract fun onViewHolderCreate(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder

    abstract fun onViewHolderBind(holder: RecyclerView.ViewHolder, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = onViewHolderCreate(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onViewHolderBind(holder, position)
    }

}