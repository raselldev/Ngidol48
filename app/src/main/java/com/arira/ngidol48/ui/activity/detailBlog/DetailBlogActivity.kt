package com.arira.ngidol48.ui.activity.detailBlog

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.KomentarAdapter
import com.arira.ngidol48.app.App.Companion.helper
import com.arira.ngidol48.app.App.Companion.pref
import com.arira.ngidol48.app.App.Companion.user
import com.arira.ngidol48.databinding.ActivityDetailBlogBinding
import com.arira.ngidol48.databinding.DialogKomentarBinding
import com.arira.ngidol48.databinding.SheetMenuBlogBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config
import com.arira.ngidol48.helper.Config.extra_model
import com.arira.ngidol48.helper.Helper
import com.arira.ngidol48.helper.Helper.shareText
import com.arira.ngidol48.helper.SweetAlert
import com.arira.ngidol48.model.Blog
import com.arira.ngidol48.model.Komentar
import com.arira.ngidol48.ui.activity.ViewImageActivity
import com.arira.ngidol48.ui.activity.addBlog.BlogViewModel
import com.arira.ngidol48.ui.activity.home.MainActivity
import com.arira.ngidol48.ui.activity.login.LoginActivity
import com.arira.ngidol48.ui.activity.profil.ProfilActivity
import com.arira.ngidol48.ui.activity.reportBlog.ReportBlogActivity
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.google.android.material.bottomsheet.BottomSheetDialog

class DetailBlogActivity : BaseActivity(), KomentarCallback {
    private lateinit var binding: ActivityDetailBlogBinding
    private var blog:Blog = Blog()

    private var listKomentar:ArrayList<Komentar> = ArrayList()
    private lateinit var adapterKomentar:KomentarAdapter

    private lateinit var dialogKomentar: AlertDialog.Builder
    private lateinit var bindingDialogKomentar: DialogKomentarBinding

    private lateinit var komentarViewModel:KomentarViewModel

    private lateinit var blogViewModel: BlogViewModel

