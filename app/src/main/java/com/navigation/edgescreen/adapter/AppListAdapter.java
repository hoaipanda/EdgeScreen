package com.navigation.edgescreen.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.navigation.edgescreen.R;
import com.navigation.edgescreen.data.AppInfo;
import com.navigation.edgescreen.data.Contact;

import java.util.ArrayList;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {

    private ArrayList<AppInfo> list;

    public AppListAdapter(ArrayList<AppInfo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.item_app_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final AppInfo appInfo = list.get(i);
        viewHolder.imIcon.setImageDrawable(appInfo.getIcon());
        viewHolder.tvNameApp.setText(appInfo.getName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickItemAppList != null) {
                    onClickItemAppList.onClickApp(appInfo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imIcon;
        TextView tvNameApp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imIcon = itemView.findViewById(R.id.imIcon);
            tvNameApp = itemView.findViewById(R.id.tvNameApp);
        }
    }

    public interface OnClickItemAppList {
        void onClickApp(AppInfo appInfo);
    }

    public OnClickItemAppList onClickItemAppList;

    public void setOnClickItemAppList(OnClickItemAppList onClickItemAppList) {
        this.onClickItemAppList = onClickItemAppList;
    }
}
