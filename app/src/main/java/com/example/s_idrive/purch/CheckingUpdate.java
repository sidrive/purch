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


                //pause thread every 10 seconds
                Thread.sleep(100000);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
    }



}
