package com.lhadalo.oladahl.autowork;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by oladahl on 16-03-07.
 */
public class LoginFragment extends Fragment {
    private OnFragmentInteraction callback;
    private Button btnLogin;
    private EditText edTxtEmail, edTxtPassword;

    /**
     * Fragment för händelsehantering.
     */
    interface OnFragmentInteraction {
        void onClickLogin(String email, String password);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        initComponents(view);
        initListeners();

        return view;
    }

    private void initComponents(View view){
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.setTitle(getResources().getString(R.string.login_title));

        btnLogin = (Button)view.findViewById(R.id.btn_login);
        edTxtEmail = (EditText)view.findViewById(R.id.edTxtEmail);
        edTxtPassword = (EditText)view.findViewById(R.id.edTxtPass);
    }

    private void initListeners(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClickLogin(edTxtEmail.getText().toString(), edTxtPassword.getText().toString());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        callback = (OnFragmentInteraction)context;
    }
}
