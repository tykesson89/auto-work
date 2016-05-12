package com.lhadalo.oladahl.autowork.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lhadalo.oladahl.autowork.ListAdapter;
import com.lhadalo.oladahl.autowork.R;

/**
 * Created by oladahl on 16-03-26.
 */
public class MainFragment extends Fragment {
    private OnFragmentInteraction callback;
    private TextView tvSalary,tvSalaryPass,tvHours,tvHoursPass,tv_title_next_pass;
    private Toolbar toolbar;
    private RecyclerView mainRecyclerList;
    private FloatingActionButton fab;
    private CoordinatorLayout coordinatorLayout;
    //private Button btnTry;

    public interface OnFragmentInteraction{
        void onFABPressed();
        void setCoordinatorLayout(CoordinatorLayout layout);
    }

    /*
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); //För att berätta att det finns en meny
    }
    */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        initComponents(view);
        return view;
    }

    private void initComponents(View view){
        tvSalary = (TextView)view.findViewById(R.id.tvSalary);
        tvSalaryPass = (TextView)view.findViewById(R.id.tvSalaryPass);
        tv_title_next_pass = (TextView)view.findViewById(R.id.tv_title_next_pass);
        tvHours = (TextView)view.findViewById(R.id.tvHours);
        tvHoursPass = (TextView)view.findViewById(R.id.tvHoursPass);
        //btnTry=(Button)view.findViewById(R.id.button_addCompany_try);


        mainRecyclerList = (RecyclerView)view.findViewById(R.id.main_recycler_list);
        mainRecyclerList.setHasFixedSize(true);

        AppCompatActivity context = (AppCompatActivity)getActivity();
        RecyclerView.LayoutManager mLinearLayoutManager = new LinearLayoutManager(context);
        mainRecyclerList.setLayoutManager(mLinearLayoutManager);

        fab = (FloatingActionButton)view.findViewById(R.id.fab);
        coordinatorLayout = (CoordinatorLayout)view.findViewById(R.id.coordinator_layout);

        initListeners();
    }

    private void initListeners(){

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onFABPressed();
            }
        });
        /*btnTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onButtonClickTry();
            }
        });*/

    }

    @Override
    public void onStart() {
        super.onStart();

        callback.setCoordinatorLayout(coordinatorLayout);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.callback = (OnFragmentInteraction)context;
    }

    public void setListAdapter(ListAdapter adapter){
        mainRecyclerList.setAdapter(adapter);
    }

    public void setTextTvSalary(String str){

        tvSalary.setText(str);
    }
    public void setTextTvSalaryPass(String str){

        tvSalaryPass.setText(str);
    }
    public void setTextTvNextPass(String str){

        tv_title_next_pass.setText(str);
    }
    public void setTextTvHours(String str){

        tvHours.setText(str);
    }
    public void setTextTvHoursPass(String str){

        tvHoursPass.setText(str);
    }
}
