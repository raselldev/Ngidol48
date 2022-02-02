package com.arira.ngidol48.helper

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.text.Spanned
import android.text.format.DateUtils
import android.util.Log
import androidx.core.text.HtmlCompat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object Helper {
    fun convertRupiah(x: Int?): String {

        val localeID = Locale("in", "ID")
        val format = NumberFormat.getCurrencyInstance(localeID)

        var hasil = format.format(x).replace(",00", "")

        return hasil

    }

    fun fromHtml(string:String):Spanned{
        return HtmlCompat.fromHtml(string, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    fun getAppVersion(context:Context):String{
        val pm: PackageManager = context.applicationContext.packageManager
        val pkgName: String = context.applicationContext.packageName
        var pkgInfo: PackageInfo?

        try {
            pkgInfo =  pm.getPackageInfo(pkgName, 0)
            return pkgInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return "1.0"
        }

//        return context.getString(R.string.versi_app)
    }


    fun convertRupiah(x: String?): String {

        if (x == null) {
            return "null"
        }

        if (x.isEmpty()){
            return "Rp0"
        }

        val localeID = Locale("in", "ID")
        val format = NumberFormat.getCurrencyInstance(localeID)

        var hasil = format.format(Integer.valueOf(x)).replace(",00","")

        return hasil
    }

    fun getDominantColor(bitmap: Bitmap?): Int {
        val newBitmap = Bitmap.createScaledBitmap(bitmap!!, 1, 1, true)
        val color = newBitmap.getPixel(0, 0)
        newBitmap.recycle()
        return color
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDateTimeFree(date: String, ygDimau: String): String {
        var hasil = ""

        //        final String formatBaru = "dd MMMM yyyy, hh:mm";
        val formatLama = "yyyy-MM-dd kk:mm:ss"
        val dateFormat = SimpleDateFormat(formatLama)

        if (date.isNotEmpty()
        ) {
            try {
                val dd = dateFormat.parse(date)
                dateFormat.applyPattern(ygDimau)
                hasil = dateFormat.format(dd)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }

        return hasil
    }

    @SuppressLint("SimpleDateFormat")
    fun convertJam(date: String, ygDimau: String): String {
        var hasil = ""

        //        final String formatBaru = "dd MMMM yyyy, hh:mm";
        val formatLama = "kk:mm:ss"
        val dateFormat = SimpleDateFormat(formatLama)

        if (date.isNotEmpty()
        ) {
            try {
                val dd = dateFormat.parse(date)
                dateFormat.applyPattern(ygDimau)
                hasil = dateFormat.format(dd)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }

        return hasil
    }


    fun waktulalu(tanggalFeed: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var time: Long = 0
        try {
            time = sdf.parse(tanggalFeed)!!.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val now = System.currentTimeMillis()

        val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
        return ago.toString()
    }

    fun convert(date: String, from: String, to: String): String {
        var hasil = ""

        val dateFormat = SimpleDateFormat(from)

        if (date.isEmpty()){
            return date
        }

        try {
            val dd = dateFormat.parse(date)
            dateFormat.applyPattern(to)
            hasil = dateFormat.format(dd)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return hasil
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDate(date: String?): String {
        var hasil = ""

        val formatBaru = " HH:mm, dd MMMM yyyy"
        val formatLama = "yyyy-MM-dd HH:mm:ss.SSS"
        val dateFormat = SimpleDateFormat(formatLama)

        if (date != null){
            try {
                val dd = dateFormat.parse(date)
                dateFormat.applyPattern(formatBaru)
                hasil = dateFormat.format(dd)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return hasil
    }

    fun getIntervalSecond(awal:String, akhir:String): Long {
        val formatLama = "yyyy-MM-dd HH:mm:ss"
        val dateFormat = SimpleDateFormat(formatLama, Locale.getDefault())
        val dateAwal = dateFormat.parse(awal)
        val dateAkhir = dateFormat.parse(akhir)

        val diff: Long = dateAkhir.time - dateAwal.time
        val seconds = diff / 1000

        log("Helper", "jarak ${seconds}")
        return seconds
    }

    fun getIntervalMinutes(awal:String, akhir:String): Long {
        val formatLama = "yyyy-MM-dd HH:mm:ss"
        val dateFormat = SimpleDateFormat(formatLama, Locale.getDefault())
        val dateAwal = dateFormat.parse(awal)
        val dateAkhir = dateFormat.parse(akhir)

        val diff: Long = dateAkhir.time - dateAwal.time
        val seconds = diff / 1000
        val minutes = seconds / 60

        return minutes
    }

    fun getIntervalHours(awal:String, akhir:String): Long {
        val formatLama = "yyyy-MM-dd HH:mm:ss"
        val dateFormat = SimpleDateFormat(formatLama, Locale.getDefault())
        val dateAwal = dateFormat.parse(awal)
        val dateAkhir = dateFormat.parse(akhir)

        val diff: Long = dateAkhir.time - dateAwal.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        return hours
    }

    fun getIntervalDays(awal:String, akhir:String): Long {
        val formatLama = "yyyy-MM-dd HH:mm:ss"
        val dateFormat = SimpleDateFormat(formatLama, Locale.getDefault())
        val dateAwal = dateFormat.parse(awal)
        val dateAkhir = dateFormat.parse(akhir)

        val diff: Long = dateAkhir.time - dateAwal.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return days
    }

    fun getCurrentHour():String{
        val sdf = SimpleDateFormat("HH", Locale.getDefault())
        return sdf.format(Date())
    }

    //MAKE LOG RESUCE
    fun log(place: String = "", msg: String){
        //CHECK RELEASE MODE [if release log not show]

        if (!Validasi().isRelease()){
            if (place.isBlank()){
                Log.e("MGS", msg)
            }else{
                Log.e("MGS " + place, msg)
            }
        }

    }

    fun getCurrentDateTime():String{
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    fun getCurrentTime():String{
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    fun getCurrentDate():String{
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    fun openAppAtStore(context: Context) {
        // you can also use BuildConfig.APPLICATION_ID
        val appId = context.packageName
        val rateIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("market://details?id=$appId")
        )
        var marketFound = false

        // find all applications able to handle our rateIntent
        val otherApps = context.packageManager
            .queryIntentActivities(rateIntent, 0)
        for (otherApp in otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName
                == "com.android.vending"
            ) {
                val otherAppActivity = otherApp.activityInfo
                val componentName = ComponentName(
                    otherAppActivity.applicationInfo.packageName,
                    otherAppActivity.name
                )
                // make sure it does NOT open in the stack of your activity
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                // task reparenting if needed
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                // if the Google Play was already open in a search result
                //  this make sure it still go to the app page you requested
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                // this make sure only the Google Play app is allowed to
                // intercept the intent
                rateIntent.component = componentName
                context.startActivity(rateIntent)
                marketFound = true
                break
            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appId")
            )
            context.startActivity(webIntent)
        }
    }

}