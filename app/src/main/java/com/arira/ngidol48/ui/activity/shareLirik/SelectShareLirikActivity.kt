package com.arira.ngidol48.ui.activity.shareLirik

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.arira.ngidol48.adapter.ShareLirikAdapter
import com.arira.ngidol48.databinding.ActivitySelectShareLirikBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config
import com.arira.ngidol48.helper.Config.extra_model
import com.arira.ngidol48.helper.Helper
import com.arira.ngidol48.helper.Validasi
import com.arira.ngidol48.model.Song
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget

class SelectShareLirikActivity : BaseActivity(), ShareLirikCallback {

    private var currentSong: Song = Song()
    private var listLyrics:ArrayList<String> = ArrayList()
    private lateinit var adapter: ShareLirikAdapter
    private lateinit var binding: ActivitySelectShareLirikBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectShareLirikBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar("Bagikan Lirik", binding.toolbar)
        currentSong = intent.getParcelableExtra(extra_model) ?: Song()

        Validasi().ijinDokumen(this)

        Glide.with(this)
            .asBitmap()
            .load(Config.BASE_STORAGE + currentSong.cover)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(24)))
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    binding.ivThumb.setImageBitmap(resource)
                    binding.ivThumb.setBackgroundColor(Helper.getDominantColor(resource))
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
        binding.tvTitle.text = currentSong.judul
        binding.tvSubtitle.text = currentSong.nama

        if (currentSong.lirik.isNotEmpty()){
            listLyrics.addAll( currentSong.lirik.split("\r\n"))
        }

        adapter = ShareLirikAdapter(listLyrics, this)
        binding.rvRilik.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@SelectShareLirikActivity.adapter
        }
        action()
    }


    fun action(){
        binding.btnShare.setOnClickListener {
            if (adapter.selectedLyricsPosition.isEmpty()){
                toast.show("Pilih lirik yang anda banget", this)
            }else{
                var selectedLirikString = ""
                adapter.selectedLyricsPosition.forEach {
                    selectedLirikString += listLyrics[it] + "\r\n"
                }

                Go(this).move(ShareLirikActivity::class.java, data = currentSong, other = selectedLirikString)
            }
        }
    }

    override fun onLirikSelected(position: Int, lirik: String) {
        if (adapter.selectedLyricsPosition.size < 6){
//            var allowAdd = true
//            if (adapter.selectedLyricsPosition.isNotEmpty()){
//                val lastPosition = adapter.selectedLyricsPosition.last()
//                allowAdd = if ((lastPosition - position) > 1){
//                    false
//                }else (lastPosition + position) <= 1
//            }
//
//            if (allowAdd) {
                adapter.selectedLyricsPosition.add(position)
                adapter.notifyDataSetChanged()
//            }



        }else{
            toast.show("Maksimal pilih 6 lirik", this)
        }

    }

    override fun onLirikRemoved(position: Int, lirik: String) {
        adapter.selectedLyricsPosition.remove(position)
        adapter.notifyDataSetChanged()
    }
}