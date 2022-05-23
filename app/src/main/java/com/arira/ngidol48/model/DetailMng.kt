package com.arira.ngidol48.model

import java.io.Serializable

class DetailMng:Serializable {
    var sesi:String = ""
    var mng:ArrayList<Mng> = ArrayList()
    var waktu:String = ""
    var event_name:String = ""
    var tanggal:String = ""
    var penukaran:String = ""
    var member_list_name:String = ""
    var id_mng:String = ""
    var is_expand:Boolean = false
}