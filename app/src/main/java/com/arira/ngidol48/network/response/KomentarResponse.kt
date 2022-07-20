package com.arira.ngidol48.network.response

import com.arira.ngidol48.model.Komentar
import java.io.Serializable

class KomentarResponse : Serializable {

    val success:Boolean  = false
    val code:Int = 0
    var position:Int = 0
    val message:String = ""
    val datas:List<Komentar> = ArrayList()
    val data:Komentar = Komentar()
    val views:String = "0"

}