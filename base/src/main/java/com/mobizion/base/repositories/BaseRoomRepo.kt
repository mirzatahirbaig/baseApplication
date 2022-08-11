
/**
 * created by tahir baig
 * 3 march 2022
 */
package com.mobizion.base.repositories

interface BaseRoomRepo<T> {
    suspend fun insert(value:T):Long
    suspend fun insert(value:List<T>):List<Long>
    suspend fun update(value:T):Int
    suspend fun delete(value:T):Int
}