package com.arira.ngidol48.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class ParentHandshake() :Serializable, Parcelable {
    var nama_event_handshake:String = ""
    var tanggal_envent_handshake:String = ""
    var id_handshake:String = ""
    var details:List<Handshake> = ArrayList()
    var nama_member:List<String> = ArrayList()
    var jalurs:List<String> = ArrayList()
    var sesis:List<String> = ArrayList()
    var total_sold:Int = 0
    var total_jadwal:Int = 0

    constructor(parcel: Parcel) : this() {
        nama_event_handshake = parcel.readString() ?: ""
        tanggal_envent_handshake = parcel.readString() ?: ""
        id_handshake = parcel.readString() ?: ""
        details = parcel.createTypedArrayList(Handshake) ?: ArrayList()
        nama_member = parcel.createStringArrayList() ?: ArrayList()
        jalurs = parcel.createStringArrayList() ?: ArrayList()
        sesis = parcel.createStringArrayList() ?: ArrayList()
        total_sold = parcel.readInt()
        total_jadwal = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nama_event_handshake)
        parcel.writeString(tanggal_envent_handshake)
        parcel.writeString(id_handshake)
        parcel.writeTypedList(details)
        parcel.writeStringList(nama_member)
        parcel.writeStringList(jalurs)
        parcel.writeStringList(sesis)
        parcel.writeInt(total_sold)
        parcel.writeInt(total_jadwal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParentHandshake> {
        override fun createFromParcel(parcel: Parcel): ParentHandshake {
            return ParentHandshake(parcel)
        }

        override fun newArray(size: Int): Array<ParentHandshake?> {
            return arrayOfNulls(size)
        }
    }

}