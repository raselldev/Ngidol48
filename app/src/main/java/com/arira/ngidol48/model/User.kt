package com.arira.ngidol48.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class User() :Serializable, Parcelable {

    var id:String = ""
    var avatar:String = ""
    var fullname:String = ""
    var gender:String = ""
    var bio:String = ""
    var cover:String = ""
    var phone:String = ""
    var email:String = ""
    var token_app:String = ""
    var status:String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString() ?: ""
        avatar = parcel.readString() ?: ""
        fullname = parcel.readString() ?: ""
        gender = parcel.readString() ?: ""
        bio = parcel.readString() ?: ""
        cover = parcel.readString() ?: ""
        phone = parcel.readString() ?: ""
        email = parcel.readString() ?: ""
        token_app = parcel.readString() ?: ""
        status = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(avatar)
        parcel.writeString(fullname)
        parcel.writeString(gender)
        parcel.writeString(bio)
        parcel.writeString(cover)
        parcel.writeString(phone)
        parcel.writeString(email)
        parcel.writeString(token_app)
        parcel.writeString(status)
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