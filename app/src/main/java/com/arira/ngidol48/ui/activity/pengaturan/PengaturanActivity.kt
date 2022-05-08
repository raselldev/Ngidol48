package com.arira.ngidol48.ui.activity.pengaturan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.arira.ngidol48.R
import com.arira.ngidol48.app.App
import com.arira.ngidol48.databinding.ActivityPengaturanBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.utilities.Go
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog

class PengaturanActivity : BaseActivity() {
    private lateinit var binding: ActivityPengaturanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaturan)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pengaturan)
        setToolbar(getString(R.string.teks_pengaturan), binding.toolbar)
        action()

        if (App.pref.getOnReview()){
            binding.linDonasi.visibility = View.GONE
        }
    }
    private fun action(){
        binding.tvSignout.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                .setCancelText(getString(R.string.teks_tidak))
                .setConfirmText(getString(R.string.teks_iya))
                .setTitleText(getString(R.string.teks_profil))
                .setContentText(getString(R.string.teks_deskripsi_keluar))
                .setConfirmClickListener {
                    it.dismiss()
                    App().clearAppData()
                    finish()
                }
                .setCancelClickListener {
                    it.dismiss()
                }
                .show()
        }

        binding.linDonasi.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://saweria.co/raflipakei")
            startActivity(openURL)
        }
        binding.linSaran.setOnClickListener {
            val i = Intent(Intent.ACTION_SENDTO)
            i.type = "message/rfc822"
            i.setData(Uri.parse("mailto:arira.studio@gmail.com"))
            startActivity(Intent.createChooser(i, "Choose an Email client :"))
        }
        binding.linNotifikasi.setOnClickListener {
            Go(this).move(PengaturanNotifikasiActivity::class.java)
        }
    }
}