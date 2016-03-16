package Networking;


import android.content.Context;
import android.content.Intent;


import com.lhadalo.oladahl.autowork.LoginActivity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Henrik on 2016-03-10.
 */
public class CreateUser extends Thread {
    private static final int port = 40001;
    private Thread thread;
    private Context context;
    private HashMap<String, String> map;
    private static final String ip = "10.2.5.71";
    private static final String tag = "Create User";
    ObjectOutputStream oos;
    ObjectInputStream ois;
    public CreateUser(HashMap<String, String> map, Context context){
        start();
        this.map = map;
        this.context = context;
    }
    public void run() {
        try {

            Socket socket = new Socket(ip, port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            while(!Thread.interrupted()){
            oos.writeObject(tag);
            oos.writeObject(map);

                Intent intent = new Intent(this.context, LoginActivity.class);
                context.startActivity(intent);

            }




        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}
