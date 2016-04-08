package com.lhadalo.oladahl.autowork.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.SQLiteDB;

import java.util.ArrayList;
import java.util.List;

import UserPackage.Company;

public class AddCompanyFragment extends Fragment {
    private OnFragmentInteraction callback;
    private Button btnAdd, btnChange, btnDelete;
    private EditText edCompany, edHourly;
    private Spinner spinner;


    public interface OnFragmentInteraction {
        void onClickBtnAddCompany(String company, double hourly);
        void onClickBtnChangeCompany(String company, double hourly);
        void onClickBtnDeleteCompany(String company, double hourly);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_company, container, false);

        initComponents(view);
        spinner();
        return view;
    }

    private void initComponents(View view) {

        btnAdd = (Button) view.findViewById(R.id.btn_addCompany);
        btnDelete = (Button) view.findViewById(R.id.btn_delete);
        btnChange = (Button) view.findViewById(R.id.btn_change);
        edCompany = (EditText) view.findViewById((R.id.etCompany));
        edHourly = (EditText) view.findViewById(R.id.etHourly);
        spinner = (Spinner)view.findViewById(R.id.spinner);
        initListeners();

    }

    private void initListeners() {


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callback.onClickBtnAddCompany(getCompany(), getHourly());
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callback.onClickBtnDeleteCompany(getCompany(), getHourly());
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickBtnChangeCompany(getCompany(), getHourly());
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



    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();

    }
    public void spinner(){
        SQLiteDB db = new SQLiteDB(this.getActivity());
        List<Company> listpro = new ArrayList<>();
        listpro = db.getAllCompanies();
        ArrayList<String> list = new ArrayList<>();
        if (listpro.size() == 0) {
            showMessage("Error", "Nothing found");

        }
        for(int j=0; j<listpro.size(); j++){

            list.add(listpro.get(j).getCompanyName());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_layout, R.id.txt, list);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(adapter);
    }

}
