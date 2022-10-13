package com.example.imageencryptor.decryptmessage

import android.app.Activity
import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import com.example.imageencryptor.keyinfo.Key
import com.example.imageencryptor.keyinfo.encryptor
import com.example.imageencryptorlibrary.encryption.PPKeyImageEncryptor
import java.io.InputStream

class DecryptMessageViewModel(application: Application) : AndroidViewModel(application) {

    private var picture: Uri? = null
    private lateinit var imageBitmap: Bitmap
    lateinit var imageEncryptor: PPKeyImageEncryptor
    var activity: Activity? = null

    lateinit var key: Key

    @RequiresApi(Build.VERSION_CODES.Q)
    fun setPicture(data: Uri?){
        picture = data
        if(picture!=null) {
            imageBitmap = getBitmapFromUri(picture!!)
        }
    }

    fun getPicture():Uri?{
        return picture
    }

    fun getBitmapFromUri(uri: Uri): Bitmap {

        val input: InputStream? = activity!!.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(input)
        if (input != null) {
            input.close()
        }
        return bitmap
    }

    fun decrypt(): String?{
        var output: String? = null
        try{
            output = String(imageEncryptor.decrypt(imageBitmap)!!)
        }catch (e: UnsupportedOperationException){

        }
        return output
    }
}