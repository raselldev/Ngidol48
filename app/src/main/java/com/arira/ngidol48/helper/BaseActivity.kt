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
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import com.arira.ngidol48.R
import com.arira.ngidol48.databinding.LayoutToolbarBinding
import com.arira.ngidol48.databinding.SheetDetailMemberBinding
import com.arira.ngidol48.model.Member
import com.arira.ngidol48.ui.activity.detailMember.DetailMemberActivity
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*


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

        bindingSheet.tvTeam.text = member.kategori
        bindingSheet.tvNama.text = member.nama_lengkap
        bindingSheet.tvShow.text = getString(R.string.teks_s_show, member.showtime)
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

        bindingSheet.btnDetail.setOnClickListener {
            bottomSheetDialog.dismiss()
            Go(this).move(DetailMemberActivity::class.java, data = member)
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

    fun newStatusBarTheme() {
        supportActionBar?.hide()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window: Window = window
            val decorView: View = window.decorView
            val wic = WindowInsetsControllerCompat(window, decorView)


            when (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    wic.isAppearanceLightStatusBars = false // true or false as desired.
                    window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    wic.isAppearanceLightStatusBars = true // true or false as desired.
                    window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
                }
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    wic.isAppearanceLightStatusBars = true // true or false as desired.
                    window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
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
    fun setNewToolbar(title: String, toolbarTitle: LayoutToolbarBinding) {
        newStatusBarTheme()

        toolbarTitle.toolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.white))
        toolbarTitle.toolbarBack.setImageResource(R.drawable.ic_back_white)

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

    fun updatePositionSign(imageView: ImageView, posisiSign:Int) {
        when(posisiSign) {
            0 -> {
                val layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START)
                imageView.layoutParams = layoutParams
            }
            1 -> {
                val layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
                imageView.layoutParams = layoutParams
            }
            2 -> {
                val layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)
                imageView.layoutParams = layoutParams
            }
        }
    }

}