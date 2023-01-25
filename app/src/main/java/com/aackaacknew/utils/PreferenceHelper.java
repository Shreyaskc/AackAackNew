package com.aackaacknew.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import java.util.UUID;

/**
 * @author Shreyas K C
 * Preference Helper for Adding Unique ID, Device ID and any other fields if necesary.
 */
public class PreferenceHelper {

    private SharedPreferences app_prefs;
    private final String PREF_NAME = "AackAack";
    private final String UNIQUE_ID = "unique_id";
    private final String DEVICE_ID = "device_id";
    private final String NAME = "name";
    private final String EMAIL = "email";
    private final String REMEBER_ME = "remember";

    public PreferenceHelper(Context context) {
        app_prefs = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
    }


    public void putUniqueId(String uniqueId) {
        Editor edit = app_prefs.edit();
        edit.putString(UNIQUE_ID, uniqueId);
        edit.commit();
    }

    public String getUniqueId() {
        return app_prefs.getString(UNIQUE_ID, null);
    }

    public void putDeviceId(String deviceId) {
        Editor edit = app_prefs.edit();
        edit.putString(DEVICE_ID, deviceId);
        edit.commit();
    }

    public String getDeviceId() {
        String deviceId = app_prefs.getString(DEVICE_ID, null);
        deviceId =  TextUtils.isEmpty(deviceId)?UUID.randomUUID().toString():deviceId;
        return deviceId;
    }

    public void putEmail(String email) {
        Editor edit = app_prefs.edit();
        edit.putString(EMAIL, email);
        edit.commit();
    }

    public String getEmail() {
        return app_prefs.getString(EMAIL, "");
    }
    public void putName(String name) {
        Editor edit = app_prefs.edit();
        edit.putString(NAME, name);
        edit.commit();
    }

    public String getName() {
        return app_prefs.getString(NAME, "");
    }

    public void putRemeber(boolean remember) {
        Editor edit = app_prefs.edit();
        edit.putBoolean(REMEBER_ME, remember);
        edit.commit();
    }

    public boolean getRemeber() {
        return app_prefs.getBoolean(REMEBER_ME, false);
    }


    public void putString(String key,String val) {
        Editor edit = app_prefs.edit();
        edit.putString(key, val);
        edit.commit();
    }

    public String getString(String key) {
        return app_prefs.getString(key, "");
    }


    public void putLong(String key,Long val) {
        Editor edit = app_prefs.edit();
        edit.putLong(key, val);
        edit.commit();
    }

    public Long getLong(String key) {
        return app_prefs.getLong(key, 0l);
    }
}