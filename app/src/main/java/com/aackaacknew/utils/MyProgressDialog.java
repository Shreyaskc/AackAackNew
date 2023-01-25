package com.aackaacknew.utils;

import android.app.Dialog;
import android.content.Context;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;

import com.aackaacknew.activities.R;

public class MyProgressDialog extends Dialog {

	public static MyProgressDialog show(Context context, CharSequence title,
			CharSequence message) {
		return show(context, title, message, false);
	}

	public static MyProgressDialog show(Context context, CharSequence title,
			CharSequence message, boolean indeterminate) {
		return show(context, title, message, indeterminate, false, null);
	}

	public static MyProgressDialog show(Context context, CharSequence title,
			CharSequence message, boolean indeterminate, boolean cancelable) {
		return show(context, title, message, indeterminate, cancelable, null);
	}

	public static MyProgressDialog show(Context context, CharSequence title,
			CharSequence message, boolean indeterminate, boolean cancelable,
			OnCancelListener cancelListener) {
		MyProgressDialog dialog = new MyProgressDialog(context);
		dialog.setTitle(title);

		dialog.setCancelable(cancelable);
		dialog.setOnCancelListener(cancelListener);
		dialog.addContentView(new ProgressBar(context),
				new LayoutParams(60, 60));
		dialog.show();

		return dialog;
	}

	public MyProgressDialog(Context context) {
		super(context, R.style.NewDialog);
	}
}
