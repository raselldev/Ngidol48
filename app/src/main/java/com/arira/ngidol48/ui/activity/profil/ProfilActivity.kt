package com.arira.ngidol48.ui.activity.profil

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.BlogAdapter
import com.arira.ngidol48.app.App.Companion.user
import com.arira.ngidol48.databinding.ActivityProfilBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config
import com.arira.ngidol48.helper.Config.extra_id
import com.arira.ngidol48.helper.SweetAlert
import com.arira.ngidol48.model.Blog
import com.arira.ngidol48.model.User
import com.arira.ngidol48.ui.activity.lagu.LaguActivity
import com.arira.ngidol48.utilities.Go
import com.arira.ngidol48.utilities.Picker
import com.bumptech.glide.Glide
import net.alhazmy13.mediapicker.Image.ImagePicker
import java.io.File

class ProfilActivity : BaseActivity() {
    private lateinit var binding: ActivityProfilBinding
    private var userData = User()
    private lateinit var viewModel: EditProfilViewModel
    private lateinit var viewModelMyProfil: ProfilByIdViewModel
    private var idUser:String = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profil)
        setToolbar(getString(R.string.teks_profil), binding.toolbar)

        idUser = intent.getStringExtra(extra_id) ?: "0"
        if (idUser.isEmpty()){
            idUser = user.id
        }

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[EditProfilViewModel::class.java]
        viewModel.context = this

        viewModelMyProfil = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[ProfilByIdViewModel::class.java]
        viewModelMyProfil.context = this

        observeProfilById()
        viewModelMyProfil.hit(idUser.toInt())

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

    private fun observeProfilById(){
        viewModelMyProfil.getLoading().observe(this){
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

        viewModelMyProfil.getError().observe(this){
            it.let {
                if (it != null){
                    SweetAlert.dismis()
                    SweetAlert.onFailure(this, it)
                }
            }
        }

        viewModelMyProfil.getResponse().observe(this){
            it.let {
                if (it != null){
                    userData = it.user

                    //update ui
                    binding.tvFavLagu.text = getString(R.string.d_lagu, it.fav_song_count)
                    binding.tvBlog.text = getString(R.string.d_blog, it.blogs.size)
                    if(it.blogs.isEmpty()){
                        binding.divBlogKosong.visibility = View.VISIBLE
                    }else{
                        binding.divBlogKosong.visibility = View.GONE
                        binding.rvBlog.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = BlogAdapter(it.blogs as ArrayList<Blog>)
                        }
                    }

                    setDataUser()
                }
            }
        }
    }

    private fun action(){
        binding.linFav.setOnClickListener {
            if (user.id == userData.id){
                Go(this).move(LaguActivity::class.java, choose = true)
            }

        }

        binding.ivEdit.setOnClickListener {
            Go(this).move(EditProfilActivity::class.java)
        }

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            observeProfilById()
        }
        binding.tvGantiAva.setOnClickListener {
            Picker(this).ambilGambarSemua(type = "ava")
        }
    }

    private fun setDataUser(){
        if (userData.id == user.id){
            binding.ivEdit.visibility = View.VISIBLE
            binding.tvGantiAva.visibility = View.VISIBLE
        }else{
            binding.ivEdit.visibility = View.GONE
            binding.tvGantiAva.visibility = View.GONE
        }
        binding.tvBlogName.text = getString(R.string.teks_blog_s_user, userData.fullname)
        binding.tvNamaDetail.text = userData.fullname
        binding.tvGender.text = userData.bio

        if (userData.avatar.contains("http")){
            Glide.with(this).load(userData.avatar).error(R.drawable.ic_baseline_person_24).placeholder(R.drawable.ic_baseline_person_24).into(binding.ivAvatar)
        }else{
            Glide.with(this).load(Config.BASE_STORAGE_IMAGE + userData.avatar).error(R.drawable.ic_baseline_person_24).placeholder(R.drawable.ic_baseline_person_24).into(binding.ivAvatar)
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