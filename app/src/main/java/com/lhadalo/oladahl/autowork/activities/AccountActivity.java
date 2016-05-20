package com.lhadalo.oladahl.autowork.activities;

import android.content.DialogInterface;
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

import com.lhadalo.oladahl.autowork.database.SQLiteDB;
import com.lhadalo.oladahl.autowork.fragments.AccountFragment;
import com.lhadalo.oladahl.autowork.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import UserPackage.User;

/**
 * Created by oladahl on 16-05-15.
 */
public class AccountActivity extends AppCompatActivity
        implements AccountFragment.OnFragmentInteraction {
    private AccountFragment fragment;
    private SQLiteDB db = new SQLiteDB(this);
    private User user;
    boolean changesToSave = false;


    MaterialEditText etFirstname = null;
    MaterialEditText etLastname = null;
    MaterialEditText etEmail = null;
    MaterialEditText etNewPass = null;
    MaterialEditText etNewPassConf = null;
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
        fragment.setTxtChangePassword("Password");

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
        createAlertDialog("Log Out?", "Are you sure you want to log out?", 1);
    }

    @Override
    public void onClickDeleteAccount() {
        createAlertDialog("Delete Account?", "You can't undo this action!", 2);
    }

    private void createAlertDialog(String title, String message, final int source) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle(title);
        dialog.setMessage(message);

        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (source == 1) {
                    Toast.makeText(AccountActivity.this, "Loggar ut...", Toast.LENGTH_SHORT).show();
                }
                else if (source == 2) {
                    inputDialog(5);
                }
                else{
                    finish();
                }
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
        else if (source == 3){
            inputs = inflater.inflate(R.layout.password_dialog, null);
            etNewPass = (MaterialEditText)inputs.findViewById(R.id.et_new_pass);
            etNewPassConf = (MaterialEditText)inputs.findViewById(R.id.et_new_pass_conf);

            btnOk = (Button)inputs.findViewById(R.id.btn_ok);
            btnCancel = (Button)inputs.findViewById(R.id.btn_cancel);
        }
        else if(source == 4){
            inputs = inflater.inflate(R.layout.confirmation_dialog, null);
            etPassword = (MaterialEditText)inputs.findViewById(R.id.et_password);

            btnOk = (Button)inputs.findViewById(R.id.btn_ok);
            btnCancel = (Button)inputs.findViewById(R.id.btn_cancel);
        }
        else{
            inputs = inflater.inflate(R.layout.confirmation_dialog, null);
            etPassword = (MaterialEditText)inputs.findViewById(R.id.et_password);

            TextView dialogDesc = (TextView)inputs.findViewById(R.id.dialog_desc);
            dialogDesc.setText("Please enter your password to delete your account.");

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
                            etFirstname.setError("No name");
                        }
                        else if (hasDigit) {
                            etFirstname.setError("Digits not allowed");
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
                            etLastname.setError("No name");
                        }
                        else if (hasDigit) {
                            etLastname.setError("Digits not allowed");
                        }
                        inputOk = false;
                    }
                    else {
                        fName = removeSpaceBefore(fName);
                    }

                    if(inputOk){
                        if( !user.getFirstname().equals(fName) || !user.getLastname().equals(lName)) {
                            changesToSave = true;
                            invalidateOptionsMenu();
                            user.setFirstname(fName);
                            user.setLastname(lName);
                            fragment.setTxtChangeName(fName + " " + lName);
                            alert.dismiss();
                        }
                        else{
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
                            etEmail.setError("Isn't an email adress");
                        }
                        else if (isEmpty) {
                            etEmail.setError("No email");
                        }

                        inputOk = false;
                    }
                    else {
                        if(!user.getEmail().equals(email)) {
                            changesToSave = true;
                            invalidateOptionsMenu();
                            email = removeSpaceBefore(email);
                            user.setEmail(email);
                            fragment.setTxtChangeEmail(email);
                            alert.dismiss();
                        }
                        else{
                            alert.dismiss();
                        }
                    }
                }
                else if (source == 3){
                    String newPass = etNewPass.getText().toString();
                    String newPassConf = etNewPassConf.getText().toString();

                    if (newPass.length() < 6) {
                        etNewPass.setError("Password must be 6 characters");
                        inputOk = false;
                    }
                    else if(!newPass.equals(newPassConf)){
                       etNewPassConf.setError("Password doesn't match");
                        inputOk = false;
                    }

                    if(inputOk){
                        changesToSave = true;
                        invalidateOptionsMenu();
                        user.setNewPassword(newPass);
                        fragment.setTxtChangePassword("Password (changed)");
                        alert.dismiss();
                    }
                }
                else if(source == 4){
                    //TODO Kolla gammalt lösenord och spara ändringar.
                }
                else{
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
        if(changesToSave){
            createAlertDialog("Discard changes?", "All changes will be lost.", 3);
        }
        else{
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
        inputDialog(4);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_account, menu);

        return true;
    }




    /*public void onClickBtnChangeUserinfo(String etFirstname, String etLastname, String email, String password) {
        boolean inputOk = true;
        boolean isEmpty, hasDigit;
        CharSequence text = null;

        isEmpty = etFirstname.length() < 1;
        hasDigit = containsDigit(etFirstname);
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
            etFirstname = removeSpaceBefore(etFirstname);
            fragment.setFirstNameError(false, null);
        }
        isEmpty = etLastname.length() < 1;
        hasDigit = containsDigit(etLastname);
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
            etLastname = removeSpaceBefore(etLastname);
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
            fragment.setPasswError(true, "Password must be 6 characters");
            inputOk = false;
        } else {
            fragment.setPasswError(false, null);
        }

        if (inputOk) {
            user = new User(etFirstname, etLastname, email, password, userId);
            new ChangeUserInfo().execute(user);
        }

    }*/

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
}
