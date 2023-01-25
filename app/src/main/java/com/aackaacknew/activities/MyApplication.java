package com.aackaacknew.activities;



import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;

public class MyApplication extends Application {

	private static Context context;

	public void onCreate() {
		super.onCreate();
		// The following line triggers the initialization of ACRA
		super.onCreate();
		MyApplication.context = getApplicationContext();
//		ACRA.init(this);

		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
//		BroadcastReceiver mReceiver = new ReceiverScreen();
//		registerReceiver(mReceiver, filter);

	}

	/**
	 * http://stackoverflow.com/questions/2002288/static-way-to-get-context-on-
	 * android
	 */
	public static Context getAppContext() {
		return MyApplication.context;
	}

	public static String getVersionName() {
		try {
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "1.0";
		}
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	@Override
	public void onTrimMemory(int level) {
		// TODO Auto-generated method stub
		super.onTrimMemory(level);
	}

}
