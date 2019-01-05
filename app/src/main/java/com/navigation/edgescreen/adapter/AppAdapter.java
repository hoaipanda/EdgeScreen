package com.navigation.edgescreen.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.navigation.edgescreen.R;
import com.navigation.edgescreen.data.AppInfo;

import java.util.ArrayList;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {
    private ArrayList<AppInfo> listApp;

    public AppAdapter(ArrayList<AppInfo> listApp) {
        this.listApp = listApp;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.item_app, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final AppInfo appInfo = listApp.get(i);
        if (appInfo != null) {
            viewHolder.tvNameApp.setText(appInfo.getName());
            viewHolder.imApp.setImageDrawable(appInfo.getIcon());
        } else {
            viewHolder.imApp.setImageResource(R.drawable.addapp);
        }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickItemApp != null) {
                    onClickItemApp.onClickApp(appInfo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listApp.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imApp;
        TextView tvNameApp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imApp = itemView.findViewById(R.id.imApp);
            tvNameApp = itemView.findViewById(R.id.tvNameApp);
        }
    }

    public interface OnClickItemApp {
        void onClickApp(AppInfo appInfo);
    }

    public OnClickItemApp onClickItemApp;

    public void setOnClickItemApp(OnClickItemApp onClickItemApp) {
        this.onClickItemApp = onClickItemApp;
    }
}
