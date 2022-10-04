package com.example.imageencryptor.keyinfo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface KeyDatabaseDao {

    @Insert
    fun insert(key: Key)

    @Update
    fun update(key: Key)

    @Query("SELECT * FROM user_keys_table WHERE id = :id")
    fun get(id: Long): Key

    @Query("DELETE FROM user_keys_table")
    fun clear()

    @Query("SELECT * FROM user_keys_table")
    fun getAllKeys(): LiveData<List<Key>>

    @Query("DELETE FROM user_keys_table WHERE id = :id")
    fun deleteKey(id: Long)
}