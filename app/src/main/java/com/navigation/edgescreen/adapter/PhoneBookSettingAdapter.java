package com.navigation.edgescreen.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.navigation.edgescreen.Common.AppUtils;
import com.navigation.edgescreen.R;
import com.navigation.edgescreen.data.Contact;
import com.navigation.edgescreen.data.PhoneBook;
import com.navigation.edgescreen.Common.SharePreContact;

import java.util.ArrayList;

public class PhoneBookSettingAdapter extends RecyclerView.Adapter<PhoneBookSettingAdapter.ViewHolder> {

    private ArrayList<PhoneBook> list;
    private Context context;

    public PhoneBookSettingAdapter(Context context, ArrayList<PhoneBook> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.item_phone_setting, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final PhoneBook phoneBook = list.get(i);
        final int pos = phoneBook.getPos();
        Glide.with(context).load(Uri.parse("file:///android_asset/imagephone/" + pos + ".png")).into(viewHolder.imPhone);
        final Contact contact = AppUtils.getContacts(context, SharePreContact.getInstance(context).getContact(pos + ""));
        if (contact != null) {
            viewHolder.imAdd.setVisibility(View.GONE);
            viewHolder.tvChar.setVisibility(View.VISIBLE);
            viewHolder.tvChar.setText(contact.getName().substring(0, 1));
            viewHolder.tvName.setText(contact.getName());
            viewHolder.imSub.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imSub.setVisibility(View.GONE);
            viewHolder.tvName.setText("Add Contact");
            viewHolder.tvChar.setVisibility(View.GONE);
            viewHolder.imAdd.setVisibility(View.VISIBLE);
        }

        viewHolder.imSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickItemPhoneBook != null) {
                    onClickItemPhoneBook.onDelete(contact, pos);
                }
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickItemPhoneBook != null) {
                    onClickItemPhoneBook.onClickPhone(contact, pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imPhone, imAdd, imSub;
        TextView tvChar, tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imPhone = itemView.findViewById(R.id.imPhone);
            imAdd = itemView.findViewById(R.id.imAdd);
            imSub = itemView.findViewById(R.id.imSub);
            tvChar = itemView.findViewById(R.id.tvChar);
            tvName = itemView.findViewById(R.id.tvNameContact);
        }
    }

    public interface OnClickItemPhoneBook {
        void onClickPhone(Contact contact, int pos);

        void onDelete(Contact contact, int pos);
    }

    public OnClickItemPhoneBook onClickItemPhoneBook;

    public void setOnClickItemPhoneBook(OnClickItemPhoneBook onClickItemPhoneBook) {
        this.onClickItemPhoneBook = onClickItemPhoneBook;
    }
}
