/**
 * created by tahir baig
 * 3 march 2022
 */

package com.mobizion.base.view.holder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<B:ViewBinding,T>(binding: B):RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(item:T,position:Int)
}