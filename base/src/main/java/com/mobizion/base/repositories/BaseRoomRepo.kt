
/**
 * created by tahir baig
 * 3 march 2022
 */
package com.mobizion.base.repositories

interface BaseRoomRepo<T> {
    suspend fun insert(value:T)
    suspend fun insert(value:List<T>)
    suspend fun update(value:T)
    suspend fun delete(value:T)
}