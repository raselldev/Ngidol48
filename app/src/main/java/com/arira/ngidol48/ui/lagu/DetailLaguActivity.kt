package com.arira.ngidol48.ui.lagu

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.arira.ngidol48.R
import com.arira.ngidol48.databinding.ActivityDetailLaguBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config.extra_model
import com.arira.ngidol48.model.Song
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import io.reactivex.annotations.NonNull

class DetailLaguActivity : BaseActivity() {
    private lateinit var binding:ActivityDetailLaguBinding
    private var lagu:Song = Song()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_lagu)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_lagu)
        setToolbar(getString(R.string.teks_lagu), binding.toolbar)

        lagu = intent.getParcelableExtra(extra_model) ?: Song()

        binding.tvJudul.text = lagu.judul
        binding.tvLirik.text = lagu.lirik
        binding.tvSetlist.text = lagu.nama

        setDataVideo()
    }

    private fun setDataVideo(){
        var videoId:String = ""
        if (lagu.song_link.isNotEmpty()) {
            videoId = if (lagu.song_link.contains("/")) {
                lagu.song_link.substring(lagu.song_link.lastIndexOf("/") + 1)
            } else {
                lagu.song_link
            }

            binding.embedYt.visibility = View.VISIBLE
            binding.embedYt.enableBackgroundPlayback(true)

            binding.embedYt.getPlayerUiController().showFullscreenButton(false)
            binding.embedYt.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(videoId, 0f)
                    youTubePlayer.play()
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