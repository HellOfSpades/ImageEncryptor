package com.secrepixel.imageencryptor

import android.app.Application
import timber.log.Timber

class ImageEncryptorApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}