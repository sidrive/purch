package com.example.s_idrive.purch;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;
import android.os.Bundle;

import com.example.s_idrive.purch.Activity.MainActivity;
import com.example.s_idrive.purch.Activity.PoBaruActivity;

/**
 * Created by IT Server on 5/20/2017.
 */

public class CheckingUpdate extends Thread {
    public void run() {

        while (true) {
            try {
                Notify("You've received new message", "message");

                //pause thread every 10 seconds
                Thread.sleep(100000);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
    }

    public void Notify(String notificationTitle, String notificationMessage){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_stat_pesan_notif);
        builder.setContentTitle(notificationTitle);//"Notifications Example")
        builder.setContentText(notificationMessage);//"This is a test notification");
        builder.setTicker("New Message Alert!");
        builder.setSmallIcon(R.drawable.ic_stat_pesan_notif);

        Intent notificationIntent = new Intent(MainActivity.this, PoBaruActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }

}
