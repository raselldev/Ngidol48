package com.arira.ngidol48.ui.activity.photoCard.listPhoto

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.SmallPhotoCardAdapter
import com.arira.ngidol48.app.App.Companion.pref
import com.arira.ngidol48.databinding.ActivityListPhotoBinding
import com.arira.ngidol48.databinding.SheetDetailPhotocardBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config.extra_model
import com.arira.ngidol48.helper.SweetAlert
import com.arira.ngidol48.helper.Validasi
import com.arira.ngidol48.model.PhotoCardImage
import com.arira.ngidol48.model.PhotoCardSign
import com.arira.ngidol48.model.PhotocardSession
import com.arira.ngidol48.network.response.PhotoCardResponse
import com.arira.ngidol48.ui.activity.photoCard.selectSign.SelectSignPhotoCardActivity
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetDialog

class ListPhotoActivity : BaseActivity(), ListPhotoCallback {

    private lateinit var viewModel: ListPhotoCardViewModel
    private lateinit var binding: ActivityListPhotoBinding
    private var session = PhotocardSession()
    private var listPhoto:ArrayList<PhotoCardImage> = ArrayList()
    private var listSign:List<PhotoCardSign> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar("Photo Card", binding.toolbar)

        Validasi().ijinDokumen(this)

        session = intent.getParcelableExtra(extra_model) ?: PhotocardSession()

        pref.setString("tag_pc", session.tag)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[ListPhotoCardViewModel::class.java]
        viewModel.context = this

        binding.tvDeskripsi.text = session.session_description

        observerData()
        viewModel.getDetail(session.id)
    }

    fun updateUI(response: PhotoCardResponse){
        listPhoto.clear()
        listPhoto.addAll(response.images)

        listSign = response.signs

        //get main image
        val mainPhoto = if (response.images.isNotEmpty()){
            response.images[0]
        }else{
            null
        }
        if (mainPhoto != null){
            listPhoto.removeFirst()

            Glide.with(this).load(mainPhoto.image)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(
                    binding.ivMain
                )

            binding.ivMain.setOnClickListener {
                Go(this).move(SelectSignPhotoCardActivity::class.java, data = mainPhoto,listData = listSign)
            }
        }

        //get left list
        if (listPhoto.size >= 6){
            val listPhotoForLeft:ArrayList<PhotoCardImage> = ArrayList()
            for (i in 0 until  6){
                listPhotoForLeft.add(listPhoto[i])
                listPhoto.removeAt(i)
            }

            binding.rvLeft.apply {
                layoutManager = GridLayoutManager(context, 2)
                adapter = SmallPhotoCardAdapter(listPhotoForLeft, this@ListPhotoActivity)
            }
        }

        //set other photo
        binding.rvBottom.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = SmallPhotoCardAdapter(listPhoto, this@ListPhotoActivity)
        }
    }

    fun observerData() {
        viewModel.getLoading().observe(this) {
            it.let {
                if (it) {
                    SweetAlert.onLoading(this)
                } else {
                    SweetAlert.dismis()
                }
            }
        }

        viewModel.getError().observe(this) {
            it.let {
                if (it != null) {
                    SweetAlert.dismis()
                    SweetAlert.onFailure(this, it)
                }
            }
        }

        viewModel.getResponse().observe(this) {
            it.let {
                if (it != null) {
                    updateUI(it)
                }
            }
        }
    }

    override fun onLongClick(data: PhotoCardImage) {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val bindingSheet:SheetDetailPhotocardBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.sheet_detail_photocard,
            null,
            false
        )
        Glide.with(this).load(data.image)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(
                bindingSheet.ivPhotoCard
            )

        bindingSheet.tvTitle.text = data.name


        bottomSheetDialog.setContentView(bindingSheet.root)


        bottomSheetDialog.show()
    }

    override fun onClick(data: PhotoCardImage) {
        if (pref.getTotalCreatePC() < session.max.toInt()) {
            Go(this).move(SelectSignPhotoCardActivity::class.java, data = data,listData = listSign)
        }else{
            SweetAlert.onFailure(this, "Anda sudah mencapai batas maksimal untuk membuat digital photo card")
        }

    }

}