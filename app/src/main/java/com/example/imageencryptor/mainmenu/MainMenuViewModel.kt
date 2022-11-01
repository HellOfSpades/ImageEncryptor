package com.example.imageencryptor.mainmenu

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.imageencryptor.keyinfo.*
import kotlinx.coroutines.*

/**
 * view model used by the MainMenuFragment
 */
class MainMenuViewModel(application: Application) :
    AndroidViewModel(application) {

    //get the key database instance using the application context
    val keyDatabase: KeyDatabaseDao = KeyDatabase.getInstance(application.applicationContext).keyDatabaseDao

    //all keys in the database
    var keys = keyDatabase.getAllKeys()

    //job and coroutine scope
    private var databaseOperationJob = Job()
    //scope of creating and processing of data gotten from it
    private var databaseOperationScope = CoroutineScope(Dispatchers.Main+databaseOperationJob)

    var selectedKey: Key? = null

    /**
     * when clicking the delete button
     */
    fun onClickDeleteKeyButton(){
        if(selectedKey!=null){
            databaseOperationScope.launch {
                removeKey(selectedKey!!)
            }
        }
    }

    /**
     * remove key from database
     */
    suspend fun removeKey(key: Key){
        Dispatchers.IO {
            keyDatabase.deleteKey(key.id)
        }
    }
}