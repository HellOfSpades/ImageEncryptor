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


class WriteMessageViewModel(application: Application) : AndroidViewModel(application){



    lateinit var binding: FragmentWriteMessageBinding
    private var picture: Uri? = null
    lateinit var imageEncryptor: PPKeyImageEncryptor
    private lateinit var imageBitmap: Bitmap
    var activity: Activity? = null
    var symbolCapacity: Int = 0
    private lateinit var encryptedBitmap: Bitmap

    //job for using the encryptor
    private var encryptOperationJob = Job()
    //encryption scope
    private var encryptOperationScope = CoroutineScope(Dispatchers.Main+encryptOperationJob)

    fun getPicture(): Uri?{
        return picture
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    fun setPicture(data: Uri?){
        picture = data
        if(picture!=null) {
            imageBitmap = getBitmapFromUri(picture!!)
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

    fun encrypt(message: String, fileName: String){
        encryptOperationScope.launch {
            encryptedBitmap = imageEncryptor.encrypt(message.toByteArray(), imageBitmap)!!
            Timber.i("encrypted: "+message)
            saveImage(fileName)
        }
    }

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
                Timber.i("created new file")
            }

            try {
                imageOutputStream = FileOutputStream(outputImageFile)
                encryptedBitmap.compress(Bitmap.CompressFormat.PNG, 100, imageOutputStream)
                Timber.i("successfully saved image to " + outputImageFile.absolutePath)
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