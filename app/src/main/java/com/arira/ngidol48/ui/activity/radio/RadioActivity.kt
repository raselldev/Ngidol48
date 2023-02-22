package com.arira.ngidol48.ui.activity.radio

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.PersistableBundle
import android.view.View
import com.arira.ngidol48.app.App.Companion.pref
import com.arira.ngidol48.databinding.ActivityRadioBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Validasi
import com.arira.ngidol48.service.MediaPlayerService


class RadioActivity : BaseActivity() {

    val Broadcast_PLAY_NEW_AUDIO = "com.arira.ngidol48.PlayNewAudio"

    private var player: MediaPlayerService? = null
    var serviceBound = false

    private lateinit var binding : ActivityRadioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRadioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar("Radio", binding.toolbar)
        Validasi().ijinReadPhone(this)
        binding.toolbar.toolbarSubtitle.visibility = View.VISIBLE
        binding.toolbar.toolbarSubtitle.text = "[BETA]"
        pref.setRadioURL("http://117.53.144.184:8001/stream")
        action()


    }

    private fun action(){
        binding.ivBtnPlay.setOnClickListener {
            if ( Validasi().ijinReadPhone(this)){
                playAudio()
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putBoolean("serviceStatus", serviceBound)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        serviceBound = savedInstanceState.getBoolean("serviceStatus")
    }

    //Binding this Client to the AudioPlayer Service
    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder: MediaPlayerService.LocalBinder = service as MediaPlayerService.LocalBinder
            player = binder.service
            serviceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            serviceBound = false
        }
    }


    private fun playAudio() {
        //Check is service is active
        if (!serviceBound) {
            //Store Serializable audioList to SharedPreferences
            val playerIntent = Intent(this, MediaPlayerService::class.java)
            startService(playerIntent)
            bindService(playerIntent, serviceConnection, BIND_AUTO_CREATE)
        } else {

            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            val broadcastIntent = Intent(Broadcast_PLAY_NEW_AUDIO)
            sendBroadcast(broadcastIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (serviceBound) {
            unbindService(serviceConnection)
            //service is active
            player!!.stopSelf()
        }
    }

}