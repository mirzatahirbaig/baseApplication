package com.mobizion.xnetwork.repository

import com.mobizion.xnetwork.response.XNetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */
abstract class XBaseApiRepo {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): XNetworkResponse<T> {
        return withContext(Dispatchers.IO){
            try {
                XNetworkResponse.Success(apiCall.invoke())
            } catch (throwable: Exception) {
                when (throwable) {
                    is HttpException -> {
                        XNetworkResponse.Failure(throwable, throwable.message, throwable.code())
                    }
                    else -> {
                        XNetworkResponse.Failure(throwable, throwable.message, null)
                    }
                }
            }
        }
    }
}