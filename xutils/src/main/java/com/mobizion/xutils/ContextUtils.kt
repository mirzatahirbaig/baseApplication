package com.mobizion.xutils

import android.annotation.SuppressLint
import android.content.Context

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 01/09/2022
 */

@SuppressLint("DiscouragedApi")
fun Context.getStatusBarHeight():Int{
    val statusBarHeightId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return resources.getDimensionPixelSize(statusBarHeightId)
}