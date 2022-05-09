package com.mobizion.base.extension

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mobizion.base.decorator.SpacesItemDecoration

fun RecyclerView.setLayoutManager(): LinearLayoutManager {
    val manager = LinearLayoutManager(context)
    layoutManager = manager
    return manager
}

fun RecyclerView.setHorizontalLayoutManager() {
    this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
}

fun RecyclerView.setGridManager() {
    this.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    val decoration = SpacesItemDecoration(5)
    this.addItemDecoration(decoration)
}

fun RecyclerView.setGridManager(spanCount: Int) {
    this.layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
}

fun RecyclerView.setStackFromEndManager() {
    val manager = LinearLayoutManager(context)
    manager.stackFromEnd = true
    this.layoutManager = manager
}