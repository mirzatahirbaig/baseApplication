package com.mobizion.xbaseroom.dao

import androidx.room.*

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */

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