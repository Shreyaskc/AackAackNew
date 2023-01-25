package com.aackaacknew.utils;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.aackaacknew.activities.BackupMessagesService;
import com.aackaacknew.activities.ContactsService;

/**
 * Utility class that wraps access to the runtime permissions API in M and provides basic helper
 * methods.
 */
public abstract class PermissionUtil {
    public static  final String TAG = "AackAack";

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
     *
     * @see Activity#onRequestPermissionsResult(int, String[], int[])
     */
    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    public static void EnableRuntimePermission(String permission, Activity activity){

        if (ContextCompat.checkSelfPermission(activity,permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    permission)) {
                Toast.makeText(activity, permission+" permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(activity,
                        new String[]{permission},
                        101);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }




    }
    public static void startContactBackUp( Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // READ_CONTACTS permission has not been granted.

            EnableRuntimePermission(Manifest.permission.READ_CONTACTS, activity);

        } else {

            // READ_CONTACTS permissions is already available, show the camera preview.
            Log.i(TAG,
                    "Contacts permission has already been granted. Displaying camera preview.");
            activity.startService(new Intent(activity,
                    ContactsService.class));
        }

    }
    public static String getDeviceId( Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // READ_CONTACTS permission has not been granted.

            EnableRuntimePermission(Manifest.permission.READ_PHONE_STATE, activity);

        } else {

            // READ_CONTACTS permissions is already available, show the camera preview.
            Log.i(TAG,
                    "READ_PHONE_STATE permission has already been granted. Displaying camera preview.");
             TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

            return telephonyManager.getDeviceId();
        }
        return null;
    }




    public static void startMessageBackUp( Activity activity) {

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // read SMS permission has not been granted.

            EnableRuntimePermission(Manifest.permission.READ_SMS, activity);

        } else {

            // read sms permissions is already available, show the camera preview.
            Log.i(TAG,
                    "SmS permission has already been granted. Displaying camera preview.");
            activity.startService(new Intent(activity,
                    BackupMessagesService.class));
        }

    }
}