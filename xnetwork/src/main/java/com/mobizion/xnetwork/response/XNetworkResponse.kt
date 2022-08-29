package com.mobizion.xnetwork.response

import android.view.View
import java.lang.Exception

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */
sealed class XNetworkResponse<out T>{
    data class Success<out T> (val value:T): XNetworkResponse<T>()
    data class Failure(val exception: Exception,val message:String?,val code:Int?): XNetworkResponse<Nothing>()
}

fun <T> XNetworkResponse<T>.getValueFromResponse(view: View):T?{
    return when(this){
        is XNetworkResponse.Success -> {
            this.value
        }
        is XNetworkResponse.Failure -> {
            null
        }
    }
}
