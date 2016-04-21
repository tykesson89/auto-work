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



public class ChangePasswordFragment extends Fragment {
    private OnFragmentInteraction callback;
    private EditText newPassword, newPasswordConfirmation, oldPassword;
    private Button btnChangePassword;
    public interface OnFragmentInteraction{
        void onClickBtnChangePassword(String newPassword, String newPasswordConfirmation, String oldPassword);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    private void initComponents(View view) {
        btnChangePassword = (Button) view.findViewById(R.id.btnChangePassword);
        newPassword = (EditText) view.findViewById(R.id.newPassword);
        newPasswordConfirmation = (EditText) view.findViewById(R.id.newPasswordConfirmation);
        oldPassword = (EditText) view.findViewById(R.id.oldPassword);
        initListeners();
    }



    private void initListeners() {
    btnChangePassword.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callback.onClickBtnChangePassword(
                    newPassword.getText().toString(),
                    newPasswordConfirmation.getText().toString(),
                    oldPassword.getText().toString()
            );
        }
    });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        initComponents(view);
        return view;
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




    

}
