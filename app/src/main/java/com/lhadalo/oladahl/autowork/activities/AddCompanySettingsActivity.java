package com.lhadalo.oladahl.autowork.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.lhadalo.oladahl.autowork.CompanyListAdapter;
import com.lhadalo.oladahl.autowork.Tag;
import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.database.DatabaseContract;
import com.lhadalo.oladahl.autowork.database.SQLiteDB;

import java.util.List;

import UserPackage.Company;

public class AddCompanySettingsActivity extends AppCompatActivity {
    SQLiteDB db = new SQLiteDB(this);
    List<Company> companies;
    CompanyListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = (ListView) findViewById(R.id.listview);
        companies = db.getAllCompanies();

       // ArrayAdapter<Company> adapter = new ArrayAdapter<Company>(this, android.R.layout.simple_list_item_1, companies);
        adapter = new CompanyListAdapter(this, companies);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createOptionsDialog(position);
            }
        });

        listView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_company, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, AddCompanyActivity.class);
            intent.putExtra(Tag.REQUEST_CODE, Tag.ADD_COMPANY_REQUEST);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onItemOptionsDialogSelected(int option, int listPosition){

    }

    public void createOptionsDialog(final int listPosition){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Edit");

        builder.setItems(new String[]{"Change", "Delete", "Cancel"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                if (position == 0) {

                    Intent intent = new Intent(getApplicationContext(), AddCompanyActivity.class);
                    intent.putExtra(DatabaseContract.CompanyEntry.COMPANY_ID, companies.get(listPosition).getCompanyId());
                    intent.putExtra(Tag.REQUEST_CODE, Tag.CHANGE_COMPANY_REQUEST);
                    startActivity(intent);

                } else if (position == 1) {
                    db.deleteCompany(companies.get(listPosition).getCompanyName());
                    companies.remove(listPosition);
                    adapter.notifyDataSetChanged();

                } else {
                    //Gör ingenting
                }
            }
        });

        builder.show();

    }

}
