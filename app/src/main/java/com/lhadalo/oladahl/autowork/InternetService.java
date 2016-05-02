package com.lhadalo.oladahl.autowork;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.lhadalo.oladahl.autowork.database.SQLiteDB;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.CountDownLatch;

import UserPackage.Company;
import UserPackage.Workpass;


/**
 * Created by Henrik on 2016-04-05.
 */
public class InternetService extends IntentService {

    public InternetService() {
        super("InternetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ServerCommunicator.newInstance(this);
    }

    private static class ServerCommunicator extends Thread {
        private SQLiteDB db;

        private Socket socket;
        private ObjectInputStream objectIn;
        private ObjectOutputStream objectOut;
        private Gson gson = new GsonBuilder().create();
        private Context context;

        public static ServerCommunicator newInstance(Context context) {
            try {
                return new ServerCommunicator(context);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        private ServerCommunicator(Context context) throws IOException {
            this.context = context;
            db = new SQLiteDB(context);

            socket = new Socket();
            socket.connect(new InetSocketAddress(Tag.IP, Tag.PORT), 4000);
            objectOut = new ObjectOutputStream(socket.getOutputStream());
            objectIn = new ObjectInputStream(socket.getInputStream());

            start();
        }

        @Override
        public void run() {
            try {
                List<Company> companiesToSync = db.getCompanysUnsynced();
                List<Workpass> workpassesToSync = db.getWorkpassesUnsynced();

                if (!companiesToSync.isEmpty()) {
                    //Synka Company
                    objectOut.writeObject(Tag.ON_CREATE_COMPANY);
                    objectOut.writeObject(String.valueOf(companiesToSync.size()));
                    for (int i = 0; i < companiesToSync.size(); i++) {
                        Company company = (Company)companiesToSync.get(i);
                        objectOut.writeObject(gson.toJson(company));

                        int companyServerId = Integer.parseInt((String)objectIn.readObject());
                        Log.v(Tag.LOGTAG, String.valueOf(companyServerId));

                        if (companyServerId >= 0) {
                            company.setServerID(companyServerId);
                            company.setIsSynced(Tag.IS_SYNCED);
                            company.setActionTag(Tag.ON_ITEM_IS_SYNCED);
                            db.changeCompany(company);
                        }
                    }

                    if(!workpassesToSync.isEmpty()) {
                        new ServerCommunicator(context);
                    }
                }
                else if (!workpassesToSync.isEmpty()) {
                    //Synka workpass
                    objectOut.writeObject(Tag.ON_CREATE_WORKPASS); //Detta spelar ingen roll, läser från modellen sedan.

                    objectOut.writeObject(String.valueOf(workpassesToSync.size()));

                    for (int i = 0; i < workpassesToSync.size(); i++) {
                        Workpass workpass = (Workpass)workpassesToSync.get(i);
                        objectOut.writeObject(gson.toJson(workpass));

                        //int serverId = Integer.parseInt((String)objectIn.readObject());
                        Workpass result = gson.fromJson((String)objectIn.readObject(), Workpass.class);
                        if (result != null) {
                            if (workpass.getActionTag().equals(Tag.ON_DELETE_WORKPASS)) {
                                db.deleteWorkpass(workpass);
                            }
                            else {
                                db.updateWorkpass(result);
                            }
                        }
                    }

                }

                objectIn.close();
                objectOut.close();
                socket.close();

            } catch (IOException | ClassNotFoundException | JsonSyntaxException | NumberFormatException e) {
                e.printStackTrace();
            }

            interrupt();
        }
    }

}



