package com.arira.ngidol48.helper

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.format.DateUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.text.HtmlCompat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object Helper {
    fun convertRupiah(x: Int?): String {

        val localeID = Locale("in", "ID")
        val format = NumberFormat.getCurrencyInstance(localeID)

        val hasil = format.format(x).replace(",00", "")

        return hasil

    }

    fun capitalizeString(str: String): String {
        var retStr = str
        try { // We can face index out of bound exception if the string is null
            retStr = str.substring(0, 1).uppercase() + str.substring(1).lowercase()
        } catch (e: Exception) {
        }
        return retStr
    }

    fun fromHtml(string:String):Spanned{
        return HtmlCompat.fromHtml(string, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    fun setTags(pTextView: TextView, pTagString: String, mContext:Context) {
        val string = SpannableString(pTagString)
        var start = -1
        var i = 0
        while (i < pTagString.length) {
            if (/*pTagString[i] == '@' ||*/ pTagString[i] == '#') {
                start = i
            } else if (pTagString[i] == ' '
                || i == pTagString.length - 1 && start != -1
            ) {
                if (start != -1) {
                    if (i == pTagString.length - 1) {
                        i++ // case for if hash is last word and there is no
                        // space after word
                    }
                    val tag = pTagString.substring(start, i)

                    string.setSpan(object : ClickableSpan() {
                        override fun onClick(p0: View) {
//                            val intents = Intent(mContext, CariFeedActivity::class.java)
//                            intents.putExtra(extra_id, tag)
//                            mContext.startActivity(intents)
                        }

                        override fun updateDrawState(ds: TextPaint) {
//                            if (tag.contains("@")) ds.setColor(Color.parseColor("#0474be")) else ds.setColor(
//                                Color.parseColor("#ed6057")
//                            )
                            if (tag.contains("#")) ds.setColor(Color.parseColor("#0474be"))
                            ds.setUnderlineText(false)
                        }
                    }, start, i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    start = -1
                }
            }
            i++
        }
        pTextView.setMovementMethod(LinkMovementMethod.getInstance())
        pTextView.setText(string)
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

    fun openAppAtStore(context: Context, appId:String) {
        // you can also use BuildConfig.APPLICATION_ID
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