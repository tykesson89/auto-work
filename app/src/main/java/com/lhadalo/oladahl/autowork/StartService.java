package com.lhadalo.oladahl.autowork;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by oladahl on 16-04-30.
 */
public class StartService {

    public static StartService startService(Context context){
        return new StartService(context);
    }

    private StartService(Context context){
        if(isConnected(context)){
            Intent serviceIntent = new Intent(context, InternetService.class);
            context.startService(serviceIntent);
        }
    }

    private static boolean isConnected(Context context) {
        ConnectivityManager connectivity =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
