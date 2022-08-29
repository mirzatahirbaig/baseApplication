package com.mobizion.xbaseadapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */

abstract class BaseViewHolder<B:ViewBinding,T>(binding: B):RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(item:T,position:Int)
}