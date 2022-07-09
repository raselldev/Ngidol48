package com.arira.ngidol48.ui.activity.home

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
import android.view.View.OnTouchListener
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.*
import com.arira.ngidol48.app.App.Companion.helper
import com.arira.ngidol48.app.App.Companion.pref
import com.arira.ngidol48.app.App.Companion.token
import com.arira.ngidol48.app.App.Companion.user
import com.arira.ngidol48.databinding.ActivityMainBinding
import com.arira.ngidol48.databinding.CustomDialogRatingAppBinding
import com.arira.ngidol48.databinding.DialogBdayBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config
import com.arira.ngidol48.helper.Config.BASE_STORAGE_IMAGE
import com.arira.ngidol48.helper.Config.TOPIC_EVENT
import com.arira.ngidol48.helper.Config.TOPIC_HANDSHAKE
import com.arira.ngidol48.helper.Config.TOPIC_MNG
import com.arira.ngidol48.helper.Config.TOPIC_NEWS
import com.arira.ngidol48.helper.Config.TOPIC_SHOWROOM
import com.arira.ngidol48.helper.SweetAlert
import com.arira.ngidol48.model.Banner
import com.arira.ngidol48.model.Member
import com.arira.ngidol48.model.Slider
import com.arira.ngidol48.ui.activity.allBlog.BlogActivity
import com.arira.ngidol48.ui.activity.chants.ChantsActivity
import com.arira.ngidol48.ui.activity.event.EventActivity
import com.arira.ngidol48.ui.activity.handshake.HandshakeActivity
import com.arira.ngidol48.ui.activity.member.MemberActivity
import com.arira.ngidol48.ui.activity.member.MemberCallback
import com.arira.ngidol48.ui.activity.mng.MngActivity
import com.arira.ngidol48.ui.activity.newKalender.KalenderActivity
import com.arira.ngidol48.ui.activity.news.BeritaActivity
import com.arira.ngidol48.ui.activity.notifikasi.NotifikasiActivity
import com.arira.ngidol48.ui.activity.pengaturan.PengaturanActivity
import com.arira.ngidol48.ui.activity.profil.ProfilActivity
import com.arira.ngidol48.ui.activity.setlist.SetlistActivity
import com.arira.ngidol48.ui.activity.viewShowroom.ViewLiveActivity
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import io.reactivex.annotations.NonNull
import java.util.*


class MainActivity : BaseActivity(), MemberCallback {
    
    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var reviewManager: ReviewManager

    private var xDelta = 0
    private var yDelta = 0

    private var selectedBdMember:Member = Member()
    private var idnLiveStreamURL:String = ""
    private var bannerData:Banner = Banner()

    private var mYouTubePlayer: YouTubePlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        statusPutih()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[HomeViewModel::class.java]
        viewModel.context = this


        /*menambakan warna untuk swipe refresh*/
        binding.swipe.setColorSchemeResources(R.color.colorPrimaryTeks,
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent)

        observerData()
        viewModel.home()

        action()

        subcribeAll()

        reviewManager = ReviewManagerFactory.create(this)

        /**
         * check user count open app and update to pref */
        var countOpenApp = pref.getOpenApp()
        countOpenApp += 1
        pref.setOpenApp(countOpenApp)

