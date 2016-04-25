package com.lhadalo.oladahl.autowork;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import com.lhadalo.oladahl.autowork.activities.MainActivity;
import com.lhadalo.oladahl.autowork.database.FetchWorkpasses;

import java.util.Calendar;

/**
 * Created by oladahl on 16-04-25.
 */
public class DrawerListener implements NavigationView.OnNavigationItemSelectedListener {
    MainActivity activity;

    public DrawerListener(Context context){
        this.activity = (MainActivity)context;
    }


    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        activity.closeDrawer();
        switch (item.getItemId()) {
            case R.id.drawer_jan:
                FetchWorkpasses.newInstance(activity, Tag.ON_UPDATE_LIST).execute(Calendar.JANUARY);
                return true; 
            case R.id.drawer_feb:
                FetchWorkpasses.newInstance(activity, Tag.ON_UPDATE_LIST).execute(Calendar.FEBRUARY);
                return true;
            case R.id.drawer_mar:
                FetchWorkpasses.newInstance(activity, Tag.ON_UPDATE_LIST).execute(Calendar.MARCH);
                return true;
            case R.id.drawer_apr:
                FetchWorkpasses.newInstance(activity, Tag.ON_UPDATE_LIST).execute(Calendar.APRIL);
                return true;
            case R.id.drawer_may:
                FetchWorkpasses.newInstance(activity, Tag.ON_UPDATE_LIST).execute(Calendar.MAY);
                return true;
            case R.id.drawer_jun:
                FetchWorkpasses.newInstance(activity, Tag.ON_UPDATE_LIST).execute(Calendar.JUNE);
                return true;
            case R.id.drawer_jul:
                FetchWorkpasses.newInstance(activity, Tag.ON_UPDATE_LIST).execute(Calendar.JULY);
                return true;
            case R.id.drawer_aug:
                FetchWorkpasses.newInstance(activity, Tag.ON_UPDATE_LIST).execute(Calendar.AUGUST);
                return true;
            case R.id.drawer_sep:
                FetchWorkpasses.newInstance(activity, Tag.ON_UPDATE_LIST).execute(Calendar.SEPTEMBER);
                return true;
            case R.id.drawer_oct:
                FetchWorkpasses.newInstance(activity, Tag.ON_UPDATE_LIST).execute(Calendar.OCTOBER);
                return true;
            case R.id.drawer_nov:
                FetchWorkpasses.newInstance(activity, Tag.ON_UPDATE_LIST).execute(Calendar.NOVEMBER);
                return true;
            case R.id.drawer_dec:
                FetchWorkpasses.newInstance(activity, Tag.ON_UPDATE_LIST).execute(Calendar.DECEMBER);
                return true;
        }

        return false;
    }
}
