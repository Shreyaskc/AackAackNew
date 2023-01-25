package com.aackaacknew.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.aackaacknew.pojo.Contact;
import com.aackaacknew.utils.PermissionUtil;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Splash extends AppCompatActivity   {
    private static int SPLASH_TIME_OUT = 3000;
    boolean check = false;
    public static int width;
    public static List<Contact> contacts = new CopyOnWriteArrayList<Contact>();

    File file, file1, file2, file3;
    SharedPreferences prefs;
    boolean demo = false;
    public static boolean myapp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        PermissionUtil.EnableRuntimePermission(Manifest.permission.READ_CONTACTS, this);
        PermissionUtil.EnableRuntimePermission(Manifest.permission.READ_SMS, this);
    try {
        new Handler().postDelayed(new Runnable() {

        /*
         * Showing splash screen with a timer. This will be useful when you
         * want to show case your app logo / company
         */

        @Override
        public void run() {
            // This method will be executed once the timer is over
            // Start your app main activity

        }
    }, SPLASH_TIME_OUT);
}
finally {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(Splash.this);
        String userid = prefs.getString("userid", "");
        String username = prefs.getString("username", "");
        if (TextUtils.isEmpty(userid)) {
            Intent i = new Intent(Splash.this, PagerActivity.class);
            startActivity(i);
        }
        else{
            Intent i = new Intent(Splash.this, Home.class);
            startActivity(i);
        }




        // close this activity
        finish();
}
    }
}
