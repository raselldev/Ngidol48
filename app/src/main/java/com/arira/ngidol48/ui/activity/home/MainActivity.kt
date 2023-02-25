package com.arira.ngidol48.ui.activity.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.*
import com.arira.ngidol48.app.App.Companion.helper
import com.arira.ngidol48.app.App.Companion.pref
import com.arira.ngidol48.databinding.ActivityMainBinding
import com.arira.ngidol48.databinding.CustomDialogRatingAppBinding
import com.arira.ngidol48.databinding.DialogBdayBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config.TOPIC_EVENT
import com.arira.ngidol48.helper.Config.TOPIC_HANDSHAKE
import com.arira.ngidol48.helper.Config.TOPIC_MNG
import com.arira.ngidol48.helper.Config.TOPIC_NEWS
import com.arira.ngidol48.helper.Config.TOPIC_SHOWROOM
import com.arira.ngidol48.helper.Config.TOPIC_STREAMING
import com.arira.ngidol48.model.Member
import com.arira.ngidol48.ui.activity.login.LoginActivity
import com.arira.ngidol48.ui.activity.member.MemberCallback
import com.arira.ngidol48.ui.activity.notifikasi.NotifikasiActivity
import com.arira.ngidol48.ui.activity.pengaturan.PengaturanActivity
import com.arira.ngidol48.ui.fragment.*
import com.arira.ngidol48.ui.fragment.home.HomeFragment
import com.arira.ngidol48.utilities.Go
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*


class MainActivity : BaseActivity(), MemberCallback {
    
    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var reviewManager: ReviewManager
    private lateinit var menu: Menu
    private lateinit var menuItem: MenuItem


    private var isShowDB:Boolean = false
    private var selectedBdMember:Member = Member()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        statusPutih()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[HomeViewModel::class.java]
        viewModel.context = this

        bottomNav()

        try {
            FirebaseMessaging.getInstance().token.addOnSuccessListener {
                if (!it.isNullOrBlank()){
                    pref.setFCMToken(it)
                }
            }
        } catch (e: RuntimeException) {

        }

        observerData()
        viewModel.home(pref.getFCMToken())

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

    private fun callFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }

    private fun bottomNav() {

        val navView: BottomNavigationView = findViewById(R.id.main_nav)
        menu = navView.menu

        supportFragmentManager.beginTransaction().replace(
            R.id.main_container,
            HomeFragment()
        ).commit()

        menuItem = menu.getItem(0)
        menuItem.isChecked = true

        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_beranda -> {
                    menuItem = menu.getItem(0)
                    menuItem.isChecked = true
                    callFragment(HomeFragment())
                }

                R.id.navigation_news -> {
                    menuItem = menu.getItem(1)
                    menuItem.isChecked = true
                    callFragment(InformasiFragment())

                }

                R.id.navigation_blog -> {
                    menuItem = menu.getItem(2)
                    menuItem.isChecked = true
                    callFragment(JadwalFragment())
                }

                R.id.navigation_member -> {
                    menuItem = menu.getItem(3)
                    menuItem.isChecked = true
                    callFragment(MemberFragment())
                }

                R.id.navigation_profil -> {
                    menuItem = menu.getItem(4)
                    menuItem.isChecked = true
                    if (pref.getIsLogin()){
                        callFragment(ProfilFragment())
                    }else{
                        Go(this).move(LoginActivity::class.java)
                    }

                }
            }
            return@setOnNavigationItemSelectedListener true
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


        binding.ivSetting.setOnClickListener {
            Go(this).move(PengaturanActivity::class.java)
        }

        binding.relNotifikasi.setOnClickListener {
            Go(this).move(NotifikasiActivity::class.java)
        }
    }


    /*subcribe notifikasi*/
    private  fun subcribeAll(){

        if (pref.getNotifNews()){
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_NEWS).addOnSuccessListener {
            }
        }

//        if (pref.getNotifNews()){
//            FirebaseMessaging.getInstance().subscribeToTopic("testtopic").addOnSuccessListener {
//            }
//        }

        if (pref.getNotifMng()){
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_MNG).addOnSuccessListener {
            }
        }

        if (pref.getNotifStreaming()){
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_STREAMING).addOnSuccessListener {
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
        viewModel.getLoading().observe(this) {
            it.let {
            }

        }

        viewModel.getError().observe(this) {
            it.let {
            }
        }

        viewModel.getResponse().observe(this) {
            it.let {
                if (it != null) {
                    /*show size notificaiton todae*/
                    if (it.notifikasi_hari_ini > 0) {
                        binding.divJmlNotif.visibility = View.VISIBLE
                        binding.tvJmlNotif.text = it.notifikasi_hari_ini.toString()
                    } else {
                        binding.divJmlNotif.visibility = View.GONE
                    }

                    /*birthday member*/
                    if (it.bday_member.isNotEmpty()) {
                        val bdayToday = findTodayBday(it.bday_member)
                        if (bdayToday.isNotEmpty()) {
                            if (!isShowDB){
                                showBdayMember(bdayToday)
                                isShowDB = true
                            }


                            selectedBdMember = bdayToday[0]
                        }


                    }

                }
            }
        }
    }

}