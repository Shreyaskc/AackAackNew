package com.aackaacknew.activities;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aackaacknew.activities.ui.messages.MessagesFragment;
import com.aackaacknew.adapters.AackDisplayAdapter;
import com.aackaacknew.adapters.SeparatedListAdapter;
import com.aackaacknew.utils.CheckInternetConnection;
import com.aackaacknew.utils.DataUrls;
import com.aackaacknew.utils.MyProgressDialog;
import com.aackaacknew.utils.UrltoValue;

public class MessageDetailsActivity extends Activity {
	ImageView imgShare, imgBack, imgLogo;
	TextView txtHeader;
	Button showmms;
	MyProgressDialog dialog2;
	public static boolean check, checkbackground;
	private ListView lstAack;
	private MyProgressDialog dialog;
	SharedPreferences prefs;
	String phonenum = "", userid = "", username = "", lastbackup = "";
	private ArrayList<String> name, body, date, type, time, header_date,
			radio_button, date_time, media, messagetype, mmstext;
	public static ArrayList<String> name_web, body_web, date_web, type_web,
			time_web, header_date_web, mmsimage, messagetype_web, mmstext_web;
	private String start = "", end = "";
	public static SeparatedListAdapter sAdapter;
	@SuppressWarnings("rawtypes")
	List lists[];
	String start_Id = "", end_Id = "", temp_Id = "", temp_Id1 = "";
	List<String> data_collection = new ArrayList<String>();
	String send_date = "", inbox_date = "";
	String sent = "sent";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aackdisplay);
		dialog2 = new MyProgressDialog(getApplicationContext());

		// To get the all id's.
		imgShare = (ImageView) findViewById(R.id.share);
		imgBack = (ImageView) findViewById(R.id.back);
		imgLogo = (ImageView) findViewById(R.id.logo);
		showmms = (Button) findViewById(R.id.showmms);
		txtHeader = (TextView) findViewById(R.id.headertext);
		lstAack = (ListView) findViewById(R.id.lstAack);

		if (Splash.width == 1080) {
			txtHeader.setTextSize(30);
		}

		// for getting lastback_up date from shard preferences
		prefs = PreferenceManager
				.getDefaultSharedPreferences(MessageDetailsActivity.this);
		lastbackup = prefs.getString("lastbackup", " ");
		checkbackground = prefs.getBoolean("checkbackground", false);
		// To select the both side convsersations
		lstAack.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if (type.size() != 0) {
					MessagesFragment.check_msg_click = false;
					int i_postion = 0;
					for (int i = 0; i < arg2; i++) {
						String key = data_collection.get(i);

						if (!key.equals("0")) {
							i_postion++;
						}
					}
					if (start_Id == "") {
						sent = "" + i_postion;
						start_Id = "" + i_postion;
						temp_Id = start_Id;
						radio_button.set(i_postion, "2");
						inbox_date = date_time.get(i_postion);
					} else {
						if ((!start_Id.equals("" + i_postion))) {

							if (end_Id == "") {
								end_Id = "" + i_postion;
								temp_Id1 = end_Id;

								if (Integer.parseInt(temp_Id) < Integer
										.parseInt(temp_Id1)) {
									radio_button.set(
											Integer.parseInt(temp_Id1), "1");
								} else {
									radio_button.set(
											Integer.parseInt(temp_Id1), "2");
									radio_button.set(Integer.parseInt(temp_Id),
											"1");
								}
							} else if ((!end_Id.equals("" + i_postion))) {
								radio_button.set(Integer.parseInt(temp_Id), "0");
								radio_button.set(Integer.parseInt(temp_Id1),
										"0");
								temp_Id1 = "" + end_Id;

								end_Id = "" + i_postion;
								temp_Id = "" + i_postion;

								if (Integer.parseInt(temp_Id1) < Integer
										.parseInt(end_Id)) {
									radio_button.set(
											Integer.parseInt(temp_Id1), "2");
									radio_button.set(Integer.parseInt(end_Id),
											"1");
								} else {
									radio_button.set(
											Integer.parseInt(temp_Id1), "1");
									radio_button.set(Integer.parseInt(end_Id),
											"2");
								}

								inbox_date = date_time.get(Integer
										.parseInt(temp_Id1));
							}
							send_date = date_time.get(i_postion);

						} else if (!sent.equals("sent")) {
							if (end_Id == "") {
								if ((!start_Id.equals("" + i_postion))) {
									end_Id = "" + i_postion;

									if (Integer.parseInt(temp_Id1) < Integer
											.parseInt(end_Id)) {
										radio_button.set(
												Integer.parseInt(temp_Id1), "2");
										radio_button.set(
												Integer.parseInt(end_Id), "1");
									} else {
										radio_button.set(
												Integer.parseInt(temp_Id1), "1");
										radio_button.set(
												Integer.parseInt(end_Id), "2");
									}

									radio_button.set(Integer.parseInt(end_Id),
											"1");
									temp_Id1 = end_Id;

								}
							} else {
								radio_button.set(Integer.parseInt(temp_Id), "0");
								radio_button.set(Integer.parseInt(temp_Id1),
										"0");
								temp_Id1 = "" + end_Id;
								end_Id = "" + i_postion;
								temp_Id = "" + i_postion;

								if (Integer.parseInt(temp_Id1) < Integer
										.parseInt(end_Id)) {
									radio_button.set(
											Integer.parseInt(temp_Id1), "2");
									radio_button.set(Integer.parseInt(end_Id),
											"1");
								} else {
									radio_button.set(
											Integer.parseInt(temp_Id1), "1");
									radio_button.set(Integer.parseInt(end_Id),
											"2");
								}

								inbox_date = date_time.get(Integer
										.parseInt(temp_Id1));

							}
							send_date = date_time.get(i_postion);
						}
					}
					setView(lstAack.getFirstVisiblePosition());
				} else {

					Toast.makeText(MessageDetailsActivity.this,
							"You need conversations.", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		prefs = PreferenceManager
				.getDefaultSharedPreferences(MessageDetailsActivity.this);
		phonenum = prefs.getString("phonenumber", "0");
		userid = prefs.getString("userid", "0");
		username = prefs.getString("name", " ");
		check = prefs.getBoolean("checkbackup", false);

		if (username.trim().length() == 0 || username.equals("No Name")) {
			txtHeader.setText(phonenum);
		} else {
			txtHeader.setText(username);
		}
		name = new ArrayList<String>();
		body = new ArrayList<String>();
		date = new ArrayList<String>();
		type = new ArrayList<String>();
		time = new ArrayList<String>();
		header_date = new ArrayList<String>();
		radio_button = new ArrayList<String>();
		date_time = new ArrayList<String>();
		media = new ArrayList<String>();
		messagetype = new ArrayList<String>();
		mmstext = new ArrayList<String>();

		name_web = new ArrayList<String>();
		body_web = new ArrayList<String>();
		date_web = new ArrayList<String>();
		type_web = new ArrayList<String>();
		time_web = new ArrayList<String>();
		header_date_web = new ArrayList<String>();
		messagetype_web = new ArrayList<String>();
		mmstext_web = new ArrayList<String>();
		mmsimage = new ArrayList<String>();

		sAdapter = new SeparatedListAdapter(this, R.layout.listviewheader); // for
																			// header

		// When called to go Next Screen(MessageShareActivity)
		imgShare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DataUrls.providers.clear();
				DataUrls.email = false;
				DataUrls.strCaption = null;
				DataUrls.conversation_to = null;
				DataUrls.screen_look = "";

				if ((inbox_date.trim().length() != 0)
						|| (send_date.trim().length() != 0)) {
					dataFilter();
				} else {
					Toast.makeText(MessageDetailsActivity.this,
							"Please select conversation Start and Stop point.",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		// To go Back Screen(Messages screen)
		imgBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DataUrls.bool_start_5msgs = false;
				MessagesFragment.checkmess = false;
				finish();
			}
		});
		// To go Back Screen(Messages)
		imgLogo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DataUrls.bool_start_5msgs = false;
				MessagesFragment.checkmess = false;
				finish();
			}
		});
		// When called restart the Activity to show mms
		showmms.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MessagesFragment.checkmess = true;
				DataUrls.bool_start_5msgs = true;
				Intent recall = new Intent(MessageDetailsActivity.this,
						MessageDetailsActivity.class);
				startActivity(recall);
				finish();
			}
		});
		// To get messages from server
		if (lastbackup.trim().length() != 0 || checkbackground) {
			if (CheckInternetConnection.isOnline(MessageDetailsActivity.this)) {
				GettingFullChat gfc = new GettingFullChat();
				gfc.execute();
			} else {
				Toast.makeText(MessageDetailsActivity.this,
						DataUrls.dialogtitle, Toast.LENGTH_SHORT).show();
				finish();
			}
		} else {
			display();// To get messages from device..
		}
	}// oncreate.......

	// async for latest messages...
	class GettingFullChat extends AsyncTask<URL, Integer, Long> {
		protected void onPreExecute() {
			dialog = MyProgressDialog.show(MessageDetailsActivity.this, null,
					null);
		}

		@Override
		protected Long doInBackground(URL... arg0) {
			if (CheckInternetConnection.isOnline(MessageDetailsActivity.this)) {
				try {
					String fullchat_res = UrltoValue
							.getValuefromUrl(DataUrls.fullchat + "userid="
									+ userid + "&number=" + phonenum);

					JSONArray ja = new JSONArray(fullchat_res);
					messagetype.clear();
					name.clear();
					messagetype.clear();
					mmstext.clear();
					media.clear();
					type.clear();
					header_date.clear();
					date_time.clear();
					date.clear();
					time.clear();
					radio_button.clear();

					int j = 0;

					for (int i = 0; i < ja.length(); i++) {
						JSONObject jo = ja.getJSONObject(i);
						if (!MessagesFragment.checkmess) {
							String msgtype = jo.getString("messagetype");
							if (msgtype.equals("sms")) {
								messagetype.add(jo.getString("messagetype"));
								name.add(jo.getString("username"));
								body.add(jo.getString("message"));
								mmstext.add(jo.getString("mmstext"));
								media.add(jo.getString("media"));
								type.add(jo.getString("type"));
								date_time.add(jo.getString("message_datetime")
										.trim());
								String date_value = conversion(jo.getString(
										"message_datetime").trim());
								date.add(date_value.substring(0, 14).trim());
								time.add(date_value.substring(15,
										date_value.length()).trim());
								radio_button.add("0");
								if (!header_date.contains(date.get(j))) {
									header_date.add(date.get(j));
								}
								j++;
							}
						} else {
							messagetype.add(jo.getString("messagetype"));
							name.add(jo.getString("username"));
							body.add(jo.getString("message"));
							mmstext.add(jo.getString("mmstext"));
							media.add(jo.getString("media"));
							type.add(jo.getString("type"));
							date_time.add(jo.getString("message_datetime")
									.trim());
							String date_value = conversion(jo.getString(
									"message_datetime").trim());
							date.add(date_value.substring(0, 14).trim());
							time.add(date_value.substring(15,
									date_value.length()).trim());
							radio_button.add("0");

							if (!header_date.contains(date.get(i))) {
								header_date.add(date.get(i));
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(MessageDetailsActivity.this,
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
			if (header_date.size() != 0)
				setView(0);
		}
	}// async..........

	// To display the messages from device
	private void display() {
		int j = 0;
		messagetype.clear();
		name.clear();
		messagetype.clear();
		mmstext.clear();
		media.clear();
		type.clear();
		header_date.clear();
		date_time.clear();
		date.clear();
		time.clear();
		radio_button.clear();
		body.clear();

		try {
			@SuppressWarnings("unused")
			int size = MessagesFragment.list_name.size();
		} catch (Exception e) {
			MessagesFragment.list_name = new ArrayList<String>();
			e.printStackTrace();
		}
		for (int i = 0; i < MessagesFragment.list_name.size(); i++) {
			if (!DataUrls.bool_start_5msgs) {
				String msgtype = MessagesFragment.list_messtype.get(i);
				if (msgtype.equals("sms")) {
					messagetype.add(MessagesFragment.list_messtype.get(i));
					name.add(MessagesFragment.list_name.get(i));
					body.add(MessagesFragment.list_body.get(i));
					type.add(MessagesFragment.list_type.get(i));
					mmstext.add(MessagesFragment.list_mmstext.get(i));
					date_time
							.add(conversion2(MessagesFragment.list_date.get(i)));
					media.add("");
					String date_value = date_time.get(j);

					date.add(date_value.substring(0, 14).trim());
					time.add(date_value.substring(15, date_value.length())
							.trim());
					radio_button.add("0");
					if (!header_date.contains(date.get(j))) {
						header_date.add(date.get(j));
					}
					j++;
				}
			} else {
				messagetype.add(MessagesFragment.list_messtype.get(i));
				name.add(MessagesFragment.list_name.get(i));
				body.add(MessagesFragment.list_body.get(i));
				type.add(MessagesFragment.list_type.get(i));
				mmstext.add(MessagesFragment.list_mmstext.get(i));
				date_time.add(conversion2(MessagesFragment.list_date.get(i)));
				media.add("");

				String date_value = date_time.get(i);

				date.add(date_value.substring(0, 14).trim());
				time.add(date_value.substring(15, date_value.length()).trim());
				radio_button.add("0");
				if (!header_date.contains(date.get(i))) {
					header_date.add(date.get(i));
				}
			}
		}
		DataUrls.bool_start_5msgs = true;
		if (header_date.size() != 0)
			setView(0);
	}// end of the display..

	public Map<String, ?> createObject(String[] values) {
		Map<String, String[]> item = new HashMap<String, String[]>();
		item.put("values", values);
		return item;
	}

	// filtering data
	class Filtering extends AsyncTask<URL, Integer, Long> {
		protected void onPreExecute() {
			dialog = MyProgressDialog.show(MessageDetailsActivity.this, null,
					null);
		}

		@Override
		protected Long doInBackground(URL... params) {
			filter_data(start, end);
			return null;
		}

		protected void onPostExecute(Long result) {
			try {
				dialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
			//TODO Share
//			Intent i = new Intent(MessageDetailsActivity.this,
//					MessageShareActivity.class);
//			i.putStringArrayListExtra("mmsimage", mmsimage);
//			i.putStringArrayListExtra("mmstext", mmstext_web);
//			i.putStringArrayListExtra("body", body_web);
//			i.putStringArrayListExtra("date", date_web);
//			i.putStringArrayListExtra("time", time_web);
//			i.putStringArrayListExtra("type", type_web);
//			i.putStringArrayListExtra("messagetype", messagetype_web);
//			i.putStringArrayListExtra("date_time", header_date_web);
//			startActivity(i);
//			finish();
		}
	}

	// Conversion of Date from one format to another format
	public String conversion(String date1) {
		SimpleDateFormat cur = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat post = new SimpleDateFormat("MM/dd/yyyy EEE HH:mm");
		String from_final = "";
		Date dateobjfrom = null;

		try {
			dateobjfrom = cur.parse(date1.trim());
			from_final = post.format(dateobjfrom);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return from_final;
	}

	// Conversion of Date from one format to another format
	public String conversion2(String date1) {
		SimpleDateFormat cur = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a");
		SimpleDateFormat post = new SimpleDateFormat("MM/dd/yyyy EEE HH:mm");
		String from_final = "";
		Date dateobjfrom = null;
		try {
			dateobjfrom = cur.parse(date1);
			from_final = post.format(dateobjfrom);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return from_final;
	}

	@SuppressWarnings("unchecked")
	public void setView(int postion) {
		lstAack.setAdapter(null);
		lists = new LinkedList[header_date.size()];
		for (int j = 0; j < lists.length; j++) {
			lists[j] = new LinkedList<Map<String, ?>>();
		}
		try {
			for (int i = 0; i < header_date.size(); i++) {
				List<String> temp_data = new ArrayList<String>();
				// Log.d("date size", "date sizee"+date.size());
				for (int j = 0; j < date.size(); j++) {
					if (header_date.get(i).equals(date.get(j))) {
						// Log.d("date","datee"+date.get(j));
						String[] s = new String[8];
						s[0] = body.get(j);
						s[1] = date.get(j);
						s[2] = time.get(j);
						s[3] = type.get(j);
						s[4] = radio_button.get(j);
						try {
							s[5] = media.get(j);
						} catch (Exception e) {
							e.printStackTrace();
						}
						s[6] = messagetype.get(j);
						s[7] = mmstext.get(j);
						lists[i].add(createObject(s));
						temp_data.add(s[3]);
					}
				}
				AackDisplayAdapter ex = new AackDisplayAdapter(
						MessageDetailsActivity.this, R.layout.aackdisplay_item,
						lists[i]);
				sAdapter.addSection("_____" + header_date.get(i) + "_____", ex);
				data_collection.add("0");
				for (String s : temp_data)
					data_collection.add(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		lstAack.setAdapter(sAdapter);

		if (MessagesFragment.check_msg_click) {
			lstAack.setSelection(sAdapter.getCount() - 1);
		} else {
			lstAack.setSelection(postion);
		}
	}

	public void filter_data(String start, String end) {

		for (int i = 0; i < date_time.size(); i++) {
			if (start.trim().length() != 0 && end.trim().length() != 0) {
				SimpleDateFormat post = new SimpleDateFormat(
						"MM/dd/yyyy EEE HH:mm");

				if (lastbackup.trim().length() != 0 || checkbackground) {
					post = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				}

				try {
					Date start1 = post.parse(start);
					Date end1 = post.parse(end);

					Date datetime = post.parse(date_time.get(i));

					if (start1.compareTo(datetime) <= 0
							&& end1.compareTo(datetime) >= 0) {
						name_web.add(name.get(i));
						mmsimage.add(media.get(i));
						body_web.add(body.get(i));
						date_web.add(date.get(i));
						mmstext_web.add(mmstext.get(i));
						messagetype_web.add(messagetype.get(i));
						time_web.add(time.get(i));
						type_web.add(type.get(i));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else if (start.trim().length() != 0 || end.trim().length() != 0) {
				if (start.compareTo(date_time.get(i)) == 0) {
					name_web.add(name.get(i));
					mmsimage.add(media.get(i));
					body_web.add(body.get(i));
					date_web.add(date.get(i));
					mmstext_web.add(mmstext.get(i));
					messagetype_web.add(messagetype.get(i));
					time_web.add(time.get(i));
					type_web.add(type.get(i));
				}
			}
		}
	}

	public void dataFilter() {
		name_web.clear();
		body_web.clear();
		date_web.clear();
		type_web.clear();
		time_web.clear();
		header_date_web.clear();
		mmsimage.clear();
		mmstext_web.clear();

		if (inbox_date.trim().length() != 0 && send_date.trim().length() != 0) {
			SimpleDateFormat post = new SimpleDateFormat("MM/dd/yyyy EEE HH:mm");

			if (lastbackup.trim().length() != 0 || checkbackground) {
				post = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			}

			try {
				Date inbox_date1 = post.parse(inbox_date);
				Date send_date1 = post.parse(send_date);

				if (inbox_date1.compareTo(send_date1) < 0) {
					start = inbox_date;
					end = send_date;
					Filtering fl = new Filtering();
					fl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				} else {
					start = send_date;
					end = inbox_date;
					Filtering fl = new Filtering();
					fl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (inbox_date.trim().length() != 0
				|| send_date.trim().length() != 0) {
			if (inbox_date.compareTo(send_date) > 0) {
				start = inbox_date;
				end = send_date;
				Filtering fl = new Filtering();
				fl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

			} else {
				start = send_date;
				end = inbox_date;
				Filtering fl = new Filtering();
				fl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}

		}
	}

	// onClick for default back button for closing the application
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			DataUrls.bool_start_5msgs = false;
			MessagesFragment.checkmess = false;
			finish();
		}
		return false;
	}

}
