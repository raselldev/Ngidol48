package com.arira.ngidol48.network.response

import com.arira.ngidol48.model.PhotoCardImage
import com.arira.ngidol48.model.PhotoCardSign
import java.io.Serializable

class PhotoCardResponse : Serializable {

    val success:Boolean  = false
    val code:Int = 0
    val message:String = ""
    val images:List<PhotoCardImage> = ArrayList()
    val signs:List<PhotoCardSign> = ArrayList()
}