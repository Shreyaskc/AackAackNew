package com.aackaacknew;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.BadTokenException;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aackaacknew.activities.R;
import com.aackaacknew.activities.Splash;
import com.aackaacknew.utils.CheckInternetConnection;
import com.aackaacknew.utils.DataUrls;
import com.aackaacknew.utils.ImageLoader_homeGrid;
import com.aackaacknew.utils.MyProgressDialog;
import com.aackaacknew.utils.WebServiceAack;
import com.aackaacknew.utils.ImageLoader_homeGrid;
import com.aackaacknew.utils.MyProgressDialog;

@TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
public class FullDetailsActivity extends Activity {
	ImageView imgAack;
	WebView webview;
	Button back;
	ImageView imgLogo;
	TextView txtHeader;
	String image, strCaption;
	ImageLoader_homeGrid loader;
	String screenLook, conversationfrom;
	RelativeLayout share;
	MyProgressDialog dialog;
	static int webview_height;
	boolean check_imageValue;
	private int width;
	public static File file1, file_tumb;
	public static boolean favs;
	SharedPreferences prefs;
	public static String file_name = "", file_name_tumb = "";
	String lastmessage, phonenum;
	private ProgressDialog progress;
	String userid;

	private static final String HTML_FORMAT = "<html><body style=\"text-align: center; background-color: black; vertical-align: center;\"><img src = \"%s\" /></body></html>";

