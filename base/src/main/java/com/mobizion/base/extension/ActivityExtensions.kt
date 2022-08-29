package com.mobizion.base.extension

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.preference.PreferenceManager
import android.view.WindowManager
import androidx.annotation.AnimRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.*

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

fun AppCompatActivity.setStatusBarDrawable(drawable: Drawable){
    val window = window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
    window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
    window.setBackgroundDrawable(drawable)
}


fun Activity.setAppLocale(localeCode: String) {
    val displayMetrics = resources.displayMetrics
    val config = resources.configuration
    config.setLocale(Locale(localeCode.toLowerCase(Locale.ROOT)))
    resources.updateConfiguration(config, displayMetrics)
    onConfigurationChanged(config)
}


fun Context.changeLanguage(languageCode:String):ContextWrapper{
    val configuration = resources.configuration
    var context = this
    val systemLocale:Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
        configuration.locales.get(0)
    }else{
        configuration.locale
    }
    if (systemLocale.language.isNotEmpty() && systemLocale.language != languageCode){
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale)
        } else {
            configuration.locale = locale
        }
        context = createConfigurationContext(configuration);
    }
    return  ContextWrapper(context)
}

/**
 * Start New Activity with animation
 *
 * @param activity the destination class name
 */
fun <A : Activity> Activity.getIntent(activity: Class<A>,requestCode:Int):Intent {
    return Intent(this, activity).also {
        it.putExtra("REQUEST_CODE",requestCode)
    }
}