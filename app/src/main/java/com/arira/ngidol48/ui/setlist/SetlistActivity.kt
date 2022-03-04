package com.arira.ngidol48.ui.setlist

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.MemberAdapter
import com.arira.ngidol48.adapter.SetlistAdapter
import com.arira.ngidol48.databinding.ActivitySetlistBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.ui.cariLagu.CariLaguActivity
import com.arira.ngidol48.utilities.Go

class SetlistActivity : BaseActivity() {
    private lateinit var binding:ActivitySetlistBinding
    private lateinit var viewModel: SetlistViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setlist)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setlist)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[SetlistViewModel::class.java]
        viewModel.context = this

        /*menambakan warna untuk swipe refresh*/
        binding.swipe.setColorSchemeResources(R.color.colorPrimaryTeks,
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent)

        setToolbar(getString(R.string.teks_lagu), binding.toolbar)
        observerData()
        viewModel.hitSetlist()

        action()
    }

    fun action(){

        binding.linCari.setOnClickListener {
            Go(this).move(CariLaguActivity::class.java)
        }

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            viewModel.hitSetlist()
        }

        binding.tvReload.setOnClickListener {
            viewModel.hitSetlist()
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
                    if (it.setlist.isNotEmpty()){
                        binding.rvData.apply {
                            layoutManager  = LinearLayoutManager(context)
                            adapter = SetlistAdapter(it.setlist)
                        }
                    }else{
                        binding.divKosong.visibility = View.VISIBLE
                    }
                }
            }
        })
    }
}