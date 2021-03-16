package com.example.datafill;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telecom.Call;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CallRecord {
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
        context.getContentResolver().delete(android.provider.CallLog.Calls.CONTENT_URI, null, null) ;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void show(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            RequestPhoneStatePermission(context);
        }

        String show_result = "No Record";

        Cursor cursor = context.getContentResolver()
                .query(CallLog.Calls.CONTENT_URI, new String[]{
                                CallLog.Calls.CACHED_NAME,  //姓名
                                CallLog.Calls.NUMBER,    //号码
                                CallLog.Calls.TYPE,  //呼入/呼出(2)/未接
                                CallLog.Calls.DATE,  //拨打时间
                                CallLog.Calls.DURATION,   //通话时长
                        }, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);

        if (cursor != null && cursor.getCount() > 0) {
            show_result = "";
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date_today = simpleDateFormat.format(date);
            int times = 0;
            while (cursor.moveToNext()) {
                String callName = cursor.getString(0);  //名称
                String callNumber = cursor.getString(1);  //号码
                //通话类型
                int callType = Integer.parseInt(cursor.getString(2));
                String callTypeStr = "";
                switch (callType) {
                    case CallLog.Calls.INCOMING_TYPE:
                        callTypeStr = "in";
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        callTypeStr = "out";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callTypeStr = "miss";
                        break;
                    default:
                        callTypeStr = "unknown";
                        break;
                }
                //拨打时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date callDate = new Date(Long.parseLong(cursor.getString(3)));
                String callDateStr;
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                callDateStr = sdf.format(callDate);

                //通话时长
                int callDuration = Integer.parseInt(cursor.getString(4));
                int min = callDuration / 60;
                int sec = callDuration % 60;
                String callDurationStr = "";
                if (sec > 0) {
                    if (min > 0) {
                        callDurationStr = min + "分" + sec + "秒";
                    } else {
                        callDurationStr = sec + "秒";
                    }
                }

                /**
                 * callName 名字
                 * callNumber 号码
                 * callTypeStr 通话类型
                 * callDateStr 通话日期
                 * callDurationStr 通话时长
                 * 请在此处执行相关UI或存储操作，之后会查询下一条通话记录
                 */
                show_result = show_result + "" + callName + "," + callNumber
                        + "," + callTypeStr + "," + callDateStr + "," + callDurationStr + "; ";
                times++;
                if(5 == times) {
                    show_result = show_result + "\n";
                    times = 0;
                }
            }
        }
        System.out.println(show_result);
    }

    private static void CallRecordsAdd(String name, String number, long timeBeforeMills, Context context) {
        ContentValues values = new ContentValues();
        if(null != name)
            values.put(CallLog.Calls.CACHED_NAME, name);
        values.put(CallLog.Calls.NUMBER, number);
        values.put(CallLog.Calls.DATE, System.currentTimeMillis() - timeBeforeMills);
        Random random = new Random();
        values.put(CallLog.Calls.DURATION, "" + (5 + random.nextInt(1100)));
        int choice = random.nextInt(6);
        if(choice <= 4) {
            values.put(CallLog.Calls.TYPE, "" + (random.nextInt(2) + 1));
        } else {
            values.put(CallLog.Calls.TYPE, "3");
        }
        values.put(CallLog.Calls.NEW, 0);
        context.getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
    }

    // TODO 通话记录99%都应该在白天，因此须对随机值进行限制已达到足够的仿真度，APK实现时需对这块进行改进。
    private static long getLongMills(long seconds) {
        Random random = new Random();
        return (long)random.nextInt((int)seconds) * (long)random.nextInt(1000);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void randomAdd(Context context, int num) {
        if(num < 10) {
            System.out.println("insert too less records to call record!");
            return;
        }
        int num_of_lastweek = 10;

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            RequestPhoneStatePermission(context);
        }

        long mill_of_week = 7 * 24 * 60 * 60;
        long mill_of_halfyear = 180 * 24 * 60 * 60;

        List<String[]> contactList = Contact.getContactList(context);
        Random random = new Random();
        if((num_of_lastweek * 180) / 7 > num)
            num_of_lastweek = (num * 7) / 180;
        for(int i = 0; i < num_of_lastweek; i++) {
            // only 1/3's record calls are in contact
            if (random.nextInt(4) == 1) {
                String[] contact = contactList.get(random.nextInt(contactList.size() - 1));
                CallRecordsAdd(contact[0], contact[1], getLongMills(mill_of_week), context);
            } else {
                CallRecordsAdd(null, Contact.getPhoneNumber(0), getLongMills(mill_of_week), context);
            }
        }

        for(int i = 0; i < num - num_of_lastweek; i++) {
            // only 1/3's record calls are in contact
            if (random.nextInt(4) == 1) {
                String[] contact = contactList.get(random.nextInt(contactList.size() - 1));
                CallRecordsAdd(contact[0], contact[1], getLongMills(mill_of_halfyear), context);
            } else {
                CallRecordsAdd(null, Contact.getPhoneNumber(0), getLongMills(mill_of_halfyear), context);
            }
        }
    }
}
