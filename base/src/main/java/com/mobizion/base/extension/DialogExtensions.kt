package com.mobizion.base.extension

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.viewbinding.ViewBinding

fun Context.createAlertDialog(bindings: ViewBinding): AlertDialog {
    return AlertDialog.Builder(this).also {
        it.setView(bindings.root)
    }.create().also { dialog ->
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}