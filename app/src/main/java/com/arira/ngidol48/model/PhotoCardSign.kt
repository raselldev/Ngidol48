package com.arira.ngidol48.model

import android.os.Parcel
import android.os.Parcelable

class PhotoCardSign() :java.io.Serializable, Parcelable {

    var id_session = ""
    var name = ""
    var image = ""

    constructor(parcel: Parcel) : this() {
        id_session = parcel.readString() ?: ""
        name = parcel.readString() ?: ""
        image = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id_session)
        parcel.writeString(name)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhotoCardSign> {
        override fun createFromParcel(parcel: Parcel): PhotoCardSign {
            return PhotoCardSign(parcel)
        }

        override fun newArray(size: Int): Array<PhotoCardSign?> {
            return arrayOfNulls(size)
        }
    }
}