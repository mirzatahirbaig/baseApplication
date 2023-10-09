package com.mobizion.xutils

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.invisible(isInvisible: Boolean) {
    visibility = if (isInvisible) View.INVISIBLE else View.VISIBLE
}

fun View.enabled(enabled: Boolean) {
    isEnabled = enabled
}

fun View.alpha(alpha:Float) {
    this.alpha = alpha
}

fun View.setBackground(drawable: Drawable,states:Array<IntArray>?,colors:IntArray?){
    background = drawable
    backgroundTintList = ColorStateList(states,colors)
}

fun View.setMargins(l: Int, t: Int, r: Int, b: Int) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val p: ViewGroup.MarginLayoutParams = layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(l, t, r, b)
        requestLayout()
    }
}

fun View.selectedState(selectedColor: Int = Color.WHITE, unSelectedColor: Int = Color.WHITE): ColorStateList {
    return ColorStateList(
        arrayOf(
            intArrayOf(android.R.attr.state_selected),
            intArrayOf(-android.R.attr.state_selected)
        ), intArrayOf(selectedColor, unSelectedColor)
    )
}

fun View.getStatusBarHeight():Int = context.getStatusBarHeight()

/**
 *  methood to update the view size according to design screen ratio in the device app
 * @Param widthFactor will use to update the width of the view
 * @Param heightFactor Factor will use to update the height of the view
 */

fun ImageView.updateSize(widthFactor:Float,heightFactor: Float){
    val displayMetrics: DisplayMetrics = context.resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels
    val screenHeight = displayMetrics.heightPixels
    val desiredWidth = (screenWidth * widthFactor).toInt()
    val desiredHeight = (screenHeight *heightFactor).toInt()
    val params = layoutParams
    params.width = desiredWidth
    params.height = desiredHeight
    layoutParams = params
}

/**
 * this method is specifically used to update the height of bottom view in chat screen
 * when keyboard is appear and disappear
 */

fun View.updateHeight(heightFactor: Float,keyboardHeight:Int = 0){
    val displayMetrics: DisplayMetrics = context.resources.displayMetrics
    val screenHeight = displayMetrics.heightPixels
    val desiredHeight = if (keyboardHeight == 0){
        (screenHeight *heightFactor).toInt()
    }else{
        val newHeight = screenHeight - keyboardHeight
        (newHeight *heightFactor).toInt()
    }
    val params = layoutParams
    params.height = desiredHeight
    layoutParams = params
    requestLayout()
}
