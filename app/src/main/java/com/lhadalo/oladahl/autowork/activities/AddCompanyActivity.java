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

import com.lhadalo.oladahl.autowork.CompanyListAdapter;
import com.lhadalo.oladahl.autowork.StartService;
import com.lhadalo.oladahl.autowork.Tag;
import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.database.DatabaseContract;
import com.lhadalo.oladahl.autowork.database.SQLiteDB;

import java.util.List;

import UserPackage.Company;

public class AddCompanyActivity extends AppCompatActivity {
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


        adapter = new CompanyListAdapter(this, companies);
        if (listView != null) {
            listView.setAdapter(adapter);
        }

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
            Intent intent = new Intent(this, AddCompanySettingsActivity.class);
            intent.putExtra(Tag.REQUEST_CODE, Tag.ADD_COMPANY_REQUEST);
            startActivityForResult(intent, Tag.ADD_COMPANY_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onItemOptionsDialogSelected(int option, int listPosition){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == Tag.ADD_COMPANY_REQUEST){
                Company companyAdded = db.getLastAddedCompany();
                companies.add(companyAdded);
                adapter.notifyDataSetChanged();
            }
            else if(requestCode == Tag.CHANGE_COMPANY_REQUEST){
                int pos = data.getIntExtra(Tag.LIST_POSITION, -1);
                if(pos >= 0){
                    long id = data.getLongExtra(DatabaseContract.CompanyEntry.COMPANY_ID, -1);
                    Company changedComp = db.getCompany(id);

                    companies.set(pos, changedComp);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void createOptionsDialog(final int listPosition){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.edit));

        builder.setItems(new String[]{getString(R.string.changeCompany), getString(R.string.deleteCompany), getString(R.string.cancel)}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                if (position == 0) {

                    Intent intent = new Intent(getApplicationContext(), AddCompanySettingsActivity.class);
                    intent.putExtra(DatabaseContract.CompanyEntry.COMPANY_ID, companies.get(listPosition).getCompanyId());
                    intent.putExtra(Tag.LIST_POSITION, listPosition);
                    intent.putExtra(Tag.REQUEST_CODE, Tag.CHANGE_COMPANY_REQUEST);
                    startActivityForResult(intent, Tag.CHANGE_COMPANY_REQUEST);

                } else if (position == 1) {
                    Company companyToDelete = companies.get(listPosition);

                    companyToDelete.setActionTag(Tag.ON_DELETE_COMPANY);
                    companyToDelete.setIsSynced(Tag.IS_NOT_SYNCED);
                    db.changeCompany(companyToDelete);
                    companies.remove(listPosition);
                    StartService.startService(getApplicationContext());
                    adapter.notifyDataSetChanged();

                } else {
                    //Gör ingenting
                }
            }
        });

        builder.show();

    }

}
