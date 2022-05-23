package com.arira.ngidol48.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class ParentMng() :Serializable, Parcelable {
    var nama_event_mng:String = ""
    var tanggal_envent_mng:String = ""
    var id_mng:String = ""
    var details:List<Mng> = ArrayList()
    var nama_member:List<String> = ArrayList()
    var jalurs:List<String> = ArrayList()
    var sesis:List<String> = ArrayList()
    var total_sold:Int = 0
    var total_jadwal:Int = 0

    constructor(parcel: Parcel) : this() {
        nama_event_mng = parcel.readString() ?: ""
        tanggal_envent_mng = parcel.readString() ?: ""
        id_mng = parcel.readString() ?: ""
        details = parcel.createTypedArrayList(Mng) ?: ArrayList()
        nama_member = parcel.createStringArrayList() ?: ArrayList()
        jalurs = parcel.createStringArrayList() ?: ArrayList()
        sesis = parcel.createStringArrayList() ?: ArrayList()
        total_sold = parcel.readInt()
        total_jadwal = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nama_event_mng)
        parcel.writeString(tanggal_envent_mng)
        parcel.writeString(id_mng)
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

    companion object CREATOR : Parcelable.Creator<ParentMng> {
        override fun createFromParcel(parcel: Parcel): ParentMng {
            return ParentMng(parcel)
        }

        override fun newArray(size: Int): Array<ParentMng?> {
            return arrayOfNulls(size)
        }
    }

}