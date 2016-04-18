package com.lhadalo.oladahl.autowork.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import UserPackage.Company;
import UserPackage.User;

import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.Tag;
import com.lhadalo.oladahl.autowork.fragments.RegistrationFragment;

/**
 * @Author: Henrik Tykesson
 * Registration activity class and inner class that handle communication with
 * server and creates a user in the database.
 */
public class RegistrationActivity extends AppCompatActivity implements RegistrationFragment.OnFragmentInteraction {
    private RegistrationFragment fragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initFragment();
    }

    private void initFragment() {
        fragment = new RegistrationFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_registration, fragment)
                .commit();
    }


    /**
     * Button listener for the activity.
     */
    @Override
    public void onClickBtnRegister(String firstName, String lastName, String email, String password,
                                   String wage, String companyName) {
        double hourlyWage = 0;
        boolean inputOk = true;
        boolean isEmpty, hasDigit;
        CharSequence text = null;

        isEmpty = firstName.length() < 1;
        hasDigit = containsDigit(firstName);
        if(isEmpty || hasDigit) {

            if(isEmpty) {
                text = "You must fill in your first name";
                fragment.setFirstNameError(true, "No name");
            }
            else if(hasDigit) {
                text = "Firstname cannot contain digits";
                fragment.setFirstNameError(true, "Digits not allowed");
            }
            inputOk = false;

        }
        else {
            if(hasSpaceBefore(firstName)) {
                firstName = removeSpaceBefore(firstName);
            }
            fragment.setFirstNameError(false, null);
        }

        isEmpty = lastName.length() < 1;
        hasDigit = containsDigit(lastName);
        if(isEmpty || hasDigit) {
            if(isEmpty) {
                text = "You must fill in your last name";
                fragment.setLastNameError(true, "No name");
            }
            else if(hasDigit) {
                text = "Lastname cannot contain digits";
                fragment.setLastNameError(true, "Digits not allowed");
            }


            inputOk = false;
        }
        else {
            if(hasSpaceBefore(lastName)) {
                //Ta bort space
            }
            fragment.setLastNameError(false, null);
        }


        isEmpty = email.isEmpty();
        boolean isntEmail = !email.contains("@");
        if(isntEmail || isEmpty) {
            if(isntEmail) {
                text = getString(R.string.toast_email_error);
                fragment.setEmailError(true, "Isn't a email adress");
            }
            else if(isEmpty) {
                text = "Email cannot be empty";
                fragment.setEmailError(true, "No email");
            }

            inputOk = false;
        }
        else {
            if(hasSpaceBefore(email)) {
                //Ta bort space
            }
            fragment.setEmailError(false, null);
        }

        if(password.length() < 6) {
            text = getString(R.string.toast_password_error);
            fragment.setPasswError(true, "Password must be 6 characters");
            inputOk = false;
        }
        else {
            fragment.setPasswError(false, null);
        }


        if(companyName.length() < 1) {
            text = "You must fill in a company name";
            fragment.setCompanyError(true, "Empty name");
            inputOk = false;
        }
        else {
            fragment.setCompanyError(false, null);
        }

        try {
            hourlyWage = Double.parseDouble(wage);
            if(wage.length() < 1) {
                text = "You must fill in your hourly wage";
                fragment.setWageError(true, "No wage");
                inputOk = false;

            }
            else {
                fragment.setWageError(false, null);
            }
        } catch (NumberFormatException w) {
            text = getString(R.string.toast_wage_number_error);
            inputOk = false;
        }

        Log.v(Tag.LOGTAG, firstName);
        //Ifall alla villkor ej är sanna, är inputOk = true
        if(inputOk) {
            User user = new User(firstName, lastName, email, password);
            Company company = new Company(companyName, hourlyWage);
            new CreateUser(RegistrationActivity.this).execute(user, company);
        }
        else {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(RegistrationActivity.this,
                    "Something went wrong, please check all fields", duration);
            toast.show();
        }
    }

    @Override
    public void onClickCancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    public boolean hasSpaceBefore(String str) {
        return Character.isWhitespace(str.charAt(0));
    }

    public String removeSpaceBefore(String str){
        String res = str;
        for(int i = 0; i < str.length() && Character.isWhitespace(res.charAt(0)); i++){
            res = res.substring(1);
        }
        return res;
    }

    public boolean containsDigit(String str) {
        for(int i = 0; i < str.length(); i++) {
            if(Character.isDigit(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Inner class for communication with server.
     */
    private class CreateUser extends AsyncTask<Object, Void, String> {
        private ObjectInputStream objectInputStream;
        private ObjectOutputStream objectOutputStream;
        private ProgressDialog progressDialog;
        private Context context;


        public CreateUser(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Object... params) {
            Company company;
            User user;
            user = (User)params[0];
            company = (Company)params[1];
            try {
                Socket socket = new Socket(Tag.IP, Tag.PORT);
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                objectOutputStream.writeObject(Tag.CREATE_USER);
                objectOutputStream.writeObject(user);
                objectOutputStream.writeObject(company);
                progressDialog.dismiss();


            } catch (Exception e) {
                return Tag.USER_ALREADY_EXISTS;
            }
            return Tag.SUCCESS; //Stod "succes" innan.
        }

        protected void onPreExecute() {
            progressDialog = progressDialog.show(context,
                    getString(R.string.dialog_title_creating_user),
                    getString(R.string.dialog_message_creating_user), true);
        }

        protected void onPostExecute(String res) {
            progressDialog.dismiss();
            if(res.equals(Tag.USER_ALREADY_EXISTS)) {
                CharSequence text = getString(R.string.toast_user_exists);
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            else if(res.contains(Tag.SUCCESS)) { //Stod "succes" innan.
                CharSequence text = getString(R.string.toast_account_created);
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                Intent data = new Intent();
                data.putExtra(Tag.EMAIL_INTENT, fragment.getEmail());

                setResult(RESULT_OK, data);
                finish();
            }
        }
    }
}
