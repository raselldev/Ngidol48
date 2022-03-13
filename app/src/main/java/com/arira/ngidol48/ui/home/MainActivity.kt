package com.arira.ngidol48.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
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
import com.arira.ngidol48.databinding.ActivityMainBinding
import com.arira.ngidol48.databinding.CustomDialogRatingAppBinding
import com.arira.ngidol48.databinding.DialogBdayBinding
import com.arira.ngidol48.databinding.SheetDetailMemberBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config
import com.arira.ngidol48.helper.Config.BASE_STORAGE
import com.arira.ngidol48.helper.Config.BASE_STORAGE_IMAGE
import com.arira.ngidol48.helper.Config.TOPIC_EVENT
import com.arira.ngidol48.helper.Config.TOPIC_HANDSHAKE
import com.arira.ngidol48.helper.Config.TOPIC_NEWS
import com.arira.ngidol48.helper.Config.TOPIC_SHOWROOM
import com.arira.ngidol48.helper.SweetAlert
import com.arira.ngidol48.model.Member
import com.arira.ngidol48.model.Slider
import com.arira.ngidol48.ui.event.EventActivity
import com.arira.ngidol48.ui.handshake.HandshakeActivity
import com.arira.ngidol48.ui.login.LoginActivity
import com.arira.ngidol48.ui.member.MemberActivity
import com.arira.ngidol48.ui.member.MemberCallback
import com.arira.ngidol48.ui.news.BeritaActivity
import com.arira.ngidol48.ui.notifikasi.NotifikasiActivity
import com.arira.ngidol48.ui.pengaturan.PengaturanActivity
import com.arira.ngidol48.ui.profil.ProfilActivity
import com.arira.ngidol48.ui.setlist.SetlistActivity
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity(), MemberCallback {
    
    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var reviewManager: ReviewManager

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

        bindingDialog.ivClose.setOnClickListener {
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

        binding.linUser.setOnClickListener {
            Go(this).move(ProfilActivity::class.java)
        }

        binding.linBlog.setOnClickListener {
            Go(this).move(LoginActivity::class.java)
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

        binding.linSetlist.setOnClickListener {
            Go(this).move(SetlistActivity::class.java)
        }

        binding.linKalender.setOnClickListener {
            Go(this).move(EventActivity::class.java)
        }

        binding.ivSemuaBerita.setOnClickListener {
            Go(this).move(BeritaActivity::class.java)
        }

        binding.linAllNews.setOnClickListener {
            Go(this).move(BeritaActivity::class.java)
        }
    }

    private  fun subcribeAll(){
        if (pref.getNotifNews()){
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_NEWS).addOnSuccessListener {
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
                    setSlider(it.slider)

                    binding.rvBerita.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = BeritaAdapter(it.news)
                    }

                    if (it.notifikasi_hari_ini > 0){
                        binding.divJmlNotif.visibility = View.VISIBLE
                        binding.tvJmlNotif.text = it.notifikasi_hari_ini.toString()
                    }else{
                        binding.divJmlNotif.visibility = View.GONE
                    }

                    if (it.bday_member.isNotEmpty()){
                        binding.linBday.visibility = View.VISIBLE
                        binding.rvBday.apply {
                            layoutManager = GridLayoutManager(context, 3)
                            adapter = MemberAdapter(it.bday_member, this@MainActivity)
                        }

                        val bdayToday = findTodayBday(it.bday_member)
                        if (bdayToday.isNotEmpty()){
                            showBdayMember(bdayToday)
                        }

                    }else{
                        binding.linBday.visibility = View.GONE
                    }

                    if(it.event.isEmpty()){
                        binding.linViewKosongEvent.visibility = View.VISIBLE
                    }else{
                        binding.linViewKosongEvent.visibility = View.GONE
                        binding.rvEvent.apply {
                            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                            adapter = EventHomeAdapter(it.event)
                        }
                    }

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
            val timerSlider: Timer
            val DELAY_MS: Long = 5000
            val PERIOD_MS: Long = 10000

            /*After setting the adapter use the timer */

            /*After setting the adapter use the timer */
            val handler = Handler(Looper.myLooper()!!)
            val Update = Runnable {
                if (currentPage == adapter.count) {
                    currentPage = 0
                }
                binding.slider.setCurrentItem(currentPage++, true)
            }

            timerSlider = Timer() // This will create a new Thread

            timerSlider.schedule(object : TimerTask() {
                // task to be scheduled
                override fun run() {
                    handler.post(Update)
                }
            }, DELAY_MS, PERIOD_MS)
            /*selesai auto sliding*/

        }
    }
}