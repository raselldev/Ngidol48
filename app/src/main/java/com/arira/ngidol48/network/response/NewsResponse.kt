package com.arira.ngidol48.network.response

import com.arira.ngidol48.model.*
import java.io.Serializable

class NewsResponse : Serializable {

    val success:Boolean  = false
    val code:Int = 0
    val message:String = ""

    var news: List<Berita> = ArrayList()


}