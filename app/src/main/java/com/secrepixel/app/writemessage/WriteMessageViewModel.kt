package com.secrepixel.app.writemessage

import android.Manifest
import android.app.Activity
import android.app.Application
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.Navigation
import com.secrepixel.app.databinding.FragmentWriteMessageBinding
import com.example.imageencryptorlibrary.encryption.PPKeyImageEncryptor
import com.secrepixel.app.*
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * view model used by the WriteMessageFragment
 */
class WriteMessageViewModel(application: Application) : AndroidViewModel(application){


    //fragment binding
    lateinit var binding: FragmentWriteMessageBinding
    //current fragment
    lateinit var fragment: WriteMessageFragment
    //selected pictures uri
    private var picture: Uri? = null
    //image encryptor used to encrypt the message
    lateinit var imageEncryptor: PPKeyImageEncryptor
    //selected pictures bitmap
    private var imageBitmap: Bitmap? = null
    var activity: Activity? = null
    //symbol capacity of the selected picture
    var symbolCapacity: Int = 0
    private lateinit var encryptedBitmap: Bitmap

    //job for using the encryptor
    private var encryptOperationJob = Job()
    //encryption scope
    private var encryptOperationScope = CoroutineScope(Dispatchers.Main+encryptOperationJob)

    fun getPicture(): Uri?{
        return picture
    }

    /**
     * set the picture to be encrypted with a message
     */
    fun setPicture(data: Uri?){
        picture = data
        if(picture!=null) {
            imageBitmap = getBitmapFromUri(activity!!, picture!!)
            symbolCapacity = imageEncryptor.getSymbolCapacity(imageBitmap!!)
        }
    }

    /**
     * encrypt the message into the selected image, and save the new image with the fileName
     * return saved image file name
     * first value, if null, image successfully saved, else fail
     * second value, if successfully saved = url
     * after finishing saving it navigates to another activity
     */
    fun encrypt(message: String, progress: ProgressDialog): String{
        //check if the user gave permission to write to external storage
        if(!(hasWriteExternalStoragePermission(getApplication()) || min29Sdk())){
            Timber.i("permission denied")
            requestWriteExternalStoragePermission(activity!!)
            throw java.lang.RuntimeException("can't save image to your phone without permission")
        }

        var fileName = getFileName()

        if(imageBitmap==null){
            throw java.lang.RuntimeException("Please select an image");
        }
        if(message.length>symbolCapacity){
            throw java.lang.RuntimeException("Your message is too big");
        }
        encryptOperationScope.launch {
            encryptedBitmap = imageEncryptor.encrypt(message.toByteArray(), imageBitmap!!)!!
            val uri = saveImage(getApplication(),fileName, encryptedBitmap, activity!!.applicationContext)
            progress.dismiss()
            Navigation.findNavController(binding.makeImageButton)
                .navigate(WriteMessageFragmentDirections.actionWriteMessageFragmentToSuccessfullyCreatedImageFragment(uri))

        }

        return fileName
    }

    /**
     * returns the filename to be used for saving the image
     */
    private fun getFileName(): String {
        return getIncrementedFileName()
    }
}