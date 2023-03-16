package com.arira.ngidol48.ui.activity.photoCard

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.arira.ngidol48.databinding.ActivityWriteNickNameBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config
import com.arira.ngidol48.model.CreatePhotoCard
import com.arira.ngidol48.ui.activity.photoCard.export.ExportPhotoCardActivity
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class WriteNickNameActivity : BaseActivity() {
    private var createPhotoCard = CreatePhotoCard()
    private var signImage:String = ""
    private var userName:String = ""
    private lateinit var binding: ActivityWriteNickNameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteNickNameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar("Create Photo Card", binding.toolbar)

        createPhotoCard = intent.getParcelableExtra(Config.extra_model) ?: CreatePhotoCard()
        signImage = createPhotoCard.sign.image

        binding.ivCardPhotocard.rotation = -5.0f

        binding.btnNext.setOnClickListener {
            createPhotoCard.nickName = userName
            Go(this).move(ExportPhotoCardActivity::class.java, data = createPhotoCard)
        }

        setData()

        binding.edtGreating.doOnTextChanged { text, start, before, count ->
            binding.tvName.text = text
            userName = text.toString()
        }
    }

    fun setData(){
        binding.tvGreating.text = createPhotoCard.greating
        Glide.with(this).load(createPhotoCard.photoCard.image)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(
                binding.ivPhotoCard
            )

        Glide.with(this).load(signImage)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(
                binding.ivSelectedSign
            )

        updatePositionSign(binding.ivSelectedSign, createPhotoCard.signPosition)
    }

}