    private lateinit var detailBlogViewModel: DetailBlogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_blog)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_blog)
        setToolbar(getString(R.string.teks_blog), binding.toolbar)

        binding.toolbar.ivMenu.visibility = View.VISIBLE

        /*komentar*/
        komentarViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[KomentarViewModel::class.java]
        komentarViewModel.context = this

        blogViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[BlogViewModel::class.java]
        blogViewModel.context = this

        detailBlogViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailBlogViewModel::class.java]
        detailBlogViewModel.context = this

        val appLinkIntent = intent
        val appLinkData = appLinkIntent.data
        if (appLinkData != null){
            val id: Int
            try{
                id = appLinkData.lastPathSegment.toString().toInt()
                blog.id = id.toString()

                detailBlogViewModel.hitDetail(blog.id)
            }catch (e:NumberFormatException){
                toast.show(getString(R.string.teks_data_tidak_valid), this)
                Go(this).move(MainActivity::class.java, clearPrevious = true)
            }

        }else{
            blog = intent.getParcelableExtra(extra_model) ?: Blog()
        }

        adapterKomentar = KomentarAdapter(listKomentar, this)


        setDataBlog()

        action()

        observeDataBlog()
        observeDataDetailBlog()
        observeDataKomentar()

        /*set data user*/
        if(pref.getIsLogin()){
            val user = pref.getUser()
            if (user.avatar.contains("default")){
                Glide.with(this).load(user.avatar).error(R.drawable.ic_baseline_person_24).placeholder(R.drawable.ic_baseline_person_24).into(binding.ivAvaUser)
            }else{
                Glide.with(this).load(Config.BASE_STORAGE_IMAGE + user.avatar).error(R.drawable.ic_baseline_person_24).placeholder(R.drawable.ic_baseline_person_24).into(binding.ivAvaUser)
            }
        }


    }

    private fun showSheetKomentar() {

        dialogKomentar = AlertDialog.Builder(this)
        bindingDialogKomentar = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.dialog_komentar,
            null,
            false
        )

        dialogKomentar.setView(bindingDialogKomentar.root)
        dialogKomentar.setCancelable(true)

        val dialogCreate = dialogKomentar.create()

        Glide.with(this)
            .asBitmap()
            .load(Config.BASE_STORAGE_IMAGE + user.avatar)
            .placeholder(R.drawable.ic_baseline_person_24)
            .error(R.drawable.ic_baseline_person_24)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    bindingDialogKomentar.ivAva.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })


        if (listKomentar.isEmpty()) {
            komentarViewModel.hitAll(blog.id)
            dialogKomentar.setCancelable(false)

        } else {
            dialogKomentar.setCancelable(true)
            bindingDialogKomentar.divKosong.visibility = View.GONE
        }

        bindingDialogKomentar.ivClose.setOnClickListener {
            dialogCreate.dismiss()
        }
        bindingDialogKomentar.rvData.apply {
            adapter = adapterKomentar
            layoutManager = LinearLayoutManager(context)
        }

        bindingDialogKomentar.swipe.setOnRefreshListener {
            bindingDialogKomentar.swipe.isRefreshing  = false
            komentarViewModel.hitAll(blog.id)
        }

        bindingDialogKomentar.ivKirim.setOnClickListener {
            val teks = bindingDialogKomentar.edtKomentar.text.toString()
            if(teks.isNotEmpty()){
                val komentar = Komentar()
                komentar.id_blog = blog.id
                komentar.komentar = teks
                komentar.avatar = user.avatar
                komentar.fullname = user.fullname
                komentar.id_user = user.id
                komentar.created_at = helper.getCurrentDateTime()

                komentarViewModel.komentar(komentar)
                listKomentar.add(0, komentar)
                adapterKomentar.notifyDataSetChanged()

                bindingDialogKomentar.edtKomentar.setText("")
                if (bindingDialogKomentar.divKosong.visibility == View.VISIBLE){
                    bindingDialogKomentar.divKosong.visibility = View.GONE
                }
            }

        }

        dialogCreate.show()

    }

    override fun delete(komentar: Komentar, position: Int) {
        komentarViewModel.delete(komentar, position)
    }

    private fun sheetMenu(){
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val bindingSheet:SheetMenuBlogBinding  = DataBindingUtil.inflate(layoutInflater, R.layout.sheet_menu_blog, null, false)

        bottomSheetDialog.setContentView(bindingSheet.root)
        bottomSheetDialog.dismissWithAnimation = true

        if (blog.id_user == user.id){
            bindingSheet.linHapus.visibility  = View.VISIBLE
            bindingSheet.linLapor.visibility  = View.GONE
        }else{
            bindingSheet.linHapus.visibility  = View.GONE
            bindingSheet.linLapor.visibility  = View.VISIBLE
        }

        bindingSheet.linLapor.setOnClickListener {
            bottomSheetDialog.dismiss()
            Go(this).move(ReportBlogActivity::class.java, data = blog)
        }

        bindingSheet.linBagikan.setOnClickListener {
            val link = Config.shareUrlBlog(blog.id)
            shareText(link, this)
        }

        bindingSheet.linHapus.setOnClickListener {
            bottomSheetDialog.dismiss()

            //show dialog delete
            val dialogAlert = AlertDialog.Builder(this)
            dialogAlert.setTitle(getString(R.string.teks_blog))
            dialogAlert.setMessage(getString(R.string.teks_detail_delete_blog))
            dialogAlert.setPositiveButton(getString(R.string.teks_ya)) { _, _ ->
                //delete
                blogViewModel.delete(blog.id)
            }
            dialogAlert.setNegativeButton(getString(R.string.teks_tidak)) { _, _ ->
                //do nothing
            }
            dialogAlert.show()
        }

        bottomSheetDialog.show()
    }

    private fun action() {
        binding.linUser.setOnClickListener {
            Go(this).move(ProfilActivity::class.java, id = blog.id_user)
        }
        binding.toolbar.ivMenu.setOnClickListener {
            sheetMenu()
        }
        binding.linKomentar.setOnClickListener {
            if (pref.getIsLogin()){
                showSheetKomentar()
            }else{
                Go(this).move(LoginActivity::class.java)
            }

        }

        binding.ivCover.setOnClickListener {
            Go(this).move(ViewImageActivity::class.java, url = Config.BASE_STORAGE_IMAGE + blog.cover)
        }
    }

    private fun setDataBlog(){
        if (!blog.cover.contains("default")){
            binding.ivCover.visibility = View.VISIBLE
            Glide.with(this)
                .asBitmap()
                .load(Config.BASE_STORAGE_IMAGE + blog.cover)
                .placeholder(R.drawable.img_logo)
                .error(R.drawable.img_logo)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                    ) {
                        binding.ivCover.setImageBitmap(resource)
                        binding.ivCover.setBackgroundColor(Helper.getDominantColor(resource))
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })
        }else{
            binding.ivCover.visibility = View.GONE
        }

        binding.ivPengguna.visibility = View.VISIBLE
        Glide.with(this)
            .asBitmap()
            .load(Config.BASE_STORAGE_IMAGE + blog.avatar)
            .placeholder(R.drawable.ic_baseline_person_24)
            .error(R.drawable.ic_baseline_person_24)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    binding.ivPengguna.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })

        binding.tvKategori.text = blog.nama_kategori
        binding.tvJudul.text = blog.judul
        binding.tvNamaPengguna.text = blog.fullname
        binding.tvTanggal.text = helper.waktulalu(blog.created_at)
        binding.tvTotalKomentar.text = getString(R.string.teks_d_komentar, blog.total_command)
        helper.setTags(binding.tvBlog, blog.blog, this)
    }

    private fun observeDataKomentar() {

        /*get data on viewmodel*/
        komentarViewModel.getLoading().observe(this) {
            it.let {
                if (it) {
                    dialogKomentar.setCancelable(false)
                    bindingDialogKomentar.shimmer.visibility = View.VISIBLE
                    bindingDialogKomentar.divKosong.visibility = View.GONE
                    bindingDialogKomentar.shimmer.startShimmer()
                } else {
//                    bottomSheetDialog.setCancelable(true)
                    bindingDialogKomentar.shimmer.stopShimmer()
                    bindingDialogKomentar.shimmer.visibility = View.GONE
                }
            }
        }

        komentarViewModel.getResponse().observe(this, Observer {
            it.let {
                if(it != null){
                    if (it.datas.isNotEmpty()) {
                        bindingDialogKomentar.divKosong.visibility = View.GONE
                        dialogKomentar.setCancelable(true)
                        listKomentar.clear()
                        listKomentar.addAll(it.datas)

                        adapterKomentar.notifyDataSetChanged()
                    }else{

                        bindingDialogKomentar.divKosong.visibility = View.VISIBLE
                    }
                }


            }
        })

        komentarViewModel.getResponseDelete().observe(this){
            if (it != null){
                listKomentar.removeAt(it.position)
                adapterKomentar.notifyDataSetChanged()
            }
        }

        komentarViewModel.getError().observe(this, Observer {
            it.let {
                if (it != null) {
                    toast.show(it, this)

//                    bindingDialogKomentar.divKosong.visibility = View.VISIBLE
                }

            }
        })
        /*end of get data viewmodel*/

    }

    private fun observeDataBlog() {

        /*get data on viewmodel*/
        blogViewModel.getLoading().observe(this, Observer {
            it.let {
                if (it) {
                    SweetAlert.onLoading(this)
                } else {
                    SweetAlert.dismis()
                }
            }
        })

        blogViewModel.getResponse().observe(this, Observer {
            it.let {
                if(it != null){
                    if (it.code == 1){
                        SweetAlert.dismis()
                        SweetAlert.success(this, getString(R.string.teks_blog), getString(R.string.teks_success_delete_blog), finish = true)
                    }else{
                        SweetAlert.onFailure(this, it.message)
                    }
                }


            }
        })


        blogViewModel.getError().observe(this, Observer {
            it.let {
                if (it != null) {
                    SweetAlert.dismis()
                    SweetAlert.onFailure(this, it)
                }

            }
        })
        /*end of get data viewmodel*/

    }

    private fun observeDataDetailBlog() {

        /*get data on viewmodel*/
        detailBlogViewModel.getLoading().observe(this, Observer {
            it.let {
                if (it) {
                    SweetAlert.onLoading(this)
                } else {
                    SweetAlert.dismis()
                }
            }
        })

        detailBlogViewModel.getResponse().observe(this, Observer {
            it.let {
                if(it != null){
                    if (it.code == 1){
                        blog = it.blog
                        setDataBlog()
                    }else{
                        SweetAlert.onFailure(this, it.message)
                    }
                }


            }
        })


        detailBlogViewModel.getError().observe(this, Observer {
            it.let {
                if (it != null) {
                    SweetAlert.dismis()
                    SweetAlert.onFailure(this, it)
                }

            }
        })
        /*end of get data viewmodel*/

    }

}