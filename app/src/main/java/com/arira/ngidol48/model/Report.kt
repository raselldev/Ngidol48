package com.arira.ngidol48.model

import java.io.Serializable

class Report:Serializable {
    var id:String = ""
    var title:String = ""
    var subtitle:String = ""
    var selected:Boolean = false

    //reportdata
    var id_user:String = "0"
    var id_blog:String = "0"
    var id_report:String = "0"
}