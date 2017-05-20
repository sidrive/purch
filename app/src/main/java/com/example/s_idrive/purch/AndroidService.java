package com.example.s_idrive.purch;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class AndroidService extends Service {
    Thread t = new CheckingUpdate();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // starting the thread
        t.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!t.isAlive())
        {
            t.start();
        }

        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return Service.START_REDELIVER_INTENT;
    }
}
