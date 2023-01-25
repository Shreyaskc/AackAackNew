package com.aackaacknew.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;

import com.aackaacknew.pojo.Contact;
import com.aackaacknew.pojo.Message;
import com.aackaacknew.utils.CheckInternetConnection;
import com.aackaacknew.utils.DataUrls;
import com.aackaacknew.utils.Deletedirectory;

public class BackupMessagesService extends Service {
	int strtId;
	int z = 0;
	File file, file1, file2;
	ArrayList<Long> numupload = new ArrayList<Long>();
	String strLastMessage_time = "";
	long final_msg_time = 0l;
	int type, type1;
	String number = "";
	static String Responce;
	private JSONArray jarry;
	JSONObject jsonSms;
	String strUserId = "", strUsername = "";
	String strResLastmessage_time;
	public static Doinback dob;
	public boolean checkmms, check2;
	public List<Message> messages = new ArrayList<Message>();
	SharedPreferences preferences;
	public static boolean backup, check;
	public static int tempvar = 0;
	static String strMyImagePath = "";
	boolean breakloop = false;

	static {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.permitAll().build());
	}

	public BackupMessagesService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();


		preferences = PreferenceManager
				.getDefaultSharedPreferences(BackupMessagesService.this);
		strUserId = preferences.getString("userid", "");
		strUsername = preferences.getString("username", "");
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("checkbackup", false);
		editor.commit();
		jarry = new JSONArray();

		android.provider.Settings.System.putInt(getContentResolver(),
				android.provider.Settings.System.WIFI_SLEEP_POLICY,
				android.provider.Settings.System.WIFI_SLEEP_POLICY_NEVER);
	}// oncreate...

	@SuppressLint("NewApi")
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
//		if (startId > 1) {
			if (CheckInternetConnection.isOnline(BackupMessagesService.this)) {

				if (!backup) {
					dob = new Doinback();
					dob.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
			}
//		}
		return Service.START_NOT_STICKY;
	}

