package com.arira.ngidol48.ui.fragment.home

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.*
import com.arira.ngidol48.app.App
import com.arira.ngidol48.app.App.Companion.pref
import com.arira.ngidol48.databinding.DialogBdayBinding
import com.arira.ngidol48.databinding.FragmentHomeBinding
import com.arira.ngidol48.helper.BaseFragment
import com.arira.ngidol48.helper.Config
import com.arira.ngidol48.helper.SweetAlert
import com.arira.ngidol48.model.*
import com.arira.ngidol48.ui.activity.allBlog.BlogActivity
import com.arira.ngidol48.ui.activity.chants.ChantsActivity
import com.arira.ngidol48.ui.activity.handshake.HandshakeActivity
import com.arira.ngidol48.ui.activity.home.HomeViewModel
import com.arira.ngidol48.ui.activity.member.MemberCallback
import com.arira.ngidol48.ui.activity.mng.MngActivity
import com.arira.ngidol48.ui.activity.news.BeritaActivity
import com.arira.ngidol48.ui.activity.photoCard.listPhoto.ListPhotoActivity
import com.arira.ngidol48.ui.activity.radio.RadioActivity
import com.arira.ngidol48.ui.activity.songHome.HomeSongActivity
import com.arira.ngidol48.ui.activity.stream.EventStreamActivity
import com.arira.ngidol48.ui.activity.viewShowroom.ViewLiveActivity
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import io.reactivex.annotations.NonNull
import java.util.*

class HomeFragment : BaseFragment(), MemberCallback {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    private var selectedBdMember: Member = Member()
    private var idnLiveStreamURL:String = ""
    private var bannerData: Banner = Banner()
    private var streamData = LiveStream()
    private var photocardSession = PhotocardSession()
    private var xDelta = 0
    private var yDelta = 0


    private var mYouTubePlayer: YouTubePlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[HomeViewModel::class.java]
        viewModel.context = activity

        /*menambakan warna untuk swipe refresh*/
        binding.swipe.setColorSchemeResources(R.color.colorPrimaryTeks,
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent)

        observerData()
        viewModel.home(App.pref.getFCMToken())

        action()

