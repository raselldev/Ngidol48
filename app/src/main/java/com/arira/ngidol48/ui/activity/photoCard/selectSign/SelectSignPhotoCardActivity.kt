package com.arira.ngidol48.ui.activity.photoCard.selectSign

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.SignAdapter
import com.arira.ngidol48.databinding.ActivitySelectSignPhotoCardBinding
import com.arira.ngidol48.databinding.SheetSignPositionBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config.extra_list
import com.arira.ngidol48.helper.Config.extra_model
import com.arira.ngidol48.model.CreatePhotoCard
import com.arira.ngidol48.model.PhotoCardImage
import com.arira.ngidol48.model.PhotoCardSign
import com.arira.ngidol48.ui.activity.photoCard.writeGreating.WriteGreatingPhotoCardActivity
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetDialog

class SelectSignPhotoCardActivity : BaseActivity() {
    private lateinit var binding: ActivitySelectSignPhotoCardBinding
    var listSign:List<PhotoCardSign> = ArrayList()
    var photoCardImage = PhotoCardImage()
    var selectedSign:PhotoCardSign = PhotoCardSign()
    var posisiSign:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectSignPhotoCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar("Create Photo Card", binding.toolbar)

        photoCardImage = intent.getParcelableExtra(extra_model) ?: PhotoCardImage()
        listSign = intent.getParcelableArrayListExtra(extra_list) ?: ArrayList()

        action()
        setData()
    }

    fun updateSign(){

        Glide.with(this).load(selectedSign.image)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(
                binding.ivSelectedSign
            )
    }

    fun action(){

        binding.tvGantiPosisiTandaTangan.setOnClickListener {
            sheetPosisiSign()
        }

        binding.btnNext.setOnClickListener {
            val createPhotoCard = CreatePhotoCard()
            createPhotoCard.sign = selectedSign
            createPhotoCard.photoCard = photoCardImage
            createPhotoCard.signPosition = posisiSign

            Go(this).move(WriteGreatingPhotoCardActivity::class.java, data = createPhotoCard)
        }
    }

    fun setData(){
        Glide.with(this).load(photoCardImage.image)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(
                binding.ivPhotoCard
            )
        if (listSign.isNotEmpty()){
            binding.rvSign.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                adapter = SignAdapter(listSign, callback = object: SignAdapter.Callback{
                    override fun onClick(sign: PhotoCardSign) {
                        selectedSign = sign
                        updateSign()
                        binding.tvGantiPosisiTandaTangan.visibility =View.VISIBLE
                    }

                })
            }
        }
    }

    fun sheetPosisiSign() {
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        dialog.dismissWithAnimation = true
        val bindingSheet: SheetSignPositionBinding = DataBindingUtil.inflate(layoutInflater, R.layout.sheet_sign_position, null, false)

        dialog.setContentView(bindingSheet.root)

        when(posisiSign){
            0-> {
                bindingSheet.tvKiri.setBackgroundResource(R.drawable.background_border_primary)
            }
            1-> {
                bindingSheet.tvTengah.setBackgroundResource(R.drawable.background_border_primary)
            }
            2-> {
                bindingSheet.tvKanan.setBackgroundResource(R.drawable.background_border_primary)
            }
        }

        bindingSheet.linKanan.setOnClickListener {
            posisiSign = 2
            updatePositionSign(binding.ivSelectedSign, posisiSign)
            dialog.dismiss()
        }

        bindingSheet.linTengah.setOnClickListener {
            posisiSign = 1
            updatePositionSign(binding.ivSelectedSign, posisiSign)
            dialog.dismiss()
        }

        bindingSheet.linKiri.setOnClickListener {
            posisiSign = 0
            updatePositionSign(binding.ivSelectedSign, posisiSign)
            dialog.dismiss()
        }

        dialog.show()
    }
}