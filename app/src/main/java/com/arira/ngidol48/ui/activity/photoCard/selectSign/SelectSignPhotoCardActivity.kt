package com.arira.ngidol48.ui.activity.photoCard.selectSign

import android.os.Bundle
import com.arira.ngidol48.databinding.ActivitySelectSignPhotoCardBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config.extra_list
import com.arira.ngidol48.helper.Config.extra_model
import com.arira.ngidol48.model.PhotoCardImage
import com.arira.ngidol48.model.PhotoCardSign
import com.arira.ngidol48.ui.activity.photoCard.writeGreating.WriteGreatingPhotoCardActivity
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class SelectSignPhotoCardActivity : BaseActivity() {
    private lateinit var binding: ActivitySelectSignPhotoCardBinding
    var listSign:List<PhotoCardSign> = ArrayList()
    var photoCardImage = PhotoCardImage()
    var selectedSign:PhotoCardSign = PhotoCardSign()
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
        binding.ivSign1.setOnClickListener {
            selectedSign = listSign[0]
            updateSign()
        }

        binding.ivSign2.setOnClickListener {
            selectedSign = listSign[1]
            updateSign()
        }

        binding.ivSign3.setOnClickListener {
            selectedSign = listSign[2]
            updateSign()
        }

        binding.btnNext.setOnClickListener {
            Go(this).move(WriteGreatingPhotoCardActivity::class.java, data = photoCardImage, other = selectedSign.image)
        }
    }

    fun setData(){
        Glide.with(this).load(photoCardImage.image)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(
                binding.ivPhotoCard
            )
        if (listSign.isNotEmpty()){
            action()

            Glide.with(this).load(listSign[0].image)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(
                    binding.ivSign1
                )

            Glide.with(this).load(listSign[1].image)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(
                    binding.ivSign2
                )

            Glide.with(this).load(listSign[2].image)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(
                    binding.ivSign3
                )
        }

    }
}