package com.lhadalo.oladahl.autowork.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.lhadalo.oladahl.autowork.R;


import java.util.ArrayList;

public class SalaryWithTax extends AppCompatActivity {
    private Button taxBtn, addWage;
    private EditText tax;
    private TextView showSalary, showAfterTax;
    private double salary=0;
    private double test;
    private double skatt;
    private ArrayList<String> strArr;
    private ArrayAdapter<String> adapter;
    private ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_salary_tax);
        salary =  getIntent().getDoubleExtra("salary",0);
        showSalary = (TextView) findViewById(R.id.tvSalarWithOuttax);
        showAfterTax = (TextView) findViewById(R.id.tvSalaryWithTax);
        list=(ListView)findViewById(R.id.itemsList);
        strArr= new ArrayList<>();
        adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, strArr);
        list.setAdapter(adapter);
        addWage = (Button)findViewById(R.id.addExtra);

        addWage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             showInputDialog();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        setSalary();
        setAfterTax();

    }


    private void setAfterTax() {

        taxBtn = (Button) findViewById(R.id.tvShowTax);
        tax = (EditText) findViewById(R.id.taxProcent);
        tax.setInputType(0x00002002);
        taxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skatt = Double.parseDouble(tax.getText().toString());
                double ska = skatt / 100;
                double dec = ska * salary;
                test = salary - dec;

                showAfterTax.setText(String.valueOf(test + " kr"));
            }
        });
    }
    private void setSalary() {
        showSalary.setText(String.valueOf(salary + " kr"));
    }

    private void showInputDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Extra income");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alertDialogBuilder.setView(input);
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String setInput = input.getText().toString();
                        if (setInput.length() > 0) {
                            strArr.add(setInput);

                            adapter.notifyDataSetChanged();

                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}



