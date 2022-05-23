package com.arira.ngidol48.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class Mng() :Serializable, Parcelable {

    var nama_lengkap: String? = null
    var avatar: String? = null
    var id:String = ""
    var id_member:String = ""
    var sesi:String = ""
    var penukaran:String = ""
    var waktu:String = ""
    var nama_member:String = ""
    var jalur:String = ""
    var id_mng:String = ""
    var nama_event_mng:String = ""
    var tanggal_envent_mng:String = ""
    var is_sold:String = ""
    var active:String = ""
    var status:String = ""
    var buy_url:String = ""

    constructor(parcel: Parcel) : this() {
        nama_lengkap = parcel.readString()
        avatar = parcel.readString()
        id = parcel.readString() ?: ""
        id_member = parcel.readString() ?: ""
        sesi = parcel.readString() ?: ""
        penukaran = parcel.readString() ?: ""
        waktu = parcel.readString() ?: ""
        nama_member = parcel.readString() ?: ""
        jalur = parcel.readString() ?: ""
        id_mng = parcel.readString() ?: ""
        nama_event_mng = parcel.readString() ?: ""
        tanggal_envent_mng = parcel.readString() ?: ""
        is_sold = parcel.readString() ?: ""
        active = parcel.readString() ?: ""
        status = parcel.readString() ?: ""
        buy_url = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nama_lengkap)
        parcel.writeString(avatar)
        parcel.writeString(id)
        parcel.writeString(id_member)
        parcel.writeString(sesi)
        parcel.writeString(penukaran)
        parcel.writeString(waktu)
        parcel.writeString(nama_member)
        parcel.writeString(jalur)
        parcel.writeString(id_mng)
        parcel.writeString(nama_event_mng)
        parcel.writeString(tanggal_envent_mng)
        parcel.writeString(is_sold)
        parcel.writeString(active)
        parcel.writeString(status)
        parcel.writeString(buy_url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Mng> {
        override fun createFromParcel(parcel: Parcel): Mng {
            return Mng(parcel)
        }

        override fun newArray(size: Int): Array<Mng?> {
            return arrayOfNulls(size)
        }
    }
}