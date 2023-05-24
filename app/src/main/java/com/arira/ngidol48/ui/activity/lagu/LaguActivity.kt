package com.arira.ngidol48.ui.activity.lagu

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.SongAdapter
import com.arira.ngidol48.databinding.ActivityLaguBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config.extra_boolean
import com.arira.ngidol48.helper.Config.extra_model
import com.arira.ngidol48.model.Setlist
import com.arira.ngidol48.model.Song
import com.arira.ngidol48.network.response.SongResponse

class LaguActivity : BaseActivity() {

    private lateinit var viewModel: SongListViewModel
    private lateinit var binding: ActivityLaguBinding
    private var setlist:Setlist = Setlist()
    private var isFav:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lagu)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lagu)

        isFav = intent.getBooleanExtra(extra_boolean, false)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[SongListViewModel::class.java]
        viewModel.context = this

        /*menambakan warna untuk swipe refresh*/
        binding.swipe.setColorSchemeResources(R.color.colorPrimaryTeks,
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent)

        setlist = intent.getParcelableExtra(extra_model) ?: Setlist()


        if (isFav){
            setNewToolbar(getString(R.string.teks_lagu_favorit_anda), binding.toolbar)
        }else{
            setNewToolbar(setlist.nama, binding.toolbar)
        }



        observerData()

        if (isFav){
            viewModel.hitFavSong()
        }else{
            viewModel.hitSong(setlist.setlist_id)
        }


        action()
    }

    fun action(){

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            if (isFav){
                viewModel.hitFavSong()
            }else{
                viewModel.hitSong(setlist.setlist_id)
            }
        }

        binding.tvReload.setOnClickListener {
            if (isFav){
                viewModel.hitFavSong()
            }else{
                viewModel.hitSong(setlist.setlist_id)
            }
        }
    }


    fun showLoading() {
        binding.divKosong.visibility = View.GONE
        binding.shimmer.visibility = View.VISIBLE
        binding.shimmer.startShimmer()
    }

    private fun removeLoading() {
        binding.shimmer.visibility = View.GONE
        binding.shimmer.stopShimmer()
    }
    fun observerData(){
        viewModel.getLoading().observe(this) {
            it?.let {
                if (it) {
                    showLoading()
                } else {
                    removeLoading()
                }
            }
        }

        viewModel.getError().observe(this) {
            it?.let {
                binding.divKosong.visibility = View.VISIBLE
            }
        }

        viewModel.getResponse().observe(this) {
            it.let {
                setData(it)
            }
        }
    }

    private fun setData(it: SongResponse?) {
        if (it != null) {
            if (it.song_list.isNotEmpty()) {
                binding.rvData.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = SongAdapter(
                        it.song_list,
                        laguSetlist = it.song_list as ArrayList<Song>
                    )
                }
            } else {
                binding.divKosong.visibility = View.VISIBLE
            }
        }
    }

}