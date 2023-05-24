package com.arira.ngidol48.ui.activity.addBlog

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.BlogKategoriAdapter
import com.arira.ngidol48.databinding.ActivityTambahBlogBinding
import com.arira.ngidol48.databinding.SheetBlogKategoriBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.SweetAlert
import com.arira.ngidol48.model.BlogKategori
import com.arira.ngidol48.ui.activity.allBlog.BlogKategoriViewModel
import com.arira.ngidol48.utilities.Picker
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import net.alhazmy13.mediapicker.Image.ImagePicker
import java.io.File

class TambahBlogActivity : BaseActivity(), BlogKategoriCallback{
    private lateinit var binding:ActivityTambahBlogBinding
    private lateinit var viewModel: BlogViewModel
    private var textDescription:String = ""

    private var coverFile: File? = null

    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bindingSheetKategori: SheetBlogKategoriBinding

    private var listKategori:ArrayList<BlogKategori> = ArrayList()
    private lateinit var adapterKategori: BlogKategoriAdapter
    private lateinit var kategoriViewModel: BlogKategoriViewModel
    private var kategoriSelected:BlogKategori = BlogKategori()

    override fun onKategoriSelected(data: BlogKategori) {
        kategoriSelected = data
        binding.tvKategori.text = data.nama
        bottomSheetDialog.dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_blog)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tambah_blog)
        setNewToolbar(getString(R.string.teks_tulis_blog), binding.toolbar)

        adapterKategori = BlogKategoriAdapter(listKategori, this)

        kategoriViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[BlogKategoriViewModel::class.java]
        kategoriViewModel.context = this

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[BlogViewModel::class.java]
        viewModel.context = this

        statusPutih()


        action()
        observeBlog()
        observeDataKategori()
    }

    fun action(){
        binding.tvKategori.setOnClickListener {
            showSheetKategori()
        }
        binding.edtIsi.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null){
                    val wordCount =   s.split(" ").toTypedArray().size
                    binding.tBagianTvJmlKata.text = "${wordCount} Kata"
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.ivDeleteCover.setOnClickListener {
            binding.tBagianIvCover.setImageResource(R.drawable.img_upload_new)
            coverFile = null
            binding.ivDeleteCover.visibility = View.GONE
        }

        binding.tBagianIvCover.setOnClickListener {
            Picker(this).ambilGambarSemua(type = "blog")
        }

        binding.btnPublish.setOnClickListener {
            val judul = binding.edtJudul.text.toString()
            textDescription = binding.edtIsi.text.toString()

            if (judul.isEmpty()){
                toast.show("Judul tidak boleh kosong", this)
            }else if (textDescription.isEmpty()){
                toast.show("konten blog tidak boleh kosong", this)
            }else if (kategoriSelected.id.isEmpty()){
                toast.show("pilih salah satu kategori", this)
            }
            else{
                if (coverFile != null){
                    viewModel.add(coverFile!!, judul, textDescription, kategoriSelected.id)
                }else{
                    viewModel.add(judul, textDescription, kategoriSelected.id)
                }
            }

        }
    }

    private fun showSheetKategori() {

        bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)

        bindingSheetKategori = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.sheet_blog_kategori,
            null,
            false
        )

        bottomSheetDialog.setContentView(bindingSheetKategori.root)


        if (listKategori.isEmpty()) {
            kategoriViewModel.hitAll()
            bottomSheetDialog.setCancelable(false)

        } else {
            bottomSheetDialog.setCancelable(true)

            bindingSheetKategori.divData.visibility = View.VISIBLE
            bindingSheetKategori.divLoading.visibility = View.GONE
        }

        bindingSheetKategori.rvKategori.apply {
            adapter = adapterKategori
            layoutManager = LinearLayoutManager(context)
        }

        bindingSheetKategori.svKota.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                adapterKategori.filter.filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                adapterKategori.filter.filter(query)
                return false
            }

        })

        bottomSheetDialog.setOnCancelListener {
            adapterKategori.filter.filter("")
        }

        bottomSheetDialog.show()

    }

    private fun observeDataKategori() {

        /*get data on viewmodel*/
        kategoriViewModel.getLoading().observe(this, Observer {
            it.let {
                if (it) {
                    bindingSheetKategori.divData.visibility = View.GONE
                    bindingSheetKategori.divLoading.visibility = View.VISIBLE
                } else {
                    bindingSheetKategori.divData.visibility = View.VISIBLE
                    bindingSheetKategori.divLoading.visibility = View.GONE
                }
            }
        })

        kategoriViewModel.getResponse().observe(this, Observer {
            it.let {
                if(it != null){
                    if (it.data.isNotEmpty()) {
                        bottomSheetDialog.setCancelable(true)
                        listKategori.clear()
                        listKategori.addAll(it.data)

                        adapterKategori.notifyDataSetChanged()
                    }
                }


            }
        })

        kategoriViewModel.getError().observe(this, Observer {
            it.let {
                if (it != null) {
                    toast.show(it, this)
                }

            }
        })
        /*end of get data viewmodel*/

    }

    fun observeBlog(){
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

        viewModel.getResponse().observe(this){
            it.let {
                if (it != null){
                    SweetAlert.dismis()
                    if (it.code == 1){
                        SweetAlert.success(this, getString(R.string.teks_blog), getString(R.string.teks_blog_tersimpan), finish = true)
                    }else{
                        SweetAlert.onFailure(this, it.message)
                    }
                }
            }
        }

        viewModel.getError().observe(this){
            it.let {
                if (it != null){
                    SweetAlert.onFailure(this, it)
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            val mpath: java.util.ArrayList<String> = data!!.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH)!!
            coverFile = File(mpath[0])

            Glide.with(this)
                .load(coverFile)
                .placeholder(R.drawable.img_upload_new)
                .error(R.drawable.img_upload_new)
                .into(binding.tBagianIvCover)

            binding.ivDeleteCover.visibility = View.VISIBLE

        }
    }
}