package com.arira.ngidol48.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.BlogAdapter
import com.arira.ngidol48.app.App
import com.arira.ngidol48.databinding.FragmentProfilBinding
import com.arira.ngidol48.helper.BaseFragment
import com.arira.ngidol48.helper.Config
import com.arira.ngidol48.helper.SweetAlert
import com.arira.ngidol48.model.Blog
import com.arira.ngidol48.model.User
import com.arira.ngidol48.ui.activity.lagu.LaguActivity
import com.arira.ngidol48.ui.activity.profil.EditProfilActivity
import com.arira.ngidol48.ui.activity.profil.EditProfilViewModel
import com.arira.ngidol48.ui.activity.profil.ProfilByIdViewModel
import com.arira.ngidol48.utilities.Go
import com.arira.ngidol48.utilities.Picker
import com.bumptech.glide.Glide
import net.alhazmy13.mediapicker.Image.ImagePicker
import java.io.File

class ProfilFragment : BaseFragment() {
    private lateinit var binding: FragmentProfilBinding
    private var userData = User()
    private lateinit var viewModel: EditProfilViewModel
    private lateinit var viewModelMyProfil: ProfilByIdViewModel
    private var idUser:String = "0"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        idUser = App.user.id

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profil, container, false)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[EditProfilViewModel::class.java]
        viewModel.context = requireContext()



        viewModelMyProfil = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[ProfilByIdViewModel::class.java]
        viewModelMyProfil.context = requireContext()

        observeProfilById()
        viewModelMyProfil.hit(idUser.toInt())

        action()
        observeProfil()
        setDataUser()

        return binding.root
    }

    private fun observeProfil(){
        viewModel.getLoading().observe(requireActivity()){
            it.let {
                if (it != null){
                    if (it){
                        SweetAlert.onLoading(requireActivity())
                    }else{
                        SweetAlert.dismis()
                    }
                }
            }
        }

        viewModel.getError().observe(requireActivity()){
            it.let {
                if (it != null){
                    SweetAlert.dismis()
                    SweetAlert.onFailure(requireActivity(), it)
                }
            }
        }

        viewModel.getResponse().observe(requireActivity()){
            it.let {
                if (it != null){
                    setDataUser()
                }
            }
        }
    }

    private fun observeProfilById(){
        viewModelMyProfil.getLoading().observe(requireActivity()){
            it.let {
                if (it != null){
                    if (it){
                        SweetAlert.onLoading(requireActivity())
                    }else{
                        SweetAlert.dismis()
                    }
                }
            }
        }

        viewModelMyProfil.getError().observe(requireActivity()){
            it.let {
                if (it != null){
                    SweetAlert.dismis()
                    SweetAlert.onFailure(requireActivity(), it)
                }
            }
        }

        viewModelMyProfil.getResponse().observe(requireActivity()){
            it.let {
                if (it != null){
                    userData = it.user

                    //update ui
                    binding.tvFavLagu.text = it.fav_song_count.toString()
                    binding.tvBlog.text = it.blogs.size.toString()
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
            if (App.user.id == userData.id){
                Go(requireActivity()).move(LaguActivity::class.java, choose = true)
            }

        }

        binding.ivEdit.setOnClickListener {
            Go(requireActivity()).move(EditProfilActivity::class.java)
        }

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            observeProfilById()
        }
        binding.tvGantiAva.setOnClickListener {
            Picker(requireActivity()).ambilGambarSemua(type = "ava")
        }
    }

    private fun setDataUser(){
        if (userData.id == App.user.id){
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
        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
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