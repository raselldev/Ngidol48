package com.arira.ngidol48.network.response

import com.arira.ngidol48.model.*
import java.io.Serializable

class ProfilResponse : Serializable {

    val success:Boolean  = false
    val code:Int = 0
    val message:String = ""
    var user: User = User()
    var blogs:List<Blog> = ArrayList()
    var fav_song_count:Int = 0


}