package com.example.imageencryptor.mainmenu

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.example.imageencryptor.databinding.FragmentMainMenuBinding
import com.example.imageencryptor.keyinfo.*
import kotlinx.coroutines.*

class MainMenuViewModel(application: Application) :
    AndroidViewModel(application) {

    //get the key database instance using the application context
    val keyDatabase: KeyDatabaseDao = KeyDatabase.getInstance(application.applicationContext).keyDatabaseDao

    lateinit var binding: FragmentMainMenuBinding

    //all keys in the database
    var keys = keyDatabase.getAllKeys()

    //adapter for the recycle view
    var keyRecycleViewAdapter = KeyRecycleViewAdapter()


    /**
     * clear the database
     */
    suspend fun clear() {
        withContext(Dispatchers.IO) {
            keyDatabase.clear()
        }
    }
}