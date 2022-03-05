package com.arira.ngidol48.utilities

import android.content.Context
import android.content.SharedPreferences
import com.arira.ngidol48.model.Song
import com.google.gson.Gson

class SharedPref(context : Context) {

    private val mypref = "MAIN_PREF"
    private val sp: SharedPreferences = context.getSharedPreferences(mypref, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sp.edit()
    private val onReview = "onReview"
    private val showRating = "showRating"
    private val openApp = "openApp"

    private val songPlay = "songPlay"

    private val notifShowroom = "notifShowroom"
    private val notifNews = "notifNews"
    private val notifEvent = "notifEvent"
    private val notifHandshake = "notifHandshake"

    fun setOnReview(value: Boolean) {
        editor.putBoolean(onReview, value)
        editor.commit()
        editor.apply()
    }

    fun setSong(data: Song): Song {
        val json = Gson().toJson(data, Song::class.java)
        sp.edit().putString(songPlay, json).apply()
        return getSong()
    }

    fun getSong(): Song {
        val data = sp.getString(songPlay, null) ?: return Song()
        return Gson().fromJson(data, Song::class.java)
    }

    fun getOnReview(): Boolean {
        return sp.getBoolean(onReview, false)
    }

    fun setOpenApp(value : Int){
        sp.edit().putInt(openApp, value).apply()
    }

    fun getNotifShowroom(): Boolean {
        return sp.getBoolean(notifShowroom, true)
    }

    fun setNotifShowroom(value : Boolean){
        sp.edit().putBoolean(notifShowroom, value).apply()
    }

    fun getNotifNews(): Boolean {
        return sp.getBoolean(notifNews, true)
    }

    fun setNotifNews(value : Boolean){
        sp.edit().putBoolean(notifNews, value).apply()
    }

    fun getNotifEvent(): Boolean {
        return sp.getBoolean(notifEvent, true)
    }

    fun setNotifEvent(value : Boolean){
        sp.edit().putBoolean(notifEvent, value).apply()
    }

    fun getNotifHandshake(): Boolean {
        return sp.getBoolean(notifHandshake, true)
    }

    fun setNotifHandshake(value : Boolean){
        sp.edit().putBoolean(notifHandshake, value).apply()
    }

    fun getOpenApp() : Int{
        return sp.getInt(openApp, 0)
    }

    fun setRated(value : Boolean){
        sp.edit().putBoolean(showRating, value).apply()
    }

    fun isRated() : Boolean{
        return sp.getBoolean(showRating, false)
    }

    fun clearAll() {
        editor.clear()
        editor.commit()
    }

}