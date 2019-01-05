package com.navigation.edgescreen.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.navigation.edgescreen.Common.AppUtils;
import com.navigation.edgescreen.Common.Contains;
import com.navigation.edgescreen.R;
import com.navigation.edgescreen.activity.ListAppActivity;
import com.navigation.edgescreen.adapter.AppAdapter;
import com.navigation.edgescreen.data.AppInfo;

import java.util.ArrayList;
import java.util.Collections;


public class AppFragment extends Fragment {
    private RecyclerView rvApp;
    private AppAdapter appAdapter;
    private Activity activity;
    private ArrayList<AppInfo> listApp = new ArrayList<>();

    public AppFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app, container, false);
        rvApp = view.findViewById(R.id.rvApp);
        activity = getActivity();
        new LoadApp().execute();
        return view;
    }


    public static ArrayList<AppInfo> getListApp(Context context) {
        ArrayList<AppInfo> list = new ArrayList<>();

        PackageManager pm = context.getPackageManager();

        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        ArrayList<ResolveInfo> mList = (ArrayList<ResolveInfo>) pm
                .queryIntentActivities(i, PackageManager.PERMISSION_GRANTED);
        Collections.sort(mList, new ResolveInfo.DisplayNameComparator(pm));


        for (ResolveInfo resInfo : mList) {
            if (!resInfo.activityInfo.applicationInfo.packageName.equals(context.getPackageName())) {
                if (resInfo.activityInfo.applicationInfo.packageName.equals("com.android.settings") || resInfo.activityInfo.applicationInfo.packageName.equals("com.android.gallery3d") || resInfo.activityInfo.applicationInfo.packageName.equals("com.android.chrome") || resInfo.activityInfo.applicationInfo.packageName.equals("com.android.contacts") || resInfo.activityInfo.applicationInfo.packageName.equals("com.android.email") || resInfo.activityInfo.applicationInfo.packageName.equals("com.android.calendar"))
                    list.add(new AppInfo(resInfo.activityInfo.applicationInfo.packageName, resInfo.activityInfo.applicationInfo.loadLabel(pm)
                            .toString(), resInfo.activityInfo.applicationInfo.loadIcon(pm)));
            }

        }

        return list;

    }


    private class LoadApp extends AsyncTask<Void, Void, ArrayList<AppInfo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            rvApp.setVisibility(View.GONE);
        }

        @Override
        protected ArrayList<AppInfo> doInBackground(Void... voids) {
            ArrayList<AppInfo> list = getListApp(activity);
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<AppInfo> appInfos) {
            super.onPostExecute(appInfos);
            listApp = appInfos;
            listApp.add(null);
            rvApp.setVisibility(View.VISIBLE);
            appAdapter = new AppAdapter(listApp);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2);
            rvApp.setLayoutManager(gridLayoutManager);
            rvApp.setAdapter(appAdapter);
            appAdapter.setOnClickItemApp(new AppAdapter.OnClickItemApp() {
                @Override
                public void onClickApp(AppInfo appInfo) {
                    if (appInfo == null) {
                        startActivityForResult(new Intent(getActivity(), ListAppActivity.class), 100);
                    } else {
                        AppUtils.actionOpenApp(activity, appInfo.getPack());
                    }
                }
            });

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                String pack = data.getStringExtra(Contains.APPINFO);
                listApp.add(AppUtils.getAppInfo(activity, pack));
                appAdapter.notifyDataSetChanged();
            }
        }
    }
}
