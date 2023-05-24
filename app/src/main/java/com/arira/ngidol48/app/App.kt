package com.arira.ngidol48.app

import android.app.Application
import android.content.res.Configuration
import android.os.Build
import android.os.Environment
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import com.arira.ngidol48.helper.Config.DIRECTORY_IMAGE_AVATAR
import com.arira.ngidol48.helper.Config.DIRECTORY_IMAGE_BLOG
import com.arira.ngidol48.helper.Config.DIRECTORY_IMAGE_PHOTOCARD
import com.arira.ngidol48.helper.Helper
import com.arira.ngidol48.helper.HelperToast
import com.arira.ngidol48.helper.Validasi
import com.arira.ngidol48.model.User
import com.arira.ngidol48.utilities.SharedPref
import java.io.File

class App : Application() {

    companion object {
        lateinit var pref: SharedPref
        var validasi = Validasi()
        var helper = Helper
        var toast = HelperToast()
        lateinit var user: User
        var token:String = ""
        var isDark = false
        val curdate:String = java.text.SimpleDateFormat(
            "yyyy-MM-dd",
            java.util.Locale("ID")
        ).format(
            java.util.Date()
        )

    }

    fun clearAppData(){
        pref.clearAll()

        user = User()
        token = ""
    }

    fun recreate() {
        onCreate()
    }


    override fun onCreate() {
        super.onCreate()
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        pref = SharedPref(this)
        user = pref.getUser()
        token = user.token_app
        setTheme(pref.isDark())

        if (pref.getUserTemp().isEmpty()){
            pref.setUserTemp("user-${(100..1999).random()}")
        }

        if (user.fullname.isEmpty()){
            user.fullname = pref.getUserTemp()
        }

        when (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                isDark = true
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                isDark = false
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                isDark = false
            }
        }
        isDark = pref.isDark()

        /*path avatar*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            DIRECTORY_IMAGE_AVATAR =  getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString()
            val sdIconStorageDir = File(DIRECTORY_IMAGE_AVATAR)
            if (!sdIconStorageDir.isDirectory) {
                //BUAT DIRECTORY
                sdIconStorageDir.mkdirs()
            }
        }
        else{
            val sdIconStorageDir = File(DIRECTORY_IMAGE_AVATAR)
            if (!sdIconStorageDir.isDirectory) {
                //BUAT DIRECTORY
                sdIconStorageDir.mkdirs()
            }
        }

        /*path blog*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            DIRECTORY_IMAGE_BLOG =  getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString()
            val sdIconStorageDir = File(DIRECTORY_IMAGE_BLOG)
            if (!sdIconStorageDir.isDirectory) {
                //BUAT DIRECTORY
                sdIconStorageDir.mkdirs()
            }
        }
        else{
            val sdIconStorageDir = File(DIRECTORY_IMAGE_BLOG)
            if (!sdIconStorageDir.isDirectory) {
                //BUAT DIRECTORY
                sdIconStorageDir.mkdirs()
            }
        }

        /*path photocard*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            DIRECTORY_IMAGE_PHOTOCARD =  getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString()
            val sdIconStorageDir = File(DIRECTORY_IMAGE_PHOTOCARD)
            if (!sdIconStorageDir.isDirectory) {
                //BUAT DIRECTORY
                sdIconStorageDir.mkdirs()
            }
        }
        else{
            val sdIconStorageDir = File(DIRECTORY_IMAGE_PHOTOCARD)
            if (!sdIconStorageDir.isDirectory) {
                //BUAT DIRECTORY
                sdIconStorageDir.mkdirs()
            }
        }
    }

    private fun setTheme(isDarkTheme: Boolean) {
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}