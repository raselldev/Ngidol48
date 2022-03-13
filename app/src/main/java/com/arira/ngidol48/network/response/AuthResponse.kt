package com.arira.ngidol48.network.response

import com.arira.ngidol48.model.*
import java.io.Serializable

class AuthResponse : Serializable {

    val success:Boolean  = false
    val code:Int = 0
    val message:String = ""
    var user: User = User()
    var new_token:String = ""


}