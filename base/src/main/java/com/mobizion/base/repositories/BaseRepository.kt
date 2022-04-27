/**
 * created by tahir baig
 * 3 march 2022
 */
package com.mobizion.base.repositories

import com.mobizion.base.response.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepository {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): NetworkResponse<T> {
        return withContext(Dispatchers.IO) {
            try {
                NetworkResponse.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        NetworkResponse.Failure(
                            false,
                            throwable.code(),
                            throwable.response()?.errorBody(),
                            throwable.response()?.message()
                        )
                    }
                    else -> {
                        NetworkResponse.Failure(true, null, null, "Check Your Internet Connection")
                    }
                }
            }
        }
    }

}