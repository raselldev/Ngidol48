package com.arira.ngidol48.ui.activity.profil

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.arira.ngidol48.R
import com.arira.ngidol48.app.App.Companion.pref
import com.arira.ngidol48.app.App.Companion.user
import com.arira.ngidol48.databinding.ActivityEditProfilBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.SweetAlert
import com.arira.ngidol48.model.User

class EditProfilActivity : BaseActivity() {

    private lateinit var binding: ActivityEditProfilBinding
    private lateinit var viewModel: EditProfilViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profil)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profil)
        setToolbar(getString(R.string.teks_perbarui_profil), binding.toolbar)
        setDataUser()

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[EditProfilViewModel::class.java]
        viewModel.context = this

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
                    SweetAlert.success(this, getString(R.string.teks_profil), getString(R.string.teks_profil_berhasil_diperbarui), finish = true)
                }
            }
        }
    }

    private fun action(){
        binding.btnPerbarui.setOnClickListener {
            val fullname = binding.edtNama.text.toString()
            val bio = binding.edtBio.text.toString()
            var gender = ""
            if (binding.rbPria.isChecked){
                gender = "M"
            }else if (binding.rbWanita.isChecked){
                gender = "F"
            }
            if (fullname.isEmpty()){
                toast.show(getString(R.string.teks_nama_kosong), this)
            }else{
                val userUpdate = User()
                userUpdate.fullname = fullname
                userUpdate.bio = bio
                userUpdate.gender = gender
                userUpdate.id = user.id
               viewModel.updateDataProfil(userUpdate)
            }
        }
    }

    private fun setDataUser(){
        val user = pref.getUser()
        binding.edtBio.setText(user.bio)
        binding.edtNama.setText(user.fullname)
        if (user.gender == "M"){
            binding.rbPria.isChecked = true
        }else if (user.gender == "F"){
            binding.rbWanita.isChecked = true
        }
    }
}