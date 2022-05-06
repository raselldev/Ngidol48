package com.arira.ngidol48.network.response

import com.arira.ngidol48.model.*
import java.io.Serializable

class KomentarResponse : Serializable {

    val success:Boolean  = false
    val code:Int = 0
    var position:Int = 0
    val message:String = ""
    val datas:List<Komentar> = ArrayList()
    val data:Komentar = Komentar()

}