package com.example.basicservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

/**
 * Created by Uddipta on 07/12/2021.
 */

public class BgService extends Service {
    private final IBinder mBinder = new MyBinder();
    //creating a MediaPlayer object
    private MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    public class MyBinder extends Binder {
        BgService getService() {
            return BgService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // getting systems default ringtone
        player = MediaPlayer.create(this,
                Settings.System.DEFAULT_RINGTONE_URI);
        Toast.makeText(this, "Ringtone playing", Toast.LENGTH_LONG).show();
        // setting loop play to true
        // this will make the ringtone continuously playing
        player.setLooping(true);

        // staring the player
        player.start();

        // we have some options for service
        // start sticky means service will be explicitly started and stopped
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Ringtone Stopped", Toast.LENGTH_LONG).show();
        // stopping the player when service is destroyed
        player.stop();
    }
}