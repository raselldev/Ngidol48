package com.arira.ngidol48.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class Event() :Serializable, Parcelable {
    var id:String = ""
    var event_id:String = ""
    var tanggal:String = ""
    var hari:String = ""
    var badge_url:String = ""
    var event_name:String = ""
    var event_time:String = ""
    var bulan_tahun:String = ""
    var member_perform:String = "0"
    var deskripsi:String = ""
    var tiket_link:String = ""
    var cover:String? = null
    var listEventName:ArrayList<String> = ArrayList()

    constructor(parcel: Parcel) : this() {
        id = parcel.readString() ?: ""
        event_id = parcel.readString() ?: ""
        tanggal = parcel.readString() ?: ""
        hari = parcel.readString() ?: ""
        badge_url = parcel.readString() ?: ""
        event_name = parcel.readString() ?: ""
        event_time = parcel.readString() ?: ""
        bulan_tahun = parcel.readString() ?: ""
        member_perform = parcel.readString() ?: ""
        deskripsi = parcel.readString() ?: ""
        tiket_link = parcel.readString() ?: ""
        cover = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(event_id)
        parcel.writeString(tanggal)
        parcel.writeString(hari)
        parcel.writeString(badge_url)
        parcel.writeString(event_name)
        parcel.writeString(event_time)
        parcel.writeString(bulan_tahun)
        parcel.writeString(member_perform)
        parcel.writeString(deskripsi)
        parcel.writeString(tiket_link)
        parcel.writeString(cover)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Event> {
        override fun createFromParcel(parcel: Parcel): Event {
            return Event(parcel)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }
    }
}