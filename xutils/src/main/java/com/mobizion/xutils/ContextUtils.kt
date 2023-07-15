package com.mobizion.xutils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import android.app.ActivityManager

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

fun Context.themImageView(imageView: ImageView, drawable:Int){

    val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
    var color = Color.parseColor("#2C2C2C")
    if (isDarkMode){
        color =  Color.parseColor("#E6E6E6")
    }
    ContextCompat.getDrawable(this, drawable)?.mutate()?.let {
        it.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        imageView.setImageDrawable(it)}
}
fun getThemeColor(reverse: Boolean = false): Int{
    val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
    var color = Color.parseColor("#E6E6E6")
    if (isDarkMode){
        color = Color.parseColor("#2C2C2C") //Dark gary color
    }
    if (reverse){
        color = if (isDarkMode){
            Color.parseColor("#E6E6E6")
        }else{
            Color.parseColor("#2C2C2C") //Dark gary color
        }

    }
    return color
}
fun Context.getXColor(int: Int): Int{
   return ContextCompat.getColor(this,int)
}

fun isActivityInBackStack(context: Context, activityClass: Class<*>): Boolean {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val taskInfo = activityManager.getRunningTasks(1)
    val componentInfo = taskInfo[0].topActivity

    return componentInfo!!.className == activityClass.name
}
