package com.lhadalo.oladahl.autowork;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;


public class RegistrationActivity extends AppCompatActivity {
    private Button btnBackToLogin, btnRegister;
    private EditText etFirstName, etLastName, etEmail, etPassword, etHoulyWage, etCompany;
    private Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        textAndButtons();
        listeners();
    }

    public void textAndButtons(){
        btnBackToLogin = (Button)findViewById(R.id.btnBackToLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        etFirstName = (EditText)findViewById(R.id.etFirstName);
        etLastName = (EditText)findViewById(R.id.etLastName);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etHoulyWage = (EditText)findViewById(R.id.etHourlyWage);
        etCompany = (EditText)findViewById(R.id.etCompany);
    }

    public void listeners(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String response;
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Firstname", etFirstName.getText().toString());
                map.put("Lastname", etLastName.getText().toString());
                map.put("Email", etEmail.getText().toString());
                map.put("Password", etPassword.getText().toString());
                map.put("HourlyWage", etHoulyWage.getText().toString());
                map.put("CompanyName", etCompany.getText().toString());
                new CreateUser().execute(map);

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


    private class CreateUser extends AsyncTask<HashMap<String, String>, Void, String>{
        private static final int port = 40001;
        private static final String ip = "192.168.1.7";
        private static final String tag = "Create User";
        private ObjectInputStream objectInputStream;
        private ObjectOutputStream objectOutputStream;

        @Override
        protected String doInBackground(HashMap<String, String>... params) {
            HashMap<String,String> map = new HashMap<String, String>();
            map = params[0];
            try {
                String response;
                Socket socket = new Socket(ip, port);
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                objectOutputStream.writeObject(tag);
                objectOutputStream.writeObject(map);
                response = objectInputStream.readObject().toString();
                if(response.contains("0")){
                    return "0";
                }
            }catch(Exception e){

            }
            return null;
        }

        protected void onPreExecute(){

        }
        protected void onPostExecute(String res){
            if(res.equals("0")){
                CharSequence text = "User Already exists";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

        }
    }
}
