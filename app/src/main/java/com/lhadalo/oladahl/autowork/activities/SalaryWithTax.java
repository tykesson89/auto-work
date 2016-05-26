package com.lhadalo.oladahl.autowork.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lhadalo.oladahl.autowork.InternetService;
import com.lhadalo.oladahl.autowork.R;



import java.util.ArrayList;

import UserPackage.Workpass;
/**
 * Created by Omar Aldulaimi on 16-04-22.
 */

public class SalaryWithTax extends AppCompatActivity {
    private Button taxBtn, addWage;
    private EditText txtTax;
    private TextView showSalary, showAfterTax;
    private double salary=0;
    private double salaryWithTax;
    private double tax;

    private ArrayList<String> strArr;
    private ArrayAdapter<String> adapter;
    private ListView list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_salary_tax);
        salary =  getIntent().getDoubleExtra("salary",0);
        setAfterTax();
        showSalary = (TextView) findViewById(R.id.tvSalarWithOuttax);
        showAfterTax = (TextView) findViewById(R.id.tvSalaryWithTax);
  /*      list=(ListView)findViewById(R.id.itemsList);
        strArr= new ArrayList<>();
        adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, strArr);
        list.setAdapter(adapter);
        addWage = (Button)findViewById(R.id.addExtra);

        addWage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (salaryWithTax == 0) {
                    createAlertDialog("Wrong", "Enter the percentage first");

                }else{
             showInputDialog();
            }}
        });*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        setSalary();


    }


    private void setAfterTax() {

        taxBtn = (Button) findViewById(R.id.tvShowTax);
        txtTax = (EditText) findViewById(R.id.taxProcent);
        txtTax.setInputType(0x00002002);
        taxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtTax.getText().toString().trim().length() == 0) {
                    createAlertDialog(getString(R.string.WrongSalary), getString(R.string.WrongProcentage));

                } else {
                    tax = Double.parseDouble(txtTax.getText().toString());
                    double ska = tax / 100;
                    double sum = ska * salary;
                    salaryWithTax = salary - sum;

                    showAfterTax.setText(String.valueOf(salaryWithTax + " kr"));

                }
            }
        });
    }
    private void setSalary() {
        showSalary.setText(String.valueOf(salary + " kr"));
    }

  /*  private void showInputDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Extra income");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText title = new EditText(this);
        title.setHint("Title");
        layout.addView(title);

        final EditText input = new EditText(this);
        input.setInputType(0x0002002);
        input.setHint("Income");
        layout.addView(input);


        alertDialogBuilder.setView(layout);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        String setInput = input.getText().toString();
                        String setTitle = title.getText().toString();

                        if (setInput.isEmpty() && setTitle.isEmpty()) {
                            createAlertDialog("Wrong", "You must write title and income");
                        } else if (setInput.isEmpty() ||  setTitle.isEmpty()) {
                            createAlertDialog("Wrong", "You must write title and income");

                        } else {
                            double strToDouble = Double.parseDouble(setInput);
                            double plus = strToDouble + salaryWithTax;
                            String doubleToString = (String.valueOf(plus));
                            strArr.add("Your salary " + salaryWithTax + " kr " + " " + "With " + setTitle + " " + doubleToString + " kr");
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
    }*/
    private void createAlertDialog(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }
}



