package com.secrepixel.app.adapters


public fun charArrayToByteArray(charArray: CharArray): ByteArray{
    var byteArray = ByteArray(charArray.size)
    for (c in 0..charArray.size){
        byteArray[c] = charArray[c].toByte()
    }
    return byteArray
}