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
import android.widget.Spinner;
import android.widget.TextView;

import com.lhadalo.oladahl.autowork.R;

public class AddCompanyFragment extends Fragment {
    private OnFragmentInteraction callback;
    private Button btnAdd, btnChange, btnDelete;
    private EditText edCompany, edHourly;
    private Spinner spinner;


    public interface OnFragmentInteraction {
        void onClickBtnAddCompany(String company, double hourly);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_company, container, false);

        initComponents(view);
        return view;
    }

    private void initComponents(View view) {

        btnAdd = (Button) view.findViewById(R.id.btn_addCompany);
        btnDelete = (Button) view.findViewById(R.id.btn_delete);
        btnChange = (Button) view.findViewById(R.id.btn_change);
        edCompany = (EditText) view.findViewById((R.id.etCompany));
        edHourly = (EditText) view.findViewById(R.id.etHourly);
        initListeners();

    }

    private void initListeners() {

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callback.onClickBtnAddCompany(getCompany(),getHourly());
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

    public String getCompany() {

        return edCompany.getText().toString();
    }

    public double getHourly(){
        return Double.parseDouble(edHourly.getText().toString());
    }

}
