package com.arira.ngidol48.ui.activity.shareLirik

interface ShareLirikCallback {
    fun onLirikSelected(position:Int, lirik:String)
    fun onLirikRemoved(position:Int, lirik:String)
}