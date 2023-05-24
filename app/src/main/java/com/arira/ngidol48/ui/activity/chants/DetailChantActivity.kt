package com.arira.ngidol48.ui.activity.chants

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.arira.ngidol48.R
import com.arira.ngidol48.app.App.Companion.helper
import com.arira.ngidol48.databinding.ActivityDetailChantBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config
import com.arira.ngidol48.helper.Config.extra_model
import com.arira.ngidol48.helper.Helper
import com.arira.ngidol48.model.Chant
import com.arira.ngidol48.model.Song
import com.arira.ngidol48.ui.activity.detailLagu.DetailLaguActivity
import com.arira.ngidol48.ui.activity.detailLagu.SongDetailViewModel
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget

class DetailChantActivity : BaseActivity() {
    private lateinit var binding:ActivityDetailChantBinding
    private var chant = Chant()
    private lateinit var songDetailViewModel: SongDetailViewModel
    private var song = Song()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_chant)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_chant)
        setNewToolbar(getString(R.string.teks_chant), binding.toolbar)

        chant = intent.getParcelableExtra(extra_model) ?: Chant()
        setChantData()
        
        songDetailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[SongDetailViewModel::class.java]
        songDetailViewModel.context = this
        
        observeDataDetail()
        
        if(chant.related_song_id != "0"){
            songDetailViewModel.detail(chant.related_song_id)
        }

        action()
    }

    private fun action(){
        binding.tvJudul.setOnClickListener {
            Go(this).move(DetailLaguActivity::class.java, data = song)
        }
    }
    
    private fun setChantData(){
        binding.tvChant.text = helper.fromHtml(chant.chant)
        binding.tvTitle.text = chant.title
    }

    private fun observeDataDetail(){
        songDetailViewModel.getResponse().observe(this){
            if (it != null){
                binding.cardSong.visibility = View.VISIBLE
                val data = it.song
                song = data

                binding.tvJudul.text = data.judul
                if (data.lirik.isEmpty()){
                    binding.tvLirikTersedia.text = getString(R.string.teks_tidak_tersedia)
                }else{
                    binding.tvLirikTersedia.text = getString(R.string.teks_tersedia)
                }

                if (data.cover.isEmpty()){
                    Glide.with(this).load(R.drawable.img_jkt48).into(binding.ivCover)
                }
                else{

                    Glide.with(this)
                        .asBitmap()
                        .load(Config.BASE_STORAGE + data.cover)
                        .into(object : CustomTarget<Bitmap>(){
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                            ) {
                                binding.ivCover.setImageBitmap(resource)
                                binding.ivCover.setBackgroundColor(Helper.getDominantColor(resource))
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {

                            }
                        })
                }

            }
        }
    }
    
}