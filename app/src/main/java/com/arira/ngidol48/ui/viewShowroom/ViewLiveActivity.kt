package com.arira.ngidol48.ui.viewShowroom

import android.app.PictureInPictureParams
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Rational
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import com.arira.ngidol48.R
import com.arira.ngidol48.app.App.Companion.helper
import com.arira.ngidol48.databinding.ActivityViewLiveBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config.extra_boolean
import com.arira.ngidol48.helper.Config.extra_model
import com.arira.ngidol48.helper.Config.extra_url
import com.arira.ngidol48.model.Member
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util


class ViewLiveActivity : BaseActivity() {

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private lateinit var binding: ActivityViewLiveBinding
    private var urlLive:String = ""
    private var member: Member = Member()
    var player: SimpleExoPlayer? = null
    private var isShowroom = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_live)
        hideSystemBars()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_live)
        statusPutih()

        isShowroom = intent.getBooleanExtra(extra_boolean, true)
        urlLive = intent.getStringExtra(extra_url) ?: ""
        member = intent.getParcelableExtra(extra_model) ?: Member()

        if (isShowroom){
            binding.ivShowroom.visibility = View.VISIBLE
            binding.ivIdn.visibility = View.GONE
        }else{
            binding.ivShowroom.visibility = View.GONE
            binding.ivIdn.visibility = View.VISIBLE
        }

        action()
    }

    private fun hideSystemBars() {
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView) ?: return
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    override fun onUserLeaveHint() {
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val aspectRatio = Rational(16, 9)
                val params = PictureInPictureParams.Builder().setAspectRatio(aspectRatio).build()
                enterPictureInPictureMode(params)
            }
        }catch (e: IllegalStateException){
            toast.show(getString(R.string.teks_perangkat_tidak_mendukung_pip), this)
        }

    }


    private fun action(){
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.ivShowroom.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.showroom-live.com" + member.profil_url)
                )
            )
        }

        binding.ivIdn.setOnClickListener {
            helper.openAppAtStore(this, "com.idntimes.idntimes")
        }
    }

    override fun onStart() {
        super.onStart()
        if (player == null) {
            //--------------------------------------
            //Creating default track selector
            //and init the player
            val adaptiveTrackSelection: AdaptiveTrackSelection.Factory = AdaptiveTrackSelection.Factory(DefaultBandwidthMeter())
            player = ExoPlayerFactory.newSimpleInstance(
                DefaultRenderersFactory(this),
                DefaultTrackSelector(adaptiveTrackSelection),
                DefaultLoadControl()
            )
            binding.videoView.setPlayer(player)
            val defaultBandwidthMeter = DefaultBandwidthMeter()
            val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                this,
                Util.getUserAgent(this, "Exo2"), defaultBandwidthMeter
            )
            val uri: Uri = Uri.parse(urlLive)
            val mainHandler = Handler()
            val mediaSource: MediaSource = HlsMediaSource(
                uri,
                dataSourceFactory, mainHandler, null
            )
            player!!.prepare(mediaSource)
            player!!.setPlayWhenReady(playWhenReady)
            player!!.addListener(object : Player.EventListener {
                override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {}
                override fun onTracksChanged(
                    trackGroups: TrackGroupArray,
                    trackSelections: TrackSelectionArray
                ) {
                }

                override fun onLoadingChanged(isLoading: Boolean) {}
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    when (playbackState) {
                        ExoPlayer.STATE_READY -> binding.loading.setVisibility(View.GONE)
                        ExoPlayer.STATE_BUFFERING -> binding.loading.setVisibility(View.VISIBLE)
                    }
                }

                override fun onRepeatModeChanged(repeatMode: Int) {}
                override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
                override fun onPlayerError(error: ExoPlaybackException?) {}
                override fun onPositionDiscontinuity(reason: Int) {}
                override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
                override fun onSeekProcessed() {}
            })
            player!!.seekTo(currentWindow, playbackPosition)
            player!!.prepare(mediaSource, true, false)
        }
    }

//    override fun onStop() {
//        releasePlayer()
//        super.onStop()
//    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }

    override fun onBackPressed() {
        releasePlayer()
        super.onBackPressed()
    }

    private fun releasePlayer() {
        if (player != null) {
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            playWhenReady = player!!.playWhenReady
            player!!.release()
            player = null
        }
    }
}