	@SuppressWarnings({ "deprecation", "static-access" })
	public void onCreate(Bundle si) {
		super.onCreate(si);
		setContentView(R.layout.profiledetails);
		if (Build.VERSION.SDK_INT >= 21) {
			webview.enableSlowWholeDocumentDraw();
		}

		back = (Button) findViewById(R.id.back);
		imgAack = (ImageView) findViewById(R.id.aackimage);
		imgLogo = (ImageView) findViewById(R.id.logo);
		txtHeader = (TextView) findViewById(R.id.header);
		webview = (WebView) findViewById(R.id.webview);
		share = (RelativeLayout) findViewById(R.id.share);

		prefs = PreferenceManager
				.getDefaultSharedPreferences(FullDetailsActivity.this);
		favs = false;

		loader = new ImageLoader_homeGrid(this);
		if (!CheckInternetConnection.isOnline(FullDetailsActivity.this)) {
			Toast.makeText(FullDetailsActivity.this, DataUrls.dialogtitle,
					Toast.LENGTH_SHORT).show();
			finish();
		}
		image = getIntent().getStringExtra("aackcontent");
		strCaption = getIntent().getStringExtra("caption");
		conversationfrom = getIntent().getStringExtra("conversationfrom");
		screenLook = getIntent().getStringExtra("screenLook");
		lastmessage = getIntent().getStringExtra("lastmessage");
		phonenum = getIntent().getStringExtra("number");

		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth(); // deprecated

		// To go Back Screen(FavsDetails)
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		// To go Back Screen(FavsDetails)
		imgLogo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		// when called share button(To share aack to social fields)

		share.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				file1 = null;
				file_tumb = null;
				file_name = "";
				file_name_tumb = "";
				DataUrls.providers = new ArrayList<String>();
				DataUrls.email = false;

//				MessageShareActivity.strScreenlook = screenLook;
				if (screenLook.equals("2")) {
					DataUrls.screen_look = "ios";
				} else if (screenLook.equals("3")) {
					DataUrls.screen_look = "android";
				}
				favs = true;
				DataUrls.strCaption = strCaption;
				DataUrls.conversation_to = conversationfrom;

				if (CheckInternetConnection.isOnline(FullDetailsActivity.this)) {
					//TODO
//					MessageShareActivity.share = false;
					webview.scrollTo(0, 0);
					generateImg();
				} else {
					Toast.makeText(getApplicationContext(),
							DataUrls.dialogtitle, Toast.LENGTH_LONG).show();
				}

				return false;
			}
		});

		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if (newProgress > 0) {
					showProgressDialog("Please Wait");
				}
				if (newProgress >= 100) {
					hideProgressDialog();
				}
			}
		});

		final String imgUrl = image;

		String strBody = "<html><body><img style='width:100%;' src='" + imgUrl
				+ "'></img>";
		String.format(HTML_FORMAT, strBody);
		webview.loadDataWithBaseURL("", strBody, null, "UTF-8", "");

		if (strCaption.equals("")) {
			txtHeader.setText("Aack");
		} else {
			txtHeader.setText(strCaption);
		}
	}// oncreare.......

	// onClick for default back button for closing the application
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			finish();
		}
		return false;
	}

	// To generate webview as image
	@SuppressWarnings("deprecation")
	private void generateImg() {
		try {
			DataUrls.from = "share";
			webview.getHeight();
			Picture p = webview.capturePicture();
			webview.setDrawingCacheEnabled(true);

			new bitmap(p).execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// To generate webview as tumbnail_image
	@SuppressWarnings("unused")
	private void generateImg_tumb() {
		try {
			DataUrls.from = "share";
			webview.scrollTo(0, 0);
			int x = webview.getHeight();
			int y = webview.getWidth();
			webview.layout(0, 0, width, 400);
			// Capture the webview as a bitmap
			webview.setDrawingCacheEnabled(true);
			Bitmap bitmap_resize = null;
			try {
				bitmap_resize = Bitmap.createBitmap(webview.getDrawingCache());
			} catch (Exception e) {
				e.printStackTrace();
			}
			webview.setDrawingCacheEnabled(false);
			File myDir = new File(Environment.getExternalStorageDirectory(),
					"AackAack");
			if (myDir.exists()) {
			} else {
				myDir.mkdir();
			}
			String fname = System.currentTimeMillis() + ".png";
			file_tumb = new File(myDir, fname);
			try {
				FileOutputStream out = new FileOutputStream(file_tumb);
				bitmap_resize.compress(Bitmap.CompressFormat.PNG, 10, out);
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			//TODO
//			MessageShareActivity.file_name_tumb = myDir + "/" + fname;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showProgressDialog(final String msg) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (progress == null || !progress.isShowing()) {
					progress = ProgressDialog.show(FullDetailsActivity.this,
							"", msg);
				}
			}
		});
	}

	public void hideProgressDialog() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					if (progress.isShowing())
						progress.dismiss();
				} catch (Throwable e) {
				}
			}
		});
	}

	class bitmap extends AsyncTask<URL, Void, Bitmap> {
		Picture pict;
		ProgressDialog progressDialog = null;

		public bitmap(Picture pict) {
			this.pict = pict;
		}

		protected void onPreExecute() {

			if (progressDialog == null) {
				progressDialog = alertMessage23();
				progressDialog.show();
			} else {
				progressDialog.show();
			}
		}

		@Override
		protected Bitmap doInBackground(URL... arg0) {
			PictureDrawable pictureDrawable = new PictureDrawable(pict);
			if (Splash.width == 480) {
				webview_height = 10000;
			} else if (Splash.width == 720) {
				webview_height = 15000;
			} else if (Splash.width == 1080) {
				webview_height = 20000;
			} else if (Splash.width == 320) {
				webview_height = 5000;
			} else {
				webview_height = 10000;
			}
			if (pictureDrawable.getIntrinsicHeight() > webview_height) {
				//TODO MessageShareActivity.share = true;
			}

			Bitmap bitmap = null;
			try {
				bitmap = Bitmap.createBitmap(
						pictureDrawable.getIntrinsicWidth(),
						pictureDrawable.getIntrinsicHeight(), Config.ARGB_8888);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
				return null;
			} catch (Exception e) {
				e.printStackTrace();
			}

			Canvas canvas = new Canvas(bitmap);
			canvas.drawPicture(pictureDrawable.getPicture());
			return bitmap;
		}

		@SuppressLint("NewApi")
		protected void onPostExecute(Bitmap result) {
			webview.setDrawingCacheEnabled(false);
			if (result == null) {
				check_imageValue = true;
			} else {
				File myDir = new File(
						Environment.getExternalStorageDirectory(), "AackAack");
				if (myDir.exists()) {
				} else {
					myDir.mkdir();
				}
				String fname = System.currentTimeMillis() + ".png";
				file1 = new File(myDir, fname);

				try {
					FileOutputStream out = new FileOutputStream(file1);
					//TODO result.compress(Bitmap.CompressFormat.PNG, 10, out);
					out.flush();
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				//TODO MessageShareActivity.file_name = myDir + "/" + fname;
			}

			if (check_imageValue) {
				DataUrls.screen_look = "";
				try {
					if (progressDialog != null)
						progressDialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				generateImg_tumb();

				if (progressDialog != null && progressDialog.isShowing())
					progressDialog.dismiss();

				if (!CheckInternetConnection.isOnline(FullDetailsActivity.this)) {
					Toast.makeText(FullDetailsActivity.this,
							DataUrls.dialogtitle, Toast.LENGTH_SHORT).show();
					finish();
				} else {
//					Intent intent = new Intent(FullDetailsActivity.this,
//							MessageShareActivity.class);
//					startActivity(intent);
//					finish();
				}
			}
		}
	}

	// Alert Message for Selected aack is big
	ProgressDialog alertMessage23() {
		ProgressDialog dialog = new ProgressDialog(FullDetailsActivity.this);
		try {
			dialog.show();
		} catch (BadTokenException e) {
		}
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.custom_dialog);
		// dialog.setMessage(Message);
		return dialog;
	}

	// Async class for to store the generated images into the server
	class ImageSharing extends AsyncTask<URL, Integer, Long> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = MyProgressDialog
					.show(FullDetailsActivity.this, null, null);
		}

		@Override
		protected Long doInBackground(URL... arg0) {
			try {
				userid = prefs.getString("userid", "0");
				String devicetime = getDateTime();
				WebServiceAack wb = new WebServiceAack(DataUrls.add_aack);
				wb.openConnection();

				wb.setStringParameter("userid", userid);
				wb.setImageParameter("logo", file_name.trim(), true); // /storage/emulated/0/folder_name/1380278410570.jpeg
				wb.setStringParameter("devicedatetime",
						devicetime.replace(" ", "%20"));
				wb.setStringParameter("number", phonenum);
				wb.setStringParameter("lastmessage", lastmessage);
				wb.setImageParameter("thumb", file_name_tumb.trim(), true);
				wb.closeConnection();
				String response = wb.getResult();

				if (!response.equals("")) {
					JSONObject jo = new JSONObject(response);
//					MessageShareActivity.aack_id = jo.getString("aackid");
//					MessageShareActivity.aackurl = jo.getString("aackurl");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Long result) {
			try {
				dialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	// To gettime
	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date).trim().replace(" ", "%20");
	}
}
