/**
 * created by tahir baig
 * 3 march 2022
 */

package com.mobizion.base.response

sealed class NetworkResponse<out T>{
    data class Success<out T> (val value:T): NetworkResponse<T>()
    data class Failure(
        val isNetworkError:Boolean,
        val errorCode:Int?,
        val errorBody: okhttp3.ResponseBody?,
        val message:String?
    ): NetworkResponse<Nothing>()
}
