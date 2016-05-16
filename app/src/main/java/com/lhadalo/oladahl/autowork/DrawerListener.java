package com.lhadalo.oladahl.autowork;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import com.lhadalo.oladahl.autowork.activities.AddCompanyActivity;
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
        activity.closeDrawer(item.getItemId());
        return true;
    }
}
