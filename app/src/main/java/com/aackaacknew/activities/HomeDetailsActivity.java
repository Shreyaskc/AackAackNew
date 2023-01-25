package com.aackaacknew.activities;

import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aackaacknew.activities.ui.explore.ExploreFragment;
import com.aackaacknew.activities.ui.home.HomeFragment;
import com.aackaacknew.utils.CheckInternetConnection;
import com.aackaacknew.utils.DataUrls;
import com.aackaacknew.utils.ImageLoader1;
import com.aackaacknew.utils.MyProgressDialog;
import com.aackaacknew.utils.UrltoValue;
import com.aackaacknew.utils.ImageLoader1;

public class HomeDetailsActivity extends Activity implements OnClickListener {
	private WebView webview;
	public static String userid;
	private ImageView imgHeadPic, imgLogo;
	ImageLoader1 imgLoader;
	private TextView txtSubname, txtName, viewallcomments, userlikesnumber;
	private Button btnBack;
	RelativeLayout rlFollow, rlFollow_reverse;
	String spann3, strStatus = "", strFollowRes = "";
	ImageView comment, like, reaack;
	JSONArray ja;
	JSONArray ja2;
	JSONObject jo, jo2, jo3;
	String liked = "";
	int temp = 0;
	TextView comentname1, comentname2, comenttext1, comenttext2;
	public static ArrayList<String> likersname = new ArrayList<String>();
	public static ArrayList<String> likerspic = new ArrayList<String>();
	public static ArrayList<String> comments = new ArrayList<String>();
	public static ArrayList<String> time = new ArrayList<String>();
	public static ArrayList<String> commentspic = new ArrayList<String>();
	public static ArrayList<String> commentersname = new ArrayList<String>();
	public static ArrayList<String> comment_time = new ArrayList<String>();

	public static boolean checlvalue_profileLoading;
	public static boolean check_home, home;

	private MyProgressDialog dialog;
	String loginuserid = "", strlikescount = "", strlikestatus = "";
	SharedPreferences preferences;
	boolean follow;

