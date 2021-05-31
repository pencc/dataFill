package com.example.datafill;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission Granted

        } else if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            // Permission Denied

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String defaultSmsApp = null;
//        String currentPn = getPackageName();//获取当前程序包名
//        defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(this);//获取手机当前设置的默认短信应用的包名
//        if (!defaultSmsApp.equals(currentPn)) {
//            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
//            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, "com.example.datafill.MmsReceiver");
//            startActivity(intent);
//        }

//        try {
//            String currentPn = getPackageName();
//            Class<?> smsClass = Class.forName("com.android.internal.telephony.SmsApplication");
//            Method method = smsClass.getMethod("setDefaultApplication", String.class, Context.class);
//            method.invoke(null, currentPn, this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Button btn_clear_contact = (Button)findViewById(R.id.btn_clear_contact);
        btn_clear_contact.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Contact.clear(MainActivity.this);
            }
        });

        Button btn_show_contact = (Button)findViewById(R.id.btn_show_contact);
        btn_show_contact.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Contact.show(MainActivity.this);
            }
        });

        Button btn_random_add_contact = (Button)findViewById(R.id.btn_random_add_contact);
        btn_random_add_contact.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Contact.randomAdd(MainActivity.this);
            }
        });

        Button btn_clear_cr = (Button)findViewById(R.id.btn_clear_cr);
        btn_clear_cr.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                CallRecord.clear(MainActivity.this);
            }
        });

        Button btn_show_cr = (Button)findViewById(R.id.btn_show_cr);
        btn_show_cr.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                CallRecord.show(MainActivity.this);
            }
        });

        Button btn_random_add_cr = (Button)findViewById(R.id.btn_random_add_cr);
        btn_random_add_cr.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                CallRecord.randomAdd(MainActivity.this, 100); // insert 100 records
            }
        });

        Button btn_clear_msg = (Button)findViewById(R.id.btn_clear_msg);
        btn_clear_msg.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                MsgRecord.clear(MainActivity.this);
            }
        });

        Button btn_show_msg = (Button)findViewById(R.id.btn_show_msg);
        btn_show_msg.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                MsgRecord.show(MainActivity.this);
            }
        });

        Button btn_random_add_msg = (Button)findViewById(R.id.btn_random_add_msg);
        btn_random_add_msg.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                MsgRecord.randomAdd(MainActivity.this, 100); // insert 100 records
                //MmsReceiver.randomAdd(MainActivity.this, 100);
            }
        });
    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        final String myPackageName = getPackageName();
//        if (!Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName)) {
//            // App is not default.
//            // Show the "not currently set as the default SMS app" interface
//            String defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(this);
//            Intent intent =
//                    new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
//            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
//                    myPackageName);
//            startActivity(intent);
//
//            intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
//            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, defaultSmsApp);
//            startActivity(intent);
//        }
//    }
}