package com.example.imageencryptor.keyinfo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Key database operations
 */
@Dao
interface KeyDatabaseDao {

    /**
     * inserts a new key into the database
     */
    @Insert
    fun insert(key: Key)

    /**
     * updates an existing key in the database
     */
    @Update
    fun update(key: Key)

    /**
     * gets a key from the database using its unique id
     */
    @Query("SELECT * FROM user_keys_table WHERE id = :id")
    fun get(id: Long): Key

    /**
     * deletes all keys in the database
     */
    @Query("DELETE FROM user_keys_table")
    fun clear()

    /**
     * returns a list of all keys in the database
     */
    @Query("SELECT * FROM user_keys_table")
    fun getAllKeys(): LiveData<List<Key>>

    /**
     * deletes a key in the database
     */
    @Query("DELETE FROM user_keys_table WHERE id = :id")
    fun deleteKey(id: Long)
}