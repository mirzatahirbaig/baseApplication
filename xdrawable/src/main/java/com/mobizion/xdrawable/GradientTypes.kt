package com.mobizion.xdrawable

import android.graphics.drawable.GradientDrawable
import androidx.annotation.IntDef

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */

/**
 *public static final int LINE = 2;
public static final int LINEAR_GRADIENT = 0;
public static final int OVAL = 1;
public static final int RADIAL_GRADIENT = 1;
public static final int RECTANGLE = 0;
public static final int RING = 3;
public static final int SWEEP_GRADIENT = 2;
 */


enum class GradientTypes(val gradientType:Int) {
    LINE(GradientDrawable.LINE),LINEAR_GRADIENT(GradientDrawable.LINEAR_GRADIENT),
    OVAL(GradientDrawable.OVAL),RADIAL_GRADIENT(GradientDrawable.RADIAL_GRADIENT),
    RECTANGLE(GradientDrawable.RECTANGLE),RING(GradientDrawable.RING),
    SWEEP_GRADIENT(GradientDrawable.SWEEP_GRADIENT)
}