package com.arira.ngidol48.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.arira.ngidol48.databinding.ActivitySplashBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.model.NotificationData
import com.arira.ngidol48.ui.activity.event.EventActivity
import com.arira.ngidol48.ui.activity.handshake.HandshakeActivity
import com.arira.ngidol48.ui.activity.home.MainActivity
import com.arira.ngidol48.ui.activity.member.MemberActivity
import com.arira.ngidol48.ui.activity.mng.MngActivity
import com.arira.ngidol48.ui.activity.myWeb.MyWebActivity
import com.arira.ngidol48.ui.activity.news.BeritaActivity
import com.arira.ngidol48.utilities.Go
import com.google.gson.Gson
import com.rommansabbir.animationx.AnimationX
import com.rommansabbir.animationx.Bounce

class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemBars()

        supportActionBar?.hide()
        
        AnimationX().setDuration(3000).setAnimation(Bounce.inUp(binding.ivLogoNewEra, AnimationX().getNewAnimatorSet())).start()

        val notificationData = handleNotificationData()
        if (notificationData != null){
            when(notificationData.type){
                "EVENT"-> Go(this).move(EventActivity::class.java, clearPrevious = true)
                "NEWS"-> Go(this).move(BeritaActivity::class.java, clearPrevious = true)
                "MEMBER"-> Go(this).move(MemberActivity::class.java, clearPrevious = true)
                "SHOWROOM"-> Go(this).move(MemberActivity::class.java, clearPrevious = true)
                "HANDSHAKE"-> Go(this).move(HandshakeActivity::class.java, clearPrevious = true)
                "MNG"-> Go(this).move(MngActivity::class.java, clearPrevious = true)
                "TIKETCOM"-> {
                    Go(this).move(MyWebActivity::class.java, url = "https://www.tiket.com${notificationData.url}", clearPrevious = true)
                }
                "TOKOPEDIA"-> {
                    Go(this).move(MyWebActivity::class.java, url = notificationData.url, clearPrevious = true)
                }
                else -> {
                    Go(this).move(MainActivity::class.java, clearPrevious = true)
                }
            }
        }else{
            Handler(Looper.getMainLooper()).postDelayed({
                Go(this).move(MainActivity::class.java, clearPrevious = true)
            }, 3000)            
        }

    }

    fun handleNotificationData(): NotificationData? {
        val bundle = intent.extras

        if (bundle != null) {
            if (bundle.get("tipe") != null){
                val notificationModel = NotificationData()
                notificationModel.type = bundle.get("tipe").toString()
                notificationModel.url = bundle.get("url").toString()

                return notificationModel
            }else{
                bundle["data"].let {
                    return Gson().fromJson(it.toString(), NotificationData::class.java)
                }
            }
        }
        return null
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

}