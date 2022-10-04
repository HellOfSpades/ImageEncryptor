package com.example.imageencryptor.keyinfo

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigInteger

@Entity(tableName = "user_keys_table")
data class Key(
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "modulus")
    var modulus: String,
    @ColumnInfo(name = "public_exponent")
    var publicExponent: String,
    @ColumnInfo(name = "private_exponent")
    var privateExponent: String? = null,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
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
        dest.writeLong(this.id)
        dest.writeString(this.name)
        dest.writeString(modulus.toString())
        dest.writeString(publicExponent.toString())
        dest.writeString(privateExponent)
    }

    companion object CREATOR : Parcelable.Creator<Key> {
        override fun createFromParcel(parcel: Parcel): Key {
            var id = parcel.readLong()
            var name = parcel.readString()!!
            var modulus = parcel.readString()!!
            var publicExponent = parcel.readString()!!
            var privateExponent = parcel.readString()


            return Key(name, modulus, publicExponent, privateExponent, id)
        }

        override fun newArray(size: Int): Array<Key?> {
            return arrayOfNulls(size)
        }
    }
}