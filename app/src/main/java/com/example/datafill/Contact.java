package com.example.datafill;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Contacts;
import android.provider.ContactsContract;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Contact {
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void RequestPhoneStatePermission(Context context) {
        int permissionCheck = context.getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_CONTACTS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS}, 100);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static List<String[]> getContactList(Context context) {
        List<String[]> result = new ArrayList<String[]>();
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                RequestPhoneStatePermission(context);
            }
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            Uri uri = intent.getData();
            ContentResolver cr = context.getContentResolver();
            Cursor cursor = cr.query(uri, null, null, null, null);
            //移动到游标到联系人表第一行
            if (cursor != null && cursor.moveToFirst()) {
                String nameStr = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String phoneStr = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String[] ContactFStr = new String[]{nameStr, phoneStr};
                result.add(ContactFStr);
                while(cursor.moveToNext()) {
                    //联系人姓名
                    nameStr = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    //读取通讯录的号码
                    phoneStr = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String[] ContactStr = new String[]{nameStr, phoneStr};
                    result.add(ContactStr);
                }
                cursor.close();
            } else {
                System.out.println("db cursor not exist.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    // TODO 随机生成的电话号码应能正确被识别到所属城市，并且大部分应该与手机号处于同一城市，APK实现时需对这块进行改进。
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static String getPhoneNumber(int type) {
        String[] phoneNumber_head = {"185", "139", "128", "133", "177", "180", "130", "131", "132", "153", "182", "158", "159", "188", "189"};
        Random phoneNumberRandom = new Random();
        int count = phoneNumberRandom.nextInt(phoneNumber_head.length - 1);
        String phoneNumber = phoneNumber_head[count];
        if(1 == type)
            phoneNumber += " ";
        phoneNumber += phoneNumberRandom.nextInt(9);
        phoneNumber += phoneNumberRandom.nextInt(9);
        phoneNumber += phoneNumberRandom.nextInt(9);
        phoneNumber += phoneNumberRandom.nextInt(9);
        if(1 == type)
            phoneNumber += " ";
        phoneNumber += phoneNumberRandom.nextInt(9);
        phoneNumber += phoneNumberRandom.nextInt(9);
        phoneNumber += phoneNumberRandom.nextInt(9);
        phoneNumber += phoneNumberRandom.nextInt(9);
        return phoneNumber;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static String getPhoneNumber() {
        return getPhoneNumber(1);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void show(Context context) {
        String result = "";
        List<String[]> contactList = getContactList(context);
        int num = contactList.size();
        for(int i = 0; i < num; i++) {
            result = result + "name:" + contactList.get(i)[0] + ",phoneNum:" + contactList.get(i)[1] + ";  ";
        }
        System.out.println(result);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void clear(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            RequestPhoneStatePermission(context);
        }

        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        context.getContentResolver().delete(uri, null, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void randomAdd(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            RequestPhoneStatePermission(context);
        }

        Random random = new Random();
        int contact_counts = 10 + random.nextInt(20);
        Name name = new Name();
        for(int i = 0; i < contact_counts; i++) {
            // 创建一个空的ContentValues
            ContentValues values = new ContentValues();

            Uri rawContactUri = context.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
            long rawContactId = ContentUris.parseId(rawContactUri);
            values.clear();

            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
            // 联系人名字
            values.put(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, name.getFamilyName());
            values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name.getName());
            // 向联系人URI添加联系人名字
            context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
            values.clear();

            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
            // 联系人的电话号码
            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, getPhoneNumber());
            // 电话类型
            values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
            // 向联系人电话号码URI添加电话号码
            context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        }
    }
}
