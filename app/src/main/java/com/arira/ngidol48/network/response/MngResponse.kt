package com.arira.ngidol48.network.response

import com.arira.ngidol48.model.*
import java.io.Serializable

class MngResponse : Serializable {

    val success:Boolean  = false
    val code:Int = 0
    val message:String = ""
    var mngs:List<ParentMng> = ArrayList()
    var mng_member:List<Mng> = ArrayList()
    var member:Member = Member()
}