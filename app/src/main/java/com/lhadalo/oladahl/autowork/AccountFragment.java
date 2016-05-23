package com.lhadalo.oladahl.autowork;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AccountFragment extends Fragment {
    private LinearLayout layoutChangeName;
    private LinearLayout layoutChangeEmail;
    private LinearLayout layoutChangePassword;


    private OnFragmentInteraction callback;

    interface OnFragmentInteraction {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.myAccount));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_clear_white_24dp));

        AppCompatActivity activity = (AppCompatActivity)getActivity();

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutChangeName = (LinearLayout)view.findViewById(R.id.layout_change_name);
        layoutChangeEmail = (LinearLayout)view.findViewById(R.id.layout_change_email);

        ImageView imgChangeName = (ImageView)layoutChangeName.getChildAt(0);
        ImageView imgChangeEmail = (ImageView)layoutChangeEmail.getChildAt(0);
        ImageView imgChangePassword = (ImageView)layoutChangePassword.getChildAt(0);

        imgChangeName.setImageDrawable(getResources().getDrawable(R.drawable.ic_account_box_black_24dp));
        imgChangeEmail.setImageDrawable(getResources().getDrawable(R.drawable.ic_mail_black_24dp));
        imgChangePassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_vpn_key_black_24dp));

        RelativeLayout layoutContentName = (RelativeLayout)layoutChangeName.getChildAt(1);
        RelativeLayout layoutContentEmail = (RelativeLayout)layoutChangeEmail.getChildAt(1);
        RelativeLayout layoutContentPassword = (RelativeLayout)layoutChangePassword.getChildAt(1);

        TextView txtChangeName = (TextView)layoutContentName.getChildAt(0);
        TextView txtChangeNameDesc = (TextView)layoutContentName.getChildAt(1);

        TextView txtChangeEmail = (TextView)layoutContentEmail.getChildAt(0);
        TextView txtChangeEmailDesc = (TextView)layoutContentEmail.getChildAt(1);

        TextView txtChangePassword = (TextView)layoutContentPassword.getChildAt(0);
        TextView txtChangePasswordDesc = (TextView)layoutContentPassword.getChildAt(1);

        /*TextView txtChangeEmail = (TextView)layoutChangeEmail.getChildAt(1);
        TextView txtChangePassword = (TextView)layoutChangePassword.getChildAt(1);*/

        txtChangeName.setText("Ola Dahl");
        txtChangeNameDesc.setText("Name");

        txtChangeEmail.setText("oladahl.lel@gmail.com");
        txtChangeEmailDesc.setText("E-mail");


        txtChangePassword.setText("Change Password");
        txtChangePasswordDesc.setText("Password");

        initListeners();
    }

    private void initListeners() {
        layoutChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        layoutChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        layoutChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

