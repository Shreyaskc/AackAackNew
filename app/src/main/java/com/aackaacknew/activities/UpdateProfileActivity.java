package com.aackaacknew.activities;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.aackaacknew.activities.ui.profile.ProfileFragment;
import com.aackaacknew.utils.CheckInternetConnection;
import com.aackaacknew.utils.DataUrls;
import com.aackaacknew.utils.Deletedirectory;
import com.aackaacknew.utils.ImageLoader1;
import com.aackaacknew.utils.MyProgressDialog;
import com.aackaacknew.utils.RoundedImageView;
import com.aackaacknew.utils.WebService;

public class UpdateProfileActivity extends FragmentActivity {

	EditText edtPhonenumber, edtFullname, edtUsername, edtPassword,
			edtpasswords, edtEmail, edtFirstname, edtLastname;
	Button btnSave, btnCancel, btnBack;
	String phonenumber, username, firstname, lastname, email, password,
			response = "0", gender, maleof, success, selectedImagePath = "0",
			deviceId = "", strPic, strNum, strLogintype, strUserid;
	Uri muri;
	ImageView profilepic;
	MyProgressDialog dialog;
	TextView txtTerms;
	Typeface typeface;
	ImageLoader1 il;
	String strImagePath_Crop, passwordnew, followStatus = "";
	static final int TAKE_PICTURE = 1, SELECT_PICTURE = 0;
	LinearLayout passwordlayout;
	String profilepicurl, profilestatus = "";
	SharedPreferences prefs;

