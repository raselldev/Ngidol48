package com.arira.ngidol48.ui.lagu

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.SongAdapter
import com.arira.ngidol48.databinding.ActivityLaguBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config.extra_model
import com.arira.ngidol48.model.Setlist
import com.arira.ngidol48.model.Song

class LaguActivity : BaseActivity() {

    private lateinit var viewModel: SongListViewModel
    private lateinit var binding: ActivityLaguBinding
    private var setlist:Setlist = Setlist()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lagu)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lagu)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[SongListViewModel::class.java]
        viewModel.context = this

        /*menambakan warna untuk swipe refresh*/
        binding.swipe.setColorSchemeResources(R.color.colorPrimaryTeks,
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent)

        setlist = intent.getParcelableExtra(extra_model) ?: Setlist()

        setToolbar(setlist.nama, binding.toolbar)
        observerData()

        viewModel.hitSong(setlist.setlist_id)

        action()
    }

    fun action(){
        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            viewModel.hitSong(setlist.setlist_id)
        }

        binding.tvReload.setOnClickListener {
            viewModel.hitSong(setlist.setlist_id)
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
                    if (it.song_list.isNotEmpty()){
                        binding.rvData.apply {
                            layoutManager  = LinearLayoutManager(context)
                            adapter = SongAdapter(it.song_list, laguSetlist = it.song_list as ArrayList<Song>)
                        }
                    }else{
                        binding.divKosong.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

}