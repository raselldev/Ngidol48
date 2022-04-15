package com.arira.ngidol48.helper

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import com.arira.ngidol48.R
import com.arira.ngidol48.databinding.LayoutToolbarBinding
import com.arira.ngidol48.databinding.SheetDetailMemberBinding
import com.arira.ngidol48.model.Member
import com.arira.ngidol48.ui.handshakeMember.HandshakeByMemberActivity
import com.arira.ngidol48.ui.myWeb.MyWebActivity
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.lang.Exception


open class BaseActivity : AppCompatActivity() {

    var toast = HelperToast()

    val curdate:String = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss",
        Locale("ID")
    ).format(
        Date()
    )

    val curDay:String = SimpleDateFormat(
        "d",
        Locale("ID")
    ).format(
        Date()
    )

    val curdateonly:String = SimpleDateFormat(
        "yyyy-MM-dd",
        Locale("ID")
    ).format(
        Date()
    )

    val curtime:String = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss",
        Locale("ID")
    ).format(
        Date()
    )

    fun showSheetMember(member:Member){
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)

        val bindingSheet: SheetDetailMemberBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.sheet_detail_member,
            null,
            false
        )
        bottomSheetDialog.dismissWithAnimation = true

        bottomSheetDialog.setContentView(bindingSheet.root)


        bindingSheet.tvTinggil.text = member.tinggi_badan
        bindingSheet.tvTanggalLahir.text = member.tanggal_lahir
        bindingSheet.tvGolDarah.text = member.gol_darah
        bindingSheet.tvHoroskop.text = member.horoskop
        bindingSheet.tvPanggilan.text = member.nama_panggilan
        bindingSheet.tvTeam.text = member.kategori
        bindingSheet.tvNama.text = member.nama_lengkap
        Glide.with(this).load(Config.BASE_STORAGE_JKT + member.avatar).into(bindingSheet.ivAva)

        if (member.generasi != "0"){
            bindingSheet.tvGenerasi.text = "Gen ${member.generasi}"
            bindingSheet.tvGenerasi.visibility = View.VISIBLE
        }else{
            bindingSheet.tvGenerasi.visibility = View.GONE
        }


        if (member.jiko.isNotEmpty()){
            bindingSheet.tvJiko.text = "`${member.jiko}`"
            bindingSheet.tvJiko.visibility = View.VISIBLE
        }else{
            bindingSheet.tvJiko.visibility = View.GONE
        }

        bindingSheet.linHandshake.setOnClickListener {
            Go(this).move(HandshakeByMemberActivity::class.java, id = member.id_member)
        }

        //media sosial
        val showMedia = member.media_instagram.isNotEmpty()

        if (showMedia){
            bindingSheet.linMedia.visibility = View.VISIBLE
            bindingSheet.tvInstagram.text = "${member.media_instagram}"
            bindingSheet.tvTiktok.text = "${member.media_tiktok}"
            bindingSheet.tvTwitter.text = "${member.media_twitter}"

            bindingSheet.linInstagram.setOnClickListener {
                launchInsta(member.media_instagram)
            }

            bindingSheet.linTiktok.setOnClickListener {
                openTikTokProfile(member.media_tiktok)
            }

            bindingSheet.linTwitter.setOnClickListener {
                openTwitter(member.media_twitter)
            }
        }else{
            bindingSheet.linMedia.visibility = View.GONE
        }

        if(member.media_showroom.isNotEmpty()){
            bindingSheet.tvShowroom.visibility = View.VISIBLE
            bindingSheet.tvShowroom.setOnClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(member.media_showroom)
                    )
                )
            }
        }else{
            bindingSheet.tvShowroom.visibility = View.GONE
        }

        //fanbase
        if (member.fanbase_name.isNotEmpty()){
            bindingSheet.linFans.visibility = View.VISIBLE
            bindingSheet.tvFanbase.text = member.fanbase_name
            bindingSheet.tvFanbaseVisit.setOnClickListener {
                Go(this).move(MyWebActivity::class.java, url = member.fanbase_page)
            }
        }else{
            bindingSheet.linFans.visibility = View.GONE
        }

        bottomSheetDialog.show()
    }

    fun openTwitter(username:String){
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("twitter://user?screen_name=$username")
                )
            )
        } catch (e: Exception) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/#!/$username")
                )
            )
        }
    }

    fun openTikTokProfile(username:String) {
        val uri: Uri = Uri.parse("https://www.tiktok.com/${username}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.zhiliaoapp.musically")


        try {
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }else{
                val forBrowser = Intent(Intent.ACTION_VIEW, uri)
                startActivity(forBrowser)
            }
        } catch (e: ActivityNotFoundException) {
            val forBrowser = Intent(Intent.ACTION_VIEW, uri)
            startActivity(forBrowser)
        }

    }

    fun launchInsta(uname:String) {
        val uriForApp: Uri = Uri.parse("http://instagram.com/_u/${uname}")
        val forApp = Intent(Intent.ACTION_VIEW, uriForApp)

        val uriForBrowser: Uri = Uri.parse("http://instagram.com/${uname}")
        val forBrowser = Intent(Intent.ACTION_VIEW, uriForBrowser)

        forApp.component =
            ComponentName(
                "com.instagram.android",
                "com.instagram.android.activity.UrlHandlerActivity"
            )

        try {
            startActivity(forApp)
        } catch (e: ActivityNotFoundException) {
            startActivity(forBrowser)
        }
    }

    fun statusPutih() {
        supportActionBar?.hide()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window: Window = window
            val decorView: View = window.decorView
            val wic = WindowInsetsControllerCompat(window, decorView)


            when (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    wic.isAppearanceLightStatusBars = false // true or false as desired.
                    window.statusBarColor = Color.BLACK
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    wic.isAppearanceLightStatusBars = true // true or false as desired.
                    window.statusBarColor = Color.WHITE
                }
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    wic.isAppearanceLightStatusBars = true // true or false as desired.
                    window.statusBarColor = Color.WHITE
                }
            }

        }


    }


    fun setToolbar(title: String, toolbarTitle: LayoutToolbarBinding) {
        statusPutih()

        when (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                toolbarTitle.toolbarBack.setImageResource(R.drawable.ic_back_white)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                toolbarTitle.toolbarBack.setImageResource(R.drawable.ic_back_black)
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                toolbarTitle.toolbarBack.setImageResource(R.drawable.ic_back_black)
            }
        }

        toolbarTitle.toolbarTitle.text = title
        toolbarTitle.toolbarBack.setOnClickListener {
            onBackPressed()
        }

        //mengubah warna status bar

    }

    fun failureRequest(context: Activity, msg: String){

        SweetAlert.dismis()
        SweetAlert.onFailure(context, msg)

    }

    fun pindah(tujuan: Context, context: Context){

        val intent = Intent(context, tujuan::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

    }

}