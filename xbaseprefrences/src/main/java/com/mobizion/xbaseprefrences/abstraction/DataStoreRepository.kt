package com.mobizion.xbaseprefrences.abstraction

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 12/09/2022
 */
interface DataStoreRepository {
    suspend fun put(key: String, value: String)
    suspend fun put(key: String, value: Int)
    suspend fun put(key: String, value: Boolean)
    suspend fun getBoolean(key: String):Boolean
    suspend fun getString(key: String):String
    suspend fun getInt(key: String):Int
    suspend fun clearData()
}