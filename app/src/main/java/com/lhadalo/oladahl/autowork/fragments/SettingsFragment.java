package com.lhadalo.oladahl.autowork.fragments;

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

import com.lhadalo.oladahl.autowork.R;

public class SettingsFragment extends Fragment {
    private OnFragmentInteraction callback;
    private Button btnChangeUserInfo, btnChangePassword, btnChangeCompanyInfo, btnDeleteAccount, btnDeleteCompany;


    public interface OnFragmentInteraction {
        void onClickBtnChangeUserInfo();
        void onClickBtnChangePassword();
        void onClickBtnChangeCompanyInfo();
        void onClickBtnDeleteAccount();
        void onClickBtnDeleteCompany();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        btnChangeUserInfo = (Button) view.findViewById(R.id.btnChangeUserInfo);
        btnChangePassword = (Button) view.findViewById(R.id.btnChangePassword);
        btnChangeCompanyInfo = (Button) view.findViewById(R.id.btnChangeCompanyInfo);
        btnDeleteAccount = (Button) view.findViewById(R.id.btnDeleteAccount);
        btnDeleteCompany = (Button) view.findViewById(R.id.btnDeleteCompany);


        initListeners();
    }



    private void initListeners() {
        btnChangeUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClickBtnChangeUserInfo();

            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClickBtnChangePassword();
            }
        });

        btnChangeCompanyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClickBtnChangeCompanyInfo();
            }
        });
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClickBtnDeleteAccount();
            }
        });
        btnDeleteCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClickBtnDeleteCompany();
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
