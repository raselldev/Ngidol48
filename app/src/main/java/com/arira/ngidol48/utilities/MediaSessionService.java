package com.arira.ngidol48.utilities;

import static com.arira.ngidol48.helper.Config.BASE_STORAGE;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.KeyEvent;

import androidx.annotation.Nullable;

import com.arira.ngidol48.R;
import com.arira.ngidol48.model.Song;
import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutionException;

public class MediaSessionService extends Service {
    public MediaPlayer mediaPlayer;
    public static final String TAG = "MediaSessionService";
    public static final int NOTIFICATION_ID = 888;
    private MediaNotificationManager mMediaNotificationManager;
    private MediaSessionCompat mediaSession;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mMediaNotificationManager = new MediaNotificationManager(this);
        mediaSession = new MediaSessionCompat(this, "SOME_TAG");
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                mediaPlayer.start();
            }

            @Override
            public void onPause() {
                mediaPlayer.pause();
            }
        });
        Notification notification = mMediaNotificationManager.getNotification(getMetadata(), getState(), mediaSession.getSessionToken());
        startForeground(NOTIFICATION_ID, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public MediaMetadataCompat getMetadata() {
        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
        Song lagu = new SharedPref(getApplicationContext()).getSong();


        Bitmap mIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.img_jkt48);

        builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, mIcon);
        builder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "JKT48");
        builder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, "JKT48 - " + lagu.getJudul());
        builder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, lagu.getNama());
        return builder.build();
    }

    private PlaybackStateCompat getState() {
        long actions = mediaPlayer.isPlaying() ? PlaybackStateCompat.ACTION_PAUSE : PlaybackStateCompat.ACTION_PLAY;
        int state = mediaPlayer.isPlaying() ? PlaybackStateCompat.STATE_PLAYING : PlaybackStateCompat.STATE_PAUSED;

        final PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder();
        stateBuilder.setActions(actions);
        stateBuilder.setState(state,
                mediaPlayer.getCurrentPosition(),
                1.0f,
                SystemClock.elapsedRealtime());
        return stateBuilder.build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if ("android.intent.action.MEDIA_BUTTON".equals(intent.getAction())) {
            KeyEvent keyEvent = (KeyEvent) intent.getExtras().get("android.intent.extra.KEY_EVENT");
            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PAUSE) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

}