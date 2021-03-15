package com.example.datafill;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
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

public class Contact {
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void RequestPhoneStatePermission(Context context) {
        int permissionCheck = context.getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_CONTACTS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS}, 100);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void show(Context context) {
        String result = "";
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
                result = result + "name:" + nameStr + ",number:" + phoneStr + ";  ";
                while(cursor.moveToNext()) {
                    //联系人姓名
                    nameStr = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    //读取通讯录的号码
                    phoneStr = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    result = result + "name:" + nameStr + ",number:" + phoneStr + ";  ";
                }
                cursor.close();
            } else {
                System.out.println("db cursor not exist.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(result);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void clear(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            RequestPhoneStatePermission(context);
        }

        Uri uri = Contacts.People.CONTENT_URI;
        context.getContentResolver().delete(uri, null, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void randomAdd(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            RequestPhoneStatePermission(context);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuffer buffer = new StringBuffer();
                try {
                    InputStream is = getAssets().open("num/" + mPro + "/" + mCity + ".txt");
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "gbk"));
                    String str = "";
                    while ((str = br.readLine()) != null) {
                        buffer.append(str);
                    }
                    String[] phoneQian = buffer.toString().split("\\D");
                    if (phoneQian.length == 0) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                showToast("参数错误，请稍后重试");
                                mProgressDialog.dismiss();
                            }
                        });
                        return;
                    }
                    //开始生成电话号码
                    List<String> list = new ArrayList<>();
                    List<Contract> contracts = new ArrayList<Contract>();
                    for (int i = 0; i < count; i++) {
                        Random random = new Random();
                        String qianqi = phoneQian[random.nextInt(phoneQian.length)];//获得前七位
                        System.out.println("前七位" + qianqi);
                        String housi = addZero(random.nextInt(9999));
                        System.out.println("后四位：" + housi);
                        String phone = qianqi + housi;
                        System.out.println("Phone:" + phone);
                        if (!list.contains(phone)) {
                            list.add(phone);
                            // addContract(i + "", phone);
                            contracts.add(new Contract(phone, phone));
                        } else {
                            i--;
                        }

                    }
                    int index = 0;
                    if (contracts.size() > 1000) {
                        while (index < contracts.size()) {
                            List<Contract> ss = new ArrayList<>();
                            for (int i = index; i < ((index + 1000) > contracts.size() ? contracts.size() : (index + 100)); i++) {

                                ss.add(contracts.get(i));
                            }
                            index = index + 1000;
                            addAll(ss);
                        }
                    } else {
                        addAll(contracts);
                    }
                    mHandler.sendEmptyMessage(-1);
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            showToast("参数错误，请稍后重试");
                            mProgressDialog.dismiss();
                        }
                    });
                }
            }
        });
        new Thread(new TestRunnable()).start();

    }
}
