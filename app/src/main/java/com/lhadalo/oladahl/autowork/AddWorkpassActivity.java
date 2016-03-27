package com.lhadalo.oladahl.autowork;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by oladahl on 16-03-26.
 */
public class AddWorkpassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_workpass);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_1);
        setSupportActionBar(toolbar);
        
        setTitle("Add workpass");


        LinearLayout layoutWorkplace = (LinearLayout)findViewById(R.id.txt_layout_workplace);

        LinearLayout layoutStart = (LinearLayout)findViewById(R.id.txt_layout_start);
        LinearLayout layoutStop = (LinearLayout)findViewById(R.id.txt_layout_stop);
        LinearLayout layoutLunch = (LinearLayout)findViewById(R.id.txt_layout_lunch);
        LinearLayout layoutAddNote = (LinearLayout)findViewById(R.id.txt_layout_add_notes);

        TextView text1 = (TextView)layoutWorkplace.getChildAt(1);
        TextView textDate1 = (TextView)layoutStart.getChildAt(1);
        TextView textTime1 = (TextView)layoutStart.getChildAt(2);

        TextView textDate2 = (TextView)layoutStop.getChildAt(1);

        TextView textTime2 = (TextView)layoutStop.getChildAt(2);
        TextView text4 = (TextView)layoutLunch.getChildAt(1);
        TextView text5 = (TextView)layoutAddNote.getChildAt(1);

        text1.setText("Workplace");
        textDate1.setText("Sun, Mar 27, 2016");
        textTime1.setText("15:15");

        textDate2.setText("Sun, Mar 27, 2016");
        textTime2.setText("18:15");

        text4.setText("Add break");
        text5.setText("Add note");


    }
}
