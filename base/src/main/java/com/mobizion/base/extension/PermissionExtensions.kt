package com.mobizion.base.extension

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Check whether to show rational dialog to the user or not.
 *
 * @param permission the permission against which the rational has to be shown
 */

fun Activity.showPermissionRational(permission: String):Boolean{
    return ActivityCompat.shouldShowRequestPermissionRationale(this,permission)
}

/**
 * @param permission to check whether app has or not
 * @return true or false base on whether app has permission or not
 */

fun Context.appHasPermission(permission: String): Boolean {
    if (permission == android.Manifest.permission.WRITE_EXTERNAL_STORAGE){
        return ContextCompat.checkSelfPermission(this, permission) ==
                PackageManager.PERMISSION_GRANTED || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
    }
    return ContextCompat.checkSelfPermission(this, permission) ==
            PackageManager.PERMISSION_GRANTED
}