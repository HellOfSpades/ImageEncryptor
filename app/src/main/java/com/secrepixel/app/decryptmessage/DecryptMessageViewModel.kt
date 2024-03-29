package com.secrepixel.app.decryptmessage

import android.app.Activity
import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import com.example.imageencryptorlibrary.encryption.PPKeyImageEncryptor
import com.example.imageencryptorlibrary.encryption.CantDecryptImageException
import com.secrepixel.app.keyinfo.Key
import java.io.InputStream
import java.lang.Exception

/**
 * view model used by the DecryptMessageFragment
 */
class DecryptMessageViewModel(application: Application) : AndroidViewModel(application) {

    //selected pictures uri
    private var picture: Uri? = null
    //selected pictures bitmap
    private var imageBitmap: Bitmap? = null
    //the image encryptor we use to decrypt the message
    lateinit var imageEncryptor: PPKeyImageEncryptor
    var activity: Activity? = null

    lateinit var key: Key

    /**
     * sets the picture that will be encrypted
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun setPicture(data: Uri?){
        picture = data
        if(picture!=null) {
            imageBitmap = getBitmapFromUri(picture!!)
        }
    }

    /**
     * returns the picture that is supposed to be encrypted
     */
    fun getPicture():Uri?{
        return picture
    }

    /**
     * converts images Uri to Bitmap
     */
    fun getBitmapFromUri(uri: Uri): Bitmap {

        val input: InputStream? = activity!!.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(input)
        if (input != null) {
            input.close()
        }
        return bitmap
    }

    /**
     * decrypts the set picture using the key passed in args
     */
    fun decrypt(): String?{
        if(imageBitmap==null)return "Please select an image"

        var output: String?
        try{
            output = String(imageEncryptor.decrypt(imageBitmap!!)!!)
        }catch (e: CantDecryptImageException){
            output = null
        }
        return output
    }
}