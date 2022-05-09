package com.mobizion.base.extension

import android.content.Context
import android.widget.Toast

/**
 * @param msg to show toast
 */
fun Context.showToast(msg:String){
    Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
}