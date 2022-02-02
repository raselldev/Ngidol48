package com.arira.ngidol48.network.response

import com.arira.ngidol48.model.*
import java.io.Serializable

class EventResponse : Serializable {

    val error:Boolean  = false
    val code:Int = 0
    val message:String = ""
    var events:List<Event> = ArrayList()

    var event:Event = Event()
    var members_perform:List<Member> = ArrayList()
    var members_bday:List<Member> = ArrayList()
    var song_list:List<Song> = ArrayList()

}