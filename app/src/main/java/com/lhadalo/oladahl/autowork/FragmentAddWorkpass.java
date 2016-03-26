package com.lhadalo.oladahl.autowork;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by oladahl on 16-03-26.
 */
public class FragmentAddWorkpass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_add_workpass);

        TextView textView =(TextView)findViewById(R.id.textView);

        textView.setText(getString(R.string.toast_all_fields_error));
    }
}
