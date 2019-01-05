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

public class PhoneBookAdapter extends RecyclerView.Adapter<PhoneBookAdapter.ViewHolder> {
    private ArrayList<PhoneBook> listPos;
    private Context context;


    public PhoneBookAdapter(Context context, ArrayList<PhoneBook> listPos) {
        this.context = context;
        this.listPos = listPos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.item_phonebook, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final int pos = listPos.get(i).getPos();
        Glide.with(context).load(Uri.parse("file:///android_asset/imagephone/" + pos + ".png")).into(viewHolder.imPhone);
        final Contact contact = AppUtils.getContacts(context, SharePreContact.getInstance(context).getContact(pos + ""));
        if (contact != null) {
            viewHolder.imAdd.setVisibility(View.GONE);
            viewHolder.tvChar.setVisibility(View.VISIBLE);
            viewHolder.tvChar.setText(contact.getName().substring(0, 1));
            viewHolder.tvNamePhone.setText(contact.getName());
            viewHolder.tvNamePhone.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imAdd.setVisibility(View.VISIBLE);
            viewHolder.tvChar.setVisibility(View.GONE);
            viewHolder.tvNamePhone.setVisibility(View.GONE);
        }

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
        return listPos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imPhone, imAdd;
        TextView tvNamePhone, tvChar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imPhone = itemView.findViewById(R.id.imPhone);
            imAdd = itemView.findViewById(R.id.imAdd);
            tvNamePhone = itemView.findViewById(R.id.tvNamePhone);
            tvChar = itemView.findViewById(R.id.tvChar);
        }
    }

    public interface OnClickItemPhoneBook {
        void onClickPhone(Contact contact, int pos);

    }

    public OnClickItemPhoneBook onClickItemPhoneBook;

    public void setOnClickItemPhoneBook(OnClickItemPhoneBook onClickItemPhoneBook) {
        this.onClickItemPhoneBook = onClickItemPhoneBook;
    }
}
