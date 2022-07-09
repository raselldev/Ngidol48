package com.arira.ngidol48.network.response

import com.arira.ngidol48.model.Chant
import java.io.Serializable

class ChantResponse : Serializable {

    val success:Boolean  = false
    val code:Int = 0
    val message:String = ""
    val chants:List<Chant> = ArrayList()


}