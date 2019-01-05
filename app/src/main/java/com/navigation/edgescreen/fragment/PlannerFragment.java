package com.navigation.edgescreen.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.navigation.edgescreen.R;
import com.navigation.edgescreen.Utility;
import com.navigation.edgescreen.adapter.PlannerAdapter;
import com.navigation.edgescreen.data.EventPlanner;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlannerFragment extends Fragment {
    private ImageView imAdd;
    private TextView tvTime;
    private RecyclerView rvPlanner;
    private ArrayList<EventPlanner> listEvent = new ArrayList<>();
    private PlannerAdapter plannerAdapter;

    public PlannerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planner, container, false);
        imAdd = view.findViewById(R.id.imAdd);
        tvTime = view.findViewById(R.id.tvTime);
        rvPlanner = view.findViewById(R.id.rvPlanner);
        imAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                startActivity(intent);
            }
        });

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        String strMonth = "";
        switch (month) {
            case 1:
                strMonth = "January";
                break;
            case 2:
                strMonth = "February";
                break;
            case 3:
                strMonth = "March";
                break;
            case 4:
                strMonth = "April";
                break;
            case 5:
                strMonth = "May";
                break;
            case 6:
                strMonth = "June";
                break;
            case 7:
                strMonth = "July";
                break;
            case 8:
                strMonth = "August";
                break;
            case 9:
                strMonth = "September";
                break;
            case 10:
                strMonth = "October";
                break;
            case 11:
                strMonth = "November";
                break;
            case 12:
                strMonth = "December";
                break;

        }
        tvTime.setText(strMonth + "," + calendar.get(Calendar.YEAR));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        listEvent = Utility.readCalendarEvent(getActivity());
        if (listEvent.size() == 0) {
            listEvent.add(new EventPlanner("", "PLANNER", 0, 0, "Have a nice day!"));
        }
        plannerAdapter = new PlannerAdapter(listEvent);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        rvPlanner.setLayoutManager(gridLayoutManager);
        rvPlanner.setAdapter(plannerAdapter);
        plannerAdapter.setOnClickItemEventPlanner(new PlannerAdapter.OnClickItemEventPlanner() {
            @Override
            public void onClickEvent(EventPlanner eventPlanner) {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event/" + eventPlanner.getCalendar_id());
                startActivity(intent);
            }
        });
    }
}
