package com.lhadalo.oladahl.autowork;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsFragment extends Fragment {
    private OnFragmentInteraction callback;
    private Button btnChangeUserInfo;
    private EditText etFirstName, etLastName, etEmail, etOldPassword, etNewPassword;
    private TextView tvDeleteAccount;

    interface OnFragmentInteraction {
        void onClickBtnChangeUserInfo(String firstName, String lastName, String email,
                                      String oldPassword, String newPassword);
        void onClickDeleteAccount(String firstName, String lastName, String email,
                                  String oldPassword, String newPassword);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        btnChangeUserInfo = (Button)view.findViewById(R.id.btnChangeUserInfo);

        etFirstName = (EditText)view.findViewById(R.id.etFirstName);
        etLastName = (EditText)view.findViewById(R.id.etLastName);
        etEmail = (EditText)view.findViewById(R.id.et_email);
        etOldPassword = (EditText)view.findViewById(R.id.etOldPassword);
        etNewPassword = (EditText)view.findViewById(R.id.etNewPassword);

        tvDeleteAccount = (TextView)view.findViewById(R.id.tvDeleteAccount);

        initListeners();
    }

    private void initListeners() {
        btnChangeUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClickBtnChangeUserInfo(
                        getFirstName(),
                        getLastName(),
                        getEmail(),
                        getOldPassword(),
                        getNewPassword());
            }
        });

        tvDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClickDeleteAccount(getFirstName(),
                        getLastName(),
                        getEmail(),
                        getOldPassword(),
                        getNewPassword());
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

    public String getFirstName() {
        return etFirstName.getText().toString();
    }

    public String getLastName() {
        return etLastName.getText().toString();
    }

    public String getEmail() {
        return etEmail.getText().toString();
    }

    public String getNewPassword() {
        return etNewPassword.getText().toString();
    }

    public String getOldPassword() {
        return etOldPassword.getText().toString();
    }
}
