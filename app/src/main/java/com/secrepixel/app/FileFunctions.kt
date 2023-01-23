package com.secrepixel.app

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


//permission request codes
private val WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE: Int = 0

/**
 * returns whether or not the sdk version of the device is 29 or higher
 */
fun min29Sdk(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
}

/**
 * get bitmap from uri
 */
fun getBitmapFromUri(activity: Activity, uri: Uri): Bitmap {

    val input: InputStream? = activity.contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(input)
    if (input != null) {
        input.close()
    }
    return bitmap
}

/**
 * saves the image as a png with the given fileName
 */
suspend fun saveImage(application: ImageEncryptorApplication, fileName: String, bitmap: Bitmap, context: Context) : Uri{
    return withContext(Dispatchers.IO) {
        if (min29Sdk()) {
            return@withContext saveImageSdk29(application, fileName, bitmap)
        } else {
            saveImageSdk28(fileName, bitmap)
            return@withContext getUri(fileName, context)
        }
    }
}

/**
 * save image with this method if the sdk is 29 or higher
 */
@RequiresApi(Build.VERSION_CODES.Q)
private fun saveImageSdk29(application: ImageEncryptorApplication, fileName: String, bitmap: Bitmap) : Uri{
    val imageCollection =
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName")
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.WIDTH, bitmap.width)
        put(MediaStore.Images.Media.HEIGHT, bitmap.height)
    }

    try {
        val contentResolver = application.contentResolver

        val uri = contentResolver.insert(imageCollection, contentValues)?.also { uri ->
            contentResolver.openOutputStream(uri).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
        }
        return uri!!
    } catch (e: IOException) {
        e.printStackTrace()
        return Uri.EMPTY
    }
}

/**
 * save image with this method if the sdk is 28 or lower
 */
private fun saveImageSdk28(fileName: String, bitmap: Bitmap){
    //declare the output stream variable outside of try/catch so that it can always be closed
    var imageOutputStream: FileOutputStream? = null

    var outputImageFile = getFile(fileName)
    if (!outputImageFile.exists()) {
        outputImageFile.createNewFile()
    }

    try {
        imageOutputStream = FileOutputStream(outputImageFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, imageOutputStream)
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
fun hasWriteExternalStoragePermission(application: ImageEncryptorApplication): Boolean {
    //check if the permission was already given before
    return ActivityCompat.checkSelfPermission(
        application.applicationContext,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED
}

/**
 * request to give the WRITE_EXTERNAL_STORAGE permission
 */
fun requestWriteExternalStoragePermission(activity: Activity) {
    ActivityCompat.requestPermissions(
        activity,
        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
        WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
    )
}

/**
 * returns the next filename that has not be used already
 */
fun getIncrementedFileName(): String {
    if(min29Sdk()){

        return ""
    }else {
        var fileName = "savedImage_"
        var increment = 0
        var file = getFile(fileName + increment.toString() + ".png")
        while (file.exists()) {
            increment++
            file = getFile((fileName + increment.toString() + ".png"))
        }
        val outputFileName = fileName + increment.toString() + ".png"
        Timber.i("chosen name: " + outputFileName)
        return outputFileName
    }
}

/**
 * returns file from fileName
 */
fun getFile(fileName: String): File {
    //open, or create the directory where the image will be stored
    var directory = File(
        Environment.getExternalStorageDirectory().toString() + "/SecrepixelOutput/"
    )
    if (!directory.exists()) {
        directory.mkdir()
    }
    //create the file
    var file: File = File(directory.absolutePath, fileName)
    return file
}

/**
 * returns the Uri of a file with the given fileName, or throws and exception
 */
fun getUri(fileName: String, context: Context): Uri{
    if(min29Sdk()){
        return MediaStore.Images.Media.getContentUri(fileName)
    }else{
        var file = File(Environment.getExternalStorageDirectory().toString() + "/SecrepixelOutput/"+fileName)
        if(!file.exists())throw java.lang.RuntimeException()
        return FileProvider.getUriForFile(context, "com.secrepixel.app.provider", file)
    }
}
