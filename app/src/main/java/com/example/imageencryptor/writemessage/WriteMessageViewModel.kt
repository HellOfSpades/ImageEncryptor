package com.example.imageencryptor.writemessage

import android.app.Activity
import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import com.example.imageencryptor.databinding.FragmentWriteMessageBinding
import com.example.imageencryptorlibrary.encryption.PPKeyImageEncryptor
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
    //selected pictures uri
    private var picture: Uri? = null
    //image encryptor used to encrypt the message
    lateinit var imageEncryptor: PPKeyImageEncryptor
    //selected pictures bitmap
    private lateinit var imageBitmap: Bitmap
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
    @RequiresApi(Build.VERSION_CODES.Q)
    fun setPicture(data: Uri?){
        picture = data
        if(picture!=null) {
            imageBitmap = getBitmapFromUri(picture!!)
            symbolCapacity = imageEncryptor.getSymbolCapacity(imageBitmap)
            binding.symbolsLeftTextView.text = symbolCapacity.toString()
        }
    }

    /**
     * get bitmap from uri
     */
    fun getBitmapFromUri(uri: Uri): Bitmap{

        val input: InputStream? = activity!!.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(input)
        if (input != null) {
            input.close()
        }
        return bitmap
    }

    /**
     * encrypt the message into the selected image, and save the new image with the fileName
     */
    fun encrypt(message: String, fileName: String){
        encryptOperationScope.launch {
            encryptedBitmap = imageEncryptor.encrypt(message.toByteArray(), imageBitmap)!!
            saveImage(fileName)
        }
    }

    /**
     * saves the image as a png with the given fileName
     */
    suspend fun saveImage(fileName: String){
        withContext(Dispatchers.IO) {
            //declar the output stream variable outside of try/catch so that it can always be closed
            var imageOutputStream: FileOutputStream? = null
            //open, or create the directory where the image will be stored
            var directory = File(
                Environment.getExternalStorageDirectory().toString() + "/ImageEncryptorOutput/"
            )
            if (!directory.exists()) {
                directory.mkdir()
            }
            //create the file
            var outputImageFile: File = File(directory.absolutePath, fileName)
            if (!outputImageFile.exists()) {
                outputImageFile.createNewFile()
            }

            try {
                imageOutputStream = FileOutputStream(outputImageFile)
                encryptedBitmap.compress(Bitmap.CompressFormat.PNG, 100, imageOutputStream)
            } catch (e: IOException) {
                e.printStackTrace()
                Timber.i(e.toString())
            } finally {
                if (imageOutputStream != null) {
                    imageOutputStream.flush()
                    imageOutputStream.close()
                }
            }
        }
    }
}