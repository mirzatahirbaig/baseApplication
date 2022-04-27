/**
 * created by tahir baig
 * 3 march 2022
 */
package com.mobizion.base.dao

import androidx.room.*

interface BaseDao<in T> {
    /**
     * Insert an object in the database.
     *
     * @param obj the object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: T):Long

    /**
     * Insert an array of objects in the database.
     * @param obj the objects to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: List<T>):List<Long>

    /**
     * Update an object from the database.
     * @param obj the object to be updated
     */
    @Update
    suspend fun update(obj: T):Int

    /**
     * Delete an object from the database
     * @param obj the object to be deleted
     */
    @Delete
    suspend fun delete(obj: T):Int
}