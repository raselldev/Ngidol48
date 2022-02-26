package com.arira.ngidol48.network.response

import com.arira.ngidol48.model.*
import java.io.Serializable

class HandshakeResponse : Serializable {

    val success:Boolean  = false
    val code:Int = 0
    val message:String = ""
    var handshakes:List<ParentHandshake> = ArrayList()
}