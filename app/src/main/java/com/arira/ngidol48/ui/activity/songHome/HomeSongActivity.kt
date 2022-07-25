package com.arira.ngidol48.ui.activity.songHome

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.KategoriLaguAdapter
import com.arira.ngidol48.adapter.SetlistAdapter
import com.arira.ngidol48.databinding.ActivityHomeSongBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config.BASE_STORAGE
import com.arira.ngidol48.helper.SweetAlert
import com.arira.ngidol48.model.Setlist
import com.arira.ngidol48.model.Song
import com.arira.ngidol48.ui.activity.cariLagu.CariLaguActivity
import com.arira.ngidol48.ui.activity.detailLagu.DetailLaguActivity
import com.arira.ngidol48.ui.activity.lagu.LaguActivity
import com.arira.ngidol48.ui.activity.lagu.LaguCallback
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide

class HomeSongActivity : BaseActivity(), LaguCallback{

    private lateinit var binding: ActivityHomeSongBinding
    private lateinit var viewModel: HomeLaguViewModel
    private var bannerMusic:Setlist = Setlist()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_song)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_song)
        setToolbar(getString(R.string.teks_lagu), binding.toolbar)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[HomeLaguViewModel::class.java]
        viewModel.context = this

        observeData()

        viewModel.hit()

        action()
    }

    private fun action(){

        binding.linCari.setOnClickListener {
            Go(this).move(CariLaguActivity::class.java)
        }

        binding.ivBanner.setOnClickListener {
            Go(this).move(LaguActivity::class.java, data = bannerMusic)
        }

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false

            viewModel.hit()
        }
    }

    private fun observeData(){
        viewModel.getLoading().observe(this){
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
                //set banner
                bannerMusic = it.banner_music
                Glide.with(this).load(BASE_STORAGE + it.banner_music.cover).into(binding.ivBanner)
                //set kategori
                binding.rvKategori.apply {
                    layoutManager  = LinearLayoutManager(context)
                    adapter = KategoriLaguAdapter(it.song_by_kategori, this@HomeSongActivity)
                }

                binding.rvSetlist.apply {
                    layoutManager  = LinearLayoutManager(context)
                    adapter = SetlistAdapter(it.setlist_teater)
                }
            }
        }

        viewModel.getError().observe(this){
            if (it != null){
                toast.show(it, this)
            }
        }
    }

    override fun onSelectSong(lagu: Song, setlist: List<Song>) {
        Go(this).move(DetailLaguActivity::class.java, data = lagu, listData = setlist)
    }
}