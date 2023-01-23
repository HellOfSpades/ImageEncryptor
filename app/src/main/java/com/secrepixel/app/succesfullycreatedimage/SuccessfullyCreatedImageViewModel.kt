package com.secrepixel.app.succesfullycreatedimage

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.secrepixel.app.MainActivity
import com.secrepixel.app.getUri

class SuccessfullyCreatedImageViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var imageUri: Uri

    fun getImageUri(): Uri{
        return imageUri
    }

    fun setImage(fileName: String, context: Context){
        imageUri = getUri(fileName, context)
    }


    fun getShareImageIntent(): Intent {
        var intent = Intent(Intent.ACTION_SEND)
        intent.setType("image/png")
        intent.putExtra(Intent.EXTRA_STREAM, imageUri)
        return intent
    }
}