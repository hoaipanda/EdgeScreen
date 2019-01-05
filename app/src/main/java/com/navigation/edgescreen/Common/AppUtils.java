package com.navigation.edgescreen.Common;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.util.Log;

import com.navigation.edgescreen.R;
import com.navigation.edgescreen.data.AppInfo;
import com.navigation.edgescreen.data.Contact;
import com.navigation.edgescreen.data.PhoneBook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AppUtils {

    private static final String[] COL = {"contact_id",
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.STARRED,
            ContactsContract.CommonDataKinds.Email.DISPLAY_NAME
    };

    private static final String[] COL_EMAIL = {ContactsContract.CommonDataKinds.Email.DATA};

    public static Contact getContacts(Context ctx, int mId) {
        Contact contact = null;
        ContentResolver contentResolver = ctx.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, COL,
                null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            if (id == mId) {
                String uri = cursor.getString(1);
                String nameNow = cursor.getString(2);
                if (nameNow.length() > 0) {
                    String startName = nameNow.substring(0, 1).toUpperCase();
                    nameNow = startName + nameNow.substring(1);
                }

                String num = cursor.getString(3);
                String email = getEmail(ctx, mId);
                contact = new Contact(id, nameNow, num, uri, email);
                break;
            }


        }
        cursor.close();
        return contact;

    }

    public static ArrayList<String> getListTheme(Context context) {
        AssetManager assetManager = context.getAssets();
        String[] files = new String[0];
        try {
            files = assetManager.list("theme");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<String>(Arrays.asList(files));
    }

    public static ArrayList<String> getListIcon(Context context) {
        AssetManager assetManager = context.getAssets();
        String[] files = new String[0];
        try {
            files = assetManager.list("icon");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<String>(Arrays.asList(files));
    }


    public static void actionOpenApp(Context context, String packagename) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packagename);
        if (launchIntent != null) {
            context.startActivity(launchIntent);//null pointer check in case package name was not found
        }
    }

    public static String getEmail(Context context, int id) {
        String email = getContactOrg(context, id, COL_EMAIL, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        if (email != null) {
            return email;
        }
        return "";
    }


    private static String getContactOrg(Context context, int id, String[] str, String str1) {
        Cursor orgCur;
        String where = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
        String[] whereParameters = new String[]{id + "", str1};
        orgCur = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, str, where, whereParameters, null);
        String orgName = "";
        if (orgCur.getCount() > 0) {
            if (orgCur.moveToFirst()) {
                orgName = orgCur.getString(0);
            }
        }
        orgCur.close();
        return orgName;
    }


    public static ArrayList<AppInfo> getListAppInfo(Context context) {
        ArrayList<AppInfo> res = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packList = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packList.size(); i++) {
            PackageInfo packInfo = packList.get(i);
            if ((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                String appName = packInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
                String pack = packInfo.applicationInfo.packageName;
                Drawable icon = packInfo.applicationInfo.loadIcon(pm);
                res.add(new AppInfo(pack, appName, icon));
            }
        }
        return res;
    }

    public static AppInfo getAppInfo(Context context, String pack) {
        AppInfo res = null;
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packList = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packList.size(); i++) {
            PackageInfo packInfo = packList.get(i);
            if ((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                String mPack = packInfo.applicationInfo.packageName;
                if (mPack.equals(pack)) {
                    String appName = packInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
                    Drawable icon = packInfo.applicationInfo.loadIcon(pm);
                    res = new AppInfo(pack, appName, icon);
                }

            }
        }
        return res;
    }


    public static ArrayList<PhoneBook> getListPosContact() {
        ArrayList<PhoneBook> list = new ArrayList<>();
        list.add(new PhoneBook(1, R.color.color1));
        list.add(new PhoneBook(2, R.color.color2));
        list.add(new PhoneBook(3, R.color.color3));
        list.add(new PhoneBook(4, R.color.color4));
        list.add(new PhoneBook(5, R.color.color5));
        list.add(new PhoneBook(6, R.color.color6));
        return list;
    }
}
