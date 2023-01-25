package com.aackaacknew.activities;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aackaacknew.FullDetailsActivity;
import com.aackaacknew.adapters.FavsDetailsAdapter;
import com.aackaacknew.fragments.FavsFragment;
import com.aackaacknew.utils.CheckInternetConnection;
import com.aackaacknew.utils.DataUrls;
import com.aackaacknew.utils.MyProgressDialog;
import com.aackaacknew.utils.UrltoValue;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedVignetteBitmapDisplayer;

public class FavsDetailsActivity extends Activity {
	ListView lstFav;
	Button btnBack;
	TextView txtSubname, txtHeader, txtName, txtfavme, txtAckpakfavs, txtCount,
			txtJilly;
	Typeface typeFace, typeFace2, typeFace3;
	boolean isFavs_first = true, loading_favs = true;
	int count, favs_scrollcount = 0, previousTotal_favs = 0;
	FavsDetailsAdapter favs_adapter;
	String strUserid, strAack_userid, strFullname, strUsername, strUserpic,
			strFavsAackCount, strFavname;
	MyProgressDialog dialog;
	ImageView imgProfilepic, imgLogo;
	private View footerView;
	ArrayList<String> arl_Aackuserid, arlLastmeg, arl_AackContent,
			arl_AackCaption, arl_Date, arl_AackUsername, arl_conversation,
			arl_Username, arl_Userpic;

