package com.navigation.edgescreen.fragment;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.navigation.edgescreen.Common.AppUtils;
import com.navigation.edgescreen.Common.Contains;
import com.navigation.edgescreen.R;
import com.navigation.edgescreen.adapter.PhoneBookAdapter;
import com.navigation.edgescreen.data.Contact;
import com.navigation.edgescreen.data.PhoneBook;
import com.navigation.edgescreen.Common.SharePreContact;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhoneBookFragment extends Fragment {
    private RecyclerView rvPhone;
    private PhoneBookAdapter phoneBookAdapter;
    private ArrayList<PhoneBook> listPos = new ArrayList<>();
    private int mPos;
    private RelativeLayout lyDetail;
    private TextView tvName, tvPhone, tvChar;
    private ImageView imCir, imGesture;
    private boolean isDetail;
    private ImageView imSms, imCall, imEmail;
    private RelativeLayout lySms, lyCall, lyEmail;
    private Contact mContact;
    private Activity context;

    public PhoneBookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_book, container, false);
        context = getActivity();
        rvPhone = view.findViewById(R.id.rvPhoneBook);
        lyDetail = view.findViewById(R.id.lyDetail);
        tvName = view.findViewById(R.id.tvNameContact);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvChar = view.findViewById(R.id.tvChar);
        imCir = view.findViewById(R.id.imCir);
        imGesture = view.findViewById(R.id.imGesture);
        imSms = view.findViewById(R.id.imSms);
        imCall = view.findViewById(R.id.imCall);
        imEmail = view.findViewById(R.id.imEmail);
        lySms = view.findViewById(R.id.lySms);
        lyCall = view.findViewById(R.id.lyCall);
        lyEmail = view.findViewById(R.id.lyEmail);

        lyEmail.setOnClickListener(lsClick);
        lyCall.setOnClickListener(lsClick);
        lySms.setOnClickListener(lsClick);

        listPos = AppUtils.getListPosContact();
        phoneBookAdapter = new PhoneBookAdapter(context, listPos);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 6, GridLayoutManager.HORIZONTAL, false);
        rvPhone.setLayoutManager(gridLayoutManager);
        rvPhone.setAdapter(phoneBookAdapter);
        phoneBookAdapter.setOnClickItemPhoneBook(new PhoneBookAdapter.OnClickItemPhoneBook() {
            @Override
            public void onClickPhone(Contact contact, int pos) {
                mPos = pos;
                if (contact == null) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);
                } else {
                    isDetail = true;
                    mContact = contact;
                    animationRightToLeft(rvPhone);
                    animationLeftToRight(lyDetail);

                    rvPhone.setVisibility(View.GONE);
                    tvName.setText(contact.getName());
                    tvPhone.setText(contact.getMobileNumber());
                    tvChar.setText(contact.getName().substring(0, 1));
                    lyDetail.setVisibility(View.VISIBLE);


                    imCall.setColorFilter(getResources().getColor(listPos.get(pos - 1).getColor()));
                    imSms.setColorFilter(getResources().getColor(listPos.get(pos - 1).getColor()));
                    imEmail.setColorFilter(getResources().getColor(listPos.get(pos - 1).getColor()));
                    imCir.setColorFilter(getResources().getColor(listPos.get(pos - 1).getColor()));
                    imGesture.setColorFilter(getResources().getColor(listPos.get(pos - 1).getColor()));

                    animationFadeIn(tvName);
                    animationFadeIn(tvPhone);
                    animationRotate(imCall);
                    animationRotate(imSms);
                    animationRotate(imEmail);


                }
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDetail) {
                    isDetail = false;
                    rvPhone.setVisibility(View.VISIBLE);
                    lyDetail.setVisibility(View.GONE);
                    animationRightToLeft(lyDetail);
                    animationLeftToRight(rvPhone);
                }
            }
        });
        return view;
    }

    private View.OnClickListener lsClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.lyCall:
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mContact.getMobileNumber()));
                    startActivity(intent);
                    break;
                case R.id.lySms:
                    String number = mContact.getMobileNumber();  // The number on which you want to send SMS
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
                    break;
                case R.id.lyEmail:
                    if (mContact.getEmail() == null) {
                        Toast.makeText(context, "Add to email for " + mContact.getName(), Toast.LENGTH_SHORT).show();
                    } else {
                        Intent mailIntent = new Intent(Intent.ACTION_VIEW);
                        Uri data = Uri.parse("mailto:?subject=" + "subject text" + "&body=" + "body text " + "&to=" + mContact.getEmail());
                        mailIntent.setData(data);
                        startActivity(Intent.createChooser(mailIntent, "Send mail..."));
                    }

                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Contains.UPDATERVPHONE);
        context.registerReceiver(receiver, intentFilter);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Contains.UPDATERVPHONE)) {
                phoneBookAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(receiver);
    }

    public static final int PICK_CONTACT = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri contactData = data.getData();
            Cursor c = context.getContentResolver().query(contactData, null, null, null, null);

            if (c.moveToFirst()) {
                int id = c.getInt(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                SharePreContact.getInstance(context).addContact(mPos + "", id);
                phoneBookAdapter.notifyDataSetChanged();
            }
        }
    }

    private void animationLeftToRight(View view) {
        TranslateAnimation translateYAnimation = new TranslateAnimation(900f, 0f, 0f, 0f);
        translateYAnimation.setDuration(400l);
        translateYAnimation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(translateYAnimation);

    }

    private void animationRightToLeft(View view) {
        TranslateAnimation translateYAnimation = new TranslateAnimation(0f, 900f, 0f, 0f);
        translateYAnimation.setDuration(400l);
        translateYAnimation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(translateYAnimation);

    }

    private void animationFadeIn(View view) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1000l);
        view.setAnimation(fadeIn);
    }

    private void animationRotate(View view) {
        RotateAnimation animRotate = new RotateAnimation(-90.0f, 0.0f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animRotate.setDuration(700);
        animRotate.setFillAfter(true);
        view.setAnimation(animRotate);

    }
}
