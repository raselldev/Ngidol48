package com.arira.ngidol48.network.response

import com.arira.ngidol48.model.LiveStream
import java.io.Serializable

class StreamResponse : Serializable {

    val success:Boolean  = false
    val code:Int = 0
    val message:String = ""
    val stream:LiveStream = LiveStream()

}