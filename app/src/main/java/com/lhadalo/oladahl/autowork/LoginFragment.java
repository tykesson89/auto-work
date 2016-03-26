package com.lhadalo.oladahl.autowork;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by oladahl on 16-03-26.
 */
public class LoginFragment extends Fragment {
    private OnFragmentInteraction callback;
    private Toolbar toolbar;
    private Button btnLogin, btnCreateUser;
    private EditText etEmail, etPassword;

    interface OnFragmentInteraction{
        void onClickBtnLogin(String email, String password);
        void onClickBtnCreateUser();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        toolbar = (Toolbar)view.findViewById(R.id.toolbar_login);
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.setTitle(getResources().getString(R.string.login_title));

        btnLogin = (Button)view.findViewById(R.id.btn_login);
        btnCreateUser = (Button)view.findViewById(R.id.btn_create_user);

        etEmail = (EditText)view.findViewById(R.id.et_email);
        etPassword = (EditText)view.findViewById(R.id.et_password);

        initListeners();
    }

    private void initListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClickBtnLogin(etEmail.getText().toString(),
                        etPassword.getText().toString());
            }
        });

        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClickBtnCreateUser();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            callback = (OnFragmentInteraction)context;
        } catch (ClassCastException e){
            Log.e("", context.getClass().getCanonicalName() +
                    " must implement OnFragmentInteraction");
        }
    }

    public void setTextetEmail(String str){
        etEmail.setText(str);
    }
}
