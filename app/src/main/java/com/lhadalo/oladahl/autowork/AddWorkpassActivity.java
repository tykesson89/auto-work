package com.lhadalo.oladahl.autowork;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        LinearLayout layout = (LinearLayout)findViewById(R.id.test_layout);
        LinearLayout layout2 = (LinearLayout)findViewById(R.id.test2_layout);

        TextView test= (TextView)layout.getChildAt(1);
        TextView test2= (TextView)layout2.getChildAt(1);


        test.setText("Random text");
        test2.setText("Random text 2");
    }
}
