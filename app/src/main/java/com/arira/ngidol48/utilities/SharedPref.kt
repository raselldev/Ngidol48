package com.arira.ngidol48.utilities

import android.content.Context
import android.content.SharedPreferences
import com.arira.ngidol48.model.Song
import com.arira.ngidol48.model.User
import com.google.gson.Gson

class SharedPref(context : Context) {

    private val mypref = "MAIN_PREF"
    private val sp: SharedPreferences = context.getSharedPreferences(mypref, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sp.edit()
    private val onReview = "onReview"
    private val isLogin = "isLogin"
    private val showRating = "showRating"
    private val openApp = "openApp"

    private val songPlay = "songPlay"

    private val radioURL = "radioURL"
    private val isDark = "isDark"

    private val userTemp = "userTemp"

    private val notifShowroom = "notifShowroom"
    private val notifNews = "notifNews"
    private val notifMng = "notifMng"
    private val notifEvent = "notifEvent"
    private val notifHandshake = "notifHandshake"
    private val notifStreaming = "notifStreaming"

    private val calenderNewView = "calenderNewView"
    private val sessionPCId = "pcSessions"
    private val totalCreatePC = "totalCreatePC"
    private val token = "token"
    private val users = "users"

    private val fcmToken:String = "fcmToken"

    fun setOnReview(value: Boolean) {
        editor.putBoolean(onReview, value)
        editor.commit()
        editor.apply()
    }

    fun setFCMToken(status : String){
        sp.edit().putString(fcmToken, status).apply()
    }
    fun getFCMToken():String{
        return sp.getString(fcmToken, "") ?: ""
    }

    fun setRadioURL(url : String){
        sp.edit().putString(radioURL, url).apply()
    }
    fun getRadioURL():String{
        return sp.getString(radioURL, "") ?: ""
    }

    fun setString(variable:String, value : String){
        sp.edit().putString(variable, value).apply()
    }
    fun getString(variable:String):String{
        return sp.getString(variable, "") ?: ""
    }

    fun setUserTemp(status : String){
        sp.edit().putString(userTemp, status).apply()
    }
    fun getUserTemp():String{
        return sp.getString(userTemp, "") ?: ""
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

    fun setUser(data: User): User {
        val json = Gson().toJson(data, User::class.java)
        sp.edit().putString(users, json).apply()
        return getUser()
    }

    fun getUser(): User {
        val data = sp.getString(users, null) ?: return User()
        return Gson().fromJson(data, User::class.java)
    }

    fun getOnReview(): Boolean {
        return sp.getBoolean(onReview, true)
    }

    fun setOpenApp(value : Int){
        sp.edit().putInt(openApp, value).apply()
    }

    fun setIsLogin(value : Boolean){
        sp.edit().putBoolean(isLogin, value).apply()
    }

    fun getIsLogin(): Boolean {
        return sp.getBoolean(isLogin, false)
    }

    fun getNewCalender(): Boolean {
        return sp.getBoolean(calenderNewView, false)
    }

    fun setNewCalender(value : Boolean){
        sp.edit().putBoolean(calenderNewView, value).apply()
    }

    fun isDark(): Boolean {
        return sp.getBoolean(isDark, false)
    }

    fun setDark(value : Boolean){
        sp.edit().putBoolean(isDark, value).apply()
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

    fun getNotifMng(): Boolean {
        return sp.getBoolean(notifMng, true)
    }

    fun setNotifMng(value : Boolean){
        sp.edit().putBoolean(notifMng, value).apply()
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

    fun getNotifStreaming(): Boolean {
        return sp.getBoolean(notifStreaming, true)
    }

    fun setNotifStreaming(value : Boolean){
        sp.edit().putBoolean(notifStreaming, value).apply()
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

    fun setSessionPC( value : String) {
        sp.edit().putString(sessionPCId, value).apply()
    }
    fun getSessionPC():String {
        return sp.getString(sessionPCId, "") ?: ""
    }

    fun setTotalCreatePC( value : Int) {
        sp.edit().putInt(totalCreatePC, value).apply()
    }
    fun getTotalCreatePC():Int {
        return sp.getInt(totalCreatePC, 0)
    }

}