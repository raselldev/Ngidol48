package com.arira.ngidol48.network.response

import java.io.Serializable

class ViewStreamResponse : Serializable {

    val success:Boolean  = false
    val code:Int = 0
    val message:String = ""
    val views:String = "0"

}