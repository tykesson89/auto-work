package com.lhadalo.oladahl.autowork;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.lhadalo.oladahl.autowork.database.SQLiteDB;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
            if (isConnected(context) == true) {


            } else {
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

    class InternetConnection extends Thread {
        private Socket socket;
        private ObjectInputStream objectIn;
        private ObjectOutputStream objectOut;

        
        public InternetConnection(){
            Thread thread = new Thread();
            try {
                socket = new Socket(Tag.IP, Tag.PORT);
                objectOut = new ObjectOutputStream(socket.getOutputStream());
                objectIn = new ObjectInputStream(socket.getInputStream());
            }catch(Exception e){
                
            }
            thread.start();
        }
        
        @Override
        public void run() {
            SQLiteDB sqLiteDB = new SQLiteDB(context);


            // TODO: 2016-04-05 Lägga till vad som ska hända i tråden.  
            
        }
    }


}



