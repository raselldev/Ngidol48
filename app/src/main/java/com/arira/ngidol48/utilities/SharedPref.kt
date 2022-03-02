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