package com.arira.ngidol48.ui.detailLagu

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.SongSmallAdapter
import com.arira.ngidol48.app.App.Companion.pref
import com.arira.ngidol48.app.App.Companion.user
import com.arira.ngidol48.databinding.ActivityDetailLaguBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config.extra_list
import com.arira.ngidol48.helper.Config.extra_model
import com.arira.ngidol48.model.Setlist
import com.arira.ngidol48.model.Song
import com.arira.ngidol48.ui.lagu.LaguActivity
import com.arira.ngidol48.ui.lagu.LaguCallback
import com.arira.ngidol48.utilities.Go
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.rommansabbir.animationx.AnimationX
import com.rommansabbir.animationx.Fade
import io.reactivex.annotations.NonNull

class DetailLaguActivity : BaseActivity(), LaguCallback {
    private lateinit var binding:ActivityDetailLaguBinding
    private var currentSong:Song = Song()
    private var listLagu:ArrayList<Song> = ArrayList()
    private var mYouTubePlayer:YouTubePlayer? = null
    private var countDownNext: CountDownTimer? = null

    private lateinit var listeningView: SongListeningViewModel
    private lateinit var songDetailViewModel: SongDetailViewModel
    private lateinit var favViewModel: SongFavViewModel
    private var totalLike:Int = 0
    private var isLike:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_lagu)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_lagu)
        setToolbar(getString(R.string.teks_lagu_jkt), binding.toolbar)

        listeningView = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[SongListeningViewModel::class.java]
        listeningView.context = this

        songDetailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[SongDetailViewModel::class.java]
        songDetailViewModel.context = this

        favViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[SongFavViewModel::class.java]
        favViewModel.context = this



        currentSong = intent.getParcelableExtra(extra_model) ?: Song()
        listLagu = intent.getParcelableArrayListExtra(extra_list) ?: ArrayList()

        if (listLagu.isNotEmpty()){
            binding.tvLaguLain.visibility = View.VISIBLE
        }else{
            binding.tvLaguLain.visibility = View.GONE
        }

        binding.embedYt.enableBackgroundPlayback(true)
        binding.embedYt.getPlayerUiController().showFullscreenButton(false)


        setDataLagu()
        action()

        observeDataDetail()
        songDetailViewModel.detail(currentSong.id)

        observeDataFavorit()
    }

    private fun action(){
        binding.ivFav.setOnClickListener {
            if (pref.getIsLogin()){
                if (isLike){
                    isLike = false
                    binding.ivFav.setImageResource(R.drawable.ic_unfav)
                    totalLike -= 1
                    favViewModel.remove(currentSong.id)
                }else{
                    isLike = true
                    binding.ivFav.setImageResource(R.drawable.ic_fav)
                    totalLike += 1
                    favViewModel.fav(currentSong.id)
                }


                binding.tvLike.text = getString(R.string.teks_d_suka, totalLike)

            }else{
                toast.show(getString(R.string.teks_anda_perlu_login),this)
            }
        }

        binding.tvSetlist.setOnClickListener {
            val setlist = Setlist()

            if (listLagu.isNotEmpty()){
                val song = listLagu[0]
                setlist.cover = song.cover
                setlist.setlist_id = song.setlist_id
                setlist.nama = song.nama
                setlist.total_lagu = listLagu.size.toString()
                Go(this).move(LaguActivity::class.java, data = setlist, choose = false)
            }else{
                val newSetlist = Setlist()
                newSetlist.id = currentSong.setlist_id
                newSetlist.setlist_id = currentSong.setlist_id
                newSetlist.nama = currentSong.nama
                Go(this).move(LaguActivity::class.java, data = newSetlist, choose = false)
            }
        }

    }

    private fun setDataLagu(){

        binding.tvJudul.text = currentSong.judul
        binding.tvLirik.text = currentSong.lirik
        binding.tvSetlist.text = currentSong.nama

        if (pref.getOnReview()){
            binding.embedYt.visibility = View.GONE
        }else{
            binding.embedYt.visibility = View.VISIBLE
            setDataVideo()
        }

        val filterSong:ArrayList<Song> = ArrayList()
        for (song in listLagu){
            if (song.id != this.currentSong.id){
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

            mYouTubePlayer?.loadVideo(videoId, 0f)

            //stop count down
            if (countDownNext != null){
                countDownNext?.cancel()
            }
        }

        this.currentSong = lagu
        setDataLagu()

        binding.scroll.postDelayed(Runnable {
            binding.scroll.fullScroll(View.FOCUS_UP)
        }, 400)
    }

    private fun getNext():Song?{
        var currentPosition = 0
        for (i in  0 until listLagu.size){
            if (listLagu[i].id == currentSong.id){
                currentPosition = i
            }
        }

        if ((currentPosition+1) < listLagu.size){
            return listLagu[currentPosition + 1]
        }
        return null
    }


    private fun setDataVideo(){
        var videoId = ""
        if (currentSong.song_link.isNotEmpty()) {
            videoId = if (currentSong.song_link.contains("/")) {
                currentSong.song_link.substring(currentSong.song_link.lastIndexOf("/") + 1)
            } else {
                currentSong.song_link
            }

            binding.embedYt.visibility = View.VISIBLE

            binding.embedYt.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {

                override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                    mYouTubePlayer = youTubePlayer

                    youTubePlayer.loadVideo(videoId, 0f)
                    currentSong.user_id = user.id

                    listeningView.listening(currentSong)
//                    youTubePlayer.play()
                }

                override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                    //PLAYING , PAUSED, ENDED, BUFFERING

                    when(state.toString()){
                        "PLAYING"->{
//                            pref.setSong(lagu)
//                            try{
//                                ContextCompat.startForegroundService(
//                                    applicationContext,
//                                    Intent(applicationContext, MediaSessionService::class.java)
//                                )
//                            }catch (e:RuntimeException){
//
//                            }catch (e:RuntimeException){
//
//                            }catch (e:RemoteException){
//
//                            }

                        }
                        "ENDED"->{
                            val nextSong = getNext()
                            if (nextSong != null){
                                //show card next
                                AnimationX().setDuration(1000)
                                    .setAnimation(Fade.outRight(binding.cardNextSong, AnimationX().getNewAnimatorSet())).start()

                                binding.cardNextSong.visibility = View.VISIBLE
                                binding.tvJudulNextSong.text = nextSong.judul

                                countDownNext = object : CountDownTimer(5000, 1000){
                                    override fun onTick(p0: Long) {
                                        binding.tvCdtNextSong.text = "(${p0/1000})"
                                    }
                                    override fun onFinish() {
                                        //change song
                                        onSelectOtherSong(nextSong)

                                        //reset count down
                                        countDownNext = null


                                        //hide card count down
                                        Handler(Looper.getMainLooper()).postDelayed({
                                            binding.cardNextSong.visibility = View.GONE
                                        }, 1000)

                                    }

                                }.start()
                            }
                        }
                        else->{
//                            try{
//                                stopService(Intent(applicationContext, MediaSessionService::class.java))
//                            }catch (e:RuntimeException){
//
//                            }catch (e:RuntimeException){
//
//                            }catch (e:RemoteException){
//
//                            }

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

    private fun observeDataDetail(){
        songDetailViewModel.getResponse().observe(this){
            if (it != null){
                if (it.listener > 10){
                    binding.tvDidengarkan.visibility = View.VISIBLE
                    binding.tvDidengarkan.text = getString(R.string.teks_dx_didengarkan, it.listener)
                }

                totalLike = it.size_fav
                isLike = it.fav
                binding.linFav.visibility = View.VISIBLE
                binding.tvLike.text = getString(R.string.teks_d_suka, it.size_fav)
                if (it.fav){
                    binding.ivFav.setImageResource(R.drawable.ic_fav)
                }else{
                    binding.ivFav.setImageResource(R.drawable.ic_unfav)
                }

            }
        }
    }

    private fun observeDataFavorit(){
        favViewModel.getError().observe(this){
            if (it != null){
                toast.show(it, this)
            }
        }
    }
}