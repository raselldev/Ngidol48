package com.arira.ngidol48.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class User() :Serializable, Parcelable {

    var id:String = ""
    var email:String = ""
    var first_name:String = ""
    var last_name:String = ""
    var birth:String = ""
    var gender:String = ""
    var token:String = ""
    var created_at:String = ""
    var password:String = ""
    var avatar:String = ""
    var avatar_url:String = ""
    var telp:String = ""
    var typelogin:String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString() ?: ""
        email = parcel.readString() ?: ""
        first_name = parcel.readString() ?: ""
        last_name = parcel.readString() ?: ""
        birth = parcel.readString() ?: ""
        gender = parcel.readString() ?: ""
        token = parcel.readString() ?: ""
        created_at = parcel.readString() ?: ""
        password = parcel.readString() ?: ""
        avatar = parcel.readString() ?: ""
        avatar_url = parcel.readString() ?: ""
        telp = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(email)
        parcel.writeString(first_name)
        parcel.writeString(last_name)
        parcel.writeString(birth)
        parcel.writeString(gender)
        parcel.writeString(token)
        parcel.writeString(created_at)
        parcel.writeString(password)
        parcel.writeString(avatar)
        parcel.writeString(avatar_url)
        parcel.writeString(telp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }


}