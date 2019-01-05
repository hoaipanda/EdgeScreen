package com.navigation.edgescreen.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.navigation.edgescreen.ItemTouchHelperAdapter;
import com.navigation.edgescreen.R;
import com.navigation.edgescreen.data.Screen;

import java.util.ArrayList;
import java.util.Collections;

public class ScreenAdapter extends RecyclerView.Adapter<ScreenAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private ArrayList<Screen> listScreen;
    private Context context;

    public ScreenAdapter(Context context, ArrayList<Screen> listScreen) {
        this.context = context;
        this.listScreen = listScreen;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.item_sreen, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final String screen = listScreen.get(i).getImage();
        Log.e("hoaiii", listScreen.get(i).getImage());
        Glide.with(context).load(Uri.parse("file:///android_asset/screen/" + screen)).into(viewHolder.imScreen);
        viewHolder.lyEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.FadeIn)
                        .duration(700)
                        .playOn(viewHolder.lyEdit);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listScreen.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(listScreen, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(listScreen, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        listScreen.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imScreen;
        RelativeLayout lyEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imScreen = itemView.findViewById(R.id.imScreen);
            lyEdit = itemView.findViewById(R.id.lyEdit);
        }
    }
}
