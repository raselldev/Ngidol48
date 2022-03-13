package com.arira.ngidol48.helper

import android.os.Environment

object Config {

    const val BASE_URL = "http://apiidol.arira.my.id/"
    const val BASE_API = BASE_URL + ""

    const val BASE_STORAGE = BASE_URL
    const val BASE_STORAGE_IMAGE = BASE_URL + "image/"
    const val BASE_STORAGE_JKT = "https://jkt48.com/"


    const val API = "apikey: NGIDOLKAKAK"

    val PUSH_NOTIFICATION: String = "PUSH_NOTIFICATION"

    const val BERHASIL = "Berhasil"

    val extra_model: String = "extraModel"
    val extra_type: String = "extraType"
    val extra_other:String = "extraOther"
    val extra_url: String = "extraUrl"
    val extra_list: String = "extraList"
    val extra_id: String = "extraID"

    val TOPIC_NEWS = "news"
    val TOPIC_EVENT = "event"
    val TOPIC_HANDSHAKE = "handshake"
    val TOPIC_SHOWROOM = "showroom"

    var DIRECTORY_IMAGE_AVATAR: String = Environment.getExternalStorageDirectory().toString() + "/NGIDOL48/avatar/"
    var DIRECTORY_IMAGE_BLOG: String = Environment.getExternalStorageDirectory().toString() + "/NGIDOL48/blog/"



}