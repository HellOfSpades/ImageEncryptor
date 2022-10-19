package com.example.imageencryptor.mainmenu

import android.app.Application
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import com.example.imageencryptor.databinding.FragmentMainMenuBinding
import com.example.imageencryptor.keyinfo.*
import kotlinx.coroutines.*

class MainMenuViewModel(application: Application) :
    AndroidViewModel(application) {

    //get the key database instance using the application context
    val keyDatabase: KeyDatabaseDao = KeyDatabase.getInstance(application.applicationContext).keyDatabaseDao

    //all keys in the database
    var keys = keyDatabase.getAllKeys()

    lateinit var selectedKey: Key

    /**
     * clear the database
     */
    suspend fun clear() {
        withContext(Dispatchers.IO) {
            keyDatabase.clear()
        }
    }
}