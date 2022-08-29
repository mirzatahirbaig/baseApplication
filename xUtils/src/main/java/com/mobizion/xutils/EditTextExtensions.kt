package com.mobizion.xutils

import android.widget.EditText

fun EditText.isEmpty():Boolean{
    return this.text.toString().trim().isEmpty()
}

fun EditText.text():String{
    return this.text.toString().trim()
}

fun EditText.password():String{
    return this.text.toString()
}