package com.arira.ngidol48.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class LiveStream() :Serializable, Parcelable {
    var id:String = ""
    var banner:String = ""
    var nama:String = ""
    var deskripsi:String = ""
    var start_date:String = ""
    var status:String = ""
    var live_status:String = ""
    var playback:String = ""
    var created_at:String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString() ?: ""
        banner = parcel.readString() ?: ""
        nama = parcel.readString() ?: ""
        deskripsi = parcel.readString() ?: ""
        start_date = parcel.readString() ?: ""
        status = parcel.readString() ?: ""
        live_status = parcel.readString() ?: ""
        playback = parcel.readString() ?: ""
        created_at = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(banner)
        parcel.writeString(nama)
        parcel.writeString(deskripsi)
        parcel.writeString(start_date)
        parcel.writeString(status)
        parcel.writeString(live_status)
        parcel.writeString(playback)
        parcel.writeString(created_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LiveStream> {
        override fun createFromParcel(parcel: Parcel): LiveStream {
            return LiveStream(parcel)
        }

        override fun newArray(size: Int): Array<LiveStream?> {
            return arrayOfNulls(size)
        }
    }
}