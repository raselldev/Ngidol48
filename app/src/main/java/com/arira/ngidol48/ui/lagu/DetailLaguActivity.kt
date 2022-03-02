package com.arira.ngidol48.ui.lagu

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.SongSmallAdapter
import com.arira.ngidol48.app.App.Companion.pref
import com.arira.ngidol48.databinding.ActivityDetailLaguBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config.extra_list
import com.arira.ngidol48.helper.Config.extra_model
import com.arira.ngidol48.model.Song
import com.arira.ngidol48.utilities.MediaSessionService
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import io.reactivex.annotations.NonNull

class DetailLaguActivity : BaseActivity(), LaguCallback {
    private lateinit var binding:ActivityDetailLaguBinding
    private var lagu:Song = Song()
    private var listLagu:ArrayList<Song> = ArrayList()
    private var mYouTubePlayer:YouTubePlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_lagu)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_lagu)
        setToolbar(getString(R.string.teks_lagu), binding.toolbar)

        lagu = intent.getParcelableExtra(extra_model) ?: Song()
        listLagu = intent.getParcelableArrayListExtra(extra_list) ?: ArrayList()

        binding.embedYt.enableBackgroundPlayback(true)
        binding.embedYt.getPlayerUiController().showFullscreenButton(false)


        setDataLagu()

    }

    private fun setDataLagu(){

        binding.tvJudul.text = lagu.judul
        binding.tvLirik.text = lagu.lirik
        binding.tvSetlist.text = lagu.nama

        if (pref.getOnReview()){
            binding.embedYt.visibility = View.GONE
        }else{
            binding.embedYt.visibility = View.VISIBLE
            setDataVideo()
        }

        val filterSong:ArrayList<Song> = ArrayList()
        for (song in listLagu){
            if (song.id != this.lagu.id){
                filterSong.add(song)
            }
        }
        binding.rvLaguLain.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = SongSmallAdapter(filterSong, callback = this@DetailLaguActivity)
        }
    }

    override fun onSelectOtherSong(lagu: Song) {
        if (mYouTubePlayer != null){
            mYouTubePlayer?.pause()

            val videoId = if (lagu.song_link.contains("/")) {
                lagu.song_link.substring(lagu.song_link.lastIndexOf("/") + 1)
            } else {
                lagu.song_link
            }

            mYouTubePlayer?.cueVideo(videoId, 0f)
            mYouTubePlayer?.play()
        }
        this.lagu = lagu
        setDataLagu()
    }

    private fun setDataVideo(){
        var videoId = ""
        if (lagu.song_link.isNotEmpty()) {
            videoId = if (lagu.song_link.contains("/")) {
                lagu.song_link.substring(lagu.song_link.lastIndexOf("/") + 1)
            } else {
                lagu.song_link
            }

            binding.embedYt.visibility = View.VISIBLE

            binding.embedYt.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {

                override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                    mYouTubePlayer = youTubePlayer

                    youTubePlayer.cueVideo(videoId, 0f)
                    youTubePlayer.play()
                }

                override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                    //PLAYING , PAUSED, ENDED, BUFFERING

                    when(state.toString()){
                        "PLAYING"->{
                            pref.setSong(lagu)
                            ContextCompat.startForegroundService(
                                applicationContext,
                                Intent(applicationContext, MediaSessionService::class.java)
                            )
                        }
                        else->{
                            stopService(Intent(applicationContext, MediaSessionService::class.java))
                        }
                    }
                }
            })

            binding.embedYt.getPlayerUiController().setFullScreenButtonClickListener {
                if (binding.embedYt.isFullScreen()) {
                    binding.embedYt.exitFullScreen()
                } else {
                    binding.embedYt.enterFullScreen()
                }
            }
        } else {
            binding.embedYt.visibility = View.GONE
        }
    }
}