package com.example.datafill;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class MmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

    }

    private static void MsgRecordsAdd(Context context, String address, String date, String type, String body) {
        try {
            Uri uri = Uri.parse("content://sms/inbox");
            ContentValues values = new ContentValues();
            values.put("address", address);
            values.put("date", date);
            values.put("type", type);
            values.put("body", body);
            context.getContentResolver().insert(uri, values);

        } catch (Exception e) {
            System.out.println("failed to MsgRecordsAdd: " + e.getMessage());
        }
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
