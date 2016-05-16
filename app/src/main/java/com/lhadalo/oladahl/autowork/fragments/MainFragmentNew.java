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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lhadalo.oladahl.autowork.DividerItemDecoration;
import com.lhadalo.oladahl.autowork.ListAdapter;
import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.VerticalSpaceItemDecoration;

public class MainFragmentNew extends Fragment {
    private static final int VERTICAL_ITEM_SPACE = 48;
    private OnFragmentInteraction callback;
    private TextView tvTitleMonth;
    private TextView tvSalaryMonth;
    private TextView tvHoursMonth;
    private TextView tvSalaryNextPass;
    private TextView tvHoursNextPass;
    private TextView tvTitleNextPass;
    private RecyclerView mainRecyclerList;
    private FloatingActionButton fab;
    private CoordinatorLayout coordinatorLayout;

    public interface OnFragmentInteraction {
        void onFABPressed();
        void setCoordinatorLayout(CoordinatorLayout layout);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_new, container, false);

        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        tvTitleNextPass = (TextView)view.findViewById(R.id.tv_title_next_pass);
        tvHoursNextPass =(TextView)view.findViewById(R.id.tv_hours_next_pass);
        tvSalaryNextPass = (TextView)view.findViewById(R.id.tv_salary_next_pass);

        tvTitleMonth = (TextView)view.findViewById(R.id.tv_title_month);
        tvHoursMonth = (TextView)view.findViewById(R.id.tv_hours_month);
        tvSalaryMonth = (TextView)view.findViewById(R.id.tv_salary_month);

        mainRecyclerList = (RecyclerView)view.findViewById(R.id.main_recycler_list);
        mainRecyclerList.setHasFixedSize(true);

        AppCompatActivity context = (AppCompatActivity)getActivity();
        RecyclerView.LayoutManager mLinearLayoutManager = new LinearLayoutManager(context);
        mainRecyclerList.setLayoutManager(mLinearLayoutManager);

        //mainRecyclerList.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
        mainRecyclerList.addItemDecoration(new DividerItemDecoration(getActivity()));

        fab = (FloatingActionButton)view.findViewById(R.id.fab);
        coordinatorLayout = (CoordinatorLayout)view.findViewById(R.id.coordinator_layout);

        initListeners();
    }

    private void initListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onFABPressed();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        callback.setCoordinatorLayout(coordinatorLayout);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (OnFragmentInteraction) context;
        } catch (ClassCastException e) {
            Log.e("", context.getClass().getCanonicalName() +
                    " must implement OnFragmentInteraction");
        }
    }


    public void setListAdapter(ListAdapter adapter){
        mainRecyclerList.setAdapter(adapter);
    }

    public void setTvTitleMonth(String str){
        tvTitleMonth.setText(str);
    }

    public void setTextTvSalary(String str){
        tvSalaryMonth.setText(str);
    }

    public void setTextTvSalaryPass(String str){
        tvSalaryNextPass.setText(str);
    }

    public void setTextTvNextPass(String str){
        tvTitleNextPass.setText(str);
    }

    public void setTextTvHours(String str){
        tvHoursMonth.setText(str);
    }

    public void setTextTvHoursPass(String str){
        tvHoursNextPass.setText(str);
    }
}
