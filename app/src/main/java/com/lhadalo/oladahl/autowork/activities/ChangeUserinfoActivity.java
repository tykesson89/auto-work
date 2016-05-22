package com.lhadalo.oladahl.autowork.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.database.SQLiteDB;
import com.lhadalo.oladahl.autowork.Tag;
import com.lhadalo.oladahl.autowork.fragments.ChangeUserinfoFragment;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import UserPackage.User;


public class ChangeUserinfoActivity extends AppCompatActivity implements ChangeUserinfoFragment.OnFragmentInteraction {

        private ChangeUserinfoFragment fragment;
        private User user;
        private int userId;
        private String firstName;
        private String lastName;
        private String email;
    private Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_userinfo);
        initFragment();
        SQLiteDB sqLiteDB = new SQLiteDB(this);
        user = sqLiteDB.getUser();
        firstName = user.getFirstname();
        lastName = user.getLastname();
        email = user.getEmail();
        userId = user.getUserid();
        Log.d(email, " ");

    }

    protected void onStart() {
        super.onStart();
        SQLiteDB sqLiteDB = new SQLiteDB(this);
        user = sqLiteDB.getUser();
        firstName = user.getFirstname();
        lastName = user.getLastname();
        email = user.getEmail();
        userId = user.getUserid();
        fragment.setTextetEmail(email);
        fragment.setTextetFirstname(firstName);
        fragment.setTextetLastname(lastName);
    }




    private void initFragment() {
        fragment = new ChangeUserinfoFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_Change_Userinfo, fragment).commit();
    }


    @Override
    public void onClickBtnChangeUserinfo(String firstname, String lastname, String email, String password) {
        boolean inputOk = true;
        boolean isEmpty, hasDigit;
        CharSequence text = null;

        isEmpty = firstname.length() < 1;
        hasDigit = containsDigit(firstname);
        if (isEmpty || hasDigit) {

            if (isEmpty) {
                text = "You must fill in your first name";
                fragment.setFirstNameError(true, "No name");
            } else if (hasDigit) {
                text = "Firstname cannot contain digits";
                fragment.setFirstNameError(true, "Digits not allowed");
            }
            inputOk = false;

        } else {
            firstname = removeSpaceBefore(firstname);
            fragment.setFirstNameError(false, null);
        }
        isEmpty = lastname.length() < 1;
        hasDigit = containsDigit(lastname);
        if (isEmpty || hasDigit) {
            if (isEmpty) {
                text = "You must fill in your last name";
                fragment.setLastNameError(true, "No name");
            } else if (hasDigit) {
                text = "Lastname cannot contain digits";
                fragment.setLastNameError(true, "Digits not allowed");
            }


            inputOk = false;
        } else {
            lastname = removeSpaceBefore(lastname);
            fragment.setLastNameError(false, null);
        }
        isEmpty = email.isEmpty();
        boolean isntEmail = !email.contains("@");
        if (isntEmail || isEmpty) {
            if (isntEmail) {
                text = getString(R.string.toast_email_error);
                fragment.setEmailError(true, "Isn't a email adress");
            } else if (isEmpty) {
                text = "Email cannot be empty";
                fragment.setEmailError(true, "No email");
            }

            inputOk = false;
        } else {
            email = removeSpaceBefore(email);
            fragment.setEmailError(false, null);
        }

        if (password.length() < 6) {
            fragment.setPasswError(true, "Password must be 6 characters");
            inputOk = false;
        } else {
            fragment.setPasswError(false, null);
        }

        if (inputOk) {
            user = new User(firstname, lastname, email, password, userId);
            new ChangeUserInfo().execute(user);
        }

    }

    public boolean hasSpaceBefore(String str) {
        return Character.isWhitespace(str.charAt(0));
    }

    public boolean containsDigit(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                return true;
            }
        }

        return false;
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

    class ChangeUserInfo extends AsyncTask<User, Void, String> {
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
                        objectOutputStream.writeObject(Tag.CHANGE_USER_INFO);
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
                    fragment.setPasswError(true, "Password is incorrect");
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
                    SQLiteDB sqLiteDB = new SQLiteDB(context);
                    sqLiteDB.updateUser(user);
                    Toast.makeText(context, "User info changed",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChangeUserinfoActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
        }
    }
}

