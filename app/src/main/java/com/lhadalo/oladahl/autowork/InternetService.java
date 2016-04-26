package com.lhadalo.oladahl.autowork;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lhadalo.oladahl.autowork.database.SQLiteDB;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import UserPackage.Workpass;


/**
 * Created by Henrik on 2016-04-05.
 */
public class InternetService extends Service {
    private Timer timer;
    private Context context = InternetService.this;
    private SQLiteDB db = new SQLiteDB(InternetService.this);
    private List<Workpass> workpasses;
    private Workpass workpass;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        if (timer != null) {
            timer.cancel();
        }
        else {
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
                workpasses = db.getWorkpassesUnsynced();

                List<Workpass> createList = new ArrayList<>(10);
                if (!workpasses.isEmpty()) {

                    for (Workpass workpass : workpasses) {
                        if (workpass.getActionTag().equals(Tag.ON_CREATE_WORKPASS)) {
                            createList.add(workpass);
                        }
                    }
                    if (!workpasses.isEmpty()) {
                        workpass = new Workpass();
                        workpass = createList.get(0);
                        new InternetConnection(workpass);
                    }
                }
                else {
                    Log.v(Tag.LOGTAG, "Ingenting i listan");
                }
            }
            else {
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
        Socket socket;
        private ObjectInputStream objectIn;
        private ObjectOutputStream objectOut;
        private Workpass workpass;

        public InternetConnection(Workpass workpass){
            this.workpass = workpass;
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(Tag.IP, Tag.PORT), 4000);
                objectOut = new ObjectOutputStream(socket.getOutputStream());

            }catch (Exception e){

            }
            start();
        }


        @Override
        public void run() {
        try{
            objectOut.writeObject(Tag.ON_CREATE_WORKPASS);
            objectOut.writeObject(workpass);
            objectIn = new ObjectInputStream(socket.getInputStream());
            String serverId =(String) objectIn.readObject();
            workpass.setServerID(Integer.parseInt(serverId));
            workpass.setIsSynced(1);
            workpass.setActionTag(null);
            db.updateWorkpass(workpass);
            } catch (Exception e) {

            e.printStackTrace();
            }




        }


}



}



