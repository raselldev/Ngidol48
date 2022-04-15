package com.arira.ngidol48.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class Handshake() :Serializable, Parcelable {

    var nama_lengkap: String? = null
    var avatar: String? = null
    var id:String = ""
    var id_member:String = ""
    var sesi:String = ""
    var waktu:String = ""
    var standby:String = ""
    var nama_member:String = ""
    var jalur:String = ""
    var id_handshake:String = ""
    var nama_event_handshake:String = ""
    var tanggal_envent_handshake:String = ""
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
        waktu = parcel.readString() ?: ""
        standby = parcel.readString() ?: ""
        nama_member = parcel.readString() ?: ""
        jalur = parcel.readString() ?: ""
        id_handshake = parcel.readString() ?: ""
        nama_event_handshake = parcel.readString() ?: ""
        tanggal_envent_handshake = parcel.readString() ?: ""
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
        parcel.writeString(waktu)
        parcel.writeString(standby)
        parcel.writeString(nama_member)
        parcel.writeString(jalur)
        parcel.writeString(id_handshake)
        parcel.writeString(nama_event_handshake)
        parcel.writeString(tanggal_envent_handshake)
        parcel.writeString(is_sold)
        parcel.writeString(active)
        parcel.writeString(status)
        parcel.writeString(buy_url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Handshake> {
        override fun createFromParcel(parcel: Parcel): Handshake {
            return Handshake(parcel)
        }

        override fun newArray(size: Int): Array<Handshake?> {
            return arrayOfNulls(size)
        }
    }
}