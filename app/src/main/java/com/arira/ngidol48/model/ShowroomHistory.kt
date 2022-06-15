package com.arira.ngidol48.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class ShowroomHistory() :Serializable, Parcelable {

    var id:String = ""
    var id_member:String = ""
    var title:String = ""
    var thumbnail_url:String = ""
    var videoId:String = ""
    var richthumbnail_url:String = ""
    var nama_lengkap:String = ""
    var avatar:String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString() ?: ""
        id_member = parcel.readString() ?: ""
        title = parcel.readString() ?: ""
        thumbnail_url = parcel.readString() ?: ""
        videoId = parcel.readString() ?: ""
        richthumbnail_url = parcel.readString() ?: ""
        nama_lengkap = parcel.readString() ?: ""
        avatar = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(id_member)
        parcel.writeString(title)
        parcel.writeString(thumbnail_url)
        parcel.writeString(videoId)
        parcel.writeString(richthumbnail_url)
        parcel.writeString(nama_lengkap)
        parcel.writeString(avatar)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShowroomHistory> {
        override fun createFromParcel(parcel: Parcel): ShowroomHistory {
            return ShowroomHistory(parcel)
        }

        override fun newArray(size: Int): Array<ShowroomHistory?> {
            return arrayOfNulls(size)
        }
    }
}