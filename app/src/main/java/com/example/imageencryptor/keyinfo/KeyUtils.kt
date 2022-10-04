package com.example.imageencryptor.keyinfo

import com.example.imageencryptorlibrary.encryption.PPKeyImageEncryptor

//this PPKeyImageEncryptor is used solely for the generation of keys
var encryptor = PPKeyImageEncryptor()

//generate a key with given name
fun generateKey(name: String): Key{
    encryptor.makeKeyPair(2048)
    return Key(name,
        encryptor.getPublicKey()!!.modulus.toString(),
        encryptor.getPublicKey()!!.modulus.toString(),
        encryptor.getPrivateKey()!!.privateExponent.toString())
}