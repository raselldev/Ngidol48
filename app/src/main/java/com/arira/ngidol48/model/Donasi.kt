package com.arira.ngidol48.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class Donasi() :Serializable, Parcelable {
    var donator_name:String = ""
    var amount_raw:String = ""
    var message:String = ""

    constructor(parcel: Parcel) : this() {
        donator_name = parcel.readString() ?: ""
        amount_raw = parcel.readString() ?: ""
        message = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(donator_name)
        parcel.writeString(amount_raw)
        parcel.writeString(message)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Donasi> {
        override fun createFromParcel(parcel: Parcel): Donasi {
            return Donasi(parcel)
        }

        override fun newArray(size: Int): Array<Donasi?> {
            return arrayOfNulls(size)
        }
    }
}