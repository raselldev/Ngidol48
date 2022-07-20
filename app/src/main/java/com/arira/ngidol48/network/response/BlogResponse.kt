package com.arira.ngidol48.network.response

import com.arira.ngidol48.model.Blog
import com.arira.ngidol48.model.Komentar
import java.io.Serializable

class BlogResponse : Serializable {

    val success:Boolean  = false
    val code:Int = 0
    val message:String = ""
    val blogs:List<Blog> = ArrayList()
    val blog:Blog = Blog()
    val comments:List<Komentar> = ArrayList()
    val totalpage:Int = 0
    val totalitem:Int = 0
    val views:String = "0"


}