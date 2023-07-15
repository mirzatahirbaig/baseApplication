package com.mobizion.xutils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mobizion.xutils.decorators.SpacesItemDecoration

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */

fun RecyclerView.setLayoutManager(): LinearLayoutManager {
    val manager = LinearLayoutManager(context)
    layoutManager = manager
    return manager
}

fun RecyclerView.setHorizontalLayoutManager() {
    this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
}

fun RecyclerView.setHorizontalLayoutManager(widthPercentage:Float, reverse:Boolean, spacing: Int = 0) {
    val decoration = SpacesItemDecoration(spacing)
    this.addItemDecoration(decoration)
    this.layoutManager = object:LinearLayoutManager(context, HORIZONTAL, reverse){
        override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
            lp?.let {
                it.width = (width*widthPercentage).toInt()-spacing
                it.height = 55.dp
            }
            return true
        }
    }
}

fun RecyclerView.setGridManager(spanCount: Int = 2,space:Int = 5) {
    this.layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
    val decoration = SpacesItemDecoration(space)
    this.addItemDecoration(decoration)
}

fun RecyclerView.setStackFromEndManager() {
    val manager = LinearLayoutManager(context)
    manager.stackFromEnd = true
    this.layoutManager = manager
}

