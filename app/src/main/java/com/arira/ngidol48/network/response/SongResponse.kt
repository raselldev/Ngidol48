package com.arira.ngidol48.network.response

import com.arira.ngidol48.model.Chant
import com.arira.ngidol48.model.Setlist
import com.arira.ngidol48.model.Song
import java.io.Serializable

class SongResponse : Serializable {

    val error:Boolean  = false
    val code:Int = 0
    val message:String = ""
    var song_list:List<Song> = ArrayList()
    var setlist:List<Setlist> = ArrayList()

    var song:Song = Song()
    var chant:Chant = Chant()
    val listener:Int = 0
    val size_fav:Int = 0
    val fav:Boolean  = false


}