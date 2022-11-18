package com.arira.ngidol48.ui.activity.detailLagu

import android.content.res.Configuration
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.SongSmallAdapter
import com.arira.ngidol48.app.App.Companion.helper
import com.arira.ngidol48.app.App.Companion.pref
import com.arira.ngidol48.app.App.Companion.user
import com.arira.ngidol48.databinding.ActivityDetailLaguBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config.extra_list
import com.arira.ngidol48.helper.Config.extra_model
import com.arira.ngidol48.model.Setlist
import com.arira.ngidol48.model.Song
import com.arira.ngidol48.ui.activity.lagu.LaguActivity
import com.arira.ngidol48.ui.activity.lagu.LaguCallback
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
    private var isTabLirik = true

    //text selection
    private var mTouchXTextSelection = 0
    private var mTouchYTextSelection = 0
    private val DEFAULT_SELECTION_LEN = 5

    var startSelected: Int = 0
    var endSelected: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_lagu)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_lagu)
        setToolbar(getString(R.string.teks_lagu_jkt), binding.toolbar)
        statusPutih()

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

        lirikSelectedSetup()
    }

    private fun lirikSelectedSetup(){
        binding.tvLirikSelected.setDefaultSelectionColor(0x50F7729A)

        binding.tvLirikSelected.setOnClickListener {
            hideShareLirik()
            binding.tvLirikSelected.hideCursor()
        }

        binding.tvLirikSelected.setOnLongClickListener {
            showSelectionCursors(mTouchXTextSelection, mTouchYTextSelection)
            true
        }

        binding.tvLirikSelected.setOnTouchListener { _, event ->
            mTouchXTextSelection = event.x.toInt()
            mTouchYTextSelection = event.y.toInt()
            false
        }
    }

