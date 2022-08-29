package com.mobizion.xbaseadapter.listeners

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */

fun interface OnItemClickedListener<T> {
    fun onItemClicked(media:T, position:Int)
}