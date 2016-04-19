package com.lhadalo.oladahl.autowork.activities;

import android.app.AlertDialog;
import android.app.Dialog;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    }

    @Override
    public void onClickBtnChangeUserInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Info");
        Context context = this;
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText firstName = new EditText(this);
        firstName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        firstName.setText(this.firstName.toString());
        layout.addView(firstName);
        final EditText lastName = new EditText(this);
        lastName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        lastName.setText(this.lastName.toString());
        layout.addView(lastName);
        final EditText email = new EditText(this);
        email.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        email.setText(this.email.toString());
        layout.addView(email);
        builder.setView(layout);
        final TextView passwordText = new TextView(this);
        passwordText.setPadding(0, 60, 0, 60);
        passwordText.setText("Please input your password to change your info");
        layout.addView(passwordText);
        final EditText password = new EditText(this);
        password.setHint("Password");

        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(password);
        builder.setView(layout);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String firstname = firstName.getText().toString();
                String lastname = lastName.getText().toString();
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                user = new User(firstname, lastname, Email, Password, userId);
                new ChangeUserInfo().execute(user);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }
    public void onClickBtnChangePassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Info");
        Context context = this;
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        builder.setView(layout);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }
    public void onClickBtnChangeCompanyInfo() {
        // TODO: 2016-04-19 Christoffer lägger till sitt här
    }
    public void onClickBtnDeleteCompany() {
        // TODO: 2016-04-19 Christoffer lägger till sitt här
    }


    public void onClickBtnDeleteAccount() {
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


            }
        }
    }



}