//	@Override
//	public void onStart(Intent intent, int startId) {
//
//	}

	public IBinder onUnBind(Intent arg0) {
		return null;
	}

	public void onStop() {
		this.stopSelf();
		try {
			if (!dob.equals(null)) {
				dob.cancel(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onPause() {
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		try {
			if (dob != null || !dob.equals(null)) {
				dob.cancel(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		while (strtId > 0) {
			this.stopSelf(strtId);
			strtId--;
		}
		onStop();

	}

	@Override
	public void onLowMemory() {
		System.gc();
	}

	// To get sms and mms from device,then sending data to server.
	class Doinback extends AsyncTask<URL, Integer, Long> {
		protected void onPreExecute() {
			check = true;
		}

		protected Long doInBackground(URL... arg0) {
			try {
				// To get the lastbackup time from server
				if (!CheckInternetConnection
						.isOnline(BackupMessagesService.this)) {
					boolean inter = false;
					while (!inter) {
						if (CheckInternetConnection
								.isOnline(BackupMessagesService.this)) {
							inter = true;
							break;
						}
					}
				}

				String url = DataUrls.lastmessage_time + "userid="
						+ strUserId;
				strResLastmessage_time = getValuefromUrl(url);
				JSONObject jsonObj = new JSONObject(strResLastmessage_time);
				strLastMessage_time = jsonObj.getString("lastdate_time");

				SharedPreferences.Editor editor = preferences.edit();
				editor.putLong("msgtime", Long.parseLong(strLastMessage_time));
				editor.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
			messages.clear();
			readSmsFromDevice();
			//checkMessages();
			// After get the all messages to send the data to server.
			sendingDatatoServer();
			return null;
		}

		protected void onCancelled() {
		}

		protected void onPostExecute(Long result) {
			if (!isCancelled()) {

				if (strLastMessage_time.trim().length() > 3) {
					SharedPreferences.Editor editor = preferences.edit();
					editor.putBoolean("checkbackup", true);
					editor.putBoolean("checkbackground", true);// By using this
																// variable to
					// display the
					// messages in
					// messages screen.
					editor.commit();
				}
				check = false;
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);

		}
	}// async class

	// To get the sms from device..
	public void readSmsFromDevice() {
		final_msg_time = preferences.getLong("msgtime", 0);
		Uri uriSMSURI = Uri.parse("content://sms/");
		String[] projection = { "*" };
		String where = "date" + ">" + final_msg_time;
		Cursor cur = getContentResolver().query(uriSMSURI, projection, where,
				null, "date");

		while (cur.moveToNext()) {

			if (!dob.isCancelled()) {

				try {
					Message mess1 = new Message();
					try {
						String _id = cur.getString(cur.getColumnIndex("_id"));
						mess1.setId(_id);
					} catch (Exception e) {
						e.printStackTrace();
						mess1.setId("0");
					}
					try {
						String number = cur.getString(cur
								.getColumnIndex("address"));

						try {
							Log.d("number", "number" + number.length());
						} catch (Exception e) {
							e.printStackTrace();
							number = "0";
						}

						number = number.replaceAll("[\\W]", "");

						if (number.length() > 1) {
							if (number.trim().length() > 10) {
								mess1.setNumber(number.substring(
										number.length() - 10, number.length()));
								mess1.setAddress(number.substring(
										number.length() - 10, number.length()));
							} else {
								mess1.setNumber(number);
								mess1.setAddress(number);
							}
							mess1.setBody(cur.getString(cur
									.getColumnIndex("body")));
							String type = cur.getString(cur
									.getColumnIndex("type"));
							Long millisecond = Long.parseLong(cur.getString(cur
									.getColumnIndex("date")));
							String dateString = DateFormat.format(
									"yyyy/MM/dd hh:mm:ss a",
									new Date(millisecond)).toString();
							mess1.setDate_millis(millisecond);
							mess1.setDate(dateString);
							mess1.setType(type);
							mess1.setmessagetype("sms");

							try {
								String threadid = cur.getString(cur
										.getColumnIndex("thread_id"));
								Log.d("Threadid", threadid);
								mess1.setThreadid(threadid);
							} catch (Exception e) {
								e.printStackTrace();
								mess1.setThreadid("0");
							}
							messages.add(mess1);

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		cur.close();

	}// readsmsfromdevice........

	// To get the mms from device..
	void checkMessages() {
		Bitmap bitmap = null;
		String dateString2 = "";
		String threadid = "";
		Long millisecond2 = null;
		Long mmsmilli = null;
		String number = null;
		Long date = null;
		Uri uriSMSURI = Uri.parse("content://mms");
		preferences = PreferenceManager
				.getDefaultSharedPreferences(BackupMessagesService.this);
		final_msg_time = preferences.getLong("msgtime", 0);
		Long seconds = final_msg_time / 1000;
		String[] projection = { "*" };
		String where = "date" + ">" + seconds;
		Cursor curPdu = getContentResolver().query(uriSMSURI, projection,
				where, null, "date");

		int l = messages.size();

		while (curPdu.moveToNext()) {
			if (dob.isCancelled()) {
			} else {
				String id = null;
				try {
					id = curPdu.getString(curPdu.getColumnIndex("_id"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					type = Integer.parseInt(curPdu.getString(curPdu
							.getColumnIndex("m_type")));
					type1 = (type == 128) ? 2 : 1;
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					checkmms = false;
					String phnumber = getAddressNumber(Integer.parseInt(id));
					try {
						Log.d("phnumber", "phnumber" + phnumber.length());
					} catch (Exception e) {
						e.printStackTrace();
						phnumber = "0";
					}
					if (phnumber.trim().length() > 10
							&& phnumber.trim().length() < 14) {
						number = phnumber.substring(phnumber.length() - 10,
								phnumber.length());
					} else {
						number = phnumber;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (id != null && number.length() > 1) {
					try {
						millisecond2 = Long.parseLong(curPdu.getString(curPdu
								.getColumnIndex("date")));
						mmsmilli = millisecond2 * 1000;
						date = millisecond2 * 1000;
						dateString2 = getDateCurrentTimeZone(millisecond2);

						try {
							threadid = curPdu.getString(curPdu
									.getColumnIndex("thread_id"));
							Log.d("Threadid", threadid);
						} catch (Exception e) {
							e.printStackTrace();
							threadid = "0";
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					ArrayList<String> text = new ArrayList<String>();
					text = getMmsTextContent(id);

					ArrayList<Bitmap> albit = new ArrayList<Bitmap>();
					albit = getMms(id);

					for (int i = 0; i < albit.size(); i++) {
						Message mess3 = new Message();
						mess3.setId(id);
						mess3.setNumber(number);
						mess3.setAddress(number);
						mess3.setThreadid(threadid);
						mess3.setType((type == 128) ? "2" : "1");
						mess3.setDate(dateString2);
						mess3.setDate_millis(mmsmilli);

						if (i < text.size()) {
							if (text.get(i) != null
									&& text.get(i).toString() != null) {
								mess3.setBody2(text.get(i).toString());
							} else {
								mess3.setBody2("0");
							}
						} else {
							mess3.setBody2("0");
						}
						String image = "";
						bitmap = albit.get(i);
						String imagenameval = "";
						String imagepath = "";
						String imageedit = "";

						if (bitmap != null) {
							try {
								imagepath = convertBitmapToFile(bitmap);
								imageedit = decodeFile(imagepath, 300, 300);

								if (!CheckInternetConnection
										.isOnline(BackupMessagesService.this)) {
									boolean inter = false;

									WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
									wifiManager.setWifiEnabled(true);

									while (!inter) {
										if (CheckInternetConnection
												.isOnline(BackupMessagesService.this)) {
											inter = true;
											break;
										}
									}
								}

//								imagenameval = uploadfile(imageedit, number,
//										threadid, date);
								Log.d("imagename iii", imagenameval);
								image = DataUrls.amazonURL + strUserId + "/"
										+ imagenameval;
								mess3.setBody(image);

							} catch (Exception e) {
								e.printStackTrace();
								String imagenameval1 = "000000-1212-1212-1212-121212121212.png";
								image = DataUrls.amazonURL + imagenameval1;
								mess3.setBody(image);
							}
							tempvar++;
						} else {
							String imagenameval1 = "000000-1212-1212-1212-121212121212.png";
							image = DataUrls.amazonURL + imagenameval1;
							mess3.setBody(image);
							tempvar++;
						}
						mess3.setmessagetype("mms");
						messages.add(mess3);

						SharedPreferences.Editor editor = preferences.edit();
						editor.putLong("messageslength", curPdu.getCount()
								- (messages.size() - l));
						editor.commit();

						deleteFolderInSDCARD();// To delete img folders in
												// SDCARD
					}
				}
			}
		}

		for (int i = 0; i < messages.size(); i++) {
			Message m = messages.get(i);
			if (m.getmessagetype().equals("mms")) {
				if (m.getBody().replace(" ", "").trim().length() < 1
						|| m.getNumber().trim().length() < 2) {
					messages.remove(i);
				}
			}
		}
		for (int i = 0; i < messages.size(); i++) {
			Message mn = messages.get(i);
			if (mn.getBody2().trim().length() < 1) {
				mn.setBody2("0");
			}
		}
		for (int i = 0; i < messages.size(); i++) {
			Message mesx = messages.get(i);
			if ((mesx.getNumber().trim().length() < 2)
					|| (mesx.getAddress().trim().length() < 2)
					|| mesx.getBody() == null
					|| (mesx.getBody().trim().length() == 0)) {
				messages.remove(i);
			}
		}
		for (int i = 0; i < messages.size(); i++) {
			Message m = messages.get(i);
			if (m.getmessagetype().equals("mms")) {
				if (m.getBody().equals("null")
						|| m.getAddress().trim().length() < 2) {
					messages.remove(i);
				}
			}
		}

		Uri uriSMSURI1 = Uri
				.parse("content://mms-sms/conversations?simple=true");
		String[] projection1 = { "*" };
		String where1 = "date" + ">" + 0;
		Cursor cur1 = getContentResolver().query(uriSMSURI1, projection1,
				where1, null, "date");
		ArrayList<String> arr = new ArrayList<String>();

		while (cur1.moveToNext()) {

			try {
				if (cur1.getString(cur1.getColumnIndex("recipient_ids"))
						.contains(" ")) {
					arr.add(cur1.getString(cur1.getColumnIndex("_id")));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < arr.size(); i++) {
			for (int k = 0; k < messages.size(); k++) {
				if (arr.get(i).equals(messages.get(k).getThreadid())) {
					messages.remove(k);
					k = k - 1;
				}
			}
		}

		// To copmare contact numbers
		for (int i = 0; i < messages.size(); i++) {
			Message message1 = messages.get(i);
			for (Contact contact : Splash.contacts) {
				String msg = message1.getNumber();
				if (msg.contains(",")) {
					String[] num = msg.split(",");
					for (int k = 0; k < num.length; k++) {
						if (num[k].equals(contact.getNo())) {
							msg = msg.replace(num[k], contact.getName());
						}
					}
					message1.setAddress(msg);
					message1.setNumber(msg);
				} else {
					if (message1.getNumber().equals(contact.getNo())) {
						message1.setAddress(contact.getName());
						message1.setNumber(contact.getNo());
					}
				}
			}
		}// end of contact numbers
	}// checkmessages......

	// After prepare the data sending data to server
	private void sendingDatatoServer() {
		for (int x = 0; x < messages.size(); x++) {
			if (dob.isCancelled()) {
				breakloop = true;
			} else {
				Message message = messages.get(x);
				try {
					if (CheckInternetConnection
							.isOnline(BackupMessagesService.this)) {
						jsonSms = new JSONObject();
						jsonSms.put("date", message.getDate());
						jsonSms.put("message",
								message.getBody().replace("&", " and "));
						jsonSms.put("username", message.getAddress());
						jsonSms.put("number", message.getNumber());
						jsonSms.put("relatedto", message.getType());
						jsonSms.put("lastbackup", message.getDate_millis());
						jsonSms.put("messagetype", message.getmessagetype());
						jsonSms.put("mmstext", message.getBody2());
						jsonSms.put("thread_id", message.getThreadid());
						jsonSms.put("original_message_date",
								message.getDate_millis());
						jarry.put(jsonSms);

						if (jarry.length() == 25) {/*
							String backup_res = null;
							if (!CheckInternetConnection
									.isOnline(BackupMessagesService.this)) {
								boolean inter = false;
								while (!inter) {
									if (CheckInternetConnection
											.isOnline(BackupMessagesService.this)) {
										backup_res = getValuefromUrlData(DataUrls.samplearray);
										jarry = new JSONArray();// for clearing
										// previous data
										inter = true;
									}
								}
							} else {
								backup_res = getValuefromUrlData(DataUrls.samplearray);
								jarry = new JSONArray();// for clearing previous
								// data
							}
							if (backup_res != null) {
								jarry = new JSONArray();
							}*/
						}
					} else {/*
						boolean inter = false;
						while (!inter) {
							if (CheckInternetConnection
									.isOnline(BackupMessagesService.this)) {
								if (jarry.length() != 0) {
									String backup_res = null;
									if (!CheckInternetConnection
											.isOnline(BackupMessagesService.this)) {
										boolean inter1 = false;
										while (!inter1) {
											if (CheckInternetConnection
													.isOnline(BackupMessagesService.this)) {
												backup_res = getValuefromUrlData(DataUrls.samplearray);
												jarry = new JSONArray();// for//
												// clearing
												// previous
												// data
												inter1 = true;
											}
										}
									} else {
										backup_res = getValuefromUrlData(DataUrls.samplearray);
										jarry = new JSONArray();// for clearing
																// previous data
									}
									if (backup_res != null) {
										jarry = new JSONArray();
									}
								}
								inter = true;
							}
						}*/
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			SharedPreferences.Editor editor = preferences.edit();

			editor.putLong("uploadedLength", (x * 100 / messages.size()));
			editor.commit();
		}
		if (!dob.isCancelled()) {/*

			String backup_res = "";
			String strmessage = "";
			if (CheckInternetConnection.isOnline(BackupMessagesService.this)) {
				backup_res = getValuefromUrlData(DataUrls.samplearray);
				jarry = new JSONArray();// for clearing previous data
			}
			try {
				if (backup_res.trim().length() != 0
						|| backup_res.equals("null")) {
					JSONObject obj = new JSONObject(backup_res);
					strmessage = obj.getString("message");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (strmessage.equals("Successfully Inserted")) {

				backup = true;
				strLastMessage_time = "1";
				preferences = PreferenceManager
						.getDefaultSharedPreferences(BackupMessagesService.this);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putLong("msgtime", Long.parseLong(strLastMessage_time));
				editor.putBoolean("sendingdata", false);
				editor.putLong("messageslength", 0);
				editor.putLong("uploadedLength", 0);
				editor.commit();
			}*/
		}
	}// send to data to server..

	// To delete image Folders in sdcard
	public void deleteFolderInSDCARD() {
		// To delete image folders in sd card
		String str = Environment.getExternalStorageDirectory().toString();
		file1 = new File(str + "/MyApp" + "/MyApp-Backup");
		Deletedirectory.deleteDirectory(file1);
		file2 = new File(str + "/TMMFOLDER");
		Deletedirectory.deleteDirectory(file2);
	}

	// To uplaod the mms to amzon server........
/*	String uploadfile(String filePath, String num, String threadid, Long date) {
		String dateString = DateFormat.format("yyyyMMddhhmmss", new Date(date))
				.toString();
		UUID idOne = UUID.randomUUID();
		String strUid = String.valueOf(idOne);

		File fileToUpload = new File(filePath);
		String strImageName = strUid + "_" + dateString + "_" + threadid + "_"
				+ num;
		String s3BucketName = getString(R.string.s3_bucket);
		String str = filePath.substring(filePath.lastIndexOf("."),
				filePath.length());
		String strnew = strImageName + str;

		if (!CheckInternetConnection.isOnline(BackupMessagesService.this)) {
			boolean inter = false;
			while (!inter) {
				if (CheckInternetConnection
						.isOnline(BackupMessagesService.this)) {
					inter = true;
					break;
				}
			}
		}

		uploader = new Uploader(this, s3Client, s3BucketName, strUserId + "/"
				+ strImageName + str, fileToUpload);

		try {
			uploader.start();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return strnew;
	}*/// end of amazon server..

	// To get the mms address from device..
	private String getAddressNumber(int id) {
		String selectionAdd = new String("msg_id=" + id);
		Uri uriAddress = Uri.parse("content://mms/" + id + "/addr");
		String[] columns = { "*" };
		Cursor cAdd = getContentResolver().query(uriAddress, columns,
				selectionAdd, null, null);
		String name = null;
		if (cAdd.moveToFirst()) {
			do {
				if (type1 == 1) {
					if (checkmms == false) {
						number = cAdd.getString(cAdd.getColumnIndex("address"))
								.replaceAll("[\\W]", "");
						if (number.trim().length() > 10) {
							number = number.substring(number.length() - 10,
									number.length());
						}
						if (number != null) {
							try {
								name = number;
								number = "";
								number = "";
							} catch (NumberFormatException nfe) {
								nfe.printStackTrace();
								if (name == null) {
									name = number;
									number = "";
								}
							}
						}
					}
					checkmms = true;
				} else {
					if (cAdd.getString(cAdd.getColumnIndex("address")).trim()
							.equals("insert-address-token")) {
					} else {
						number = cAdd.getString(cAdd.getColumnIndex("address"))
								.replaceAll("[\\W]", "");
						if (number.trim().length() > 10) {
							number = number.substring(number.length() - 10,
									number.length());
						}
						if (number != null) {
							try {
								if (name == null || name.equals("")
										|| name.length() == 0) {
									name = number;
									number = "";
								} else {
									name = name + "," + number;
									number = "";
								}
							} catch (NumberFormatException nfe) {
								nfe.printStackTrace();
								if (name == null || name.equals("")
										|| name.length() == 0) {
									name = number;
									number = "";
								} else {
									name = name + "," + number;
									number = "";
								}
							}
						}
					}
				}
			} while (cAdd.moveToNext());
		}
		if (cAdd != null) {
			cAdd.close();
		}
		return name;
	}// end of getting address

	// To interacting to server
	/*public String getValuefromUrlData(String url) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost();
			request.setURI(new URI(url));
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			if (strUserId.trim().length() != 0
					&& strUsername.trim().length() != 0 && jarry.length() != 0) {
				preferences = PreferenceManager
						.getDefaultSharedPreferences(BackupMessagesService.this);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean("sendingdata", true);
				editor.commit();
			}

			nameValuePairs.add(new BasicNameValuePair("userid", strUserId));
			nameValuePairs.add(new BasicNameValuePair("username", strUsername));
			nameValuePairs
					.add(new BasicNameValuePair("data", jarry.toString()));

			UrlEncodedFormEntity entity_st = new UrlEncodedFormEntity(
					nameValuePairs, "UTF-8");
			request.setEntity(entity_st);
			HttpResponse response = client.execute(request);
			HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				Responce = EntityUtils.toString(resEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Responce;
	}*/// end of interacting to server

	// To get the current timezone
	public static String getDateCurrentTimeZone(long timestamp) {
		String localTime = null;
		try {
			Calendar calendar = Calendar.getInstance();
			TimeZone tz = calendar.getTimeZone();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a");
			sdf.setTimeZone(tz);
			localTime = sdf.format(new Date(timestamp * 1000));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return localTime;
	}// end of timezone

	// To decode the image..
	private String decodeFile(String path, int DESIREDWIDTH, int DESIREDHEIGHT) {
		String strMyImagePath = null;
		Bitmap scaledBitmap = null;
		try {
			Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path,
					DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);
			if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap
					.getHeight() <= DESIREDHEIGHT)) {
				scaledBitmap = ScalingUtilities.createScaledBitmap(
						unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT,
                        ScalingUtilities.ScalingLogic.FIT);
			} else {
				unscaledBitmap.recycle();
				return path;
			}
			String extr = Environment.getExternalStorageDirectory().toString();
			File mFolder = new File(extr + "/TMMFOLDER");
			if (!mFolder.exists()) {
				mFolder.mkdir();
			}
			String s = "tmp" + z + ".png";
			File f = new File(mFolder.getAbsolutePath(), s);
			strMyImagePath = f.getAbsolutePath();
			FileOutputStream fos = null;
			fos = new FileOutputStream(f);
			scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
			fos.flush();
			fos.close();
			scaledBitmap.recycle();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		if (strMyImagePath == null) {
			return path;
		}
		z++;
		return strMyImagePath;
	}// decode the image...

	// To get text content from mms..
	public ArrayList<String> getMmsTextContent(String mmsId) {
		String body = null;
		ArrayList<String> arlMMS = new ArrayList<String>();
		String selectionPart = "mid=" + mmsId;
		Uri uri = Uri.parse("content://mms/part");
		Cursor cursor = getContentResolver().query(uri, null, selectionPart,
				null, null);

		if (cursor != null && cursor.getCount() > 0) {
			try {
				if (cursor.moveToFirst()) {
					do {
						String partId = cursor.getString(cursor
								.getColumnIndex("_id"));
						String type = cursor.getString(cursor
								.getColumnIndex("ct"));
						if ("text/plain".equals(type)) {
							String data = cursor.getString(cursor
									.getColumnIndex("_data"));
							if (data != null) {
								// implementation of this method below
								body = getMmsText(partId);
								arlMMS.add(body);
							} else {
								body = cursor.getString(cursor
										.getColumnIndex("text"));
								arlMMS.add(body);
							}
						}
					} while (cursor.moveToNext());
					return arlMMS;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cursor.close();
			}
		}
		return null;
	}

	// To get the text
	private String getMmsText(String id) {
		Uri partURI = Uri.parse("content://mms/part/" + id);
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		try {
			is = getContentResolver().openInputStream(partURI);
			if (is != null) {
				InputStreamReader isr = new InputStreamReader(is, "UTF-8");
				BufferedReader reader = new BufferedReader(isr);
				String temp = reader.readLine();
				while (temp != null) {
					sb.append(temp);
					temp = reader.readLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	// To get the mms..
	public ArrayList<Bitmap> getMms(String mmsId) {
		Bitmap bitmap = null;
		ArrayList<Bitmap> arlBitmap = new ArrayList<Bitmap>();
		String selectionPart = "mid=" + mmsId;
		Uri uri = Uri.parse("content://mms/part");
		Cursor cPart = getContentResolver().query(uri, null, selectionPart,
				null, null);
		if (cPart != null && cPart.getCount() > 0) {
			try {
				if (cPart.moveToFirst()) {
					do {
						String partId = cPart.getString(cPart
								.getColumnIndex("_id"));
						String type = cPart.getString(cPart
								.getColumnIndex("ct"));
						if ("image/jpeg".equals(type)
								|| "image/bmp".equals(type)
								|| "image/gif".equals(type)
								|| "image/jpg".equals(type)
								|| "image/png".equals(type)) {
							bitmap = getMmsImage(partId);
							arlBitmap.add(bitmap);
						}
					} while (cPart.moveToNext());
					return arlBitmap;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cPart.close();
			}
		}
		return arlBitmap;
	}

	// To get bitmap from mms
	private Bitmap getMmsImage(String _id) {
		Uri partURI = Uri.parse("content://mms/part/" + _id);
		InputStream is = null;
		Bitmap bitmap = null;
		try {
			is = getContentResolver().openInputStream(partURI);
			bitmap = BitmapFactory.decodeStream(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}

	// To convert the bitmap to file..
	public static String convertBitmapToFile(Bitmap bitmap) {
		String extr = Environment.getExternalStorageDirectory().toString();
		File mFolder = new File(extr + "/MyApp");
		if (!mFolder.exists()) {
			mFolder.mkdir();
		}
		String strF = mFolder.getAbsolutePath();
		File mSubFolder = new File(strF + "/MyApp-Backup");

		if (!mSubFolder.exists()) {
			mSubFolder.mkdir();
		}
		String s = Calendar.getInstance().getTimeInMillis() + ".png";
		File f = new File(mSubFolder.getAbsolutePath(), s);
		strMyImagePath = f.getAbsolutePath();
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.PNG, 60, fos);

			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strMyImagePath;
	}
	private final String USER_AGENT = "Mozilla/5.0";
	public String getValuefromUrl(String url) {
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.addRequestProperty("Cache-Control", "no-cache");
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			return response.toString();
		} catch (Exception ex) {

			ex.printStackTrace();
			return "zero";
		}
	}
}