	private static final String HTML_FORMAT = "<html><body style=\"text-align: center; background-color: black; vertical-align: center;\"><img src = \"%s\" /></body></html>";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homedetailsnew);

		// To get the all id's
		webview = (WebView) findViewById(R.id.web);
		reaack = (ImageView) findViewById(R.id.reaack);
		comment = (ImageView) findViewById(R.id.comment);
		comentname1 = (TextView) findViewById(R.id.commentname1);
		comentname2 = (TextView) findViewById(R.id.commentname2);
		comenttext1 = (TextView) findViewById(R.id.commentext1);
		comenttext2 = (TextView) findViewById(R.id.commentext2);
		like = (ImageView) findViewById(R.id.like);
		viewallcomments = (TextView) findViewById(R.id.viewcoments);
		userlikesnumber = (TextView) findViewById(R.id.userlikesnum);
		rlFollow = (RelativeLayout) findViewById(R.id.imagelast);
		rlFollow_reverse = (RelativeLayout) findViewById(R.id.imagelast_reverse);
		btnBack = (Button) findViewById(R.id.back);
		txtSubname = (TextView) findViewById(R.id.subname);
		txtName = (TextView) findViewById(R.id.title);
		imgHeadPic = (ImageView) findViewById(R.id.image);
		imgLogo = (ImageView) findViewById(R.id.logo);
		userid = ExploreFragment.homeuserid;
		imgLoader = new ImageLoader1(this);

		home = false;

		userlikesnumber.setOnClickListener(this);
		rlFollow_reverse.setOnClickListener(this);
		rlFollow.setOnClickListener(this);
		like.setOnClickListener(this);
		comment.setOnClickListener(this);
		reaack.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		imgLogo.setOnClickListener(this);
		txtName.setOnClickListener(this);

		preferences = PreferenceManager
				.getDefaultSharedPreferences(HomeDetailsActivity.this);
		loginuserid = preferences.getString("userid", "");

		Intent in = getIntent();

		@SuppressWarnings("unused")
		String userid = in.getStringExtra("userid");

		try {
			if (!(ExploreFragment.homepic.equals(null))) {
				imgLoader.DisplayImage(ExploreFragment.homepic, imgHeadPic);
			}
			txtName.setText(ExploreFragment.homestrname);
			txtSubname.setText("@" + ExploreFragment.homestrusername);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (ExploreFragment.homeuserid != null) {
			if (ExploreFragment.homeuserid.equals(loginuserid)
					|| ExploreFragment.homeresharestatus) {
				reaack.setVisibility(View.GONE);
				reaack.setOnClickListener(this);
			}
		}

		dialog = MyProgressDialog.show(HomeDetailsActivity.this, null, null);
		FollowDob dob = new FollowDob();
		dob.execute();
	}// oncreate.....

	// onClick for default back button for closing the application
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			comments.clear();
			setResult(RESULT_OK);
			if (follow) {
				Intent i = new Intent(HomeDetailsActivity.this,
						MainActivity.class);
				startActivity(i);
				finish();
			} else {
				finish();
			}
		}
		return false;
	}

	// To know the Followstatus
	class FollowDob extends AsyncTask<URL, Integer, Long> {
		protected Long doInBackground(URL... params) {
			if (CheckInternetConnection.isOnline(HomeDetailsActivity.this)) {
				String res = UrltoValue.getValuefromUrl(DataUrls.getcomments
						+ ExploreFragment.homeaackid);
				String likes = UrltoValue.getValuefromUrl(DataUrls.likedusers
						+ ExploreFragment.homeaackid);
				String likescount = UrltoValue
						.getValuefromUrl(DataUrls.aackcount
								+ ExploreFragment.homeaackid + "&userid="
								+ loginuserid);
				if (HomeFragment.follow_status_check) {
					try {
						strFollowRes = UrltoValue
								.getValuefromUrl(DataUrls.followstatus
										+ "&userid=" + loginuserid
										+ "&followuserid=" + userid);
						jo = new JSONObject(strFollowRes);
						strStatus = jo.getString("followstatus");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				try {
					strlikescount = new JSONObject(likescount)
							.getString("aackcount");
					strlikestatus = new JSONObject(likescount)
							.getString("likestatus");

					ja2 = new JSONArray(likes);
					for (int i = 0; i < ja2.length(); i++) {
						jo3 = ja2.getJSONObject(i);
						likersname.add(jo3.getString("fullname"));
						likerspic.add(jo3.getString("profilepic"));
					}
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				try {
					ja = new JSONArray(res);
					for (int i = 0; i < ja.length(); i++) {
						jo = ja.getJSONObject(i);
						comments.add(" " + jo.getString("comment"));
						time.add(jo.getString("datetime"));
						commentspic.add(jo.getString("profilepic"));
						commentersname.add(jo.getString("username"));
						comment_time.add(jo.getString("devicedatetime"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(HomeDetailsActivity.this,
								DataUrls.dialogtitle, Toast.LENGTH_SHORT)
								.show();
					}
				});
			}
			return null;
		}

		@SuppressLint("NewApi")
		protected void onPostExecute(Long result) {
			try {
				final String imgUrl = ExploreFragment.homeaack;
				try {
					dialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
				String strBody = "<html><body><img style='width:100%;' src='"
						+ imgUrl + "'></img>";
				String.format(HTML_FORMAT, strBody);
				webview.loadDataWithBaseURL("", strBody, null, "UTF-8", "");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (HomeFragment.follow_status_check) {
				if (strStatus.equals("follow")) {
					rlFollow_reverse.setVisibility(View.VISIBLE);
				} else {
					rlFollow.setVisibility(View.VISIBLE);
				}
			}
			if (strlikescount.equals("1")) {
				userlikesnumber.setTextColor(Color.RED);
				userlikesnumber.setText(strlikescount + " " + "like");
			} else {
				userlikesnumber.setTextColor(Color.RED);
				userlikesnumber.setText(strlikescount + " " + "likes");
			}
			if (comments.size() >= 2) {
				temp = 2;
			} else {
				temp = comments.size();
			}
			if (strlikestatus.equals("0")) {
				like.setBackgroundResource(R.drawable.like);
			} else {
				like.setBackgroundResource(R.drawable.like_on);
			}
			if (comments.size() >= 2) {
				viewallcomments.setText("View all" + " " + comments.size()
						+ " comments");
			} else if (comments.size() == 1) {
				viewallcomments.setText("View 1 comment");
			} else {
				viewallcomments.setText("View 0 comments");
			}
			if (commentersname.size() > 0) {
				if (commentersname.size() == 1) {
					comentname1.setTextColor(Color.RED);
					comentname1.setText(commentersname.get(commentersname
							.size() - 1));
					if (comments.get(comments.size() - 1).contains("#")) {
						ForegroundColorSpan span = new ForegroundColorSpan(
								Color.RED);
						SpannableString s1 = new SpannableString(
								comments.get(comments.size() - 1));
						String[] s2 = comments.get(comments.size() - 1).split(
								" ");
						int currIndex = 0;
						for (String word : s2) {
							if (word.startsWith("#")) {
								s1.setSpan(span, currIndex,
										currIndex + word.length(), 0);
							}
							currIndex += (word.length() + 1);
						}
						comenttext1.setText(s1);
					} else {
						comenttext1.setTextColor(Color.parseColor("#000000"));
						comenttext1.setText(comments.get(comments.size() - 1));
					}
				} else {
					comentname1.setTextColor(Color.RED);
					comentname1.setText(commentersname.get(commentersname
							.size() - 1));
					comentname2.setTextColor(Color.RED);
					comentname2.setText(commentersname.get(commentersname
							.size() - 2));
					if (comments.get(comments.size() - 2).contains("#")) {
						ForegroundColorSpan span = new ForegroundColorSpan(
								Color.RED);
						SpannableString s1 = new SpannableString(
								comments.get(comments.size() - 2));
						String[] s2 = comments.get(comments.size() - 2).split(
								" ");
						int currIndex = 0;
						for (String word : s2) {
							if (word.startsWith("#")) {
								s1.setSpan(span, currIndex,
										currIndex + word.length(), 0);
							}
							currIndex += (word.length() + 1);
						}
						comenttext2.setText(s1);
					} else {
						comenttext2.setTextColor(Color.parseColor("#000000"));
						comenttext2.setText(comments.get(comments.size() - 2));
					}
					if (comments.get(comments.size() - 1).contains("#")) {
						ForegroundColorSpan span = new ForegroundColorSpan(
								Color.RED);
						SpannableString s1 = new SpannableString(
								comments.get(comments.size() - 1));
						String[] s2 = comments.get(comments.size() - 1).split(
								" ");

						int currIndex = 0;
						for (String word : s2) {
							if (word.startsWith("#")) {
								s1.setSpan(span, currIndex,
										currIndex + word.length(), 0);
							}
							currIndex += (word.length() + 1);
						}
						comenttext1.setText(s1);
					} else {
						comenttext1.setTextColor(Color.parseColor("#000000"));
						comenttext1.setText(comments.get(comments.size() - 1));
					}
				}
			}
		}
	}

	// To know number of likes
	class Like extends AsyncTask<URL, Integer, Long> {
		public void onPreExecute() {
			dialog = MyProgressDialog
					.show(HomeDetailsActivity.this, null, null);
		}

		protected Long doInBackground(URL... params) {
			if (CheckInternetConnection.isOnline(HomeDetailsActivity.this)) {
				String res2 = UrltoValue
						.getValuefromUrl(DataUrls.likeaack + loginuserid
								+ "&aackid=" + ExploreFragment.homeaackid);
				try {
					jo2 = new JSONObject(res2);
					liked = jo2.getString("message");
					strlikescount = jo2.getString("aackcount");
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(HomeDetailsActivity.this,
								DataUrls.dialogtitle, Toast.LENGTH_SHORT)
								.show();
					}
				});
			}
			return null;
		}

		@SuppressLint("NewApi")
		protected void onPostExecute(Long result) {
			if (strlikescount.equals("1")) {
				userlikesnumber.setTextColor(Color.RED);
				userlikesnumber.setText(strlikescount + " " + "like");
			} else {
				userlikesnumber.setTextColor(Color.RED);
				userlikesnumber.setText(strlikescount + " " + "likes");
			}
			if (liked.equalsIgnoreCase("UnLiked")) {
				like.setBackgroundResource(R.drawable.like);
			} else if (liked.equalsIgnoreCase("Liked")) {
				like.setBackgroundResource(R.drawable.like_on);
			}
			try {
				dialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// Async class for Follow/UnFollow
	class GridItemclick extends AsyncTask<URL, Integer, Long> {
		Context con;
		JSONArray ja;
		JSONObject jo;
		String searchresponse, status, strFollowingCount;

		protected Long doInBackground(URL... arg0) {
			if (CheckInternetConnection.isOnline(HomeDetailsActivity.this)) {
				searchresponse = UrltoValue
						.getValuefromUrl(DataUrls.addfallowers + "&userid="
								+ loginuserid + "&followuserid=" + userid);
				try {
					jo = new JSONObject(searchresponse);
					status = jo.getString("message");
					strFollowingCount = jo.getString("followingcount");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(HomeDetailsActivity.this,
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
			if (status.contains("You are already following")) {
				Toast.makeText(HomeDetailsActivity.this,
						"You are already following", Toast.LENGTH_LONG).show();
			} else {
				try {
					rlFollow.setVisibility(View.GONE);
					rlFollow_reverse.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	class GridItemclick_Unfollow extends AsyncTask<URL, Integer, Long> {
		Context con;
		JSONArray ja;
		JSONObject jo;
		String strFollowRes, strStatus;

		protected Long doInBackground(URL... arg0) {
			if (CheckInternetConnection.isOnline(HomeDetailsActivity.this)) {
				strFollowRes = UrltoValue.getValuefromUrl(DataUrls.unfollow
						+ "&userid=" + loginuserid + "&followuserid=" + userid);
				try {
					jo = new JSONObject(strFollowRes);
					strStatus = jo.getString("message");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(HomeDetailsActivity.this,
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
			if (strStatus.contains("successfully removed")) {

				if (HomeFragment.explore_home_check) {
					// SlideAnimationThenCallLayout.follow_status_check=false;
					check_home = true;
					/*
					 * Intent intent = new
					 * Intent(HomeDetailsActivity.this,MainActivity.class);
					 * check_home = true; startActivity(intent); finish();
					 */
				}
				rlFollow_reverse.setVisibility(View.GONE);
				rlFollow.setVisibility(View.VISIBLE);
			}
		}
	}

	class MyWebViewClient extends WebViewClient {
		@SuppressLint("SetJavaScriptEnabled")
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.getSettings().setJavaScriptEnabled(true);
			view.loadUrl(url);
			return true;
		}
	}

	@Override
	public void onClick(View v) {
		// To go to likers activity..
		if (v.equals(userlikesnumber)) {
//			Intent ints = new Intent(HomeDetailsActivity.this,
//					LikersActivity.class);
//			startActivity(ints);
//			finish();
		}
		// To go Back Screen(MainActivity)
		else if (v.equals(imgLogo)) {
			comments.clear();
			setResult(RESULT_OK);
			if (HomeFragment.explore_home_check) {
				HomeFragment.explore_home_check = false;
				DataUrls.homenew = true;
				if (follow) {
					Intent i = new Intent(HomeDetailsActivity.this,
							MainActivity.class);
					startActivity(i);
				}
			}
			finish();
		} else if (v.equals(btnBack)) {
			comments.clear();
			setResult(RESULT_OK);

			if (HomeFragment.explore_home_check) {
				HomeFragment.explore_home_check = false;
				DataUrls.homenew = true;
				if (follow) {
					Intent i = new Intent(HomeDetailsActivity.this,
							MainActivity.class);
					startActivity(i);
				}
			}
			finish();
		}
		// To show the all comments..
		else if (v.equals(viewallcomments)) {
//			Intent addcmt = new Intent(HomeDetailsActivity.this,
//					AddcommentActivity.class);
//			startActivity(addcmt);
//			finish();
		}
		// To visit others profile
		else if (v.equals(txtName)) {
			if (HomeFragment.follow_status_check) {
//				Intent intent = new Intent(HomeDetailsActivity.this,
//						ProfileActivity.class);
//				intent.putExtra("userid", userid);
//				startActivity(intent);
//				finish();
			}
		}
		// For unfollow
		else if (v.equals(rlFollow_reverse)) {
			if (CheckInternetConnection.isOnline(HomeDetailsActivity.this)) {
				GridItemclick_Unfollow griditem = new GridItemclick_Unfollow();
				griditem.execute();
				follow = true;
			} else {
				Toast.makeText(HomeDetailsActivity.this, DataUrls.dialogtitle,
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}
		// For follow..
		else if (v.equals(rlFollow)) {
			if (CheckInternetConnection.isOnline(HomeDetailsActivity.this)) {
				GridItemclick griditem = new GridItemclick();
				griditem.execute();
				follow = true;
			} else {
				Toast.makeText(HomeDetailsActivity.this, DataUrls.dialogtitle,
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}
		// For like the aack
		else if (v.equals(like)) {
			Like likedob = new Like();
			likedob.execute();
		}
		// To add the comment..
		else if (v.equals(comment)) {
//			Intent addcmt = new Intent(HomeDetailsActivity.this,
//					AddcommentActivity.class);
//			startActivity(addcmt);
//			finish();
		}
		// To reaack(reshare) the aack
		else if (v.equals(reaack)) {
//			DataUrls.providers = new ArrayList<String>();
//			home = true;
//			DataUrls.email = false;
//
//			Intent reshare = new Intent(HomeDetailsActivity.this,
//					MessageShareActivity.class);
//			startActivity(reshare);
//			finish();
		}

	}

}
