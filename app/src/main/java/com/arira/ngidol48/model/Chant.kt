package com.arira.ngidol48.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class Chant() : Serializable, Parcelable{
    var id:String = ""
    var title:String = ""
    var chant:String = ""
    var related_song_id:String = ""
    var status:String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString() ?: ""
        title = parcel.readString() ?: ""
        chant = parcel.readString() ?: ""
        related_song_id = parcel.readString() ?: ""
        status = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(chant)
        parcel.writeString(related_song_id)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Chant> {
        override fun createFromParcel(parcel: Parcel): Chant {
            return Chant(parcel)
        }

        override fun newArray(size: Int): Array<Chant?> {
            return arrayOfNulls(size)
        }
    }
}
