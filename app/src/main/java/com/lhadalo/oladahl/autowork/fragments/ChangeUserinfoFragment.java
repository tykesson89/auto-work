package com.lhadalo.oladahl.autowork.fragments;

import android.content.Context;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.lhadalo.oladahl.autowork.R;


public class ChangeUserinfoFragment extends Fragment {
    private OnFragmentInteraction callback;
    private EditText etFirstname, etLastname, etEmail, etPassword;
    private Button btnChangeUserInfo;
    public interface OnFragmentInteraction{
        void onClickBtnChangeUserinfo(String firstname, String Lastname, String email, String password);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    private void initComponents(View view) {
        btnChangeUserInfo = (Button) view.findViewById(R.id.btnChangeUserInfo);
        etFirstname = (EditText) view.findViewById(R.id.etFirstname);
        etLastname = (EditText) view.findViewById(R.id.etLastname);
        etEmail = (EditText) view.findViewById(R.id.etEmail1);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        initListeners();
    }



    private void initListeners() {
        btnChangeUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickBtnChangeUserinfo(
                        etFirstname.getText().toString(),
                        etLastname.getText().toString(),
                        etEmail.getText().toString(),
                        etPassword.getText().toString()
                );
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_userinfo, container, false);

        initComponents(view);
        return view;
    }
    public void setTextetEmail(String str){
        etEmail.setText(str);
    }
    public void setTextetFirstname(String str){
        etFirstname.setText(str);
    }
    public void setTextetLastname(String str){
        etLastname.setText(str);
    }




    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (OnFragmentInteraction) context;
        } catch (ClassCastException e) {
            Log.e("", context.getClass().getCanonicalName() +
                    " must implement OnFragmentInteraction");
        }
    }

    public void setFirstNameError(boolean error, String message) {
        if(error) {
            etFirstname.setError(message);
        }
        else {
            etFirstname.setError(null);
        }
    }

    public void setLastNameError(boolean error, String message) {
        if(error) {
            etLastname.setError(message);
        }
        else {
            etLastname.setError(null);
        }
    }

    public void setEmailError(boolean error, String message) {
        if(error) {
            etEmail.setError(message);
        }
        else {
            etEmail.setError(null);
        }
    }

    public void setPasswError(boolean error, String message) {
        if(error) {
            etPassword.setError(message);
        }
        else {
            etPassword.setError(null);
        }
    }






}
