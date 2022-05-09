package com.mobizion.base.extension

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.snackbar.Snackbar

fun View.showSimpleSnackBar(msg: String) {
    Snackbar.make(
        this,
        msg,
        Snackbar.LENGTH_LONG
    ).show()
}

fun View.showSnackBar(msg: String, gravity: Int, backgroundColor: Int) {
    val snackBar = getSnackbar(msg)
    val snackBarView = snackBar.view
    val params: FrameLayout.LayoutParams = snackBarView.layoutParams as FrameLayout.LayoutParams
    params.gravity = gravity
    params.height = FrameLayout.LayoutParams.WRAP_CONTENT
    params.width = FrameLayout.LayoutParams.MATCH_PARENT
    snackBarView.layoutParams = params
    snackBarView.setBackgroundColor(backgroundColor)
    snackBar.show()
}

fun View.getSnackbar(msg: String): Snackbar = Snackbar.make(this, msg, Snackbar.LENGTH_SHORT)

fun View.showErrorSnackBar(msg: String) {
    showSnackBar(msg, Gravity.TOP, Color.RED)
}

fun View.showSuccessSnackBar(msg: String) {
    showSnackBar(msg, Gravity.TOP, Color.GREEN)
}