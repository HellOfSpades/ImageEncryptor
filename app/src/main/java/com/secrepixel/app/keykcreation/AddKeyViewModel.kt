package com.secrepixel.app.keykcreation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.secrepixel.app.keyinfo.*
import kotlinx.coroutines.*

/**
 * view model used by the AddKeyFragment
 */
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

    /**
     * insert a key created from the information provided by the user
     */
    fun constructAndInsertKeyIntoDatabase(name: String, modulus: String, publicExponent: String, privateExponent: String?){
        val key = constructKey(name, modulus, publicExponent, privateExponent)
        databaseOperationScope.launch {
            insertKeyIntoDatabase( key)
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