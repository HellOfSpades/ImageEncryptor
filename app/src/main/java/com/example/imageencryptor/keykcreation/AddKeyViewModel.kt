package com.example.imageencryptor.keykcreation

import android.app.Application
import android.view.KeyCharacterMap
import androidx.lifecycle.AndroidViewModel
import com.example.imageencryptor.keyinfo.Key
import com.example.imageencryptor.keyinfo.KeyDatabase
import com.example.imageencryptor.keyinfo.KeyDatabaseDao
import com.example.imageencryptor.keyinfo.generateKey
import kotlinx.coroutines.*
import timber.log.Timber

class AddKeyViewModel(application: Application) : AndroidViewModel(application) {

    //get the key database instance using the application context
    val keyDatabase: KeyDatabaseDao = KeyDatabase.getInstance(application.applicationContext).keyDatabaseDao

    //job and coroutine scope
    private var databaseOperationJob = Job()
    //scope of creating and processing of data gotten from it
    private var databaseOperationScope = CoroutineScope(Dispatchers.Main+databaseOperationJob)

    /**
     * generate a new key and insert it into the database
     */
    fun generateAndInsertNewKeyToDatabase( name: String){
        databaseOperationScope.launch {
            var generatedKey = generateKey(name)
            insertKeyIntoDatabase(generatedKey)
        }
    }
    fun constructAndInsertKeyIntoDatabase(name: String, modulus: String, publicExponent: String, privateExponent: String?){
        databaseOperationScope.launch {
            insertKeyIntoDatabase( Key(name, modulus, publicExponent, privateExponent))
        }
    }

    /**
     * insert key into the database
     */
    private suspend fun insertKeyIntoDatabase( key: Key){
        coroutineScope {
            launch(Dispatchers.IO) {
                keyDatabase.insert(key)
            }
        }
    }
}