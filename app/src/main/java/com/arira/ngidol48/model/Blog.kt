package com.arira.ngidol48.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class Blog() :Serializable, Parcelable {


    var id:String = ""
    var id_user:String = ""
    var cover:String = ""
    var judul:String = ""
    var blog:String = ""
    var status:String = ""
    var created_at:String = ""
    var updated_at:String = ""
    var fullname:String = ""
    var avatar:String = ""
    var kategori_id:String = ""
    var nama_kategori:String = ""
    var total_command:Int = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readString() ?: ""
        id_user = parcel.readString() ?: ""
        cover = parcel.readString() ?: ""
        judul = parcel.readString() ?: ""
        blog = parcel.readString() ?: ""
        status = parcel.readString() ?: ""
        created_at = parcel.readString() ?: ""
        updated_at = parcel.readString() ?: ""
        fullname = parcel.readString() ?: ""
        avatar = parcel.readString() ?: ""
        kategori_id = parcel.readString() ?: ""
        nama_kategori = parcel.readString() ?: ""
        total_command = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(id_user)
        parcel.writeString(cover)
        parcel.writeString(judul)
        parcel.writeString(blog)
        parcel.writeString(status)
        parcel.writeString(created_at)
        parcel.writeString(updated_at)
        parcel.writeString(fullname)
        parcel.writeString(avatar)
        parcel.writeString(kategori_id)
        parcel.writeString(nama_kategori)
        parcel.writeInt(total_command)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Blog> {
        override fun createFromParcel(parcel: Parcel): Blog {
            return Blog(parcel)
        }

        override fun newArray(size: Int): Array<Blog?> {
            return arrayOfNulls(size)
        }
    }
}