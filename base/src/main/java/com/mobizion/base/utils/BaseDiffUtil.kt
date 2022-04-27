/**
 * created by tahir baig
 * 3 march 2022
 */
package com.mobizion.base.utils

import androidx.recyclerview.widget.DiffUtil

abstract class BaseDiffUtil<T:Any> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

}