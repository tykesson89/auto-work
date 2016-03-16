package com.lhadalo.oladahl.autowork;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

/**
 *@Author: Henrik Tykesson
 * Registration activity class and inner class that handle communication with
 * server and creates a user in the database.
 *
 */
public class RegistrationActivity extends Activity {
    private Button btnBackToLogin, btnRegister;
    private EditText etFirstName, etLastName, etEmail, etPassword, etHoulyWage, etCompany;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        textAndButtons();
        listeners();
    }

    /**
     * Method that handle all the EditTexts and Buttons.
     */
    public void textAndButtons() {
        btnBackToLogin = (Button) findViewById(R.id.btnBackToLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etHoulyWage = (EditText) findViewById(R.id.etHourlyWage);
        etCompany = (EditText) findViewById(R.id.etCompany);
    }

    /**
     * Button listener for the activity.
     */
    public void listeners() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Firstname", etFirstName.getText().toString());
                map.put("Lastname", etLastName.getText().toString());
                map.put("Email", etEmail.getText().toString());
                map.put("Password", etPassword.getText().toString());
                map.put("HourlyWage", etHoulyWage.getText().toString());
                map.put("CompanyName", etCompany.getText().toString());
                if(etPassword.getText().toString().length()< 5){
                    CharSequence text = "Password need to be atleast 6 characters";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(RegistrationActivity.this, text, duration);
                    toast.show();
                }else if(!etEmail.getText().toString().contains("@")){
                    CharSequence text = "Invalid Email";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(RegistrationActivity.this, text, duration);
                    toast.show();
                }else if(etFirstName.getText().toString().length()<1 || etCompany.getText().toString().length()<1 || etHoulyWage.getText().toString().length()<1 || etLastName.getText().toString().length()<1){
                    CharSequence text = "You have to fill all the fields";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(RegistrationActivity.this, text, duration);
                    toast.show();
                }else {
                    new CreateUser(RegistrationActivity.this).execute(map);
                }

            }
        });
        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    /**
     * Inner class for communikation with server.
     */
    private class CreateUser extends AsyncTask<HashMap<String, String>, Void, String> {
        private static final int port = 45001;
        private static final String ip = "85.235.21.222";
        private static final String tag = "Create User";
        private ObjectInputStream objectInputStream;
        private ObjectOutputStream objectOutputStream;
        private ProgressDialog progressDialog;
        private Context context;

        public CreateUser(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(HashMap<String, String>... params) {

            HashMap<String, String> map = new HashMap<String, String>();
            map = params[0];
            try {
                String response;
                Socket socket = new Socket(ip, port);
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                objectOutputStream.writeObject(tag);
                objectOutputStream.writeObject(map);
                progressDialog.dismiss();
                response = objectInputStream.readObject().toString();

                if (response.contains("User Already Exists")) {
                    return "User Already Exists";
                } else if (response.contains("Only Int")) {
                    return "Only Int";
                } else  {
                    return "succes";
                }

            } catch (Exception e) {
            }
            return null;
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
            } else if (res.contains("Only Int")) {
                CharSequence text = "Hourly wage have to be integers";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            } else if (res.contains("succes")) {
                CharSequence text = "Account created";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
