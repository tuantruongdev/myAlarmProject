package com.example.myalarmproject;



import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;


public class alarmReceiver extends BroadcastReceiver {

    void displayNoti(){




    }

    @Override
    public void onReceive(Context context , Intent intent){
        Log.d("im in","in receiver");



        Bundle extras = intent.getExtras();
        Intent myIntent= new Intent(context,Music.class);



        myIntent.putExtras(extras);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(myIntent);
        } else {
            context.startService(myIntent);
        }





    }

}
