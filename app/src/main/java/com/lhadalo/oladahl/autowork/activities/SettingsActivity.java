package com.lhadalo.oladahl.autowork.activities;

import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import UserPackage.User;

import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.database.SQLiteDB;
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
        userId = user.getUserid();
    }

    private void initFragment() {
        fragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_settings, fragment).commit();
    }
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClickBtnChangeUserInfo() {
        Intent intent = new Intent(SettingsActivity.this, ChangeUserinfoActivity.class);
        startActivity(intent);

    }
    public void onClickBtnChangePassword() {
        Intent intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
        startActivity(intent);


    }

    public void onClickBtnChangeCompanyInfo() {
        startActivity(new Intent(this, AddCompanyActivity.class));
    }
    public void onClickBtnDeleteCompany() {
        // TODO: 2016-04-19 Christoffer lägger till sitt här
    }

    public void onClickBtnDeleteAccount() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialogBuilder.setMessage("Are your sure you want to delete your account");

        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                new DeleteUser().execute(user);
            }
        });

        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            alertDialog.dismiss();
            }
        });

        final AlertDialog alersDialog = alertDialogBuilder.create();
        alersDialog.show();

    }


    class DeleteUser extends AsyncTask<User, Void, String> {
        private ObjectInputStream objectInputStream;
        private ObjectOutputStream objectOutputStream;
        private ProgressDialog progressDialog;

        protected String doInBackground(User... params) {
            User user = params[0];
            try {
                Socket socket = new Socket(Tag.IP, Tag.PORT);
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








}

