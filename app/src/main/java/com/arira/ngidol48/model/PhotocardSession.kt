package com.arira.ngidol48.model

import android.os.Parcel
import android.os.Parcelable

class PhotocardSession() :java.io.Serializable, Parcelable {

    var id:String = ""
    var session_name:String = ""
    var session_description:String = ""
    var session_banner:String = ""
    var id_member:String = ""
    var max:String = "0"
    var tag:String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString() ?: ""
        session_name = parcel.readString() ?: ""
        session_description = parcel.readString() ?: ""
        session_banner = parcel.readString() ?: ""
        id_member = parcel.readString() ?: ""
        max = parcel.readString() ?: "0"
        tag = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(session_name)
        parcel.writeString(session_description)
        parcel.writeString(session_banner)
        parcel.writeString(id_member)
        parcel.writeString(max)
        parcel.writeString(tag)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhotocardSession> {
        override fun createFromParcel(parcel: Parcel): PhotocardSession {
            return PhotocardSession(parcel)
        }

        override fun newArray(size: Int): Array<PhotocardSession?> {
            return arrayOfNulls(size)
        }
    }
}