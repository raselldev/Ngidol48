package com.arira.ngidol48.ui.activity.mng

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.MngAdapter
import com.arira.ngidol48.databinding.ActivityHistoryMngBinding
import com.arira.ngidol48.helper.BaseActivity

class HistoryMngActivity : BaseActivity() {
    private lateinit var binding: ActivityHistoryMngBinding
    private lateinit var viewModel: MngViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_mng)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_history_mng)
        setNewToolbar(getString(R.string.teks_mng_vc), binding.toolbar)

        /*menambakan warna untuk swipe refresh*/
        binding.swipe.setColorSchemeResources(R.color.colorPrimaryTeks,
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MngViewModel::class.java]
        viewModel.context = this

        observerData()

        viewModel.hitHistory()

        action()
    }

    fun action(){

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            viewModel.hitHistory()
        }

        binding.tvReload.setOnClickListener {
            viewModel.hitHistory()
        }
    }

    fun observerData(){
        viewModel.getLoading().observe(this) {
            it.let {
                if (it) {
                    binding.divKosong.visibility = View.GONE
                    binding.shimmer.visibility = View.VISIBLE
                    binding.shimmer.startShimmer()
                } else {
                    binding.shimmer.visibility = View.GONE
                    binding.shimmer.stopShimmer()
                }
            }
        }

        viewModel.getError().observe(this) {
            it.let {
                if (it != null) {
                    binding.divKosong.visibility = View.VISIBLE

                }
            }
        }

        viewModel.getResponse().observe(this) {
            it.let {
                if (it != null) {
                    if (it.mngs.isNotEmpty()) {
                        binding.rvData.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = MngAdapter(it.mngs)
                        }
                    } else {
                        binding.divKosong.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}