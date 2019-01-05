package com.navigation.edgescreen.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.navigation.edgescreen.Common.AppUtils;
import com.navigation.edgescreen.Common.Contains;
import com.navigation.edgescreen.R;
import com.navigation.edgescreen.adapter.PhoneBookSettingAdapter;
import com.navigation.edgescreen.data.Contact;
import com.navigation.edgescreen.data.PhoneBook;
import com.navigation.edgescreen.Common.SharePreContact;

import java.util.ArrayList;

public class PhoneBookActivity extends AppCompatActivity {
    private RecyclerView rvPhone;
    private PhoneBookSettingAdapter phoneBookAdapter;
    private ArrayList<PhoneBook> listPos;
    private int mPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_book);
        rvPhone = findViewById(R.id.rvPhoneBook);
        listPos = AppUtils.getListPosContact();
        phoneBookAdapter = new PhoneBookSettingAdapter(this, listPos);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        rvPhone.setLayoutManager(gridLayoutManager);
        rvPhone.setAdapter(phoneBookAdapter);
        phoneBookAdapter.setOnClickItemPhoneBook(new PhoneBookSettingAdapter.OnClickItemPhoneBook() {
            @Override
            public void onClickPhone(Contact contact, int pos) {
                mPos = pos;
                if (contact == null) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, 100);
                } else {

                }
            }

            @Override
            public void onDelete(Contact contact, int pos) {
                SharePreContact.getInstance(PhoneBookActivity.this).removeContact(pos + "");
                phoneBookAdapter.notifyDataSetChanged();
                Intent intent = new Intent();
                intent.setAction(Contains.UPDATERVPHONE);
                sendBroadcast(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri contactData = data.getData();
            Cursor c = getContentResolver().query(contactData, null, null, null, null);

            if (c.moveToFirst()) {
                int id = c.getInt(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                SharePreContact.getInstance(this).addContact(mPos + "", id);
                phoneBookAdapter.notifyDataSetChanged();
            }
        }
    }


}
