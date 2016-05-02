package com.lhadalo.oladahl.autowork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import UserPackage.Company;

/**
 * Created by stoffe on 2016-04-28.
 */
public class CompanyListAdapter extends ArrayAdapter<Company> {
    private Context context;
    private List<Company> companies;

    public CompanyListAdapter(Context context, List<Company> companies) {
        super(context, -1, companies);
        this.context = context;
        this.companies = companies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_layout_company_settings, parent, false);

        TextView textCompany = (TextView)rowView.findViewById(R.id.txt_company);
        TextView textHourly = (TextView)rowView.findViewById(R.id.txt_hourly);

        textCompany.setText(companies.get(position).getCompanyName());
        textHourly.setText(companies.get(position).getHourlyWage() + " Kr");

        return rowView;
    }
}
