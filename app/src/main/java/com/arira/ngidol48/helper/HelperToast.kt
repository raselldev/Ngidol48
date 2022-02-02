package com.arira.ngidol48.helper

import android.content.Context
import android.widget.Toast

open class HelperToast {
    fun show(msg: String, context: Context){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

//    fun show(msg: String, ctv: FragmentActivity?){
//        Toast.makeText(ctv, msg, Toast.LENGTH_SHORT).show()
//    }
}