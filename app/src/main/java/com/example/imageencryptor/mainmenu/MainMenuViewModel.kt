package com.example.imageencryptor.mainmenu

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.example.imageencryptor.databinding.FragmentMainMenuBinding
import com.example.imageencryptor.keyinfo.*
import kotlinx.coroutines.*

class MainMenuViewModel(application: Application, val keyDatabase: KeyDatabaseDao) :
    AndroidViewModel(application) {

    lateinit var binding: FragmentMainMenuBinding

    //job and coroutine scope
    private var databaseOperationJob = Job()
    //scope of creating and processing of data gotten from it
    private var databaseOperationScope = CoroutineScope(Dispatchers.Main+databaseOperationJob)


    //all keys in the database
    var keys = keyDatabase.getAllKeys()

    //adapter for the recycle view
    var keyRecycleViewAdapter = KeyRecycleViewAdapter()

    //generate a new key and insert it into the database
    fun generateAndInsertNewKeyToDatabase( name: String){
        databaseOperationScope.launch {
            var generatedKey = generateKey(name)
            insertKeyIntoDatabase(generatedKey)
        }
    }
    //insert key into the database
    private suspend fun insertKeyIntoDatabase( key: Key){
        coroutineScope {
            launch(Dispatchers.IO) {
                keyDatabase.insert(key)
            }
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            keyDatabase.clear()
        }
    }
}