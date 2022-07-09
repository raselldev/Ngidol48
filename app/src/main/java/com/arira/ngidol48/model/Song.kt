package com.arira.ngidol48.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class Song() :Serializable, Parcelable {
    var id:String = ""
    var lagu_id:String = ""
    var setlist_id:String = ""
    var judul:String = ""
    var song_link:String = ""
    var lirik:String = ""
    var cover:String = ""
    var nama:String = ""

    var user_id:String = ""
    var song_id:String = ""
    var chant:Chant = Chant()

    constructor(parcel: Parcel) : this() {
        id = parcel.readString() ?: ""
        lagu_id = parcel.readString() ?: ""
        setlist_id = parcel.readString() ?: ""
        judul = parcel.readString() ?: ""
        song_link = parcel.readString() ?: ""
        lirik = parcel.readString() ?: ""
        cover = parcel.readString() ?: ""
        nama = parcel.readString() ?: ""

        user_id = parcel.readString() ?: ""
        song_id = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(lagu_id)
        parcel.writeString(setlist_id)
        parcel.writeString(judul)
        parcel.writeString(song_link)
        parcel.writeString(lirik)
        parcel.writeString(cover)
        parcel.writeString(nama)

        parcel.writeString(user_id)
        parcel.writeString(song_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }
}