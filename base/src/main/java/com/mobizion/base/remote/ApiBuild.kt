/**
 * created by tahir baig
 * 3 march 2022
 */

package com.mobizion.base.remote

import com.mobizion.base.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * this will create api when we have a single base url for the whole project
 */

fun <Api> buildApi(
    api:Class<Api>,
    baseUrl:String
):Api{
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(OkHttpClient.Builder().also { client ->
            if (BuildConfig.DEBUG){
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                client.addInterceptor(logging)
            }
        }.build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(api)

}


/**
 * this will create api when we have multiple urls
 */

fun <Api> buildApi(
    api:Class<Api>
):Api{
    return Retrofit.Builder()
        .client(OkHttpClient.Builder().also { client ->
            if (BuildConfig.DEBUG){
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                client.addInterceptor(logging)
            }
        }.build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(api)

}