package com.lhadalo.oladahl.autowork.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.lhadalo.oladahl.autowork.database.SQLiteDB;
import com.lhadalo.oladahl.autowork.fragments.AccountFragment;
import com.lhadalo.oladahl.autowork.R;

import UserPackage.User;

/**
 * Created by oladahl on 16-05-15.
 */
public class AccountActivity extends AppCompatActivity
        implements AccountFragment.OnFragmentInteraction{
    private AccountFragment fragment;
    private SQLiteDB db = new SQLiteDB(this);
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = db.getUser();

        setContentView(R.layout.activity_account);

        initFragment();
    }

    private void initFragment(){
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
        Toast.makeText(AccountActivity.this, "EditName", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickEditEmail() {
        Toast.makeText(AccountActivity.this, "EditEmail", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickEditPassword() {
        Toast.makeText(AccountActivity.this, "EditPassword", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickLogOut() {
        createDialog("Log Out?", "Are you sure you want to log out?", 1);
    }

    @Override
    public void onClickDeleteAccount() {
        createDialog("Delete Account?", "You can't undo this action!", 2);
    }
    
    private void createDialog(String title, String message, final int source){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        
        dialog.setTitle(title);
        dialog.setMessage(message);
        
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(source == 1) {
                    Toast.makeText(AccountActivity.this, "Loggar ut...", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AccountActivity.this, "Tar bort account", Toast.LENGTH_SHORT).show();
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
}
