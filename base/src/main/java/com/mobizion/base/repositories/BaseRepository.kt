package com.mobizion.base.repositories

import com.google.gson.Gson
import com.mobizion.base.errors.APIError
import com.mobizion.base.response.NetworkResponse
import retrofit2.HttpException

abstract class BaseRepository {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): NetworkResponse<T> {
        return try {
            NetworkResponse.Success(apiCall.invoke())
        } catch (throwable: Exception) {
            when (throwable) {
                is HttpException -> {
                    throwable.parseException()
                }
                else -> {
                    NetworkResponse.Failure(
                        false,
                        null,
                        "Check Your Internet Connection"
                    )
                }
            }
        }
    }

    private fun HttpException.parseException(): NetworkResponse.Failure{
        var errorResponse = NetworkResponse.Failure(
            true,
            APIError(
                this.code(),
                message()
            ),
            message()
        )
        try {
            response()?.let { response ->
                response.errorBody()?.let { errorBody ->
                    val apiError = Gson().fromJson(errorBody.charStream(), APIError::class.java)
                    errorResponse = NetworkResponse.Failure(
                        true,
                        APIError(
                            response.code(),
                            apiError.message
                        ),
                        apiError.message
                    )
                }
            }
        }catch (exception:Exception){
        }
        return errorResponse
    }
}