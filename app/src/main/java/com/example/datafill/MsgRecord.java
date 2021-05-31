package com.example.datafill;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.Telephony;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MsgRecord {
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void RequestPhoneStatePermission(Context context) {
        int permissionCheck = context.getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_CALL_LOG);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_CALL_LOG, Manifest.permission.READ_CALL_LOG}, 100);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void clear(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            RequestPhoneStatePermission(context);
        }
        context.getContentResolver().delete(CallLog.Calls.CONTENT_URI, null, null) ;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void show(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            RequestPhoneStatePermission(context);
        }

        String show_result = "empty";

        Uri uri = Uri.parse("content://sms");
        Cursor c = context.getContentResolver().query(uri, new String[]{"address","date","type","body"}, null, null, null);
        while(c.moveToNext()){
            String address = c.getString(c.getColumnIndex("address"));
            String date = c.getString(c.getColumnIndex("date"));
            String type = c.getString(c.getColumnIndex("type"));
            String body = c.getString(c.getColumnIndex("body"));

            show_result = "address:" + address + "; data:" + date + "; type:" + type + "; body:" + body + ";";
        }

        System.out.println(show_result);
    }

    private static void MsgRecordsAdd(Context context, String address, String date, String type, String body) {
        try {
            Uri uri = Uri.parse("content://sms/inbox");
            ContentValues values = new ContentValues();
            values.put("address", address);
            values.put("date", date);
            values.put("type", type);
            values.put("body", body);
            Uri result = context.getContentResolver().insert(uri, values);
            if(null == result)
                System.out.println("failed to add msg record");

        } catch (Exception e) {
            System.out.println("failed to MsgRecordsAdd: " + e.getMessage());
        }
    }

    // TODO 通话记录99%都应该在白天，因此须对随机值进行限制已达到足够的仿真度，APK实现时需对这块进行改进。
    private static long getLongMills(long seconds) {
        Random random = new Random();
        return (long)random.nextInt((int)seconds) * (long)random.nextInt(1000);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void randomAdd(Context context, int num) {
        System.out.println("11111");
        // 这里的传参一定要在真机上获取，比如 收到短信参数是什么，发送是什么
        MsgRecordsAdd(context, "130 5506 1444", "1622287579103", "1", "jiubugaosuni3");
        MsgRecordsAdd(context, "13566061111", "1622287579001", "1", "jiubugaosuni4");
        System.out.println("msg add succeed!");
    }
}
