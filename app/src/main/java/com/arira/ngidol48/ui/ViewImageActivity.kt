package com.arira.ngidol48.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.arira.ngidol48.R
import com.arira.ngidol48.databinding.ActivityViewImageBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config.extra_url
import com.bumptech.glide.Glide

class ViewImageActivity : BaseActivity() {
    private var url = ""
    private lateinit var binding: ActivityViewImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_image)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_image)
        url = intent.getStringExtra(extra_url) ?: ""

        statusPutih()
        binding.ivBack.setOnClickListener {
            finish()
        }
        Glide.with(this).load(url).into(binding.ivData)
    }
}