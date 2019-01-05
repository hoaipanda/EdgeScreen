package com.navigation.edgescreen.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.navigation.edgescreen.R;

import java.util.ArrayList;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ViewHolder> {
    private ArrayList<String> listTheme;
    private Context context;

    public ThemeAdapter(Context context, ArrayList<String> listTheme) {
        this.context = context;
        this.listTheme = listTheme;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.item_theme, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final String theme = listTheme.get(i);
        Glide.with(context).load(Uri.parse("file:///android_asset/theme/" + theme)).into(viewHolder.imTheme);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickItemTheme != null) {
                    onClickItemTheme.onClickTheme(theme);
                    YoYo.with(Techniques.FadeIn)
                            .duration(700)
                            .playOn(viewHolder.itemView);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTheme.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imTheme;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imTheme = itemView.findViewById(R.id.imTheme);
        }
    }

    public OnClickItemTheme onClickItemTheme;

    public void setOnClickItemTheme(OnClickItemTheme onClickItemTheme) {
        this.onClickItemTheme = onClickItemTheme;
    }

    public interface OnClickItemTheme {
        void onClickTheme(String theme);
    }
}
