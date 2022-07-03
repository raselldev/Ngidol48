package com.arira.ngidol48.ui.activity.pengaturan

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.arira.ngidol48.R
import com.arira.ngidol48.app.App.Companion.pref
import com.arira.ngidol48.databinding.ActivityPengaturanNotifikasiBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config.TOPIC_EVENT
import com.arira.ngidol48.helper.Config.TOPIC_HANDSHAKE
import com.arira.ngidol48.helper.Config.TOPIC_MNG
import com.arira.ngidol48.helper.Config.TOPIC_NEWS
import com.arira.ngidol48.helper.Config.TOPIC_SHOWROOM
import com.google.firebase.messaging.FirebaseMessaging

class PengaturanNotifikasiActivity : BaseActivity() {
    private lateinit var binding: ActivityPengaturanNotifikasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaturan_notifikasi)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_pengaturan_notifikasi)
        setToolbar(getString(R.string.teks_notifikasi), binding.toolbar)

        setData()
        action()
    }

    private fun action(){
        binding.switchShowroom.setOnClickListener {
            pref.setNotifShowroom(binding.switchShowroom.isChecked)
            if (!binding.switchShowroom.isChecked){
                FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC_SHOWROOM).addOnSuccessListener {
                }
            }
        }

        binding.switchNews.setOnClickListener {
            pref.setNotifNews(binding.switchNews.isChecked)
            if (!binding.switchNews.isChecked){
                FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC_NEWS).addOnSuccessListener {
                }
            }
        }

        binding.switchEvent.setOnClickListener {
            pref.setNotifEvent(binding.switchEvent.isChecked)
            if (!binding.switchEvent.isChecked){
                FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC_EVENT).addOnSuccessListener {
                }
            }
        }

        binding.switchHandshake.setOnClickListener {
            pref.setNotifHandshake(binding.switchHandshake.isChecked)
            if (!binding.switchHandshake.isChecked){
                FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC_HANDSHAKE).addOnSuccessListener {
                }
            }
        }

        binding.switchMng.setOnClickListener {
            pref.setNotifMng(binding.switchMng.isChecked)
            if (!binding.switchMng.isChecked){
                FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC_MNG).addOnSuccessListener {
                }
            }
        }
    }

    private fun setData(){
        binding.switchEvent.isChecked = pref.getNotifEvent()
        binding.switchHandshake.isChecked = pref.getNotifHandshake()
        binding.switchMng.isChecked = pref.getNotifMng()
        binding.switchNews.isChecked = pref.getNotifNews()
        binding.switchShowroom.isChecked = pref.getNotifShowroom()
    }
}