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
import android.widget.Toast;

public class RegistrationFragment extends Fragment {
    private OnFragmentInteraction callback;
    private Toolbar toolbar;
    private Button btnBackToLogin, btnRegister;
    private EditText etFirstName, etLastName, etEmail, etPassword, etHoulyWage, etCompany;
    private String email;

    interface OnFragmentInteraction {
        void onClickBtnRegister(String firstName, String lastName, String email,
                                String password, String hourlyWage, String companyName);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        toolbar = (Toolbar)view.findViewById(R.id.toolbar_registration);
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.setTitle(getResources().getString(R.string.registration_title));

        btnRegister = (Button)view.findViewById(R.id.btnRegister);
        etFirstName = (EditText)view.findViewById(R.id.etFirstName);
        etLastName = (EditText)view.findViewById(R.id.etLastName);
        etEmail = (EditText)view.findViewById(R.id.et_email);
        etPassword = (EditText)view.findViewById(R.id.etPassword);
        etHoulyWage = (EditText)view.findViewById(R.id.etHourlyWage);
        etCompany = (EditText)view.findViewById(R.id.etCompany);


        initListeners();
    }

    private void initListeners() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClickBtnRegister(
                        etFirstName.getText().toString(),
                        etLastName.getText().toString(),
                        etEmail.getText().toString(),
                        etPassword.getText().toString(),
                        etHoulyWage.getText().toString(),
                        etCompany.getText().toString()
                );
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (OnFragmentInteraction) context;
        } catch (ClassCastException e) {
            Log.e("", context.getClass().getCanonicalName() +
                    " must implement OnFragmentInteraction");
        }
    }
}