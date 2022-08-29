package com.mobizion.xutils

import android.content.res.Resources


/**
 * convert int to db
 */

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()