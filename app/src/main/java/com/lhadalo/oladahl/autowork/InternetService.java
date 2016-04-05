package com.lhadalo.oladahl.autowork;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.lhadalo.oladahl.autowork.activities.LoginActivity;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Henrik on 2016-04-05.
 */
public class InternetService extends Service {
    private Timer timer;
    private Context context = InternetService.this;
    
   
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        if (timer != null) {
            timer.cancel();
        } else {
            timer = new Timer();
        }
        timer.scheduleAtFixedRate(new Task(), 0, 60000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }


    class Task extends TimerTask {
        @Override
        public void run() {
            if(isConnected(context) == true){
                // TODO: 2016-04-05 Vad ska hända om applikationen har internetuppkoppling. 
            }else{
                // TODO: 2016-04-05 Vad ska hända om applikationen inte har internetuppkoppling. 
            }

        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }


}



