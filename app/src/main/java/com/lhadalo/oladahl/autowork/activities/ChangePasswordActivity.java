package com.lhadalo.oladahl.autowork.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.database.SQLiteDB;
import com.lhadalo.oladahl.autowork.Tag;
import com.lhadalo.oladahl.autowork.fragments.ChangePasswordFragment;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import UserPackage.User;


public class ChangePasswordActivity extends AppCompatActivity implements ChangePasswordFragment.OnFragmentInteraction {
    private ChangePasswordFragment fragment;
    private User user;
    private int userId;
    private Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SQLiteDB sqLiteDB = new SQLiteDB(this);
        user = sqLiteDB.getUser();
        userId = user.getUserid();
    }

    private void initFragment() {
        fragment = new ChangePasswordFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_Change_password, fragment).commit();
    }

    public boolean hasSpaceBefore(String str) {
        return Character.isWhitespace(str.charAt(0));
    }

    public String removeSpaceBefore(String str) {
        String res = str;
        for (int i = 0; i < str.length() && Character.isWhitespace(res.charAt(0)); i++) {
            res = res.substring(1);
        }

        return res;
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


    @Override
    public void onClickBtnChangePassword(String newPassword, String newPasswordConfirmation, String oldPassword) {
        boolean inputOk = true;
        if (!newPassword.equals(newPasswordConfirmation)) {
            fragment.setNewPasswordError(true, "Password does not match");
            fragment.setNewPasswordConfirmationError(true, "Password does not match");
            inputOk = false;
        }

        if (newPassword.length() < 6) {
            fragment.setNewPasswordError(true, "Password must be 6 characters");
            inputOk = false;
        } else {
            fragment.setNewPasswordError(false, null);
        }
        if (newPasswordConfirmation.length() < 6) {
            fragment.setNewPasswordConfirmationError(true, "Password must be 6 characters");
            inputOk = false;
        } else {
            fragment.setNewPasswordConfirmationError(false, null);
        }
        if (oldPassword.length() < 6) {
            fragment.setOldPasswordError(true, "Password must be 6 characters");
            inputOk = false;
        } else {
            fragment.setOldPasswordError(false, null);
        }

        if (inputOk) {
            user.setNewPassword(newPassword);
            user.setOldPassword(oldPassword);
            user.setUserid(userId);
            new ChangePassword().execute(user);
        }




    }


    class ChangePassword extends AsyncTask<User, Void, String> {
        private ObjectInputStream objectInputStream;
        private ObjectOutputStream objectOutputStream;


        protected String doInBackground(User... params) {
            if(isConnected(context)==true) {
                User user = params[0];
                try {
                    try {
                        Socket socket = new Socket();
                        socket.connect(new InetSocketAddress(Tag.IP, Tag.PORT), 4000);
                        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        objectInputStream = new ObjectInputStream(socket.getInputStream());
                        objectOutputStream.writeObject("Change Password");
                        objectOutputStream.writeObject(user);

                        String response = (String) objectInputStream.readObject();
                        return response;
                    } catch (SocketTimeoutException e) {
                        return "Server is offline";
                    }

                } catch (Exception e) {
                    return "Server is offline";
                }
            }else{
                return "No Internet Connection";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("Password is incorrect")){
                fragment.setOldPasswordError(true, "Password is incorrect");
            }else if(s.equals("Server is offline")){
                Toast.makeText(context, "Server is offline",
                        Toast.LENGTH_SHORT).show();
            }else if(s.equals("No Internet Connection")){
                Toast.makeText(context, "You have no Internet Connection",
                        Toast.LENGTH_SHORT).show();
            }else if(s.equals("Something went wrong")){
                Toast.makeText(context, "Something went wrong",
                        Toast.LENGTH_SHORT).show();
            }else if(s.equals("Success")){
                Toast.makeText(context, "Password Changed",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangePasswordActivity.this, SettingsActivity.class);
                startActivity(intent);
            }


        }
    }
}

