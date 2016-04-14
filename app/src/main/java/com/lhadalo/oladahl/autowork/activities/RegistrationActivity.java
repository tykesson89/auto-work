package com.lhadalo.oladahl.autowork.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import UserPackage.Company;
import UserPackage.User;

import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.SQLiteCommand;
import com.lhadalo.oladahl.autowork.Tag;
import com.lhadalo.oladahl.autowork.fragments.RegistrationFragment;

/**
 * @Author: Henrik Tykesson
 * Registration activity class and inner class that handle communication with
 * server and creates a user in the database.
 */
public class RegistrationActivity extends AppCompatActivity implements RegistrationFragment.OnFragmentInteraction{
    private RegistrationFragment fragment;
    private boolean inputOk = false;

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
        fragment.resetError();

        try {
            hourlyWage = Double.parseDouble(wage);
        } catch (NumberFormatException w) {
            /*CharSequence text = getString(R.string.toast_wage_number_error);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(RegistrationActivity.this, text, duration);
            toast.show();*/

        }



        if (password.length() < 6) {
            CharSequence text = getString(R.string.toast_password_error);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(RegistrationActivity.this, text, duration);
            toast.show();
            fragment.setLayoutPassError(true);

        }
        else{
            fragment.setLayoutPassError(false);
        }

        if (!email.contains("@")) {
            CharSequence text = getString(R.string.toast_email_error);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(RegistrationActivity.this, text, duration);
            toast.show();
            fragment.setLayoutEmailError(true);


        }
        if (firstName.length() < 1) {
            CharSequence text = "You must fill in your first name";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(RegistrationActivity.this, text, duration);
            toast.show();
            fragment.setFirstNameError(true);


        }
        if(lastName.length() < 1){
            CharSequence text = "You must fill in your last name";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(RegistrationActivity.this, text, duration);
            toast.show();
            fragment.setLayoutLastNameError(true);

        }
        if (companyName.length() < 1){
            CharSequence text = "You must fill in a company name";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(RegistrationActivity.this, text, duration);
            toast.show();
            fragment.setLayoutCompanyError(true);

        }
        if(hourlyWage < 1) {
            CharSequence text = "You must fill in your hourly wage";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(RegistrationActivity.this, text, duration);
            toast.show();
            fragment.setLayoutWageError(true);

        }else {
            User user = new User(firstName, lastName, email, password);
            Company company = new Company(companyName, hourlyWage);
            new CreateUser(RegistrationActivity.this).execute(user, company);
        }

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
            user = (User) params[0];
            company = (Company) params[1];
            try {
                Socket socket = new Socket(Tag.IP, Tag.PORT);
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                objectOutputStream.writeObject(Tag.CREATE_USER);
                objectOutputStream.writeObject(user);
                objectOutputStream.writeObject(company);
                progressDialog.dismiss();


            }catch (Exception e){
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
            if (res.equals(Tag.USER_ALREADY_EXISTS)) {
                CharSequence text = getString(R.string.toast_user_exists);
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            } else if (res.contains(Tag.SUCCESS)) { //Stod "succes" innan.
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
