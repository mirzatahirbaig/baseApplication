/**
 * created by tahir baig
 * 3 march 2022
 */

package com.mobizion.base.response

import android.view.View
import com.mobizion.base.errors.APIError
import com.mobizion.base.extension.showErrorSnackBar

sealed class NetworkResponse<out T>{
    data class Success<out T> (val value:T): NetworkResponse<T>()
    data class Failure(
        val isHttpException:Boolean,
        val error:APIError?,
        val message:String?
    ): NetworkResponse<Nothing>()
}


fun <T> NetworkResponse<T>.getValueFromResponse(view: View):T?{
    return when(this){
        is NetworkResponse.Success -> {
            this.value
        }
        is NetworkResponse.Failure -> {
            this.message?.let { message ->
                view.showErrorSnackBar(message)
            }
            null
        }
    }
}
