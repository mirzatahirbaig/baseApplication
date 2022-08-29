package com.mobizion.xdrawable

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */

fun getLayeredDrawable(drawables:Array<Drawable>) = LayerDrawable(drawables)