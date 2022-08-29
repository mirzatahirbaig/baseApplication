package com.mobizion.xutils

import android.content.Context
import android.widget.Toast

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */

/**
 * @param msg to show toast
 */
fun Context.showToast(msg:String){
    Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
}