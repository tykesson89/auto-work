package com.lhadalo.oladahl.autowork.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.lhadalo.oladahl.autowork.ListAdapter;
import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.database.SQLiteDB;

import java.util.List;

import UserPackage.Workpass;

/**
 * Created by oladahl on 16-04-07.
 */
public class TestActivity extends AppCompatActivity implements ListAdapter.ItemClickListener{
    private SQLiteDB database = new SQLiteDB(this);
    private ListAdapter mListAdapter;
    private List<Workpass> workpassList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);

        workpassList = database.getAllWorkpasses();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.workpass_list);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        if(workpassList != null){
            mListAdapter = new ListAdapter(this, workpassList);
            recyclerView.setAdapter(mListAdapter);
        }

    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, String.valueOf(position), Toast.LENGTH_SHORT).show();
    }
}
