package com.arira.ngidol48.utilities

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context : Context) {

    private val mypref = "MAIN_PREF"
    private val sp: SharedPreferences = context.getSharedPreferences(mypref, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sp.edit()
    private val onReview = "onReview"
    private val showRating = "showRating"
    private val openApp = "openApp"

    fun setOnReview(value: Boolean) {
        editor.putBoolean(onReview, value)
        editor.commit()
        editor.apply()
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