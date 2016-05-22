package com.lhadalo.oladahl.autowork.fragments;


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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.Tag;

public class AccountFragment extends Fragment {
    private Toolbar toolbar;
    private TextView txtChangeName;
    private TextView txtChangeEmail;
    private TextView txtChangePassword;

    private Button btnEditName, btnEditEmail, btnEditPassword, btnLogOut, btnDeleteAccount;

    private OnFragmentInteraction callback;

    public interface OnFragmentInteraction {
        void onClickLogOut();

        void onClickDeleteAccount();

        void onClickEditName();

        void onClickEditEmail();

        void onClickEditPassword();

        void onClickClose();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        toolbar.setTitle("My Account");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_clear_white_24dp));

        AppCompatActivity activity = (AppCompatActivity)getActivity();

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout layoutChangeName = (LinearLayout)view.findViewById(R.id.layout_change_name);
        LinearLayout layoutChangeEmail = (LinearLayout)view.findViewById(R.id.layout_change_email);

        ImageView imgChangeName = (ImageView)layoutChangeName.getChildAt(0);
        ImageView imgChangeEmail = (ImageView)layoutChangeEmail.getChildAt(0);
        ImageView imgChangePassword = (ImageView)view.findViewById(R.id.img_password);

        imgChangeName.setImageDrawable(getResources().getDrawable(R.drawable.ic_account_box_black_24dp));
        imgChangeEmail.setImageDrawable(getResources().getDrawable(R.drawable.ic_mail_black_24dp));
        imgChangePassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_vpn_key_black_24dp));

        RelativeLayout layoutContentName = (RelativeLayout)layoutChangeName.getChildAt(1);
        RelativeLayout layoutContentEmail = (RelativeLayout)layoutChangeEmail.getChildAt(1);

        txtChangeName = (TextView)layoutContentName.getChildAt(0);
        txtChangeEmail = (TextView)layoutContentEmail.getChildAt(0);

        TextView txtChangeNameDesc = (TextView)layoutContentName.getChildAt(1);
        TextView txtChangeEmailDesc = (TextView)layoutContentEmail.getChildAt(1);

        btnEditName = (Button)layoutChangeName.getChildAt(2);
        btnEditEmail = (Button)layoutChangeEmail.getChildAt(2);
        btnEditPassword = (Button)view.findViewById(R.id.btn_edit_password);

        btnEditName.setTag(Tag.BTN_EDIT_NAME);
        btnEditEmail.setTag(Tag.BTN_EDIT_EMAIL);

        txtChangeNameDesc.setText("Name");
        txtChangeEmailDesc.setText("E-mail");

        txtChangePassword = (TextView)view.findViewById(R.id.txt_password);

        btnLogOut = (Button)view.findViewById(R.id.btn_logout);
        btnDeleteAccount = (Button)view.findViewById(R.id.btn_delete_account);

        initListeners();
    }

    private void initListeners() {
        OnClickListener listener = new OnClickListener();


        btnEditName.setOnClickListener(listener);

        btnEditEmail.setOnClickListener(listener);

        btnEditPassword.setOnClickListener(listener);

        btnLogOut.setOnClickListener(listener);

        btnDeleteAccount.setOnClickListener(listener);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClickClose();
            }
        });
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

    public void setTxtChangeName(String str) {
        txtChangeName.setText(str);
    }

    public void setTxtChangeEmail(String str) {
        txtChangeEmail.setText(str);
    }

    public void setTxtChangePassword(String str) {
        txtChangePassword.setText(str);
    }

    private class OnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String tag = (String)view.getTag();
            if (tag != null) {
                if (tag.equals(Tag.BTN_EDIT_NAME)) {
                    callback.onClickEditName();
                }
                else {
                    callback.onClickEditEmail();
                }
            }
            else {
                int id = view.getId();
                switch (id){
                    case R.id.btn_edit_password:
                        callback.onClickEditPassword();
                        break;
                    case R.id.btn_logout:
                        callback.onClickLogOut();
                        break;
                    case R.id.btn_delete_account:
                        callback.onClickDeleteAccount();
                        break;
                }

            }

        }
    }
}

