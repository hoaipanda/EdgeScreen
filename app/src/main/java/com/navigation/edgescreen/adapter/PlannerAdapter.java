package com.navigation.edgescreen.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.navigation.edgescreen.R;
import com.navigation.edgescreen.data.EventPlanner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PlannerAdapter extends RecyclerView.Adapter<PlannerAdapter.ViewHolder> {
    private ArrayList<EventPlanner> listEvent;

    public PlannerAdapter(ArrayList<EventPlanner> listEvent) {
        this.listEvent = listEvent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.item_planner, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final EventPlanner eventPlanner = listEvent.get(i);
        viewHolder.tvName.setText(eventPlanner.getNameEvent());
        if (eventPlanner.getStartDate() == 0) {
            viewHolder.tvStart.setText("07:00AM");
        } else {
            viewHolder.tvStart.setText(getHour(eventPlanner.getStartDate()));
        }
        if (eventPlanner.getEndDate() == 0) {
            viewHolder.tvStart.setText("00:00AM");
        } else {
            viewHolder.tvEnd.setText(getHour(eventPlanner.getEndDate()));
        }

        viewHolder.tvDes.setText(eventPlanner.getDescription());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickItemEventPlanner != null) {
                    onClickItemEventPlanner.onClickEvent(eventPlanner);
                }
            }
        });
    }


    public static String getHour(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    @Override
    public int getItemCount() {
        return listEvent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvStart, tvEnd, tvDes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvStart = itemView.findViewById(R.id.tvStart);
            tvEnd = itemView.findViewById(R.id.tvEnd);
            tvDes = itemView.findViewById(R.id.tvDes);
        }
    }

    public interface OnClickItemEventPlanner {
        void onClickEvent(EventPlanner eventPlanner);
    }

    public OnClickItemEventPlanner onClickItemEventPlanner;

    public void setOnClickItemEventPlanner(OnClickItemEventPlanner onClickItemEventPlanner) {
        this.onClickItemEventPlanner = onClickItemEventPlanner;
    }
}
