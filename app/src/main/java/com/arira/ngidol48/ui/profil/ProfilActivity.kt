package com.arira.ngidol48.ui.profil

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.arira.ngidol48.R
import com.arira.ngidol48.app.App
import com.arira.ngidol48.app.App.Companion.pref
import com.arira.ngidol48.databinding.ActivityProfilBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config
import com.arira.ngidol48.helper.SweetAlert
import com.arira.ngidol48.model.User
import com.arira.ngidol48.utilities.Go
import com.arira.ngidol48.utilities.Picker
import com.bumptech.glide.Glide
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog
import net.alhazmy13.mediapicker.Image.ImagePicker
import java.io.File

class ProfilActivity : BaseActivity() {
    private lateinit var binding: ActivityProfilBinding
    private var user = User()
    private lateinit var viewModel: EditProfilViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profil)
        setToolbar(getString(R.string.teks_profil), binding.toolbar)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[EditProfilViewModel::class.java]
        viewModel.context = this

        setDataUser()
        action()
        observeProfil()

    }

    private fun observeProfil(){
        viewModel.getLoading().observe(this){
            it.let {
                if (it != null){
                    if (it){
                        SweetAlert.onLoading(this)
                    }else{
                        SweetAlert.dismis()
                    }
                }
            }
        }

        viewModel.getError().observe(this){
            it.let {
                if (it != null){
                    SweetAlert.dismis()
                    SweetAlert.onFailure(this, it)
                }
            }
        }

        viewModel.getResponse().observe(this){
            it.let {
                if (it != null){
                    setDataUser()
                }
            }
        }
    }

    private fun action(){
        binding.tvPerbarui.setOnClickListener {
            Go(this).move(EditProfilActivity::class.java)
        }
        binding.tvKeluar.setOnClickListener {
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

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            setDataUser()
        }
        binding.tvGantiAva.setOnClickListener {
            Picker(this).ambilGambarSemua(type = "ava")
        }
    }

    private fun setDataUser(){
        user = pref.getUser()

        binding.tvNamaDetail.text = user.fullname
        binding.tvTlp.text = if (user.phone.isNotEmpty()){
            user.phone
        }else{
            getString(R.string.teks_)
        }
        binding.tvEmail.text = if (user.email.isNotEmpty()){
            user.email
        }else{
            getString(R.string.teks_)
        }
        binding.tvGender.text = if (user.gender.isNotEmpty()){
            if (user.gender == "M"){
                getString(R.string.teks_pria)
            }else{
                getString(R.string.teks_wanita)
            }
        }else{
            getString(R.string.teks_)
        }

        if (user.avatar.contains("http")){
            Glide.with(this).load(user.avatar).error(R.drawable.ic_baseline_person_24).placeholder(R.drawable.ic_baseline_person_24).into(binding.ivAvatar)
        }else{
            Glide.with(this).load(Config.BASE_STORAGE_IMAGE + user.avatar).error(R.drawable.ic_baseline_person_24).placeholder(R.drawable.ic_baseline_person_24).into(binding.ivAvatar)
        }
    }

    override fun onResume() {
        super.onResume()
        setDataUser()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            val mpath: java.util.ArrayList<String> = data!!.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH)!!
            val avatarFile = File(mpath[0])

            Glide.with(this)
                .load(avatarFile)
                .placeholder(R.drawable.ic_baseline_person_24)
                .error(R.drawable.ic_baseline_person_24)
                .into(binding.ivAvatar)
            viewModel.changeAvatar(avatarFile)

        }
    }
}