	public void onCreate(Bundle si) {
		super.onCreate(si);
		setContentView(R.layout.update_profile);

		il = new ImageLoader1(UpdateProfileActivity.this);
		typeface = Typeface.createFromAsset(getAssets(),
				"fonts/Roboto-Bold.ttf");

		// To get the all id's..
		edtFirstname = (EditText) findViewById(R.id.firstname);
		edtLastname = (EditText) findViewById(R.id.lastname);
		edtUsername = (EditText) findViewById(R.id.username);
		edtPhonenumber = (EditText) findViewById(R.id.phonenumber);
		edtEmail = (EditText) findViewById(R.id.email);
		edtpasswords = (EditText) findViewById(R.id.password);
		btnSave = (Button) findViewById(R.id.save_btn);
		btnCancel = (Button) findViewById(R.id.cancel_btn);
		txtTerms = (TextView) findViewById(R.id.terms);
		passwordlayout = (LinearLayout) findViewById(R.id.passwordlayout);
		btnBack = (Button) findViewById(R.id.back);
		profilepic = (ImageView) findViewById(R.id.profilepic);
		// edtpasswords.setTransformationMethod(PasswordTransformationMethod.getInstance());
		edtpasswords.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		edtpasswords.setSelection(edtpasswords.getText().length());

		txtTerms.setText("Terms & Conditions");

		// InputFilter for allow only characters
		InputFilter filter1 = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				for (int i = start; i < end; i++) {
					if (Character.isSpaceChar(source.charAt(i))) {
						return "";
					}
				}
				return null;
			}
		};
		edtUsername.setFilters(new InputFilter[] { filter1 });

		prefs = PreferenceManager
				.getDefaultSharedPreferences(UpdateProfileActivity.this);

		// To change profile pic
		profilepic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog1();
			}
		});

		// To go BackScreen(Profile)
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Intent(UpdateProfileActivity.this, MainActivity.class);
				setResult(RESULT_OK);
				finish();
			}
		});

		// To update the changes
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (CheckInternetConnection
						.isOnline(UpdateProfileActivity.this)) {
					if (validation()) {

						Register reg = new Register();
						reg.execute();
					}
				} else {
					Toast.makeText(UpdateProfileActivity.this,
							"No Internet Connection ", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		// To cancel the changes
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager inputMethodManager = (InputMethodManager) UpdateProfileActivity.this
						.getSystemService(Activity.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(
						UpdateProfileActivity.this.getCurrentFocus()
								.getWindowToken(), 0);
				finish();
			}
		});

		firstname = getIntent().getStringExtra("firstname");
		lastname = getIntent().getStringExtra("lastname");
		email = getIntent().getStringExtra("email");
		username = getIntent().getStringExtra("username");
		password = getIntent().getStringExtra("password").trim()
				.replace(" ", "");
		selectedImagePath = getIntent().getStringExtra("profilepic");
		strNum = getIntent().getStringExtra("number");
		strLogintype = getIntent().getStringExtra("logintype");
		strUserid = getIntent().getStringExtra("userid");

		if (!strLogintype.equals("3")) {
			passwordlayout.setVisibility(View.GONE);
		}

		edtUsername.setText(username);
		edtpasswords.setText(password);
		edtEmail.setText(email);
		edtFirstname.setText(firstname);
		edtLastname.setText(lastname);
		edtPhonenumber.setText(strNum);

		if (!selectedImagePath.equals(null)) {
			try {
				il.DisplayImage(selectedImagePath.trim(), profilepic);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}// oncreate..

	// onClick for default back button for closing the application
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			new Intent(UpdateProfileActivity.this, MainActivity.class);
			setResult(RESULT_OK);
			finish();
		}
		return false;
	}

	public void showDialog1() {
		final Dialog dialog = new Dialog(UpdateProfileActivity.this,
				R.style.PauseDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.social_share);
		dialog.setTitle("Social Sharing");
		dialog.setCancelable(false);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		LinearLayout take = (LinearLayout) dialog.findViewById(R.id.take);
		Button cancel = (Button) dialog.findViewById(R.id.cancel);
		LinearLayout choose = (LinearLayout) dialog.findViewById(R.id.choose);

		// Called when choose button.
		choose.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, 0);
				try {
					dialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// Called when cancel button.
		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {

				try {
					dialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		// Called when take button.
		take.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				if (isSDCARDMounted()) {
					Intent photoPickerIntent = new Intent(
							MediaStore.ACTION_IMAGE_CAPTURE);
					photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT,
							getTempFile());
					photoPickerIntent.putExtra("outputFormat",
							Bitmap.CompressFormat.JPEG.toString());
					photoPickerIntent.putExtra("return-data", true);
					startActivityForResult(photoPickerIntent, 1);
					try {
						dialog.dismiss();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(UpdateProfileActivity.this,
							"You need to insert SD card", Toast.LENGTH_LONG)
							.show();
					try {
						dialog.dismiss();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		dialog.show();
	}

	// For cropping..
	public void startCrop(String m_Path) {
		Intent m_cropIntent = new Intent("com.android.camera.action.CROP");
		m_cropIntent.setDataAndType(Uri.fromFile(new File(m_Path)), "image/*");
		m_cropIntent.putExtra("crop", "true");
		m_cropIntent.putExtra("aspectX", 1);
		m_cropIntent.putExtra("aspectY", 1);
		m_cropIntent.putExtra("outputX", 256);
		m_cropIntent.putExtra("outputY", 256);
		Uri cropUri = getTempFile();
		strImagePath_Crop = cropUri.getPath();
		m_cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
		m_cropIntent.putExtra("return-data", true);
		startActivityForResult(m_cropIntent, 535);
	}

	boolean checkEmailCorrect(String email) {
		String pttn = "^\\D.+@.+\\.[a-z]+";
		Pattern p = Pattern.compile(pttn);
		Matcher m = p.matcher(email);
		if (m.matches()) {
			return true;
		} else {
			Toast.makeText(UpdateProfileActivity.this,
					"Please Enter Valid Email", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	private boolean isSDCARDMounted() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

	private Uri getTempFile() {
		File root = new File(Environment.getExternalStorageDirectory(),
				"AackAack");
		if (!root.exists()) {
			root.mkdirs();
		}
		String filename = "" + System.currentTimeMillis();
		File file = new File(root, filename + ".jpeg");
		muri = Uri.fromFile(file);
		selectedImagePath = muri.getPath();
		return muri;
	}

	@SuppressWarnings("deprecation")
	public void onActivityResult(int requestcode, int resultcode, Intent data) {
		super.onActivityResult(requestcode,resultcode,data);
		switch (requestcode) {
		case TAKE_PICTURE:
			if (resultcode == RESULT_OK) {
				try {
					startCrop(muri.getPath());
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		case SELECT_PICTURE:
			if (resultcode == RESULT_OK) {
				try {
					Uri selectedImageUri = data.getData();
					selectedImagePath = getPath(selectedImageUri);
					startCrop(selectedImagePath);
				} catch (Exception e) {
					Toast.makeText(UpdateProfileActivity.this,
							"cannot select image from here", Toast.LENGTH_LONG)
							.show();
					e.printStackTrace();
				}
				break;
			}
		case 535:
			if (resultcode == RESULT_OK) {
				if (strImagePath_Crop != "") {
					Bitmap bm = BitmapFactory.decodeFile(strImagePath_Crop);
					int scale = 1;
					int width_tmp = bm.getWidth(), height_tmp = bm.getHeight();
					while (true) {
						if (width_tmp < 300 || height_tmp < 300)
							break;
						width_tmp /= 2;
						height_tmp /= 2;
						scale++;
					}
					BitmapFactory.Options o = new BitmapFactory.Options();
					o.inSampleSize = scale;
					try {
						Bitmap bitmap = getCorrectBitmap(strImagePath_Crop);
						profilepic.setImageDrawable(new BitmapDrawable(
								new RoundedImageView(getApplicationContext())
										.getCroppedBitmap(bitmap, 350)));

					} catch (Exception e) {
					}
				}
				break;
			}
		case 123:
			if (resultcode == RESULT_OK) {
				data.getStringExtra("abc");
				selectedImagePath = data.getStringExtra("image");
				File myFile = null;
				if (selectedImagePath != null)
					myFile = new File(selectedImagePath);
				if (myFile != null) {
					Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
					profilepic.setImageDrawable(new BitmapDrawable(
							new RoundedImageView(getApplicationContext())
									.getCroppedBitmap(bitmap, 350)));

				}
			}
		}
	}// onactivity result..

	public static Bitmap getCorrectBitmap(String path) {
		BitmapFactory.decodeFile(path);
		BitmapFactory.Options o = new BitmapFactory.Options();
		int scale = 1;
		o.inSampleSize = scale;
		Bitmap bitmap_for_rotate = BitmapFactory.decodeFile(path, o);
		try {
			ExifInterface exif = new ExifInterface(path);
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			float angle = 0f;
			if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				angle = 90;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
				angle = 180;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				angle = 270;
			}
			Matrix m = new Matrix();
			m.postRotate(angle);

			Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(path),
					null, o);
			bitmap_for_rotate = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
					bmp.getHeight(), m, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap_for_rotate;
	}

	public String getPath(Uri uri) {
		try {
			String[] projection = { MediaStore.Images.Media.DATA,
					MediaStore.Images.ImageColumns.ORIENTATION };
			@SuppressWarnings("deprecation")
			Cursor cursor = managedQuery(uri, projection, null, null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			int orientation_ColumnIndex = cursor
					.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);
			cursor.moveToFirst();
			cursor.getString(orientation_ColumnIndex);

			if (cursor.getString(column_index) == null) {
				Toast.makeText(UpdateProfileActivity.this,
						"Please Select Image from Gallery", Toast.LENGTH_SHORT)
						.show();
				return "";
			} else {
				return cursor.getString(column_index);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(UpdateProfileActivity.this,
					"Please Select Image from Gallery", Toast.LENGTH_SHORT)
					.show();
			return "";
		}
	}

	// For validation..
	private boolean validation() {
		passwordnew = edtpasswords.getText().toString().trim().replace(" ", "");
		;
		username = edtUsername.getText().toString();
		selectedImagePath.trim();
		if ((selectedImagePath.trim().length() == 0)
				|| (selectedImagePath.trim().toString() == " ")
				|| (selectedImagePath.trim().toString() == "0")) {
			Toast.makeText(UpdateProfileActivity.this,
					"Please Select ProfilePic ", Toast.LENGTH_SHORT).show();
			return false;
		} else if ((username.length() == 0) || (username.toString() == " ")) {
			Toast.makeText(UpdateProfileActivity.this,
					"Please enter UserName ", Toast.LENGTH_SHORT).show();
			return false;
		} else if ((edtFirstname.getText().toString().trim().length() == 0)
				|| (edtFirstname.getText().toString().trim() == " ")) {
			Toast.makeText(UpdateProfileActivity.this,
					"Please enter Firstname ", Toast.LENGTH_SHORT).show();
			return false;
		} else if ((edtPhonenumber.getText().toString().trim().replace(" ", "")
				.length() == 0)
				|| (edtPhonenumber.getText().toString().trim() == " ")) {
			Toast.makeText(UpdateProfileActivity.this,
					"Please enter Phonenumber ", Toast.LENGTH_SHORT).show();
			return false;
		} else if ((edtPhonenumber.getText().toString().trim().replace(" ", "")
				.length() < 10)) {
			Toast.makeText(UpdateProfileActivity.this,
					"Please enter valid Phonenumber ", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if (!checkEmailCorrect(edtEmail.getText().toString().trim()
				.replace(" ", ""))) {
			return false;
		}
		if (strLogintype.equals("3")) {
			if (passwordnew.trim().length() == 0) {
				Toast.makeText(UpdateProfileActivity.this,
						"Please enter Password", Toast.LENGTH_SHORT).show();
				return false;
			} else if ((passwordnew.length() <= 6)
					&& (strLogintype.equals("3"))) {
				Toast.makeText(UpdateProfileActivity.this,
						"Password must be more than 6 characters ",
						Toast.LENGTH_SHORT).show();
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	// To update user details..
	class Register extends AsyncTask<URL, Integer, Long> {
		JSONObject jo;
		String status;

		public void onPreExecute() {
			dialog = MyProgressDialog.show(UpdateProfileActivity.this, null,
					null);
		}

		protected Long doInBackground(URL... arg0) {
			if (CheckInternetConnection.isOnline(UpdateProfileActivity.this)) {
				try {
					String imagetype = selectedImagePath.trim().substring(
							selectedImagePath.trim().length() - 3,
							selectedImagePath.trim().length());
					if (imagetype.equalsIgnoreCase("png")) {
						DataUrls.from = "share";
					} else {
						DataUrls.from = "registration";
					}

					WebService wb = new WebService(DataUrls.profile_update);
					wb.openConnection();
					wb.setStringParameter("userid", strUserid);
					wb.setStringParameter("firstname", edtFirstname.getText()
							.toString().trim());
					wb.setStringParameter("lastname", edtLastname.getText()
							.toString().trim());
					wb.setStringParameter("email", edtEmail.getText()
							.toString().trim().replace(" ", ""));
					wb.setStringParameter("number", edtPhonenumber.getText()
							.toString());
					wb.setStringParameter("username", username);
					wb.setStringParameter("password", passwordnew);
					if (selectedImagePath.contains("http")) {
						wb.setStringParameter("flag", "no");
						wb.setStringParameter("picture", selectedImagePath);

					} else {
						wb.setStringParameter("flag", "yes");
						wb.setImageParameter("logo", selectedImagePath.trim(),
								true);
					}
					wb.closeConnection();
					response = wb.getResult();
					jo = new JSONObject(response);
					success = jo.getString("message");
					try {
						profilepicurl = jo.getString("profilepic");

					} catch (Exception e) {
						e.printStackTrace();
					}
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("profilepic", profilepicurl);
					editor.commit();

					ProfileFragment.profilepic = DataUrls.baseURL + "/images/"
							+ profilepicurl;

					URL url;
					try {
						url = new URL(ProfileFragment.profilepic);
						ProfileFragment.image = BitmapFactory.decodeStream(url
								.openConnection().getInputStream());
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					try {
						followStatus = jo.getString("followstatus");
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(UpdateProfileActivity.this,
								DataUrls.dialogtitle, Toast.LENGTH_SHORT)
								.show();
					}
				});
			}
			return null;
		}

		protected void onPostExecute(Long result) {

			try {
				dialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (response.equals("0")) {
				Toast.makeText(UpdateProfileActivity.this,
						"Please check internet connection", Toast.LENGTH_LONG)
						.show();
			} else if (response
					.equalsIgnoreCase("User with this UserName already exists")) {
				Toast.makeText(UpdateProfileActivity.this,
						"User with this UserName already exists",
						Toast.LENGTH_LONG).show();
			} else if (response
					.equalsIgnoreCase("User with this Email already exists")) {
				Toast.makeText(UpdateProfileActivity.this,
						"User with this Email already exists",
						Toast.LENGTH_LONG).show();
			} else if (response.equalsIgnoreCase("zero")) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						UpdateProfileActivity.this);
				alertDialogBuilder.setTitle("Aack Aack");
				alertDialogBuilder
						.setMessage(
								"Internal server error please try after few minutes")
						.setCancelable(false)
						.setNegativeButton("Ok",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
									}
								});
				AlertDialog alertDialog = alertDialogBuilder.create();
				// show it
				alertDialog.show();
			} else {
				if (response.trim().length() != 0 && !response.equals(null))
					if (success.equals("Success")) {
						Toast.makeText(UpdateProfileActivity.this,
								"Profile updated successfully",
								Toast.LENGTH_LONG).show();
						HomeDetailsActivity.check_home = false;
						DataUrls.share_myfav1 = "";
						DataUrls.profileupdate = true;
						ProfileFragment.str = "1";
						if (password.equals(edtpasswords.getText().toString()
								.trim().replace(" ", ""))) {
							Intent ints = new Intent(
									UpdateProfileActivity.this,
									MainActivity.class);
							startActivity(ints);
							finish();
						} else {
							Intent ints = new Intent(
									UpdateProfileActivity.this,
									SignInActivity.class);
							startActivity(ints);
							finish();
						}
						String str2 = Environment.getExternalStorageDirectory()
								.toString();
						File file = new File(str2 + "/.AackAack");
						Deletedirectory.deleteDirectory(file);
					} else {
						Toast.makeText(UpdateProfileActivity.this, success,
								Toast.LENGTH_LONG).show();
					}
			}
		}
	}// Async class

	public void showDialog_Confirm() {
		try {
			final Dialog dialog = new Dialog(UpdateProfileActivity.this,
					R.style.PauseDialog);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.logincheck_dialog);
			dialog.setTitle("Aack Aack");
			dialog.setCancelable(false);
			dialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(Color.TRANSPARENT));
			final RelativeLayout ok = (RelativeLayout) dialog
					.findViewById(R.id.ok);
			TextView text = (TextView) dialog.findViewById(R.id.text);
			text.setText("success");
			ok.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					ok.setBackgroundColor(Color.RED);
					try {
						dialog.dismiss();
					} catch (Exception e) {
						e.printStackTrace();
					}
					Intent i = new Intent(UpdateProfileActivity.this,
							SignInActivity.class);
					startActivity(i);
				}
			});

			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Bitmap StringToBitMap(String encodedString) {
		try {
			byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
					encodeByte.length);
			return bitmap;
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}
}
