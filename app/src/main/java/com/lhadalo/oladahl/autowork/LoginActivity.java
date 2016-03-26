package com.lhadalo.oladahl.autowork;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import UserPackage.User;

public class LoginActivity extends AppCompatActivity {
    private final int requestCode = 1;
    private Toolbar toolbar;
    private Button btnLogin, btnCreateUser;
    private EditText etEmail, etPassword;
    private SQLiteDB db = new SQLiteDB(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Creating database", " ");
        SQLiteDB sqLiteDB = new SQLiteDB(this);
        sqLiteDB.getWritableDatabase();
        Log.d("Database created", " ");
        if (sqLiteDB.isLoggedIn() == true) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {


            setContentView(R.layout.activity_login);
            initComponents();
            initListeners();
        }
    }

    

    public void initComponents(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        btnLogin = (Button)findViewById(R.id.btn_login);
        btnCreateUser = (Button)findViewById(R.id.btn_create_user);

        etEmail = (EditText)findViewById(R.id.userEmail);
        etPassword = (EditText)findViewById(R.id.user_password);

        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.login_title));

    }

    public void initListeners(){
        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivityForResult(intent, requestCode);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email =  etEmail.getText().toString();
                String password = etPassword.getText().toString();
                User user = new User(email, password);

                new Login(LoginActivity.this).execute(user);
            }
        });
    }

    private class Login extends AsyncTask<User, Void, String>{
        private static final int port = 45001;
        private static final String ip = "192.168.1.77";
        private String tag = "Login";
        private Context context;
        
        private Socket socket;
        private ObjectOutputStream objectOut;
        private ObjectInputStream objectIn;
        private ProgressDialog progressDialog;

        public Login(Context context){
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            progressDialog = progressDialog.show(context, "Login", "Logging in", true);
        }

        @Override
        protected String doInBackground(User... users) {

            User user = users[0];
            try{
                socket = new Socket(ip, port);
                objectOut = new ObjectOutputStream(socket.getOutputStream());
                objectIn = new ObjectInputStream(socket.getInputStream());

                objectOut.writeObject(tag);
                objectOut.writeObject(user);

                progressDialog.dismiss();

                user = (User)objectIn.readObject();
                db.loginUser(user);


            } catch (Exception e){
                return "User not found";
            }

            return "success";
        }


        @Override
        protected void onPostExecute(String res) {
            progressDialog.dismiss();

            if(res.equals("User not found")){
                Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show();
            }
            else if(res.equals("success")){
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == this.requestCode){
            if(resultCode == RESULT_OK){
                etEmail.setText(data.getStringExtra("Email"));
            }
        }

    }
}

