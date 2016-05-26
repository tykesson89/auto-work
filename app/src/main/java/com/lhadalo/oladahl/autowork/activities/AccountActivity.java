package com.lhadalo.oladahl.autowork.activities;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lhadalo.oladahl.autowork.Tag;
import com.lhadalo.oladahl.autowork.database.SQLiteDB;
import com.lhadalo.oladahl.autowork.fragments.AccountFragment;
import com.lhadalo.oladahl.autowork.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import UserPackage.User;

public class AccountActivity extends AppCompatActivity
        implements AccountFragment.OnFragmentInteraction {
    private AccountFragment fragment;
    private SQLiteDB db = new SQLiteDB(this);
    private User user;
    boolean changesToSave = false;


    private MaterialEditText etFirstname = null;
    private MaterialEditText etLastname = null;
    private MaterialEditText etEmail = null;
    private MaterialEditText etNewPass = null;
    private MaterialEditText etNewPassConf = null;
    private MaterialEditText etPassword = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = db.getUser();

        setContentView(R.layout.activity_account);

        initFragment();
    }

    private void initFragment() {
        fragment = new AccountFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_account, fragment).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        fragment.setTxtChangeName(user.getFirstname() + " " + user.getLastname());
        fragment.setTxtChangeEmail(user.getEmail());
        fragment.setTxtChangePassword(getString(R.string.password_hint));

    }

    @Override
    public void onClickEditName() {
        inputDialog(1);
    }

    @Override
    public void onClickEditEmail() {
        inputDialog(2);
    }

    @Override
    public void onClickEditPassword() {
        inputDialog(3);
    }

    @Override
    public void onClickLogOut() {
        createAlertDialog(getString(R.string.Account_logout), getString(R.string.Sure_logout), 1);
    }

    @Override
    public void onClickDeleteAccount() {
        createAlertDialog(getString(R.string.Account_delete), getString(R.string.Cannot_undo), 2);
    }

    @Override
    public void onClickClose() {
        if (changesToSave) {
            createAlertDialog(getString(R.string.Account_changes), getString(R.string.Changes_lost), 3);
        }
        else {
            finish();
        }
    }

    public void onActionLogOutPressed() {
        SQLiteDB sqLiteDB = new SQLiteDB(this);
        sqLiteDB.deleteAll();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void createAlertDialog(String title, String message, final int source) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle(title);
        dialog.setMessage(message);

        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (source == 1) {
                    onActionLogOutPressed();
                    Toast.makeText(AccountActivity.this, getString(R.string.Account_logingOut), Toast.LENGTH_SHORT).show();
                }
                else if (source == 2) {
                    inputDialog(5);
                }
                else {
                    finish();
                }
            }
        });

        dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }

    private void inputDialog(final int source) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        Button btnCancel = null;
        Button btnOk = null;

        dialog.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View inputs = null;

        if (source == 1) {
            inputs = inflater.inflate(R.layout.name_dialog, null);
            etFirstname = (MaterialEditText)inputs.findViewById(R.id.et_firstname);
            etLastname = (MaterialEditText)inputs.findViewById(R.id.et_lastname);

            etFirstname.setText(user.getFirstname());
            etLastname.setText(user.getLastname());


            btnCancel = (Button)inputs.findViewById(R.id.btn_cancel);
            btnOk = (Button)inputs.findViewById(R.id.btn_ok);


        }
        else if (source == 2) {
            inputs = inflater.inflate(R.layout.email_dialog, null);
            etEmail = (MaterialEditText)inputs.findViewById(R.id.et_email);
            etEmail.setText(user.getEmail());

            btnOk = (Button)inputs.findViewById(R.id.btn_ok);
            btnCancel = (Button)inputs.findViewById(R.id.btn_cancel);
        }
        else if (source == 3) {
            inputs = inflater.inflate(R.layout.password_dialog, null);
            etNewPass = (MaterialEditText)inputs.findViewById(R.id.et_new_pass);
            etNewPassConf = (MaterialEditText)inputs.findViewById(R.id.et_new_pass_conf);

            btnOk = (Button)inputs.findViewById(R.id.btn_ok);
            btnCancel = (Button)inputs.findViewById(R.id.btn_cancel);
        }
        else if (source == 4) {
            inputs = inflater.inflate(R.layout.confirmation_dialog, null);
            etPassword = (MaterialEditText)inputs.findViewById(R.id.et_password);

            btnOk = (Button)inputs.findViewById(R.id.btn_ok);
            btnCancel = (Button)inputs.findViewById(R.id.btn_cancel);
        }
        else {
            inputs = inflater.inflate(R.layout.confirmation_dialog, null);
            etPassword = (MaterialEditText)inputs.findViewById(R.id.et_password);

            TextView dialogDesc = (TextView)inputs.findViewById(R.id.dialog_desc);
            dialogDesc.setText(getString(R.string.Enter_password));

            btnOk = (Button)inputs.findViewById(R.id.btn_ok);
            btnCancel = (Button)inputs.findViewById(R.id.btn_cancel);
        }

        dialog.setView(inputs);

        final AlertDialog alert = dialog.create();
        alert.show();

        assert btnOk != null;
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean inputOk = true;
                boolean isEmpty, hasDigit;
                if (source == 1) {

                    String fName = etFirstname.getText().toString();
                    String lName = etLastname.getText().toString();


                    isEmpty = fName.length() < 1;
                    hasDigit = containsDigit(fName);
                    if (isEmpty || hasDigit) {

                        if (isEmpty) {
                            etFirstname.setError(getString(R.string.No_name));
                        }
                        else if (hasDigit) {
                            etFirstname.setError(getString(R.string.No_digits));
                        }
                        inputOk = false;

                    }
                    else {
                        fName = removeSpaceBefore(fName);
                    }

                    isEmpty = lName.length() < 1;
                    hasDigit = containsDigit(lName);
                    if (isEmpty || hasDigit) {

                        if (isEmpty) {
                            etLastname.setError(getString(R.string.No_name));
                        }
                        else if (hasDigit) {
                            etLastname.setError(getString(R.string.No_digits));
                        }
                        inputOk = false;
                    }
                    else {
                        fName = removeSpaceBefore(fName);
                    }

                    if (inputOk) {
                        if (!user.getFirstname().equals(fName) || !user.getLastname().equals(lName)) {
                            changesToSave = true;
                            invalidateOptionsMenu();
                            user.setFirstname(fName);
                            user.setLastname(lName);
                            fragment.setTxtChangeName(fName + " " + lName);
                            alert.dismiss();
                        }
                        else {
                            alert.dismiss();
                        }
                    }
                }
                else if (source == 2) {
                    String email = etEmail.getText().toString();
                    isEmpty = email.isEmpty();
                    boolean isntEmail = !email.contains("@");
                    if (isntEmail || isEmpty) {
                        if (isntEmail) {
                            etEmail.setError(getString(R.string.No_Email_Adress));
                        }
                        else if (isEmpty) {
                            etEmail.setError(getString(R.string.NoMail));
                        }

                        inputOk = false;
                    }
                    else {
                        if (!user.getEmail().equals(email)) {
                            changesToSave = true;
                            invalidateOptionsMenu();
                            email = removeSpaceBefore(email);
                            user.setEmail(email);
                            fragment.setTxtChangeEmail(email);
                            alert.dismiss();
                        }
                        else {
                            alert.dismiss();
                        }
                    }
                }
                else if (source == 3) {
                    String newPass = etNewPass.getText().toString();
                    String newPassConf = etNewPassConf.getText().toString();

                    if (newPass.length() < 6) {
                        etNewPass.setError(getString(R.string.toast_password_error));
                        inputOk = false;
                    }
                    else if (!newPass.equals(newPassConf)) {
                        etNewPassConf.setError(getString(R.string.Password_not_match));
                        inputOk = false;
                    }

                    if (inputOk) {
                        changesToSave = true;
                        invalidateOptionsMenu();
                        user.setNewPassword(newPass);
                        fragment.setTxtChangePassword(getString(R.string.Password_changed));
                        alert.dismiss();
                    }
                }
                else if (source == 4) {
                    //TODO Kolla gammalt lösenord och spara ändringar.
                    user.setOldPassword(etPassword.getText().toString());
                    alert.dismiss();
                    new ChangeUserInfo(AccountActivity.this).execute(user);
                }
                else {
                    //TODO Kolla gammalt lösenord och ta bort account.
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (changesToSave) {
            createAlertDialog(getString(R.string.Account_changes), getString(R.string.Changes_lost), 3);
        }
        else {
            finish();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (changesToSave) {
            menu.findItem(R.id.action_change_account).setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_account) {
            inputDialog(4);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_account, menu);

        return true;
    }

    private void setPasswordIsWrong() {
        etNewPass.setError(getString(R.string.Wrong_password));
    }


    public String removeSpaceBefore(String str) {
        String res = str;
        for (int i = 0; i < str.length() && Character.isWhitespace(res.charAt(0)); i++) {
            res = res.substring(1);
        }

        return res;
    }

    public boolean hasSpaceBefore(String str) {
        return Character.isWhitespace(str.charAt(0));
    }

    public boolean containsDigit(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    private static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
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

    class ChangeUserInfo extends AsyncTask<User, Void, String> {
        private ObjectInputStream objectInputStream;
        private ObjectOutputStream objectOutputStream;
        private AccountActivity activity;
        private ProgressDialog progDialog;
        private Context context;

        public ChangeUserInfo(Context context) {
            this.context = context;
            this.activity = (AccountActivity)context;
        }


        @Override
        protected void onPreExecute() {
            progDialog = ProgressDialog.show(context, getString(R.string.Internet_Sync), getString(R.string.Sync_server), true);
        }

        protected String doInBackground(User... params) {

            if (isConnected(context) == true) {
                User user = params[0];
                try {
                    try {
                        Socket socket = new Socket();
                        socket.connect(new InetSocketAddress(Tag.IP, Tag.PORT), 4000);
                        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        objectInputStream = new ObjectInputStream(socket.getInputStream());
                        objectOutputStream.writeObject(Tag.CHANGE_USER_INFO);
                        objectOutputStream.writeObject(user);

                        String response = (String)objectInputStream.readObject();
                        return response;
                    } catch (SocketTimeoutException e) {
                        return getString(R.string.Server_offline);
                    }

                } catch (Exception e) {
                    return getString(R.string.Server_offline);
                }
            }
            else {
                return getString(R.string.No_conn);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            progDialog.dismiss();

            if (s.equals("Password is incorrect")) {
                Toast.makeText(context, "Password is incorrect", Toast.LENGTH_SHORT).show();
            }
            else if (s.equals("Server is offline")) {
                Toast.makeText(context, "Server is offline", Toast.LENGTH_SHORT).show();
            }
            else if (s.equals("No Internet Connection")) {
                Toast.makeText(context, "You have no Internet Connection", Toast.LENGTH_SHORT).show();
            }
            else if (s.equals("Something went wrong")) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
            else if (s.equals("Success")) {
                SQLiteDB sqLiteDB = new SQLiteDB(context);
                sqLiteDB.updateUser(user);
                finish();
                Toast.makeText(context, "User info changed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}