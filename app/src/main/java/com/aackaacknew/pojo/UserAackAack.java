package com.aackaacknew.pojo;

import android.graphics.Bitmap;

public class UserAackAack {

	public static String userId;
	public static boolean data_backUp = false;
	public static Bitmap imageBitmap;

	public static String getUserId() {
		return userId;
	}

	public static void setUserId(String userId) {
		UserAackAack.userId = userId;
	}

	public static boolean isData_backUp() {
		return data_backUp;
	}

	public static void setData_backUp(boolean data_backUp) {
		UserAackAack.data_backUp = data_backUp;
	}

}
