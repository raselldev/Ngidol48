package com.arira.ngidol48.network.response

import com.arira.ngidol48.model.*
import java.io.Serializable

class HomeResponse : Serializable {

    val error:Boolean  = false
    val code:Int = 0
    val message:String = ""
    var slider:List<Slider> = ArrayList()
    var news:List<Berita> = ArrayList()
    var bday_member:List<Member> = ArrayList()
    var live_showroom:List<Member> = ArrayList()
    var live_stream:LiveStream = LiveStream()
    var sr_today:List<ShowroomHistory> = ArrayList()
    var event:List<Event> = ArrayList()
    var idn:Idn = Idn()
    var notifikasi_hari_ini:Int = 0
    var for_review:Int = 0
    var app_version:Int = 0
    var show_banner:String = "0"
    var show_banner_stream:String = "0"
    var show_special_video:String = "0"
    var banner:Banner = Banner()
    var special_video:SpecialVideo = SpecialVideo()
    var session:PhotocardSession = PhotocardSession()
    var radio:Radio? = Radio()
    var merch:List<Merch> = ArrayList()

}