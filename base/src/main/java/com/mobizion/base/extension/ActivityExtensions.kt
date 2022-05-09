package com.mobizion.base.extension

import android.app.Activity
import android.content.Intent
import androidx.annotation.AnimRes

/**
 * Start New Activity with animation
 *
 * @param activity the destination class name
 */
fun <A : Activity> Activity.startNewActivity(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        it.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        this.startActivity(it)
        finish()
    }
}

/**
 * Start New Activity with animation
 * @param activity the destination class name
 * @param startAnimRes start animation Id
 * @param endAnimRes end animation Id
 */

fun <A : Activity> Activity.startNewActivityWithAnimation(activity: Class<A>, @AnimRes startAnimRes: Int, @AnimRes endAnimRes: Int) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        this.startActivity(it)
        overridePendingTransition(startAnimRes, endAnimRes)
        this.finish()
    }
}