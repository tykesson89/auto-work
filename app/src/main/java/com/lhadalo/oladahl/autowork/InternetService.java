package com.lhadalo.oladahl.autowork;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lhadalo.oladahl.autowork.database.SQLiteDB;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import UserPackage.Company;
import UserPackage.Workpass;


/**
 * Created by Henrik on 2016-04-05.
 */
public class InternetService extends Service {

    private Timer timer;
    private Context context = InternetService.this;
    private SQLiteDB db = new SQLiteDB(InternetService.this);
    private Workpass workpass;
    private List<Workpass> createList, changeList;

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
        timer.scheduleAtFixedRate(new Task(), 0, 40000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    class Task extends TimerTask {
        @Override
        public void run() {
            if (isConnected(context) == true) {
                //List<Company> companies = db.getCompanysUnsynced();
                List<Workpass> workpasses = db.getWorkpassesUnsynced();

                if (!workpasses.isEmpty()) {
                    new InternetConnection(workpasses);
                }
                else {
                    Log.v(Tag.LOGTAG, "Inget mer i listan");
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
        private List<Workpass> workpasses;
        List<Company> companies;

        public InternetConnection(List<Workpass> workpasses) {
            this.workpasses = workpasses;

            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(Tag.IP, Tag.PORT), 4000);
                objectOut = new ObjectOutputStream(socket.getOutputStream());
                objectIn = new ObjectInputStream(socket.getInputStream());

            } catch (SocketTimeoutException e){
                interrupt();
            } catch (Exception e) {

            }
            start();
        }

        @Override
        public void run() {
            try {
                objectOut.writeObject(Tag.ON_CREATE_WORKPASS);
                objectOut.writeObject(String.valueOf(workpasses.size()));


                for (Workpass pass : workpasses) {
                    Log.v(Tag.LOGTAG, String.valueOf(pass.getServerID()));
                    objectOut.writeObject(pass);

                    String serverId = (String) objectIn.readObject();

                    if (pass.getActionTag().equals(Tag.ON_DELETE_WORKPASS)) {
                        db.deleteWorkpass(Long.parseLong(serverId));
                    }
                    else {
                        pass.setServerID(Integer.parseInt(serverId));
                        pass.setIsSynced(1);
                        pass.setActionTag(Tag.ON_WORKPASS_IS_SYNCED);
                        db.updateWorkpass(pass);
                    }
                }

                socket.close();

                interrupt();
                stopThisService();
            } catch (SocketTimeoutException e){
                interrupt();
            } catch (Exception e) {

                e.printStackTrace();
            }
        }

    }

    public void stopThisService(){
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}



