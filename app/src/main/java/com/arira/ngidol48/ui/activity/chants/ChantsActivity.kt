package com.arira.ngidol48.ui.activity.chants

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.ChantAdapter
import com.arira.ngidol48.databinding.ActivityChantsBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.model.Chant

class ChantsActivity : BaseActivity() {
    private lateinit var viewModel: ChantViewModel
    private lateinit var binding: ActivityChantsBinding
    private lateinit var chantAdapter: ChantAdapter
    private var listChant:ArrayList<Chant> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chants)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chants)
        setToolbar(getString(R.string.teks_chant), binding.toolbar)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[ChantViewModel::class.java]
        viewModel.context = this

        chantAdapter = ChantAdapter(listChant)
        binding.rvData.apply {
            layoutManager  = LinearLayoutManager(context)
            adapter = chantAdapter
        }

        action()
        observerData()
        viewModel.hitAll()
    }

    fun action(){

        binding.svPencarian.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                chantAdapter.filter.filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })

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
                    binding.divKosong.visibility = View.VISIBLE

                }
            }
        })

        viewModel.getResponse().observe(this, Observer {
            it.let {
                if (it != null) {
                    if (it.chants.isNotEmpty()){
                        listChant.clear()
                        listChant.addAll(it.chants)

                        chantAdapter.notifyDataSetChanged()
                    }else{
                        binding.divKosong.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

}