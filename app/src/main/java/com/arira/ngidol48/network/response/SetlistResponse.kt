package com.arira.ngidol48.network.response

import com.arira.ngidol48.model.LaguKategori
import com.arira.ngidol48.model.Setlist
import java.io.Serializable

class SetlistResponse : Serializable {

    val error:Boolean  = false
    val code:Int = 0
    val message:String = ""
    var setlist:List<Setlist> = ArrayList()

    var setlist_teater:List<Setlist> = ArrayList()
    var banner_music:Setlist = Setlist()
    var song_by_kategori:List<LaguKategori> = ArrayList()
}