package Networking;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;


import com.lhadalo.oladahl.autowork.LoginActivity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Henrik on 2016-03-10.
 */
public class CreateUser extends AsyncTask<HashMap<String, String>, Void, String> {
    private static final int port = 40001;
    private Thread thread;
    private String response;
    private HashMap<String, String> map;
    private static final String ip = "192.168.1.7";
    private static final String tag = "Create User";
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;


    protected void onPreExecute (){

    }

    @Override
    protected String doInBackground(HashMap<String, String>... params) {
        try{
            Log.d("Tråden skapades", "nummer 1");
            String response;
            Socket socket = new Socket(ip, port);
            Log.d("ansluten till socket", "nummer 2");
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(tag);
            Log.d("Första meddelandet", "nummer 3");
            response = objectInputStream.readObject().toString();

        }catch (Exception e){

        }
        return null;
    }
    protected void onPostExecute (String result){

    }
}
