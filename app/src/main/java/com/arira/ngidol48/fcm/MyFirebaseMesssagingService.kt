package com.arira.ngidol48.fcm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.arira.ngidol48.R
import com.arira.ngidol48.helper.Config
import com.arira.ngidol48.ui.activity.home.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONException
import java.net.HttpURLConnection
import java.net.URL

class MyFirebaseMesssagingService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        /*MENGAMBIL DATA PAYLOAD NOTIFIKASI UNTUK DI SEBAR KESEMUA ACTIVITY*/
        try {

            val pushNotification = Intent(Config.PUSH_NOTIFICATION)
            pushNotification.putExtra("payload", p0.data.get("payload"))
            pushNotification.putExtra("type", p0.data.get("type"))
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)

        } catch (e: JSONException) {

        }


        /*MEMANNGGIL SEND NOTIFICATION UNTUK MENAMPILKAN NOTIFIKASI*/
        p0.notification?.let {
            try {
                sendNotification(
                    p0.notification?.body!!,
                    p0.data.get("type"),
                    p0.notification?.imageUrl)
            } catch (e: KotlinNullPointerException) {

            }
        }


    }


    /*@Param messageBody  = teks yang di tampilkan di dalam pesan notifikasi*/
    @SuppressLint("ServiceCast")
    private fun sendNotification(messageBody: String, type: String?, imageUrl: Uri?) {

        var go = Intent(this, MainActivity::class.java)

//        val pendingIntent = PendingIntent.getActivity(
//            this, 0 /* Request code */, go,
//            PendingIntent.FLAG_ONE_SHOT
//        )

        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                this,
                0,
                go,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this,
                0, go, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.img_jkt48)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val bitmapImage = getBitmapfromUrl(imageUrl)
        if (bitmapImage != null){
//            notificationBuilder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmapImage))
            notificationBuilder.setLargeIcon(bitmapImage)
        }


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
            .build()
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.enableVibration(true)
            channel.importance = NotificationManager.IMPORTANCE_HIGH
            channel.setSound(defaultSoundUri, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    private fun getBitmapfromUrl(imageUrl: Uri?): Bitmap? {
        return try {
            val url = URL(imageUrl?.toString())
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            null
        }
    }


}