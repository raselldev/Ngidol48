package com.arira.ngidol48.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class Member() : Serializable, Parcelable{
    var id:String = ""
    var nama_lengkap:String = ""
    var nama_panggilan:String = ""
    var tanggal_lahir:String = ""
    var avatar:String = ""
    var tinggi_badan:String = ""
    var horoskop:String = ""
    var gol_darah:String = ""
    var id_member:String = ""
    var status:String = ""
    var kategori:String = ""
    var jiko:String = ""
    var generasi:String = ""
    var showtime:String = ""

    var media_instagram:String = ""
    var media_tiktok:String = ""
    var media_twitter:String = ""
    var media_showroom:String = ""
    var fanbase_name:String = ""
    var fanbase_page:String = ""
    var fanbase_register:String = ""

    var stream_url:String = ""
    var profil_url:String = ""
    var tanggal_showroom:String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString() ?: ""
        nama_lengkap = parcel.readString() ?: ""
        nama_panggilan = parcel.readString() ?: ""
        tanggal_lahir = parcel.readString() ?: ""
        avatar = parcel.readString() ?: ""
        tinggi_badan = parcel.readString() ?: ""
        horoskop = parcel.readString() ?: ""
        gol_darah = parcel.readString() ?: ""
        id_member = parcel.readString() ?: ""
        status = parcel.readString() ?: ""
        kategori = parcel.readString() ?: ""
        jiko = parcel.readString() ?: ""
        generasi = parcel.readString() ?: ""
        showtime = parcel.readString() ?: ""
        media_instagram = parcel.readString() ?: ""
        media_tiktok = parcel.readString() ?: ""
        media_twitter = parcel.readString() ?: ""
        media_showroom = parcel.readString() ?: ""
        fanbase_name = parcel.readString() ?: ""
        fanbase_page = parcel.readString() ?: ""
        fanbase_register = parcel.readString() ?: ""
        stream_url = parcel.readString() ?: ""
        profil_url = parcel.readString() ?: ""
        tanggal_showroom = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nama_lengkap)
        parcel.writeString(nama_panggilan)
        parcel.writeString(tanggal_lahir)
        parcel.writeString(avatar)
        parcel.writeString(tinggi_badan)
        parcel.writeString(horoskop)
        parcel.writeString(gol_darah)
        parcel.writeString(id_member)
        parcel.writeString(status)
        parcel.writeString(kategori)
        parcel.writeString(jiko)
        parcel.writeString(generasi)
        parcel.writeString(showtime)
        parcel.writeString(media_instagram)
        parcel.writeString(media_tiktok)
        parcel.writeString(media_twitter)
        parcel.writeString(media_showroom)
        parcel.writeString(fanbase_name)
        parcel.writeString(fanbase_page)
        parcel.writeString(fanbase_register)
        parcel.writeString(stream_url)
        parcel.writeString(profil_url)
        parcel.writeString(tanggal_showroom)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Member> {
        override fun createFromParcel(parcel: Parcel): Member {
            return Member(parcel)
        }

        override fun newArray(size: Int): Array<Member?> {
            return arrayOfNulls(size)
        }
    }

}