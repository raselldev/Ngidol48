package com.arira.ngidol48.ui.activity.cariLagu

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.PagerAdapter
import com.arira.ngidol48.databinding.ActivityCariLaguBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.network.response.SongResponse
import com.arira.ngidol48.ui.fragment.LaguFragment
import com.arira.ngidol48.ui.fragment.SetlistFragment
import com.arira.ngidol48.ui.activity.lagu.LaguActivity
import com.arira.ngidol48.utilities.Go
import com.google.android.material.tabs.TabLayoutMediator

class CariLaguActivity : BaseActivity(), CariLaguCallback {
    private lateinit var binding: ActivityCariLaguBinding
    private lateinit var viewModel: CariLaguViewModel
    private var querySeach:String = ""

    override fun callReload() {
        viewModel.cari(querySeach)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cari_lagu)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cari_lagu)
        setToolbar(getString(R.string.teks_lagu), binding.toolbar)
        binding.toolbar.ivMenu.visibility = View.VISIBLE
        binding.toolbar.ivMenu.setImageResource(R.drawable.ic_fav)

        setupPager(null)


        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[CariLaguViewModel::class.java]
        viewModel.context = this
        observerData()

        action()
    }

    private fun action(){
        binding.toolbar.ivMenu.setOnClickListener {
            Go(this).move(LaguActivity::class.java, choose = true)
        }

        binding.svPencarian.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                querySeach = query
                viewModel.cari(query)
                return false
            }

        })
    }

    private fun setupPager(data: SongResponse?){
        val adapter = PagerAdapter(this)
        if (data != null){
            adapter.addFragment(LaguFragment(data.song_list, this), "Lagu (${data.song_list.size})")
            adapter.addFragment(SetlistFragment(data.setlist, this), "Lainnya (${data.setlist.size})")

        }else{
            adapter.addFragment(LaguFragment(ArrayList(), this), "Lagu")
            adapter.addFragment(SetlistFragment(ArrayList(), this), "Lainnya")

        }

        binding.pager.adapter = adapter
        binding.pager.currentItem = 0
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
    }

    fun observerData(){
        viewModel.getLoading().observe(this, Observer {
            it.let {
                if (it){
                    binding.shimmer.visibility = View.VISIBLE
                    binding.shimmer.startShimmer()

                    binding.tabLayout.visibility = View.GONE
                    binding.pager.visibility = View.GONE
                }else{
                    binding.shimmer.visibility = View.GONE
                    binding.shimmer.stopShimmer()

                    binding.tabLayout.visibility = View.VISIBLE
                    binding.pager.visibility = View.VISIBLE
                }
            }
        })

        viewModel.getError().observe(this, Observer {
            it.let {
                if (it != null){
                }
            }
        })

        viewModel.getResponse().observe(this, Observer {
            it.let {
                if (it != null) {
                    setupPager(it)
                }
            }
        })
    }
}