        /** check how many user open the app,
         * if > 10 dialog for rating was show
         * and not rate app, dialog will be show*/
        if (pref.getOpenApp() > 5 && !pref.isRated()){
            Log.e("RATE", "show rate")
            showRateApp()
        }
    }

    override fun onResume() {
        super.onResume()

        if (mYouTubePlayer != null){
            mYouTubePlayer!!.pause()
        }

        if(pref.getIsLogin()){
            val user = pref.getUser()
            binding.linUser.visibility = View.VISIBLE
            binding.tvNama.text = getString(R.string.teks_halo_s_nama, user.fullname)
            binding.tvBodyNama.text = getString(R.string.teks_ucakapan_pagi)
            if (user.avatar.contains("http")){
                Glide.with(this).load(user.avatar).error(R.drawable.ic_baseline_person_24).placeholder(R.drawable.ic_baseline_person_24).into(binding.ivAvatarUser)
            }else{
                Glide.with(this).load(BASE_STORAGE_IMAGE + user.avatar).error(R.drawable.ic_baseline_person_24).placeholder(R.drawable.ic_baseline_person_24).into(binding.ivAvatarUser)
            }
        }else{
            binding.linUser.visibility = View.GONE
        }

        if (pref.getOnReview()){
            binding.linBlog.visibility = View.GONE
        }else{
            binding.linBlog.visibility = View.VISIBLE
        }
    }


    /**
     * menampilkan review manager
     * menggunakan goole play API untuk merating*/
    fun showRateApp() {

        val request: Task<ReviewInfo> = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We can get the ReviewInfo object
                val reviewInfo: ReviewInfo = task.result
                val flow: Task<Void> = reviewManager.launchReviewFlow(this, reviewInfo)
                flow.addOnCompleteListener {
                    pref.setRated(true)
                }
                flow.addOnSuccessListener {
                }
                flow.addOnFailureListener {
                }
            } else {
                //gagal memanggil revie manager, menampilkan alternative
                showDialogRating()
            }
        }
    }

    private fun showDialogRating(){
        val dialog = AlertDialog.Builder(this)
        val bindingDialog= DataBindingUtil.inflate<CustomDialogRatingAppBinding>(layoutInflater,R.layout.custom_dialog_rating_app,null,false)
        dialog.setView(bindingDialog.root)
        val dialogCreate = dialog.create()
        dialogCreate.window!!.setBackgroundDrawableResource(R.color.transparant)
        with(bindingDialog){
            tvUlas.setOnClickListener {
                helper.openAppAtStore(this@MainActivity)
                dialogCreate.dismiss()
            }
            tvNanti.setOnClickListener {
                helper.openAppAtStore(this@MainActivity)
                dialogCreate.dismiss()
            }
            tvTidak.setOnClickListener {
                pref.setRated(true)
            }
        }
        dialogCreate.show()
    }


    private fun showBdayMember(bdayMember:List<Member>){
        val dialog = AlertDialog.Builder(this)
        val bindingDialog = DataBindingUtil.inflate<DialogBdayBinding>(
            LayoutInflater.from(this),
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
            if (curDay == helper.convert(bdayMember[i].tanggal_lahir, "d MMMM yyyy", "d")){
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

        binding.linChant?.setOnClickListener {
            Go(this).move(ChantsActivity::class.java)
        }

        binding.ivBanner.setOnClickListener {
            if (bannerData.value.isNotEmpty()){
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(bannerData.value)
                startActivity(openURL)
            }
        }

        binding.divPlayIdn.setOnClickListener {
            Go(this).move(ViewLiveActivity::class.java, url = idnLiveStreamURL, choose = false)
        }

        binding.linUser.setOnClickListener {
            if (user.id.isEmpty()){
                user = pref.getUser()
                token = user.token_app
            }
            Go(this).move(ProfilActivity::class.java, id = user.id)
        }

        binding.linBlog.setOnClickListener {
            Go(this).move(BlogActivity::class.java)
        }

        binding.ivSetting.setOnClickListener {
            Go(this).move(PengaturanActivity::class.java)
        }

        binding.swipe.setOnRefreshListener {
            viewModel.home()
        }

        binding.icNotification.setOnClickListener {
            Go(this).move(NotifikasiActivity::class.java)
        }

        binding.linMember.setOnClickListener {
            Go(this).move(MemberActivity::class.java)
        }

        binding.linHandshake.setOnClickListener {
            Go(this).move(HandshakeActivity::class.java)
        }

        binding.linMng.setOnClickListener {
            Go(this).move(MngActivity::class.java)
        }

        binding.linSetlist.setOnClickListener {
            Go(this).move(SetlistActivity::class.java)
        }

        binding.linKalender.setOnClickListener {
            if (pref.getNewCalender()){
                Go(this).move(KalenderActivity::class.java)
            }else{
                Go(this).move(EventActivity::class.java)
            }

        }

        binding.ivSemuaBerita.setOnClickListener {
            Go(this).move(BeritaActivity::class.java)
        }

        binding.linAllNews.setOnClickListener {
            Go(this).move(BeritaActivity::class.java)
        }

        binding.ivCloseBd.setOnClickListener {
            binding.relBdayAva.visibility = View.GONE
        }

        binding.relBdayAva.setOnTouchListener(onTouchAvaBdListener())
        binding.relBdayAva.setOnClickListener {
            toast.show("click", this)
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onTouchAvaBdListener(): OnTouchListener {
        var fromMove  = false
        return OnTouchListener { view, event ->
            val x = event.rawX.toInt()
            val y = event.rawY.toInt()
            Log.e("NOW", "${event.action}")
            when (event.action and MotionEvent.ACTION_MASK) {

                MotionEvent.ACTION_BUTTON_PRESS ->{
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
                    if (!fromMove){
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

    private  fun subcribeAll(){

        if (pref.getNotifNews()){
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_NEWS).addOnSuccessListener {
            }
        }

        if (pref.getNotifMng()){
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_MNG).addOnSuccessListener {
            }
        }

        if (pref.getNotifEvent()){
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_EVENT).addOnSuccessListener {
            }
        }

        if (pref.getNotifHandshake()){
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_HANDSHAKE).addOnSuccessListener {
            }
        }

        if (pref.getNotifShowroom()){
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_SHOWROOM)
            .addOnSuccessListener {
                Log.e("SHOWROM", "update")
            }
        }



    }

    fun observerData(){
        viewModel.getLoading().observe(this, Observer {
            it.let {
                if (it != null){
                    binding.swipe.isRefreshing = it
                }
            }

        })

        viewModel.getError().observe(this, Observer {
            it.let {
                if (it != null){
                    binding.swipe.isRefreshing = false
                    SweetAlert.onFailure(this, it)
                }

            }
        })

        viewModel.getResponse().observe(this, Observer {
            it.let {
                if (it != null) {
                    /*show banner data*/
                    Log.e("D", "sjoe ${it.show_banner}")
                    if(it.show_banner == "1"){
                        bannerData = it.banner
                        binding.ivBanner.let { it1 ->
                            Glide.with(this).load(it.banner.image)
                                .diskCacheStrategy(DiskCacheStrategy.DATA)
                                .into(
                                    it1
                                )
                        }
                        binding.cardBanner?.visibility = View.VISIBLE
                    }else{
                        binding.cardBanner?.visibility = View.GONE
                    }

                    //idnlive
                    val idn = it.idn
                    if(idn.status == "live"){
                        binding.linIdn.visibility = View.VISIBLE
                        Glide.with(this).load(idn.image_url).into(binding.ivThumbIdn)
                        idnLiveStreamURL = idn.playback_url
                    }else{
                        binding.linIdn.visibility = View.GONE
                    }

                    /*show informasi*/
                    binding.rvBerita.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = BeritaAdapter(it.news)
                    }

                    /*show size notificaiton todae*/
                    if (it.notifikasi_hari_ini > 0){
                        binding.divJmlNotif.visibility = View.VISIBLE
                        binding.tvJmlNotif.text = it.notifikasi_hari_ini.toString()
                    }else{
                        binding.divJmlNotif.visibility = View.GONE
                    }

                    /*birthday member*/
                    if (it.bday_member.isNotEmpty()){
                        binding.linBday.visibility = View.VISIBLE
                        binding.rvBday.apply {
                            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
                                layoutManager = GridLayoutManager(context, 5)
                            }else{
                                layoutManager = GridLayoutManager(context, 4)
                            }

                            adapter = MemberAdapter(it.bday_member, this@MainActivity)
                        }

                        val bdayToday = findTodayBday(it.bday_member)
                        if (bdayToday.isNotEmpty()){
                            showBdayMember(bdayToday)

                            selectedBdMember = bdayToday[0]
                            binding.relBdayAva.visibility = View.VISIBLE
                            Glide.with(this).load(Config.BASE_STORAGE_JKT + selectedBdMember.avatar).into(binding.ivAvaBday)
                        }


                    }else{
                        binding.relBdayAva.visibility = View.GONE
                        binding.linBday.visibility = View.GONE
                    }

                    /*event today*/
                    if(it.event.isEmpty()){
                        binding.linViewKosongEvent.visibility = View.VISIBLE
                    }else{
                        binding.linViewKosongEvent.visibility = View.GONE
                        binding.rvEvent.apply {
                            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                            adapter = EventHomeAdapter(it.event)
                        }
                    }

                    /*show showroom member online*/
                    if(it.live_showroom.isEmpty()){
                        binding.linShowroom.visibility = View.GONE
                    }else{
                        binding.tvJmlMember.text = "( ${it.live_showroom.size} Member )"
                        binding.linShowroom.visibility = View.VISIBLE
                        binding.rvShowroom.apply {
                            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                            adapter = ShowroomAdapter(it.live_showroom)
                        }
                    }

                    if(it.sr_today.isEmpty()){
                        binding.linShowroomHistory?.visibility = View.GONE
                    }else{
                        binding.tvJmlMemberHistory?.text = "( ${it.sr_today.size} Replay )"
                        binding.linShowroomHistory?.visibility = View.VISIBLE
                        binding.rvShowroomHistory?.apply {
                            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                            adapter = HistoryShowroomAdapter(it.sr_today)
                        }
                    }

                    /*show blog menu*/
                    if (pref.getOnReview()){
                        binding.linBlog.visibility = View.GONE
                    }else{
                        binding.linBlog.visibility = View.VISIBLE
                    }

                    //show special video replay slider
                    if (it.show_special_video == "1"){
                        binding.embedYt?.visibility = View.VISIBLE
                        binding.slider.visibility = View.GONE
                        setDataVideo(it.special_video.videoId)
                    }else{
                        binding.embedYt?.visibility = View.GONE
                        binding.slider.visibility = View.VISIBLE
                        /*show slider data*/
                        setSlider(it.slider)
                    }

                }
            }
        })
    }

    fun setSlider(slider: List<Slider>){
        var currentPage = 0
        if (slider.isNotEmpty()) {
            val adapter = SliderAdapter(slider as ArrayList<Slider>, this)
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

            binding.embedYt?.visibility = View.VISIBLE

            binding.embedYt?.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {

                override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                    mYouTubePlayer = youTubePlayer

                    youTubePlayer.loadVideo(videoId, 0f)
                }

                override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                    //PLAYING , PAUSED, ENDED, BUFFERING
                    Log.e("STATUS", "state. ${state}")
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
            binding.embedYt?.getPlayerUiController()?.showFullscreenButton(false)

        } else {
            binding.embedYt?.visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        if (mYouTubePlayer != null){
            mYouTubePlayer!!.pause()
        }
    }

}