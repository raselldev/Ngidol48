package com.arira.ngidol48.model

import android.os.Parcel
import android.os.Parcelable

class CreatePhotoCard() :java.io.Serializable, Parcelable {

    var signPosition = 0 //0.1.2
    var greating:String = ""
    var nickName:String = ""
    var photoCard = PhotoCardImage()
    var sign = PhotoCardSign()

    constructor(parcel: Parcel) : this() {
        signPosition = parcel.readInt()
        greating = parcel.readString() ?: ""
        nickName = parcel.readString() ?: ""
        photoCard = parcel.readParcelable(PhotoCardImage::class.java.classLoader) ?: PhotoCardImage()
        sign = parcel.readParcelable(PhotoCardSign::class.java.classLoader) ?: PhotoCardSign()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(signPosition)
        parcel.writeString(greating)
        parcel.writeString(nickName)
        parcel.writeParcelable(photoCard, flags)
        parcel.writeParcelable(sign, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CreatePhotoCard> {
        override fun createFromParcel(parcel: Parcel): CreatePhotoCard {
            return CreatePhotoCard(parcel)
        }

        override fun newArray(size: Int): Array<CreatePhotoCard?> {
            return arrayOfNulls(size)
        }
    }


}