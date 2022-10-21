package com.example.imageencryptor.keyinfo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.*

/**
 * database class responsible for storing the keys
 */
@Database(entities = [Key::class], version = 1, exportSchema = false)
abstract class KeyDatabase : RoomDatabase() {

    abstract val keyDatabaseDao: KeyDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: KeyDatabase? = null

        /**
         * get the singleton instance of this class
         */
        fun getInstance(context: Context): KeyDatabase {
            synchronized(this) {
                if (INSTANCE == null) INSTANCE = Room
                    .databaseBuilder(
                        context.applicationContext,
                        KeyDatabase::class.java,
                        "user_keys_table"
                    )
                    .fallbackToDestructiveMigration()
                    .build()

                return INSTANCE!!
            }
        }
    }
}