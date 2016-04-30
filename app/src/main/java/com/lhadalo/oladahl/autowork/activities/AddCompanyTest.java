package com.lhadalo.oladahl.autowork.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.database.SQLiteDB;

import java.util.List;

import UserPackage.Company;

/**
 * Created by oladahl on 16-04-29.
 */
public class AddCompanyTest extends AppCompatActivity{
    SQLiteDB db = new SQLiteDB(this);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_company_test);

        List<Company> companyList = db.getAllCompanies();
        ArrayAdapter<Company> adapter = new ArrayAdapter<Company>(this, android.R.layout.simple_list_item_1, companyList);

        ListView list = (ListView)findViewById(R.id.testlist);


        assert list != null;
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });

    }

    public void onClickButtonAdd(View view) {
        startActivity(new Intent(getBaseContext(), AddCompanyActivity.class));
    }
}
