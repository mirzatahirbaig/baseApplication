package com.mobizion.xutils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

object DarkModePreference {
    private const val DEFAULT_DARK_MODE = false

    fun getDarkModePreference(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(DARK_MODE, DEFAULT_DARK_MODE)
    }

    fun setDarkModePreference(context: Context, isEnabled: Boolean) {
        val sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(DARK_MODE, isEnabled).apply()
    }

    fun applyDarkMode(context: Context) {
        val isEnabled = getDarkModePreference(context)
        if (isEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
