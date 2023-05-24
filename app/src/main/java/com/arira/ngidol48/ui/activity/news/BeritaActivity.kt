package com.arira.ngidol48.ui.activity.news

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.BeritaAdapter
import com.arira.ngidol48.databinding.ActivityBeritaBinding
import com.arira.ngidol48.helper.BaseActivity

class BeritaActivity : BaseActivity() {

    private lateinit var binding: ActivityBeritaBinding
    private lateinit var viewModel: BeritaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_berita)
        viewModel =  ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[BeritaViewModel::class.java]
        viewModel.context = this

        binding = DataBindingUtil.setContentView(this, R.layout.activity_berita)
        setNewToolbar(getString(R.string.teks_informasi), binding.toolbar)

        /*menambakan warna untuk swipe refresh*/
        binding.swipe.setColorSchemeResources(R.color.colorPrimaryTeks,
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent)

        observerData()
        viewModel.hitAll()

        action()
    }

    fun action(){

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
                    if (it.news.isNotEmpty()){
                        binding.rvData.apply {
                            layoutManager  = LinearLayoutManager(context)
                            adapter = BeritaAdapter(it.news)
                        }
                    }else{
                        binding.divKosong.visibility = View.VISIBLE
                    }
                }
            }
        })
    }
}