package com.arira.ngidol48.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class Setlist() :Serializable, Parcelable {
    var id:String = ""
    var nama:String = ""
    var cover:String = ""
    var setlist_id:String = ""
    var total_lagu:String = ""
    var kategori:String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString() ?: ""
        nama = parcel.readString() ?: ""
        cover = parcel.readString() ?: ""
        setlist_id = parcel.readString() ?: ""
        total_lagu = parcel.readString() ?: ""
        kategori = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nama)
        parcel.writeString(cover)
        parcel.writeString(setlist_id)
        parcel.writeString(total_lagu)
        parcel.writeString(kategori)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Setlist> {
        override fun createFromParcel(parcel: Parcel): Setlist {
            return Setlist(parcel)
        }

        override fun newArray(size: Int): Array<Setlist?> {
            return arrayOfNulls(size)
        }
    }
}