package com.secrepixel.app.keyinfo

import android.graphics.Bitmap
import com.example.imageencryptorlibrary.encryption.PPKeyImageEncryptor
import java.lang.Exception
import java.math.BigInteger
import java.security.spec.InvalidKeySpecException

//this PPKeyImageEncryptor is used solely for the generation of keys
//it should not be used for anything else
private var encryptor = PPKeyImageEncryptor()

/**
 * generate a key with given name
 */
fun generateKey(name: String): Key {
    encryptor.makeKeyPair(2048)
    return Key(
        name,
        encryptor.getPublicKey()!!.modulus.toString(),
        encryptor.getPublicKey()!!.publicExponent.toString(),
        encryptor.getPrivateKey()!!.privateExponent.toString()
    )
}

/**
 * construct key from given values
 */
fun constructKey(
    name: String,
    modulus: String,
    publicExponent: String,
    privateExponent: String?
): Key {

    //check if the values are correct and can be used
    if (privateExponent != null) {
        encryptor.setPublicPrivateKey(
            BigInteger(modulus),
            BigInteger(publicExponent),
            BigInteger(privateExponent)
        )
        testPublicPrivateKey()
    } else {
        encryptor.setPublicKey(BigInteger(modulus), BigInteger(publicExponent))
        testPublicKey()
    }

    //return a key object if there were no problems creating the key
    return Key(name, modulus, publicExponent, privateExponent)
}

fun testPublicKey(){
    try {
        val message = "hello"
        encryptor.encrypt(message.toByteArray(), generateBitMap())
    }catch (e: Exception){
        throw InvalidKeySpecException()
    }
}

fun testPublicPrivateKey(){
    try {
        val message = "hello"
        val encryptedBitmap = encryptor.encrypt(message.toByteArray(), generateBitMap())!!
        if (String(encryptor.decrypt(encryptedBitmap)) != (message)) {
            throw InvalidKeySpecException()
        }
    }catch (e: Exception){
        throw InvalidKeySpecException()
    }
}

/**
 * generate a bitmap for testing
 */
private fun generateBitMap(): Bitmap{
    val width = 700
    val height = 200;
    val config = Bitmap.Config.ARGB_8888
    return Bitmap.createBitmap(width, height, config);
}