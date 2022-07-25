package com.arira.ngidol48.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.arira.ngidol48.databinding.ActivitySplashBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.ui.activity.home.MainActivity
import com.arira.ngidol48.utilities.Go
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

//        AnimationX().setDuration(2000)
//            .setAnimation(Attention.ruberBand(binding.ivLogo, AnimationX().getNewAnimatorSet())).start()

        AnimationX().setDuration(3000).setAnimation(Bounce.inUp(binding.ivLogoNewEra, AnimationX().getNewAnimatorSet())).start()

        Handler(Looper.getMainLooper()).postDelayed({
            Go(this).move(MainActivity::class.java, clearPrevious = true)
        }, 3000)
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