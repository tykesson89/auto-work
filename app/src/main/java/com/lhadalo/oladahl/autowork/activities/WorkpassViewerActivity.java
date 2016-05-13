package com.lhadalo.oladahl.autowork.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.widget.Toast;

import com.lhadalo.oladahl.autowork.R;
import com.lhadalo.oladahl.autowork.Tag;
import com.lhadalo.oladahl.autowork.database.DatabaseContract;
import com.lhadalo.oladahl.autowork.database.FetchWorkpasses;
import com.lhadalo.oladahl.autowork.database.SQLiteDB;
import com.lhadalo.oladahl.autowork.fragments.WorkpassViewerFragment;

import java.text.SimpleDateFormat;
import java.util.Locale;

import UserPackage.Company;
import UserPackage.Workpass;

/**
 * Created by oladahl on 16-05-10.
 */
public class WorkpassViewerActivity extends AppCompatActivity
        implements WorkpassViewerFragment.OnFragmentInteraction{
    private WorkpassViewerFragment fragment;
    SQLiteDB db = new SQLiteDB(this);
    private Workpass workpass = null;

    private int resultCode = RESULT_CANCELED;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workpass_viewer);
        initFragment();

        try {
            workpass = db.getWorkpass(
                    getIntent().getLongExtra(DatabaseContract.WorkpassEntry.WORKPASS_ID, -1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initFragment() {
        fragment = new WorkpassViewerFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_workpass_viewer, fragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        setWorkpassData();
    }

    private void setWorkpassData(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("kk:mm", Locale.getDefault());

        if (workpass != null) {
            fragment.setTxtTitle(workpass.getTitle());

            Company company = db.getCompany(workpass.getCompanyID());
            fragment.setImgWorkplace(
                    getResources().getDrawable(R.drawable.ic_business_center_black_24dp));
            fragment.setTxtWorkplace(company.getCompanyName() + " \n(" + company.getHourlyWage() + " kr/h)");


            String date = dateFormat.format(workpass.getStartDateTime().getTime());
            String time = timeFormat.format(workpass.getStartDateTime().getTime())
                    + " - " + timeFormat.format(workpass.getEndDateTime().getTime());

            fragment.setImgTime(getResources().getDrawable(R.drawable.ic_query_builder_black_24dp));
            fragment.setTxtTime(date + ", " + time + "\n" + workpass.getWorkingHours() + " timmar");

            fragment.setImgSalary(getResources().getDrawable(R.drawable.ic_attach_money_black_24dp));
            fragment.setTxtSalary(String.valueOf(workpass.getSalary()) + " kr");

            if(workpass.getBreaktime() == 0){
                fragment.setLayoutBreakGone();
            }
            else{
                fragment.setImgBreak(getResources().getDrawable(R.drawable.ic_av_timer_black_24dp));
                fragment.setTxtBreak(String.valueOf(workpass.getBreaktime()));
            }

            if(workpass.getNote().isEmpty()){
                fragment.setLayoutNoteGone();
            }
            else{
                fragment.setImgNote(getResources().getDrawable(R.drawable.ic_note_black_24dp));
                fragment.setTxtNote(workpass.getNote());
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            workpass = db.getWorkpass(workpass.getWorkpassID());
            setWorkpassData();
            this.resultCode = RESULT_OK;
        }
    }

    @Override
    public void onClickFAB() {
        Intent intent = new Intent(this, AddWorkpassActivity.class);
        intent.putExtra(DatabaseContract.WorkpassEntry.WORKPASS_ID, workpass.getWorkpassID());
        intent.putExtra(Tag.REQUEST_CODE, Tag.UPDATE_WORKPASS_REQUEST);

        startActivityForResult(intent, Tag.UPDATE_WORKPASS_REQUEST);
    }

    @Override
    public void onClickDelete() {
        createAlertDialog();
    }

    @Override
    public void onClickClose() {
        setResult(resultCode);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(resultCode);
        finish();
    }

    private void createAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Delete?");
        alertDialog.setMessage("You can't undo this (for now)");

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent data = new Intent();
                data.putExtra(Tag.LIST_POSITION, getIntent().getIntExtra(Tag.LIST_POSITION, -1));
                workpass.setActionTag(Tag.ON_DELETE_WORKPASS);
                workpass.setIsSynced(Tag.IS_NOT_SYNCED);
                db.updateWorkpass(workpass);

                setResult(Tag.RESULT_WORKPASS_DELETED, data);

                finish();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }
}