//    override fun onUserLeaveHint() {
//        try{
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                val aspectRatio = Rational(16, 9)
//                val params = PictureInPictureParams.Builder().setAspectRatio(aspectRatio).build()
//                enterPictureInPictureMode(params)
//            }
//        }catch (e: IllegalStateException){
//            toast.show(getString(R.string.teks_perangkat_tidak_mendukung_pip), this)
//        }
//
//    }

    private fun action(){
        binding.ivBack.setOnClickListener { 
            onBackPressed()
        }

        binding.tvTabChant.setOnClickListener {
            if (!currentSong.chant.id.isNullOrBlank()){
                isTabLirik = false
                setupTabAction()
            }
        }

        binding.tvTabLirik.setOnClickListener {
            isTabLirik = true
            setupTabAction()
        }

        binding.ivFav.setOnClickListener {
            if (pref.getIsLogin()){
                if (isLike){
                    isLike = false
                    binding.ivFav.setImageResource(R.drawable.ic_fav_outlen)
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

        when (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.tvLirik.setTextColor(ContextCompat.getColor(this, R.color.white))
                binding.tvChant.setTextColor(ContextCompat.getColor(this, R.color.white))
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.tvChant.setTextColor(ContextCompat.getColor(this, R.color.dark))
                binding.tvLirik.setTextColor(ContextCompat.getColor(this, R.color.dark))
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                binding.tvChant.setTextColor(ContextCompat.getColor(this, R.color.dark))
                binding.tvLirik.setTextColor(ContextCompat.getColor(this, R.color.dark))
            }
        }

        binding.tvJudul.text = currentSong.judul
        binding.tvLirik.text = currentSong.lirik
        binding.tvLirikSelected.text = currentSong.lirik
//        tv_lirik_selected
        binding.tvChant.text = helper.fromHtml(currentSong.chant.chant)
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

        firstSetupTab()
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
        songDetailViewModel.detail(currentSong.id)

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

    private fun firstSetupTab(){

        isTabLirik = true
        binding.linLirik.visibility = View.VISIBLE
        binding.linChant.visibility = View.GONE

        when (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                //is dark
                if (!currentSong.chant.id.isNullOrBlank()){
                    binding.tvTabLirik.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryTeks))
                    binding.tvTabChant.setTextColor(ContextCompat.getColor(this, R.color.white))
                }else{
                    binding.tvTabLirik.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryTeks))
                    binding.tvTabChant.setTextColor(ContextCompat.getColor(this, R.color.abu_pekat))
                }

            }
            Configuration.UI_MODE_NIGHT_NO -> {
                if (!currentSong.chant.id.isNullOrBlank()){
                    binding.tvTabLirik.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryTeks))
                    binding.tvTabChant.setTextColor(ContextCompat.getColor(this, R.color.dark))
                }else{
                    binding.tvTabLirik.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryTeks))
                    binding.tvTabChant.setTextColor(ContextCompat.getColor(this, R.color.abu_pekat))
                }
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                if (!currentSong.chant.id.isNullOrBlank()){
                    binding.tvTabLirik.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryTeks))
                    binding.tvTabChant.setTextColor(ContextCompat.getColor(this, R.color.dark))
                }else{
                    binding.tvTabLirik.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryTeks))
                    binding.tvTabChant.setTextColor(ContextCompat.getColor(this, R.color.abu_pekat))
                }
            }
        }
    }

    private fun setupTabAction(){
        if (isTabLirik){
            binding.linLirik.visibility = View.VISIBLE
            binding.linChant.visibility = View.GONE
            when (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    //is dark
                    binding.tvTabLirik.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryTeks))
                    binding.tvTabChant.setTextColor(ContextCompat.getColor(this, R.color.white))
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    binding.tvTabLirik.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryTeks))
                    binding.tvTabChant.setTextColor(ContextCompat.getColor(this, R.color.dark))
                }
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    binding.tvTabLirik.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryTeks))
                    binding.tvTabChant.setTextColor(ContextCompat.getColor(this, R.color.dark))
                }
            }
        }else{
            binding.linLirik.visibility = View.GONE
            binding.linChant.visibility = View.VISIBLE
            when (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    //is dark
                    binding.tvTabLirik.setTextColor(ContextCompat.getColor(this, R.color.white))
                    binding.tvTabChant.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryTeks))
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    binding.tvTabLirik.setTextColor(ContextCompat.getColor(this, R.color.dark))
                    binding.tvTabChant.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryTeks))
                }
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    binding.tvTabLirik.setTextColor(ContextCompat.getColor(this, R.color.dark))
                    binding.tvTabChant.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryTeks))
                }
            }
        }

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
                currentSong.chant = it.chant
                binding.tvChant.text = helper.fromHtml(currentSong.chant.chant)
                firstSetupTab()

                if (it.listener > 10){
                    binding.tvDidengarkan.visibility = View.VISIBLE
                }
                binding.tvDidengarkan.text = getString(R.string.teks_dx_didengarkan, it.listener)

                totalLike = it.size_fav
                isLike = it.fav
                binding.linFav.visibility = View.VISIBLE
                binding.tvLike.text = getString(R.string.teks_d_suka, it.size_fav)
                if (it.fav){
                    binding.ivFav.setImageResource(R.drawable.ic_fav)
                }else{
                    binding.ivFav.setImageResource(R.drawable.ic_fav_outlen)
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


    /*CURSOR FOR LIRIK*/
    private fun showSelectionCursors(x: Int, y: Int) {
        binding.tvLirikSelected.hideCursor()
        showShareLirik()

        val start: Int = binding.tvLirikSelected.getPreciseOffset(x, y)
        startSelected = songDetailViewModel.countStart(start, currentSong.lirik)

        if (start > -1) {

            endSelected = songDetailViewModel.coutToNextParaph(start, currentSong.lirik)
            if (endSelected > binding.tvLirikSelected.text.length) {
                endSelected = binding.tvLirikSelected.text.length - 1
            }

            var end: Int = start + DEFAULT_SELECTION_LEN
            if (end >= binding.tvLirikSelected.text.length) {
                end = binding.tvLirikSelected.text.length - 1
            }

            binding.tvLirikSelected.showSelectionControls(start, end)
        }

    }

    private fun showShareLirik() {
//        toolbar_komen.visibility = View.VISIBLE
    }

    private fun hideShareLirik() {
    }
}