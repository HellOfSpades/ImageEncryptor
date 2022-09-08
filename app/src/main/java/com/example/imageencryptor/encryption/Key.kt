package com.example.imageencryptor.encryption

import java.math.BigInteger

data class Key(
    var modulus: BigInteger,
    var publicExponent: BigInteger,
    var privateExponent: BigInteger? = null
) {
    override fun toString(): String {
        var output: String = "modulus: "+modulus+"\npublic exponent: "+publicExponent
        if(privateExponent!=null){
            output+="\nprivate exponent: "+privateExponent
        }
        return output
    }
}