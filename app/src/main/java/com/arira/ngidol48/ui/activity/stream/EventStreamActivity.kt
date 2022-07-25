package com.arira.ngidol48.ui.activity.stream

import android.app.PictureInPictureParams
import android.net.Uri
import android.os.*
import android.util.Rational
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.LiveChatAdapter
import com.arira.ngidol48.app.App.Companion.helper
import com.arira.ngidol48.app.App.Companion.user
import com.arira.ngidol48.databinding.ActivityEventStreamBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config.extra_model
import com.arira.ngidol48.helper.SweetAlert
import com.arira.ngidol48.model.LiveChat
import com.arira.ngidol48.model.LiveStream
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.rommansabbir.animationx.AnimationX
import com.rommansabbir.animationx.Bounce


class EventStreamActivity : BaseActivity() {
    private lateinit var binding: ActivityEventStreamBinding
    private var stream = LiveStream()
    private var showComment = false
    private var roomChat:String = ""
    val roomLiveChatReference = FirebaseDatabase.getInstance("https://ngidol48-f4526-default-rtdb.asia-southeast1.firebasedatabase.app").reference
    private lateinit var viewModel: EventStreamViewModel

    private lateinit var chatDatabase: DatabaseReference

    private var listLiveChat:ArrayList<LiveChat> = ArrayList()
    private lateinit var adapterLiveChat:LiveChatAdapter

    private var exoPlayer: SimpleExoPlayer? = null
    private lateinit var countDownTimer:CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_stream)

        //firebase datbase
        chatDatabase = Firebase.database.reference

        stream = intent.getParcelableExtra(extra_model) ?: LiveStream()

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[EventStreamViewModel::class.java]
        viewModel.context = this

        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_stream)
        setToolbar(getString(R.string.teks_stream), binding.toolbar)


        timerToRefeshViewer()

        loadData()

        action()

        observeData()

        //live chat
        adapterLiveChat = LiveChatAdapter(listLiveChat)
        binding.rvKomentar.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterLiveChat
        }

        readLiveChatFromDatabase()

        AnimationX().setDuration(1000).setAnimation(Bounce.inUp(binding.linSatusKomentar, AnimationX().getNewAnimatorSet())).start()

