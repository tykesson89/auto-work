package com.lhadalo.oladahl.autowork.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import UserPackage.User;

import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.SQLiteDB;
import com.lhadalo.oladahl.autowork.Tag;
import com.lhadalo.oladahl.autowork.fragments.SettingsFragment;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SettingsActivity extends AppCompatActivity implements SettingsFragment.OnFragmentInteraction {
    private SettingsFragment fragment;
    private User user;
    private int userId;
    private String firstName;
    private String lastName;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initFragment();
        SQLiteDB sqLiteDB = new SQLiteDB(this);
        user = sqLiteDB.getUser();
        firstName = user.getFirstname();
        lastName = user.getLastname();
        email = user.getEmail();
    }

    private void initFragment() {
        fragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_settings, fragment).commit();
    }
    protected void onStart() {
        super.onStart();
        fragment.setTextFirstName(firstName);
        fragment.setTextLastName(lastName);
        fragment.setTextEmail(email);

    }

    @Override
    public void onClickBtnChangeUserInfo(String firstName, String lastName, String email,
                                         String oldPassword, String newPassword, String newPasswordCheck) {
        User user = new User(firstName, lastName, email, newPassword, userId, oldPassword);
        new ChangeUserInfo().execute(user);
    }

    @Override
    public void onClickDeleteAccount() {


        new DeleteUser().execute(user);
    }


    class DeleteUser extends AsyncTask<User, Void, String> {
        private static final int port = 45001;
        private static final String ip = "85.235.21.222";
        private ObjectInputStream objectInputStream;
        private ObjectOutputStream objectOutputStream;
        private ProgressDialog progressDialog;

        protected String doInBackground(User... params) {
            User user = params[0];
            try {
                Socket socket = new Socket(ip, port);
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                objectOutputStream.writeObject(Tag.DELETE_USER);
                objectOutputStream.writeObject(user);

                String response =(String) objectInputStream.readObject();
                return response;

            }catch (Exception e){

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
           if(s.equals("User deleted")){
               SQLiteDB sqLiteDB = new SQLiteDB(SettingsActivity.this);
               sqLiteDB.deleteAll();
               Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
               startActivity(intent);
               finish();
           }
        }
    }




    class ChangeUserInfo extends AsyncTask<User, Void, String>{





        protected String doInBackground(User... params) {
            return null;
        }
    }
}
