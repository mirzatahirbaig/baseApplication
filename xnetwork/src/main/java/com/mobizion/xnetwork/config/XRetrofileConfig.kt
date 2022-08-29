package com.mobizion.xnetwork.config

import com.mobizion.xnetwork.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */

fun <Api> configureApi(
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