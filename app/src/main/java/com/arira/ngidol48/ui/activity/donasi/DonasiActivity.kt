package com.arira.ngidol48.ui.activity.donasi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.DonasiAdapter
import com.arira.ngidol48.databinding.ActivityDonasiBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.SweetAlert

class DonasiActivity : BaseActivity() {
    private lateinit var binding: ActivityDonasiBinding
    private lateinit var viewModel: DonasiViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donasi)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DonasiViewModel::class.java]
        viewModel.context = this

        binding = DataBindingUtil.setContentView(this, R.layout.activity_donasi)
        setToolbar(getString(R.string.teks_donasi), binding.toolbar)


        action()
        observeData()
        viewModel.hitAll()
    }

    private fun action(){

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            viewModel.hitAll()
        }

        binding.btnDonasi.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://saweria.co/raflipakei")
            startActivity(openURL)
        }
    }

    private fun observeData(){
        viewModel.getLoading().observe(this) {
            if (it != null){
                if (it){
                    SweetAlert.onLoading(this)
                }else{
                    SweetAlert.dismis()
                }
            }
        }

        viewModel.getResponse().observe(this){
            if (it != null){
                binding.rvData.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = DonasiAdapter(it.donations)
                }
            }
        }
    }
}