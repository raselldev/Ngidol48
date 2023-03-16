package com.arira.ngidol48.model

import android.os.Parcel
import android.os.Parcelable

class Radio() :java.io.Serializable, Parcelable {

    var id:String = ""
    var description:String = ""
    var name:String = ""
    var song_list:String = ""
    var image:String = ""
    var playback:String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString() ?: ""
        description = parcel.readString() ?: ""
        name = parcel.readString() ?: ""
        song_list = parcel.readString() ?: ""
        image = parcel.readString() ?: ""
        playback = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(description)
        parcel.writeString(name)
        parcel.writeString(song_list)
        parcel.writeString(image)
        parcel.writeString(playback)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Radio> {
        override fun createFromParcel(parcel: Parcel): Radio {
            return Radio(parcel)
        }

        override fun newArray(size: Int): Array<Radio?> {
            return arrayOfNulls(size)
        }
    }
}