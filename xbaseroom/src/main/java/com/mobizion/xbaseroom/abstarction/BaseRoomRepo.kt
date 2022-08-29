package com.mobizion.xbaseroom.abstarction

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */

interface BaseRoomRepo<T> {
    suspend fun insert(value:T):Long
    suspend fun insert(value:List<T>):List<Long>
    suspend fun update(value:T):Int
    suspend fun delete(value:T):Int
}