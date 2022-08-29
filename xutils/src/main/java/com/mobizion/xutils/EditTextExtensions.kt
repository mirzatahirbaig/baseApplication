package com.mobizion.xutils

import android.widget.EditText

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */

fun EditText.isEmpty():Boolean{
    return this.text.toString().trim().isEmpty()
}

fun EditText.text():String{
    return this.text.toString().trim()
}

fun EditText.password():String{
    return this.text.toString()
}