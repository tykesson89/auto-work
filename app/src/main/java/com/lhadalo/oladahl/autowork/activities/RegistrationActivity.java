package com.lhadalo.oladahl.autowork.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

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
            firstName = removeSpaceBefore(firstName);
            fragment.setFirstNameError(false, null);
        }

        isEmpty = lastName.length() < 1;
        hasDigit = containsDigit(lastName);
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
            lastName = removeSpaceBefore(lastName);
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
            text = getString(R.string.toast_password_error);
            fragment.setPasswError(true, "Password must be 6 characters");
            inputOk = false;
        } else {
            fragment.setPasswError(false, null);
        }


        if (companyName.length() < 1) {
            text = "You must fill in a company name";
            fragment.setCompanyError(true, "Empty name");
            inputOk = false;
        } else {
            companyName = removeSpaceBefore(companyName);
            fragment.setCompanyError(false, null);
        }

        try {
            hourlyWage = Double.parseDouble(wage);
            if (wage.length() < 1) {
                text = "You must fill in your hourly wage";
                fragment.setWageError(true, "No wage");
                inputOk = false;

            } else {
                fragment.setWageError(false, null);
            }
        } catch (NumberFormatException w) {
            text = getString(R.string.toast_wage_number_error);
            inputOk = false;
        }


        //Ifall alla villkor är sanna, är inputOk = true
        if (inputOk) {
            User user = new User(firstName, lastName, email, password);
            Company company = new Company(companyName, hourlyWage);
            new CreateUser(RegistrationActivity.this).execute(user, company);
        } else {
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

    public String removeSpaceBefore(String str) {
        String res = str;
        for (int i = 0; i < str.length() && Character.isWhitespace(res.charAt(0)); i++) {
            res = res.substring(1);
        }

        return res;
    }

    public boolean containsDigit(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                return true;
            }
        }

        return false;
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

    /**
     * Inner class for communication with server.
     */
    private class CreateUser extends AsyncTask<Object, Void, String> {
        private ObjectInputStream objectInputStream;
        private ObjectOutputStream objectOutputStream;
        private ProgressDialog progressDialog;
        private Context context;
        private Socket socket;


        public CreateUser(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Object... params) {


            if(isConnected(this.context)==true) {
                Company company;
                User user;
                user = (User) params[0];
                company = (Company) params[1];
                try{
                    try {
                       Socket socket = new Socket();
                        socket.connect(new InetSocketAddress(Tag.IP, Tag.PORT), 4000);

                        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        objectInputStream = new ObjectInputStream(socket.getInputStream());
                        objectOutputStream.writeObject(Tag.CREATE_USER);
                        objectOutputStream.writeObject(user);
                        objectOutputStream.writeObject(company);
                        String response = (String) objectInputStream.readObject();
                        return response;
                    } catch (SocketTimeoutException s) {
                        return "The server is offline";
                    }
                    }catch(Exception e){
                        return "The server is offline";
                    }

            }else{
                return "You have no Internet Connection";
            }

        }

        protected void onPreExecute() {
            progressDialog = progressDialog.show(context,
                    getString(R.string.dialog_title_creating_user),
                    getString(R.string.dialog_message_creating_user), true);
        }

        protected void onPostExecute(String res) {
            progressDialog.dismiss();
            if (res.equals("The server is offline")) {
                CharSequence text = "The server is offline";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            } else if (res.equals("You have no Internet Connection")) {
                CharSequence text = "You have no Internet Connection";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }else if(res.equals("User Already Exists")) {
                CharSequence text = "User Already Exists";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }else if(res.equals("Something went wrong")) {
                CharSequence text = "Something went wrong";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }else if(res.equals("No Email")) {
                CharSequence text = "Invalid Email";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }else{
                CharSequence text = "Account created";
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
