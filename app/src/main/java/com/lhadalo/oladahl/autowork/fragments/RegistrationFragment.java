package com.lhadalo.oladahl.autowork.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.lhadalo.oladahl.autowork.R;

public class RegistrationFragment extends Fragment {
    private OnFragmentInteraction callback;
    private Toolbar toolbar;
    private Button btnBackToLogin, btnRegister;
    private EditText etFirstName, etLastName, etEmail, etPassword, etHoulyWage, etCompany;
    private String email;

    ScrollView scrollView;
    TextInputLayout etLayoutFirstName, etLayoutLastName, etLayoutEmail, etLayoutPass,
            etLayoutCompany, etLayoutWage;

    int counter = 1;

    public interface OnFragmentInteraction {
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

    /**
     * Method that initializes all components.
     */
    private void initComponents(View view) {
        toolbar = (Toolbar)view.findViewById(R.id.toolbar_registration);
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.setTitle(getResources().getString(R.string.registration_title));

        btnRegister = (Button)view.findViewById(R.id.btnRegister);
        etFirstName = (TextInputEditText)view.findViewById(R.id.etFirstName);
        etLastName = (EditText)view.findViewById(R.id.etLastName);
        etEmail = (EditText)view.findViewById(R.id.et_email);
        etPassword = (EditText)view.findViewById(R.id.etPassword);
        etHoulyWage = (EditText)view.findViewById(R.id.etHourlyWage);
        etCompany = (EditText)view.findViewById(R.id.etCompany);

        etLayoutFirstName = (TextInputLayout)view.findViewById(R.id.et_layout_first_name);
        etLayoutLastName = (TextInputLayout)view.findViewById(R.id.et_layout_last_name);
        etLayoutEmail = (TextInputLayout)view.findViewById(R.id.et_layout_email);
        etLayoutPass = (TextInputLayout)view.findViewById(R.id.et_layout_password);
        etLayoutCompany = (TextInputLayout)view.findViewById(R.id.et_layout_company);
        etLayoutWage = (TextInputLayout)view.findViewById(R.id.et_layout_wage);

        scrollView = (ScrollView)view.findViewById(R.id.scrollView);

        /*etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                scrollView.smoothScrollTo(0, 1);
            }
        });*/

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                scrollView.smoothScrollTo(0, 500);
            }
        });

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

    public void resetError() {
        if(etLayoutFirstName.isErrorEnabled()) etLayoutFirstName.setError(null);
        if(etLayoutLastName.isErrorEnabled()) etLayoutLastName.setError(null);
        if(etLayoutEmail.isErrorEnabled()) etLayoutEmail.setError(null);
        if(etLayoutPass.isErrorEnabled()) etLayoutPass.setError(null);
        if(etLayoutCompany.isErrorEnabled()) etLayoutCompany.setError(null);
        if(etLayoutWage.isErrorEnabled()) etLayoutWage.setError(null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (OnFragmentInteraction)context;
        } catch (ClassCastException e) {
            Log.e("", context.getClass().getCanonicalName() +
                    " must implement OnFragmentInteraction");
        }
    }

    public String getEmail() {
        return etEmail.getText().toString();
    }


    public void setFirstNameError(boolean error, String message) {
        if(error) {
            etLayoutFirstName.setError(message);
        }
        else {
            etLayoutFirstName.setError(null);
        }
    }

    public void setLastNameError(boolean error, String message) {
        if(error) {
            etLayoutLastName.setError(message);
        }
        else {
            etLayoutLastName.setError(null);
        }
    }

    public void setEmailError(boolean error, String message) {
        if(error) {
            etLayoutEmail.setError(message);
        }
        else {
            etLayoutEmail.setError(null);
        }
    }

    public void setPasswError(boolean error, String message) {
        if(error) {
            etLayoutPass.setError(message);
        }
        else {
            etLayoutPass.setError(null);
        }
    }

    public void setCompanyError(boolean error, String message) {
        if(error) {
            etLayoutCompany.setError(message);
        }
        else {
            etLayoutCompany.setError(null);
        }
    }

    public void setWageError(boolean error, String message) {
        if(error) {
            etLayoutWage.setError(message);
        }
        else {
            etLayoutWage.setError(null);
        }
    }
}