package com.arira.ngidol48.network.response

import com.arira.ngidol48.model.*
import java.io.Serializable

class SetlistResponse : Serializable {

    val error:Boolean  = false
    val code:Int = 0
    val message:String = ""
    var setlist:List<Setlist> = ArrayList()

}