//        binding.tvDisklaimer!!.animationXAttention(Attention.ATTENTION_SHAKE)
    }

    private fun timerToRefeshViewer(){
        countDownTimer = object : CountDownTimer(30 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                viewModel.views(stream.id)
            }
        }
    }

    /*watch data from server*/
    private fun observeData(){
        viewModel.getLoading().observe(this){
            if (it != null){
                binding.swipe.isRefreshing = it
            }
        }

        viewModel.getResponse().observe(this){
            if (it != null){
                stream = it.stream
                loadData()
                if (exoPlayer == null){
                    if (stream.live_status == "1"){
                        loadStream()
                    }
                }

            }
        }

        viewModel.getError().observe(this){
            if (it != null){
                SweetAlert.onFailure(this, it)
            }
        }

        viewModel.getViews().observe(this){
            if (it != null){
                if (!it.views.isNullOrBlank()){
                    val viewer = it.views.toIntOrNull()
                    if (viewer != null){
                        binding.linViews?.visibility = View.VISIBLE
                        binding.tvViews?.text = viewer.toString()
                    }else{
                        binding.linViews?.visibility = View.GONE
                    }

                }else{
                    binding.linViews?.visibility = View.GONE
                }

                countDownTimer.start()
            }
        }
    }

    /*send live chat to firebase database*/
    private fun sendChat(message:String){

        val chat = LiveChat()
        chat.roomId = roomChat
        chat.userName = user.fullname
        chat.userId = user.id
        chat.userAva = user.avatar

        chat.message = message
        chat.created_at = helper.getCurrentDateTime()
        val currentTimestamp = System.currentTimeMillis().toString()
        try {
            roomLiveChatReference.child(roomChat).child(currentTimestamp).setValue(chat)
            binding.edtKomentar.setText("")

            //disable edtKomentar
            binding.edtKomentar.isEnabled = false

            //nyalakan timer
            Handler(Looper.getMainLooper()).postDelayed({
                binding.edtKomentar.isEnabled = true
            }, 3000)

            //enable komentar

        } catch (e: DatabaseException){
            toast.show(getString(R.string.teks_gagal_mengirim_pesan), this)
        }
    }

    /*gathering action for view*/
    private fun action(){

        binding.ivKirim.setOnClickListener {
            val message = binding.edtKomentar.text.toString()
            if (message.isNotEmpty()){
                sendChat(message)
            }
        }

        binding.swipe.setOnRefreshListener {
            if (exoPlayer != null){
                releasePlayer()
            }

            viewModel.detail(stream.id)
        }

        binding.linSatusKomentar.setOnClickListener {
            showComment = !showComment

            if (showComment){
                binding.ivStatusKomentar.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
                binding.tvStatusKomentar.text = getString(R.string.teks_tutup_komentar)
                binding.linLiveChat.visibility = View.VISIBLE
                binding.linStreamInformation.visibility = View.GONE
                AnimationX().setDuration(1000).setAnimation(Bounce.inDown(binding.linSatusKomentar, AnimationX().getNewAnimatorSet())).start()
            }else{
                AnimationX().setDuration(1000).setAnimation(Bounce.inUp(binding.linSatusKomentar, AnimationX().getNewAnimatorSet())).start()

                binding.ivStatusKomentar.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24)
                binding.tvStatusKomentar.text = getString(R.string.teks_tampilkan_komentar)
                binding.linLiveChat.visibility = View.GONE
                binding.linStreamInformation.visibility = View.VISIBLE
            }
        }
    }

    /*load data set to view and properties*/
    private fun loadData(){
        roomChat = "room-${stream.id}"

        binding.tvJudul.text = stream.nama
        binding.tvDeskripsi.text = helper.fromHtml(stream.deskripsi)

        if (stream.live_status == "1"){
            binding.relStream?.visibility = View.VISIBLE
            binding.exoPlayer.visibility = View.VISIBLE
            binding.relWaiting.visibility = View.GONE
        }else{
            binding.relStream?.visibility = View.GONE
            val timeConverted = helper.convert(stream.start_date, "yyyy-MM-dd HH:mm:ss", "dd MMM yyyy HH:mm")
            binding.tvWaktuStream.text = getString(R.string.streaming_akan_dimulai_waiting_time, timeConverted)
            binding.exoPlayer.visibility = View.GONE
            binding.relWaiting.visibility = View.VISIBLE
            Glide.with(this).load(stream.banner).placeholder(R.drawable.img_logo).error(R.drawable.img_logo).into(binding.ivThumb)
        }
    }

    override fun onStart() {
        super.onStart()
        if (stream.live_status == "1"){
            loadStream()
        }

    }

    /*load streaming view*/
    private fun loadStream(){
        //initiate Player
        //Create a default TrackSelector
        val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory: TrackSelection.Factory =
            AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector: TrackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        //Create the player
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector)
        binding.exoPlayer.player = exoPlayer

        val rtmpDataSourceFactory = RtmpDataSourceFactory()
        // This is the MediaSource representing the media to be played.
        val videoSource: MediaSource = ExtractorMediaSource.Factory(rtmpDataSourceFactory)
            .createMediaSource(Uri.parse(stream.playback))

        // Prepare the player with the source.
        exoPlayer!!.prepare(videoSource)
        //auto start playing
        exoPlayer!!.playWhenReady = true



        //hit to update views
        viewModel.watch(stream.id)


        //hit to get views
        viewModel.views(stream.id)
    }

    /*read live chat database*/
    private fun readLiveChatFromDatabase(){
        val bookingDbListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(LiveChat::class.java)?.let {
                        listLiveChat.add(it)
                        adapterLiveChat.notifyDataSetChanged()

                        if (listLiveChat.size > 0){
                            binding.rvKomentar.scrollToPosition(listLiveChat.size - 1)
                        }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        }
        roomLiveChatReference.child(roomChat).addChildEventListener(bookingDbListener)
    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }

    override fun onBackPressed() {
        releasePlayer()
        super.onBackPressed()
    }

    private fun releasePlayer() {
        countDownTimer.cancel()

        if (exoPlayer != null) {
            viewModel.leave(stream.id)

            exoPlayer!!.release()
            exoPlayer = null
        }
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

}