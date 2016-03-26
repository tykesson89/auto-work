package com.lhadalo.oladahl.autowork;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import UserPackage.Company;
import UserPackage.User;

/**
 * @Author: Henrik Tykesson
 * Registration activity class and inner class that handle communication with
 * server and creates a user in the database.
 */
public class RegistrationActivity extends AppCompatActivity implements RegistrationFragment.OnFragmentInteraction{
    private RegistrationFragment fragment;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initFragment();
    }

    private void initFragment(){
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

        try {
            hourlyWage = Double.parseDouble(wage);
        } catch (NumberFormatException w) {
            CharSequence text = "Your Hourly Wage have to be in number format";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(RegistrationActivity.this, text, duration);
            toast.show();
        }
        if (password.length() < 6) {
            CharSequence text = "Password need to be atleast 6 characters";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(RegistrationActivity.this, text, duration);
            toast.show();
        } else if (!email.contains("@")) {
            CharSequence text = "Invalid Email";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(RegistrationActivity.this, text, duration);
            toast.show();
        } else if (firstName.length() < 1 || lastName.length() < 1 || hourlyWage < 1 || companyName.length() < 1) {
            CharSequence text = "You have to fill all the fields";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(RegistrationActivity.this, text, duration);
            toast.show();
        } else {
            User user = new User(firstName, lastName, email, password);
            Company company = new Company(companyName, hourlyWage);
            new CreateUser(RegistrationActivity.this).execute(user, company);
        }

    }

    /**
     * Inner class for communication with server.
     */
    private class CreateUser extends AsyncTask<Object, Void, String> {
        private static final int port = 45001;
        private static final String ip = "192.168.1.77";
        private static final String tag = "Create User";
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
            user = (User) params[0];
            company = (Company) params[1];
            try {
                Socket socket = new Socket(ip, port);
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                objectOutputStream.writeObject(tag);
                objectOutputStream.writeObject(user);
                objectOutputStream.writeObject(company);
                progressDialog.dismiss();


            }catch (Exception e){
                return "User Already Exists";
            }
            return "succes";
        }
        protected void onPreExecute() {
            progressDialog = progressDialog.show(context, "Creating user", "Creating User", true);
        }

        protected void onPostExecute(String res) {
            progressDialog.dismiss();
            if (res.equals("User Already Exists")) {
                CharSequence text = "User already exists";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            } else if (res.contains("succes")) {
                CharSequence text = "Account created";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                Intent data = new Intent();
                data.putExtra("Email", fragment.getEmail());

                setResult(RESULT_OK, data);
                finish();
            }
        }
    }
}