        return binding.root
    }


    private fun showBdayMember(bdayMember:List<Member>){
        val dialog = AlertDialog.Builder(requireActivity())
        val bindingDialog = DataBindingUtil.inflate<DialogBdayBinding>(
            LayoutInflater.from(requireContext()),
            R.layout.dialog_bday,
            null,
            false
        )
        dialog.setView(bindingDialog.root)
        dialog.setCancelable(true)

        val dialogCreate = dialog.create()
        dialogCreate.window!!.setBackgroundDrawableResource(R.color.transparant)

        bindingDialog.rvAva.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = AvaMemberAdapter(bdayMember, bdayMember.size)
        }

        var memberName = ""
        for (i in bdayMember.indices){
            memberName += if (i == bdayMember.size - 1){
                " ${bdayMember[i].nama_lengkap}"
            }else{
                "${bdayMember[i].nama_lengkap}, "
            }
        }

        bindingDialog.tvNamaMember.text = memberName

        bindingDialog.btnClose.setOnClickListener {
            dialogCreate.dismiss()
        }

        dialogCreate.setOnDismissListener {

        }
        dialogCreate.show()
    }

    fun findTodayBday(bdayMember:List<Member>):ArrayList<Member>{
        val listBday:ArrayList<Member> = ArrayList()
        for (i in bdayMember.indices){
            if (curDay == App.helper.convert(bdayMember[i].tanggal_lahir, "d MMMM yyyy", "d")){
                listBday.add(bdayMember[i])
            }
        }
        return listBday
    }

    @SuppressLint("SetTextI18n")
    override fun onSelect(member: Member) {
        showSheetMember(member)
    }

    private fun action(){

        binding.cardPhotocard.setOnClickListener {
            //reset photocard session
            if (pref.getSessionPC() != photocardSession.id) {
                pref.setSessionPC(photocardSession.id)
                pref.setTotalCreatePC(0)
            }
            Go(requireActivity()).move(ListPhotoActivity::class.java, data =  photocardSession)
        }

        binding.linChant.setOnClickListener {
            Go(requireContext()).move(ChantsActivity::class.java)
        }

        binding.ivBanner.setOnClickListener {
            if (bannerData.value.isNotEmpty()){
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(bannerData.value)
                startActivity(openURL)
            }
        }

        binding.divPlayIdn.setOnClickListener {
            Go(requireContext()).move(ViewLiveActivity::class.java, url = idnLiveStreamURL, choose = false)
        }

        binding.ivLiveStream.setOnClickListener {
            Go(requireContext()).move(EventStreamActivity::class.java, data = streamData, choose = false)
        }


        binding.swipe.setOnRefreshListener {
            viewModel.home(App.pref.getFCMToken())
        }

        binding.linHandshake.setOnClickListener {
            Go(requireContext()).move(HandshakeActivity::class.java)
        }

        binding.linMng.setOnClickListener {
            Go(requireContext()).move(MngActivity::class.java)
        }

        binding.linSetlist.setOnClickListener {
            Go(requireContext()).move(HomeSongActivity::class.java)
        }

        binding.linBlog.setOnClickListener {
            Go(requireContext()).move(BlogActivity::class.java)
        }

        binding.ivSemuaBerita.setOnClickListener {
            Go(requireContext()).move(BeritaActivity::class.java)
        }


        binding.ivCloseBd.setOnClickListener {
            binding.relBdayAva.visibility = View.GONE
        }

        binding.relBdayAva.setOnTouchListener(onTouchAvaBdListener())
        binding.relBdayAva.setOnClickListener {
            toast.show("click", requireContext())
        }

    }

    fun observerData(){
        viewModel.getLoading().observe(requireActivity()) {
            it.let {
                if (it != null) {
                    binding.swipe.isRefreshing = it
                }
            }
        }

        viewModel.getError().observe(requireActivity()) {
            it.let {
                if (it != null) {
                    binding.swipe.isRefreshing = false
                    SweetAlert.onFailure(requireActivity(), it)
                }

            }
        }

        viewModel.getResponse().observe(requireActivity()) {
            it.let {
                if (it != null) {

                    /*show photocard session*/
                    try {
                        if (it.session_cathleenexus.id.isNotEmpty()) {
                            this.photocardSession = it.session_cathleenexus
                            binding.cardPhotocard.visibility = View.VISIBLE
                            Glide.with(this).load(it.session_cathleenexus.session_banner)
                                .diskCacheStrategy(DiskCacheStrategy.DATA)
                                .into(
                                    binding.icPhotocard
                                )

                            binding.tvTitlePhotocard.text = it.session_cathleenexus.session_name
                        }else {
                            binding.cardPhotocard.visibility = View.GONE
                        }
                    }catch (_: java.lang.NullPointerException){

                    }

                    it.radio?.let { radio ->
                        setRadio(radio)
                    }


                    /*show banner data*/
                    if (it.show_banner == "1") {
                        bannerData = it.banner
                        binding.ivBanner.let { it1 ->
                            Glide.with(this).load(it.banner.image)
                                .diskCacheStrategy(DiskCacheStrategy.DATA)
                                .into(
                                    it1
                                )
                        }
                        binding.cardBanner.visibility = View.VISIBLE
                    } else {
                        binding.cardBanner.visibility = View.GONE
                    }

                    if (it.show_banner_stream == "1") {
                        binding.cardLivestrem?.visibility = View.VISIBLE

                        streamData = it.live_stream
                        Glide.with(this).load(streamData.banner).into(binding.ivLiveStream!!)
                    } else {
                        binding.cardLivestrem?.visibility = View.GONE
                    }

                    //idnlive
                    val idn = it.idn
                    if (idn.status == "live") {
                        binding.linIdn.visibility = View.VISIBLE
                        Glide.with(this).load(idn.image_url).into(binding.ivThumbIdn)
                        idnLiveStreamURL = idn.playback_url
                    } else {
                        binding.linIdn.visibility = View.GONE
                    }

                    /*show informasi*/
                    binding.rvBerita.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = BeritaAdapter(it.news)
                    }

                    /*birthday member*/
                    if (it.bday_member.isNotEmpty()) {
                        binding.linBday.visibility = View.VISIBLE
                        binding.rvBday.apply {
                            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                layoutManager = GridLayoutManager(context, 5)
                            } else {
                                layoutManager = GridLayoutManager(context, 4)
                            }

                            adapter = MemberAdapter(it.bday_member, this@HomeFragment, titleWhite = true)
                        }

                        val bdayToday = findTodayBday(it.bday_member)
                        if (bdayToday.isNotEmpty()) {
//                            showBdayMember(bdayToday)

                            selectedBdMember = bdayToday[0]
                            binding.relBdayAva.visibility = View.VISIBLE
                            Glide.with(this).load(Config.BASE_STORAGE_JKT + selectedBdMember.avatar)
                                .into(binding.ivAvaBday)
                        }


                    } else {
                        binding.relBdayAva.visibility = View.GONE
                        binding.linBday.visibility = View.GONE
                    }

                    /*event today*/
                    if (it.event.isEmpty()) {
                        binding.linViewKosongEvent.visibility = View.VISIBLE
                    } else {
                        binding.linViewKosongEvent.visibility = View.GONE
                        binding.rvEvent.apply {
                            layoutManager =
                                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                            adapter = EventHomeAdapter(it.event)
                        }
                    }

                    /*show showroom member online*/
                    if (it.live_showroom.isEmpty()) {
                        binding.linShowroom.visibility = View.GONE
                    } else {
                        binding.tvJmlMember.text = "( ${it.live_showroom.size} Member )"
                        binding.linShowroom.visibility = View.VISIBLE
                        binding.rvShowroom.apply {
                            layoutManager =
                                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                            adapter = ShowroomAdapter(it.live_showroom)
                        }
                    }

                    if (it.sr_today.isEmpty()) {
                        binding.linShowroomHistory.visibility = View.GONE
                    } else {
                        binding.tvJmlMemberHistory.text = "( ${it.sr_today.size} Replay )"
                        binding.linShowroomHistory.visibility = View.VISIBLE
                        binding.rvShowroomHistory.apply {
                            layoutManager =
                                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                            adapter = HistoryShowroomAdapter(it.sr_today)
                        }
                    }

                    //show special video replay slider
                    if (it.show_special_video == "1") {
                        binding.embedYt.visibility = View.VISIBLE
                        binding.slider.visibility = View.GONE
                        setDataVideo(it.special_video.videoId)
                    } else {
                        binding.embedYt.visibility = View.GONE
                        binding.slider.visibility = View.VISIBLE
                        /*show slider data*/
                        setSlider(it.slider)
                    }

                    /*show blog menu*/
                    if (pref.getOnReview()) {
                        binding.linBlog.visibility = View.GONE
                    } else {
                        binding.linBlog.visibility = View.VISIBLE
                    }

                    if(it.merch.isNotEmpty()) {
                        binding.linMerch.visibility  = View.VISIBLE
                        binding.rvMerch.apply {
                            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, true)
                            adapter = MerchAdapter(it.merch)
                        }
                    }else {
                        binding.linMerch.visibility  = View.GONE
                    }

                }
            }
        }
    }

    fun setSlider(slider: List<Slider>){
        var currentPage = 0
        if (slider.isNotEmpty()) {
            val adapter = SliderAdapter(slider as ArrayList<Slider>, requireActivity())
            binding.slider.adapter = adapter

            /*auto sliding*/
            val DELAY_MS: Long = 5000
            val PERIOD_MS: Long = 10000

            /*After setting the adapter use the timer */

            /*After setting the adapter use the timer */
            val handler = Handler(Looper.myLooper()!!)
            val update = Runnable {
                if (currentPage == adapter.count) {
                    currentPage = 0
                }
                binding.slider.setCurrentItem(currentPage++, true)
            }

            val timerSlider = Timer() // This will create a new Thread

            timerSlider.schedule(object : TimerTask() {
                // task to be scheduled
                override fun run() {
                    handler.post(update)
                }
            }, DELAY_MS, PERIOD_MS)
            /*selesai auto sliding*/

        }
    }

    private fun setDataVideo(videoId:String){
        binding.viewGroup.keepScreenOn = true
        if (videoId.isNotEmpty()) {

            binding.embedYt.visibility = View.VISIBLE

            binding.embedYt.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {

                override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                    mYouTubePlayer = youTubePlayer

                    youTubePlayer.loadVideo(videoId, 0f)
                }

                override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                    //PLAYING , PAUSED, ENDED, BUFFERING
                    when(state.toString()){
                        "PLAYING"->{

                        }
                        "ENDED"->{
                        }
                        else->{

                        }
                    }
                }
            })
            binding.embedYt.getPlayerUiController().showFullscreenButton(false)

        } else {
            binding.embedYt.visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        if (mYouTubePlayer != null){
            mYouTubePlayer!!.pause()
        }
    }

    private fun setRadio(radio: Radio?) {
        radio?.let {
            if (radio.id.isNotEmpty()) {
                binding.cardRadio.visibility  = View.VISIBLE
                binding.tvRadioTitle.text  = it.name
                binding.tvRadioDesc.text = it.description
                binding.cardRadio.setOnClickListener {
                    Go(requireContext()).move(RadioActivity::class.java, data = radio)
                }
            }

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onTouchAvaBdListener(): View.OnTouchListener {
        var fromMove  = false
        return View.OnTouchListener { view, event ->
            val x = event.rawX.toInt()
            val y = event.rawY.toInt()
            Log.e("NOW", "${event.action}")
            when (event.action and MotionEvent.ACTION_MASK) {

                MotionEvent.ACTION_BUTTON_PRESS -> {
                    Log.e("NOW", "btn press")
                }
                MotionEvent.ACTION_DOWN -> {
                    fromMove = false
                    Log.e("NOW", "down")
                    val lParams = view.layoutParams as RelativeLayout.LayoutParams
                    xDelta = x - lParams.leftMargin
                    yDelta = y - lParams.topMargin
                }
                MotionEvent.ACTION_UP -> {
                    if (!fromMove) {
                        showSheetMember(selectedBdMember)
                    }

                }
                MotionEvent.ACTION_MOVE -> {
                    fromMove = true
                    Log.e("NOW", "move")
                    val layoutParams = view
                        .layoutParams as RelativeLayout.LayoutParams
                    layoutParams.leftMargin = x - xDelta
                    layoutParams.topMargin = y - yDelta
                    layoutParams.rightMargin = 0
                    layoutParams.bottomMargin = 0
                    view.layoutParams = layoutParams
                }
            }
            binding.viewGroup.invalidate()
            true
        }
    }


}