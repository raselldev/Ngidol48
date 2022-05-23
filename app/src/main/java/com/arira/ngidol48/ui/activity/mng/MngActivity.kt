package com.arira.ngidol48.ui.activity.mng

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.MngAdapter
import com.arira.ngidol48.databinding.ActivityMngBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.utilities.Go

class MngActivity : BaseActivity() {
    private lateinit var binding: ActivityMngBinding
    private lateinit var viewModel: MngViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_mng)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mng)
        setToolbar(getString(R.string.teks_mng_vc), binding.toolbar)

        /*menambakan warna untuk swipe refresh*/
        binding.swipe.setColorSchemeResources(R.color.colorPrimaryTeks,
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MngViewModel::class.java]
        viewModel.context = this

        observerData()

        viewModel.hitAll()

        action()
    }

    fun action(){
        binding.tvRiwayat.setOnClickListener {
            Go(this).move(HistoryMngActivity::class.java)
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


                    if (it.mngs.isNotEmpty()){
                        binding.rvData.apply {
                            layoutManager  = LinearLayoutManager(context)
                            adapter = MngAdapter(it.mngs)
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