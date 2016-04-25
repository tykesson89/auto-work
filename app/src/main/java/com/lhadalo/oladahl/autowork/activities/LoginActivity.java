package com.lhadalo.oladahl.autowork.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import UserPackage.Company;
import UserPackage.User;

import com.lhadalo.oladahl.autowork.database.BufferDatabase;
import com.lhadalo.oladahl.autowork.InternetService;
import com.lhadalo.oladahl.autowork.InternetSettingsActivity;
import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.database.SQLiteDB;
import com.lhadalo.oladahl.autowork.Tag;
import UserPackage.Workpass;
import com.lhadalo.oladahl.autowork.fragments.LoginFragment;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteraction{
    private LoginFragment fragment;
    private final int requestCode = 1;
    private SQLiteDB db = new SQLiteDB(this);
    private String ip;
    private int port;
    private Context context = this;

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
            ArrayList<Workpass> workpassArrayList = new ArrayList<>();
            if(isConnected(this.context)==true) {
                User user = users[0];
                try {
                    try {
                        Socket socket = new Socket();
                        socket.connect(new InetSocketAddress(Tag.IP, Tag.PORT), 4000);
                        objectOut = new ObjectOutputStream(socket.getOutputStream());
                        objectIn = new ObjectInputStream(socket.getInputStream());

                        objectOut.writeObject(Tag.LOGIN);
                        objectOut.writeObject(user);

                        progressDialog.dismiss();
                        Object obj = objectIn.readObject();
                        if (obj instanceof String) {
                            String response = (String) obj;
                            return response;
                        } else {
                            user = (User)obj;
                            db.loginUser(user);
                            try {
                                companyArrayList = (ArrayList<Company>) objectIn.readObject();
                            } catch (Exception e){
                                return "Något blev fel här";
                            }
                            for (int i = 0; i < companyArrayList.size(); i++) {
                                Company companyToAdd = companyArrayList.get(i);
                                long id = db.addCompany(companyToAdd);
                                companyToAdd.setCompanyId(id);

                            }

//                workpassArrayList = (ArrayList<Workpass>)objectIn.readObject();
//                for(int i = 0; i < workpassArrayList.size(); i++){
//                    db.addloginWorkpass(workpassArrayList.get(i));
//                }
                        }
                    } catch (SocketTimeoutException s) {
                        return "The server is offline";
                    }
                } catch (Exception e) {
                    //return "Something went wrong";
                }
            }else{
                return "You have no internet connection";
            }

            return Tag.SUCCESS;
        }


        @Override
        protected void onPostExecute(String res) {
            progressDialog.dismiss();

            if(res.equals("Something went wrong")){
                Toast.makeText(context, "Something went wrong",
                        Toast.LENGTH_SHORT).show();
            }else if(res.equals("Wrong Email")){
                Toast.makeText(context, "Email does not exists",
                        Toast.LENGTH_SHORT).show();
            }else if(res.equals("Wrong password")){
                Toast.makeText(context, "Wrong password",
                        Toast.LENGTH_SHORT).show();
            }else if(res.equals("The server is offline")){
                Toast.makeText(context, "The server is offline",
                        Toast.LENGTH_SHORT).show();
            }else if(res.equals("You have no internet connection")){
                Toast.makeText(context, "You have no internet connection",
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
            if(isConnected(context)==true) {
                String str = strings[0];
                try {
                    try {
                        Socket socket = new Socket();
                        socket.connect(new InetSocketAddress(Tag.IP, Tag.PORT), 4000);
                        objectOut = new ObjectOutputStream(socket.getOutputStream());
                        objectIn = new ObjectInputStream(socket.getInputStream());

                        objectOut.writeObject(Tag.New_Password);
                        objectOut.writeObject(str);
                        String response;
                        response = (String) objectIn.readObject();
                        return response;
                    } catch (SocketTimeoutException we) {
                        return "Server is offline";
                    }

                } catch (Exception e) {
                    return "Something went wrong";
                }
            }else{
                return "No Internet Connection";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("No Email")){
                Toast.makeText(context, "Email does not exists",
                        Toast.LENGTH_SHORT).show();
            }else if(s.equals("Server is offline")){
                Toast.makeText(context, "Server is offline",
                        Toast.LENGTH_SHORT).show();
            }else if(s.equals("No Internet Connection")){
                Toast.makeText(context, "You have no Internet Connection",
                        Toast.LENGTH_SHORT).show();
            }else if(s.equals("Something went wrong")){
                Toast.makeText(context, "Something went wrong",
                        Toast.LENGTH_SHORT).show();
            }else if(s.equals("Email sent")){
                Toast.makeText(context, "Password Changed",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}