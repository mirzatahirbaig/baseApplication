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