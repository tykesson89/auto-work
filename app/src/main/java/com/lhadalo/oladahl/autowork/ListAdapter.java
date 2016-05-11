package com.lhadalo.oladahl.autowork;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.GregorianCalendar;
import java.util.List;

import UserPackage.Workpass;

/**
 * Created by oladahl on 16-04-07.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    public interface ItemClickListener {
        void onItemClick(int position);
    }

    private Context context;
    ItemClickListener clickListener;
    List<Workpass> content;

    public ListAdapter(Context context, ItemClickListener clickListener, List<Workpass> content) {
        this.context = context;
        this.clickListener = clickListener;
        this.content = content;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowLayout = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.row_layout, parent, false);

        return new ViewHolder(rowLayout);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int pos) {
        //Sätt information från modell
        holder.txtTitleList.setText(content.get(pos).getTitle());
    
        GregorianCalendar start = content.get(pos).getStartDateTime();
        GregorianCalendar end = content.get(pos).getEndDateTime();

        String date = DateUtils.formatDateTime(context,
                start.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE);

        String time =
                DateUtils.formatDateTime(context,
                        start.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME) + " - "
                        + DateUtils.formatDateTime(context, end.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME);

        holder.txtDateList.setText(date);
        holder.txtTimeList.setText(time);

        //Sätt lyssnare i ViewHolder
        holder.setClickListener(clickListener);
    }


    @Override
    public int getItemCount() {
        return content.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtTitleList, txtDateList, txtTimeList;
        private ItemClickListener clickListener;

        public ViewHolder(View rowLayout) {
            super(rowLayout);

            //Initiera element
            txtTitleList = (TextView)rowLayout.findViewById(R.id.txt_title_list);
            txtDateList = (TextView)rowLayout.findViewById(R.id.txt_date_list);
            txtTimeList = (TextView)rowLayout.findViewById(R.id.txt_time_list);

            //Initiera lyssnare
            rowLayout.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener clickListener) {
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition());
        }
    }

}
