package com.example.imageencryptor.mainmenu

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.imageencryptor.keyinfo.KeyDatabaseDao
import java.lang.IllegalArgumentException

class MainMenuViewModelFactory (val application: Application, val keyDatabase: KeyDatabaseDao): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainMenuViewModel::class.java)){
            return MainMenuViewModel(application, keyDatabase) as T
        }
        throw IllegalArgumentException()
    }
}