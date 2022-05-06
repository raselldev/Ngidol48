package com.arira.ngidol48.network.response

import com.arira.ngidol48.model.*
import java.io.Serializable

class FavoritResponse : Serializable {

    val success:Boolean  = false
    val code:Int = 0
    var position:Int = 0
    val message:String = ""
    val song_list:List<Song> = ArrayList()
}