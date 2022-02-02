package com.arira.ngidol48.network.response

import com.arira.ngidol48.model.*
import java.io.Serializable

class SongResponse : Serializable {

    val error:Boolean  = false
    val code:Int = 0
    val message:String = ""
    var song_list:List<Song> = ArrayList()

}