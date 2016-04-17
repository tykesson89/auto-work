package com.lhadalo.oladahl.autowork.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import UserPackage.Company;
import UserPackage.User;

import com.lhadalo.oladahl.autowork.BufferDatabase;
import com.lhadalo.oladahl.autowork.InternetService;
import com.lhadalo.oladahl.autowork.InternetSettingsActivity;
import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.SQLiteDB;
import com.lhadalo.oladahl.autowork.Tag;
import UserPackage.WorkpassModel;
import com.lhadalo.oladahl.autowork.fragments.LoginFragment;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteraction{
    private LoginFragment fragment;
    private final int requestCode = 1;
    private SQLiteDB db = new SQLiteDB(this);
    private String ip;
    private int port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLiteDB sqLiteDB = new SQLiteDB(this);
        sqLiteDB.getWritableDatabase();
        BufferDatabase bufferDatabase = new BufferDatabase(this);
        bufferDatabase.getWritableDatabase();
        Intent service = new Intent(this, InternetService.class);
        startService(service);

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
    public void onClickNewPassword() {
        final EditText txtEmail = new EditText(this);


        txtEmail.setHint("Email");

        new AlertDialog.Builder(this)
                .setTitle("New Password")
                .setMessage("Input your email to get new Password")
                .setView(txtEmail)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String str = txtEmail.getText().toString();
                        new NewPassword().execute(str);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();

    }

    @Override
    public void onClickInternetSettings() {
        Intent intent = new Intent(this, InternetSettingsActivity.class);
        startActivity(intent);

    }

    @Override
    public void onClickBtnCreateUser() {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivityForResult(intent, requestCode);
    }



    private class Login extends AsyncTask<User, Void, String>{
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
            ArrayList<Company> companyArrayList = new ArrayList<>();
            ArrayList<WorkpassModel>workpassModelArrayList = new ArrayList<>();
           
            User user = users[0];
            try{
                socket = new Socket(Tag.IP, Tag.PORT);
                objectOut = new ObjectOutputStream(socket.getOutputStream());
                objectIn = new ObjectInputStream(socket.getInputStream());

                objectOut.writeObject(Tag.LOGIN);
                objectOut.writeObject(user);

                progressDialog.dismiss();

                user = (User)objectIn.readObject();
                db.loginUser(user);
                companyArrayList = (ArrayList<Company>)objectIn.readObject();
                for(int i = 0; i < companyArrayList.size(); i++){
                    db.addCompany(companyArrayList.get(i));
                }
                
                workpassModelArrayList = (ArrayList<WorkpassModel>)objectIn.readObject();
                for(int i = 0; i < workpassModelArrayList.size(); i++){
                    db.addloginWorkpass(workpassModelArrayList.get(i));
                }


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
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
    private class NewPassword extends AsyncTask<String, Void, String>{
        private Socket socket;
        private ObjectOutputStream objectOut;
        private ObjectInputStream objectIn;

        @Override
        protected String doInBackground(String... strings) {
            String str = strings[0];
            try {
                socket = new Socket(Tag.IP, Tag.PORT);
                objectOut = new ObjectOutputStream(socket.getOutputStream());
                objectIn = new ObjectInputStream(socket.getInputStream());

                objectOut.writeObject(Tag.New_Password);
                objectOut.writeObject(str);
                String response;
                response = (String)objectIn.readObject();
                return response;
            }catch (Exception e){

            }
            return "fail";
        }

        @Override
        protected void onPostExecute(String res) {
              if(res == null){
                  Toast.makeText(LoginActivity.this, getString(R.string.toast_user_not_found),
                          Toast.LENGTH_SHORT).show();
              }
            else if(res.equals("fail")){
                Toast.makeText(LoginActivity.this, getString(R.string.toast_user_not_found),
                        Toast.LENGTH_SHORT).show();
            } else if(res.equals(Tag.SUCCESS)){
                Toast.makeText(LoginActivity.this, "Password sent",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}