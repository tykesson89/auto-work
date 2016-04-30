package com.lhadalo.oladahl.autowork;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonWriter;
import com.lhadalo.oladahl.autowork.database.SQLiteDB;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import UserPackage.Company;
import UserPackage.Workpass;


/**
 * Created by Henrik on 2016-04-05.
 */
public class InternetService extends IntentService {
    public boolean companyNotSynced = false;
    private Timer timer;
    private Context context = InternetService.this;
    private SQLiteDB db = new SQLiteDB(InternetService.this);
    private Workpass workpass;

    public InternetService() {
        super("InternetService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Starter.newInstance(this);
    }


    private static class Starter extends Thread {
        private SQLiteDB db;
        private Globals globals;

        public static Starter newInstance(Context context) {
            return new Starter(context);
        }

        public Starter(Context context) {
            db = new SQLiteDB(context);

            start();
        }

        @Override
        public void run() {
            List<Object> companiesToSync = new ArrayList<Object>(db.getCompanysUnsynced());
            List<Object> workpassesToSync = new ArrayList<Object>(db.getWorkpassesUnsynced());

            if (!companiesToSync.isEmpty()) {
                globals.setCompanyIsSynced(false);

                Communicator.newInstance(db, companiesToSync, this);

                try {
                    synchronized (this) {
                        this.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else if(!workpassesToSync.isEmpty()){
                Communicator.newInstance(db, workpassesToSync, this);
            }

            interrupt();
        }
    }

    private static class Communicator extends Thread {
        private SQLiteDB db;
        private List<Object> dataToSync;
        private final Starter starter;
        Socket socket;
        private ObjectInputStream objectIn;
        private ObjectOutputStream objectOut;
        Gson gson = new GsonBuilder().create();

        public static Communicator newInstance(SQLiteDB db, List<Object> dataToSync, Starter starter) {
            return new Communicator(db, dataToSync, starter);
        }

        private Communicator(SQLiteDB db, List<Object> dataToSync, Starter starter) {
            this.db = db;
            this.dataToSync = dataToSync;
            this.starter = starter;
            socket = new Socket();
            try {
                socket.connect(new InetSocketAddress(Tag.IP, Tag.PORT), 4000);
                objectOut = new ObjectOutputStream(socket.getOutputStream());
                objectIn = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            start();
        }

        @Override
        public void run() {

            try {
                if (dataToSync.get(0) instanceof Company) {
                    Log.v(Tag.LOGTAG, "Company");
                    synchronized (starter) {
                        //Synka company

                        starter.notify(); //Notifiera att synkningen är färdig
                    }
                }
                else {
                    //Synka workpass
                    objectOut.writeObject(Tag.ON_CREATE_WORKPASS); //Detta spelar ingen roll, läser från modellen sedan.
                    objectOut.writeObject(String.valueOf(dataToSync.size()));

                    for (int i = 0; i < dataToSync.size(); i++) {
                        Workpass w = (Workpass)dataToSync.get(i);
                        objectOut.writeObject(gson.toJson(w));

                        try {
                            Workpass wServer = gson.fromJson((String)objectIn.readObject(), Workpass.class);
                            db.updateWorkpass(wServer);
                        } catch (JsonSyntaxException e){
                            e.printStackTrace();
                            Log.v(Tag.LOGTAG, "Något fel på JSON-syntaxen");
                        }
                    }

                    objectIn.close();
                    objectOut.close();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            interrupt();
        }
    }

    private void writeToServer(OutputStream outputStream, int source) throws IOException {

        if (source == 1) {

        }
        else {

        }


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
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
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

            } catch (SocketTimeoutException e) {
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

                    String serverId = (String)objectIn.readObject();

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
            } catch (SocketTimeoutException e) {
                interrupt();
            } catch (Exception e) {

                e.printStackTrace();
            }
        }

    }

    public void stopThisService() {
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}



