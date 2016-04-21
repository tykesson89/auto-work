package com.lhadalo.oladahl.autowork.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.SQLiteDB;
import com.lhadalo.oladahl.autowork.Tag;
import com.lhadalo.oladahl.autowork.fragments.ChangeUserinfoFragment;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import UserPackage.User;


public class ChangeUserinfoActivity extends AppCompatActivity implements ChangeUserinfoFragment.OnFragmentInteraction {
    private ChangeUserinfoFragment fragment;
    private User user;
    private int userId;
    private String firstName;
    private String lastName;
    private String email;

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

    @Override
    protected void onStart() {
        super.onStart();

        fragment.setTextetEmail("hej");
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


    class ChangeUserInfo extends AsyncTask<User, Void, String> {
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

                String response = (String) objectInputStream.readObject();
                return response;

            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {


        }
    }
}

