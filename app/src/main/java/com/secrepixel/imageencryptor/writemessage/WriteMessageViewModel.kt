package com.secrepixel.imageencryptor.writemessage

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.ContentResolver
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import com.secrepixel.imageencryptor.databinding.FragmentWriteMessageBinding
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
    private var imageBitmap: Bitmap? = null
    var activity: Activity? = null
    //symbol capacity of the selected picture
    var symbolCapacity: Int = 0
    private lateinit var encryptedBitmap: Bitmap

    //job for using the encryptor
    private var encryptOperationJob = Job()
    //encryption scope
    private var encryptOperationScope = CoroutineScope(Dispatchers.Main+encryptOperationJob)

    //permission request codes
    private val WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE: Int = 0

    fun getPicture(): Uri?{
        return picture
    }

    /**
     * set the picture to be encrypted with a message
     */
    fun setPicture(data: Uri?){
        picture = data
        if(picture!=null) {
            imageBitmap = getBitmapFromUri(picture!!)
            symbolCapacity = imageEncryptor.getSymbolCapacity(imageBitmap!!)
        }
    }

    /**
     * returns whether or not the sdk version of the device is 29 or higher
     */
    fun min29Sdk(): Boolean{
        return Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q
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
     * return whether or not the message was successfully encrypted into the image
     */
    fun encrypt(message: String, fileName: String): String?{
        if(imageBitmap==null){
            return "Please select an image"
        }
        if(message.length>symbolCapacity){
            return "Your message is too big"
        }

        encryptOperationScope.launch {
            encryptedBitmap = imageEncryptor.encrypt(message.toByteArray(), imageBitmap!!)!!
            saveImage(fileName)
        }
        return null
    }

    /**
     * saves the image as a png with the given fileName
     */
    suspend fun saveImage(fileName: String){
        withContext(Dispatchers.IO) {
            if(min29Sdk()){
                saveImageSdk29(fileName)
            }else{
                saveImageSdk28(fileName)
            }
        }
    }

    /**
     * save image with this method if the sdk is 29 or higher
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImageSdk29(fileName: String){
        val imageCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.WIDTH, encryptedBitmap.width)
            put(MediaStore.Images.Media.HEIGHT, encryptedBitmap.height)
        }

        try{
            val contentResolver = getApplication<Application>().contentResolver

            contentResolver.insert(imageCollection, contentValues)?.also {uri->
                contentResolver.openOutputStream(uri).use {outputStream ->
                    encryptedBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                }
            }
        }catch (e: IOException){
            e.printStackTrace()
        }
    }

    /**
     * save image with this method if the sdk is 28 or lower
     */
    private fun saveImageSdk28(fileName: String){
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

    /**
     * check if the app has permission to write to external storage
     */
    fun hasWriteExternalStoragePermission(): Boolean{
        //check if the permission was already given before
        return ActivityCompat.checkSelfPermission(getApplication<Application>().applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
    }

    /**
     * request to give the WRITE_EXTERNAL_STORAGE permission
     */
    fun requestWriteExternalStoragePermission(){
        ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)
    }
}