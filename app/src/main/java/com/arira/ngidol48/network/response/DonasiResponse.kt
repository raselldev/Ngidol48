package com.arira.ngidol48.network.response

import com.arira.ngidol48.model.Donasi
import java.io.Serializable

class DonasiResponse : Serializable {

    val success:Boolean  = false
    val code:Int = 0
    val message:String = ""
    val donations:List<Donasi> = ArrayList()

}