package com.arira.ngidol48.ui.activity.handshake

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.HandshakeAdapter
import com.arira.ngidol48.databinding.ActivityHandshakeBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.utilities.Go

class HandshakeActivity : BaseActivity() {
    private lateinit var binding: ActivityHandshakeBinding
    private lateinit var viewModel: HandshakeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_handshake)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_handshake)
        setToolbar(getString(R.string.teks_handshake_vc), binding.toolbar)

        /*menambakan warna untuk swipe refresh*/
        binding.swipe.setColorSchemeResources(R.color.colorPrimaryTeks,
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[HandshakeViewModel::class.java]
        viewModel.context = this

        observerData()

        viewModel.hitAll()

        action()
    }

    fun action(){
        binding.tvRiwayat.setOnClickListener {
            Go(this).move(HistoryHandshakeActivity::class.java)
        }

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            viewModel.hitAll()
        }

        binding.tvReload.setOnClickListener {
            viewModel.hitAll()
        }
    }

    fun observerData(){
        viewModel.getLoading().observe(this, Observer {
            it.let {
                if (it){
                    binding.divKosong.visibility = View.GONE
                    binding.shimmer.visibility = View.VISIBLE
                    binding.shimmer.startShimmer()
                }else{
                    binding.shimmer.visibility = View.GONE
                    binding.shimmer.stopShimmer()
                }
            }
        })

        viewModel.getError().observe(this, Observer {
            it.let {
                if (it != null){
                    binding.tvRiwayat.visibility = View.GONE
                    binding.tvReload.visibility = View.VISIBLE

                    binding.divKosong.visibility = View.VISIBLE

                }
            }
        })

        viewModel.getResponse().observe(this, Observer {
            it.let {
                if (it != null) {


                    if (it.handshakes.isNotEmpty()){
                        binding.rvData.apply {
                            layoutManager  = LinearLayoutManager(context)
                            adapter = HandshakeAdapter(it.handshakes)
                        }
                    }else{
                        binding.divKosong.visibility = View.VISIBLE

                        binding.tvRiwayat.visibility = View.VISIBLE
                        binding.tvReload.visibility = View.GONE
                    }
                }
            }
        })
    }
}