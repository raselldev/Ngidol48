package com.arira.ngidol48.model

import android.os.Parcel
import android.os.Parcelable

class Merch() :java.io.Serializable, Parcelable {
    var name:String = ""
    var price:String = ""
    var imageUrl:String = ""
    var productUrl:String = ""
    var preorder:String = ""

    constructor(parcel: Parcel) : this() {
        name = parcel.readString() ?: ""
        price = parcel.readString() ?: ""
        imageUrl = parcel.readString() ?: ""
        productUrl = parcel.readString() ?: ""
        preorder = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(price)
        parcel.writeString(imageUrl)
        parcel.writeString(productUrl)
        parcel.writeString(preorder)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Merch> {
        override fun createFromParcel(parcel: Parcel): Merch {
            return Merch(parcel)
        }

        override fun newArray(size: Int): Array<Merch?> {
            return arrayOfNulls(size)
        }
    }

}