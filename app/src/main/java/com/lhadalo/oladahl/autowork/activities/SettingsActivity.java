package com.lhadalo.oladahl.autowork.activities;

import android.app.AlertDialog;
import android.app.Dialog;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

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
        userId = user.getUserid();
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
        user = new User(firstName, lastName, email, oldPassword, userId, newPassword);

        if(!newPassword.equals(newPasswordCheck)) {
                CharSequence text = "Password does not match";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(SettingsActivity.this, text, duration);
                toast.show();

        }else if(firstName.isEmpty()){
            CharSequence text = "Please enter your first name";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(SettingsActivity.this, text, duration);
            toast.show();
        }else if(lastName.isEmpty()){
            CharSequence text = "Please enter your last name";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(SettingsActivity.this, text, duration);
            toast.show();
        }else if(email.isEmpty()){
            CharSequence text = "Please enter your email";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(SettingsActivity.this, text, duration);
            toast.show();
        }else if(oldPassword.isEmpty()){
            CharSequence text = "Please enter your password";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(SettingsActivity.this, text, duration);
            toast.show();

        }else{
            new ChangeUserInfo().execute(user);
        }

    }

    @Override
    public void onClickDeleteAccount() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are your sure you want to delete your account");

        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                new DeleteUser().execute(user);
            }
        });

        alertDialogBuilder.setNegativeButton("NO",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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




    class ChangeUserInfo extends AsyncTask<User, Void, String>{
        private ObjectInputStream objectInputStream;
        private ObjectOutputStream objectOutputStream;
        private ProgressDialog progressDialog;




        protected String doInBackground(User... params) {
            User user = params[0];
            try {
                Socket socket = new Socket(Tag.IP, Tag.PORT);
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                objectOutputStream.writeObject(Tag.CHANGE_USER_INFO);
                objectOutputStream.writeObject(user);

                String response =(String) objectInputStream.readObject();
                return response;

            }catch (Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("Password is incorrect")){
                CharSequence text = "Password is incorrect";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(SettingsActivity.this, text, duration);
                toast.show();
            }else if(s.equals("Something went wrong")){
                CharSequence text = "Something went wrong";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(SettingsActivity.this, text, duration);
                toast.show();
            }else if(s.equals("Success")){
                SQLiteDB sqLiteDB = new SQLiteDB(SettingsActivity.this);
                sqLiteDB.updateUser(user);
                CharSequence text = "User Info is changed";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(SettingsActivity.this, text, duration);
                toast.show();
                fragment.setTextEmail(user.getEmail());
                fragment.setTextFirstName(user.getFirstname());
                fragment.setTextLastName(user.getLastname());

            }
        }
    }



}

