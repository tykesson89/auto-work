package com.lhadalo.oladahl.autowork.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.database.DatabaseContract;
import com.lhadalo.oladahl.autowork.database.SQLiteDB;

import java.util.List;

import UserPackage.Workpass;

/**
 * Created by oladahl on 16-05-10.
 */
public class WorkpassViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_workpass_viewer);
        SQLiteDB db = new SQLiteDB(this);
        Workpass pass = null;
        try {
            pass = db.getWorkpass(getIntent().getLongExtra(DatabaseContract.WorkpassEntry.WORKPASS_ID, -1));
        } catch(Exception e){
            e.printStackTrace();
        }

        if(pass != null) {

            LinearLayout layoutWorkplace = (LinearLayout)findViewById(R.id.txt_layout_workplace);
            ImageView imgWorkplace = (ImageView)layoutWorkplace.getChildAt(0);
            imgWorkplace.setImageDrawable(getResources().getDrawable(R.drawable.ic_business_center_black_24dp));

            TextView txtWorkplace = (TextView)layoutWorkplace.getChildAt(1);
            txtWorkplace.setText(db.getCompany(pass.getCompanyID()).getCompanyName());


            LinearLayout time = (LinearLayout)findViewById(R.id.txt_layout_start);
            TextView textView = (TextView)time.getChildAt(1);

            textView.setText("Sunday, Mars 27, 2016\n13:45 - 15:00");
        }
    }
}