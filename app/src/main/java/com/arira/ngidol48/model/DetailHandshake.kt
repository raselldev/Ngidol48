package com.arira.ngidol48.model

import java.io.Serializable

class DetailHandshake:Serializable {
    var sesi:String = ""
    var handshake:ArrayList<Handshake> = ArrayList()
    var waktu:String = ""
    var standby:String = ""
    var member_list_name:String = ""
    var is_expand:Boolean = false
}