	public void onCreate(Bundle si) {
		super.onCreate(si);
		setContentView(R.layout.favsdetails);

		arl_Aackuserid = new ArrayList<String>();
		arl_AackContent = new ArrayList<String>();
		arl_AackCaption = new ArrayList<String>();
		arl_Date = new ArrayList<String>();
		arl_Username = new ArrayList<String>();
		arl_AackUsername = new ArrayList<String>();
		arl_conversation = new ArrayList<String>();
		arl_Userpic = new ArrayList<String>();
		arlLastmeg = new ArrayList<String>();

		typeFace = Typeface.createFromAsset(getAssets(),
				"fonts/Roboto-Regular.ttf");
		typeFace2 = Typeface.createFromAsset(getAssets(),
				"fonts/Roboto-Light.ttf");
		typeFace3 = Typeface.createFromAsset(getAssets(),
				"fonts/Roboto-Bold.ttf");

		// for footer
		if (CheckInternetConnection.isOnline(this)) {
			footerView = getLayoutInflater().inflate(R.layout.listfooter, null);
		}
		// To get the all id's..
		txtfavme = (TextView) findViewById(R.id.favme);
		txtAckpakfavs = (TextView) findViewById(R.id.ackpakfavs);
		btnBack = (Button) findViewById(R.id.back);
		txtSubname = (TextView) findViewById(R.id.subname);
		txtHeader = (TextView) findViewById(R.id.header);
		txtName = (TextView) findViewById(R.id.name);
		txtCount = (TextView) findViewById(R.id.count);
		txtJilly = (TextView) findViewById(R.id.jilly);
		imgProfilepic = (ImageView) findViewById(R.id.image);
		imgLogo = (ImageView) findViewById(R.id.logo);
		lstFav = (ListView) findViewById(R.id.list);

		txtName.setTypeface(typeFace);
		txtSubname.setTypeface(typeFace);
		txtHeader.setTypeface(typeFace2);
		txtAckpakfavs.setTypeface(typeFace);
		txtfavme.setTypeface(typeFace);
		txtJilly.setTypeface(typeFace3);

		lstFav.setOnScrollListener(new EndlessScrollListenerFavs());
		lstFav.addFooterView(footerView);

		strUserid = FavsFragment.strUserId;
		strAack_userid = FavsFragment.strAack_id;

		DataUrls.initImageLoader(FavsDetailsActivity.this);
		if (!CheckInternetConnection.isOnline(FavsDetailsActivity.this)) {
			Toast.makeText(FavsDetailsActivity.this, DataUrls.dialogtitle,
					Toast.LENGTH_SHORT).show();
			finish();
		}
		// To go Back Screen(FavsFragment)
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});
		// To go Back Screen(FavsFragment)
		imgLogo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});
		// When called List Item(Favs_Aack)
		lstFav.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				try {
					Intent profiledet = new Intent(FavsDetailsActivity.this,
							FullDetailsActivity.class);
					profiledet.putExtra("aackcontent", arl_AackContent.get(pos));
					profiledet.putExtra("caption", arl_AackCaption.get(pos));
					profiledet.putExtra("screenLook", getIntent()
							.getStringExtra(("screenLook")));
					profiledet.putExtra("conversationfrom", getIntent()
							.getStringExtra(("conversationfrom")));
					profiledet.putExtra("lastmessage", getIntent()
							.getStringExtra("lastmessage"));
					profiledet.putExtra("number",
							getIntent().getStringExtra("number"));
					startActivity(profiledet);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		doInFavsListtData data = new doInFavsListtData();
		data.execute();
	}

	// onClick for default back button for closing the application
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			setResult(RESULT_OK);
			finish();
		}
		return false;
	}

	// async class for favs list data...
	class doInFavsListtData extends AsyncTask<URL, Integer, Long> {
		protected void onPreExecute() {
			if (isFavs_first)
				dialog = MyProgressDialog.show(FavsDetailsActivity.this, null,
						null);
		}

		@Override
		protected Long doInBackground(URL... arg0) {
			if (CheckInternetConnection.isOnline(FavsDetailsActivity.this)) {
				try {
					String loginResponse = null, strDate = null;
					strDate = getDateTime();
					loginResponse = UrltoValue
							.getValuefromUrl(DataUrls.favs_details + "userid="
									+ strUserid + "&aackuserid="
									+ strAack_userid + "&devicedatetime="
									+ strDate.replaceAll(" ", "%20")
									+ "&start=" + favs_scrollcount);

					count = 0;
					if (!loginResponse.equals("zero")) {
						try {
							JSONObject jobject = new JSONObject(loginResponse);
							strFullname = jobject.getString("fullname");
							strUsername = jobject.getString("username");
							strUserpic = jobject.getString("userpic");
							strFavsAackCount = jobject
									.getString("favaackcount");
							strFavname = jobject.getString("fav4mename");

							JSONArray jsonArray = jobject.getJSONArray("aacks");
							if (jsonArray.length() != 0) {
								for (int i = 0; i < jsonArray.length(); i++) {
									count++;// added for aptr elements count..
									JSONObject jsonObject = jsonArray
											.getJSONObject(i);
									arl_Aackuserid.add(jsonObject
											.getString("aackuserid"));
									arl_AackContent.add(jsonObject
											.getString("aackcontent"));
									arl_AackCaption.add(jsonObject
											.getString("aackcaption"));
									arl_conversation.add(jsonObject
											.getString("conversationfrom"));
									arl_Date.add(jsonObject
											.getString("devicedatetime"));
									arl_Username.add(jsonObject
											.getString("username"));
									arl_AackUsername.add(jsonObject
											.getString("aackusername"));
									arl_Userpic.add(jsonObject
											.getString("aackuserpic"));
									arlLastmeg.add(jsonObject
											.getString("lastmessage"));
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(FavsDetailsActivity.this,
								DataUrls.dialogtitle, Toast.LENGTH_SHORT)
								.show();
					}
				});
			}
			return null;
		}

		protected void onPostExecute(Long result) {
			DisplayImageOptions optionscustomimge = new DisplayImageOptions.Builder()
					.cacheInMemory(true).cacheOnDisc(true)
					.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
					.displayer(new RoundedVignetteBitmapDisplayer(150, 4))
					.build();

			txtName.setText(strFullname);
			txtJilly.setText(strFavname);
			txtCount.setText(strFavsAackCount);

			try {
				if (!strUsername.equals(null)
						&& strUsername.trim().length() != 0) {
					txtSubname.setText("@" + strUsername);
				}
			} catch (Exception e) {
				strUsername = "";
				e.printStackTrace();
			}
			try {
				ImageLoader.getInstance().displayImage(
						strUserpic.replace(" ", "%20"), imgProfilepic,
						optionscustomimge, null);
			} catch (Exception e) {
				Resources res = getResources(); // need this to fetch the
												// drawable
				Drawable draw = res.getDrawable(R.drawable.default_profilepic);
				imgProfilepic.setImageDrawable(draw);
				e.printStackTrace();
			}
			if (isFavs_first) {
				favs_adapter = new FavsDetailsAdapter(FavsDetailsActivity.this,
						R.layout.favsdetails_item, strFavname, arl_AackCaption,
						arl_conversation, arl_AackUsername, arl_Date,
						arl_Userpic, arlLastmeg, arl_Aackuserid);
				lstFav.setAdapter(favs_adapter);
			} else {
				favs_adapter.notifyDataSetChanged();
			}
			// This is for footerview.......
			if (count != 10) {
				if (lstFav.getFooterViewsCount() == 1)
					lstFav.removeFooterView(footerView);
				count = 0;
			} else {
				if (lstFav.getFooterViewsCount() == 0)
					lstFav.addFooterView(footerView);
			}
			if (isFavs_first) {
				try {
					dialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			isFavs_first = false;
		}
	}// async..........
		// for loading 10 by 10.

	public class EndlessScrollListenerFavs implements OnScrollListener {
		public EndlessScrollListenerFavs() {
		}

		public EndlessScrollListenerFavs(int visibleThreshold) {
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (loading_favs) {
				if (totalItemCount > previousTotal_favs) {
					loading_favs = false;
					previousTotal_favs = totalItemCount;
				}
			}
			if (!loading_favs
					&& (firstVisibleItem + visibleItemCount) == totalItemCount) {
				favs_scrollcount = favs_scrollcount + 10;
				doInFavsListtData dob = new doInFavsListtData();
				dob.execute();
				loading_favs = true;
			}
		}

		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}
	}

	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
}
