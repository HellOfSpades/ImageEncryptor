package com.example.imageencryptor.encryption

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.math.BigInteger

data class Key(
    var name: String,
    var modulus: BigInteger,
    var publicExponent: BigInteger,
    var privateExponent: BigInteger? = null
): Parcelable {

    override fun toString(): String {
        var output: String = "modulus: "+modulus+"\npublic exponent: "+publicExponent
        if(privateExponent!=null){
            output+="\nprivate exponent: "+privateExponent
        }
        return output
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        if(dest==null)return;
        dest.writeString(this.name)
        dest.writeString(modulus.toString())
        dest.writeString(publicExponent.toString())
        if(privateExponent==null) dest.writeString(null)
        else dest.writeString(privateExponent.toString())
    }

    companion object CREATOR : Parcelable.Creator<Key> {
        override fun createFromParcel(parcel: Parcel): Key {
            var name = parcel.readString()!!
            var modulus = BigInteger(parcel.readString())
            var publicExponent = BigInteger(parcel.readString())
            var privateExponent = parcel.readString()?.let { BigInteger(it) }


            return Key(name, modulus, publicExponent, privateExponent)
        }

        override fun newArray(size: Int): Array<Key?> {
            return arrayOfNulls(size)
        }
    }
}