package com.lhadalo.oladahl.autowork;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import UserPackage.User;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteraction{
    private LoginFragment fragment;
    private final int requestCode = 1;
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
            initFragment();
        }
    }

    private void initFragment(){
        fragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_login, fragment)
                .commit();
    }

    @Override
    public void onClickBtnLogin(String email, String password) {
        User user = new User(email, password);
        new Login(LoginActivity.this).execute(user);
    }

    @Override
    public void onClickBtnCreateUser() {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivityForResult(intent, requestCode);
    }

    private class Login extends AsyncTask<User, Void, String>{
        //TODO Hämta port och ip från inställningar
        private static final int port = 45001;
        private static final String ip = "192.168.1.77";

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
            progressDialog = progressDialog.show(context, getString(R.string.dialog_title_login),
                    getString(R.string.dialog_message_login), true);
        }

        @Override
        protected String doInBackground(User... users) {

            User user = users[0];
            try{
                socket = new Socket(ip, port);
                objectOut = new ObjectOutputStream(socket.getOutputStream());
                objectIn = new ObjectInputStream(socket.getInputStream());

                objectOut.writeObject(Tag.LOGIN);
                objectOut.writeObject(user);

                progressDialog.dismiss();

                user = (User)objectIn.readObject();
                db.loginUser(user);


            } catch (Exception e){
                return Tag.USER_NOT_FOUND;
            }

            return Tag.SUCCESS;
        }


        @Override
        protected void onPostExecute(String res) {
            progressDialog.dismiss();

            if(res.equals(Tag.USER_NOT_FOUND)){
                Toast.makeText(context, getString(R.string.toast_user_not_found),
                        Toast.LENGTH_SHORT).show();
            }
            else if(res.equals(Tag.SUCCESS)){
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == this.requestCode){
            if(resultCode == RESULT_OK){
                fragment.setTextetEmail(data.getStringExtra(Tag.EMAIL_INTENT));
            }
        }

    }
}

