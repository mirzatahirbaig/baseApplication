package com.mobizion.xbaseadapter.difutil

import androidx.recyclerview.widget.DiffUtil

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */

abstract class BaseDiffUtil<T : Any> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

}