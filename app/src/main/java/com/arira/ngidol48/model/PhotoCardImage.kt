package com.arira.ngidol48.model

import android.os.Parcel
import android.os.Parcelable

class PhotoCardImage() :java.io.Serializable, Parcelable {
    var id:String = ""
    var id_session:String = ""
    var name:String = ""
    var image:String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString() ?: ""
        id_session = parcel.readString() ?: ""
        name = parcel.readString() ?: ""
        image = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(id_session)
        parcel.writeString(name)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhotoCardImage> {
        override fun createFromParcel(parcel: Parcel): PhotoCardImage {
            return PhotoCardImage(parcel)
        }

        override fun newArray(size: Int): Array<PhotoCardImage?> {
            return arrayOfNulls(size)
        }
    }
}