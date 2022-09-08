package com.example.imageencryptor.writemessage

import android.app.Activity
import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.AndroidViewModel
import com.example.imageencryptor.databinding.FragmentWriteMessageBinding
import com.example.imageencryptorlibrary.encryption.PPKeyImageEncryptor
import timber.log.Timber
import java.io.InputStream


class WriteMessageViewModel(application: Application) : AndroidViewModel(application){



    lateinit var binding: FragmentWriteMessageBinding
    private var picture: Uri? = null
    lateinit var imageEncryptor: PPKeyImageEncryptor
    private lateinit var imageBitmap: Bitmap
    var activity: Activity? = null
    var symbolCapacity: Int = 0

    init {
        imageEncryptor = PPKeyImageEncryptor()
        imageEncryptor.makeKeyPair(2048)
    }


    fun getPicture(): Uri?{
        return picture
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    fun setPicture(data: Uri?){
        picture = data
        if(picture!=null) {
            imageBitmap = getBitmapFromUri(picture!!)
            var color = imageBitmap.getPixel(0,0)
            symbolCapacity = imageEncryptor.getSymbolCapacity(imageBitmap)
            binding.symbolsLeftTextView.text = symbolCapacity.toString()
        }
    }

    fun getBitmapFromUri(uri: Uri): Bitmap{

        val input: InputStream? = activity!!.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(input)
        if (input != null) {
            input.close()
        }
        return bitmap
    }
}