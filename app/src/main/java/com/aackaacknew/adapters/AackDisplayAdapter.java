package com.aackaacknew.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aackaacknew.activities.R;
import com.aackaacknew.utils.NewImageLoader;

public class AackDisplayAdapter extends ArrayAdapter<Map<String, ?>> {
	private LayoutInflater inflater;
	private int resId = 0;
	public static boolean checkavataer;
	public boolean check;
	private Activity context;
	ViewHolder holder;
	NewImageLoader imageloader;
	private List<Map<String, ?>> listitem;
	private List<View> views = new ArrayList<View>();
	SharedPreferences prefs;
	AvatarDownloader displaybase64;
	String lastbackup = "";

	public AackDisplayAdapter(Activity c, int aackadapter,
			List<Map<String, ?>> dataList) {
		super(c, aackadapter, dataList);
		this.listitem = dataList;
		prefs = PreferenceManager.getDefaultSharedPreferences(c);
		this.resId = aackadapter;
		this.context = c;
		imageloader = new NewImageLoader(context);
		displaybase64 = new AvatarDownloader(c);

		lastbackup = prefs.getString("lastbackup", "");

		for (int i = 0; i < listitem.size(); i++) {
			Map<String, ?> item = listitem.get(i);
			item.get("values");
		}

		inflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {
		convertView = inflater.inflate(resId, parent, false);

		holder = new ViewHolder();

		holder.pbsentbox = (ProgressBar) convertView
				.findViewById(R.id.mms_sentprogress);
		holder.pbinbox = (ProgressBar) convertView
				.findViewById(R.id.mms_inboxprogress);

		holder.mmstextsent = (TextView) convertView
				.findViewById(R.id.mms_textsent);
		holder.mmstextinbox = (TextView) convertView
				.findViewById(R.id.mms_textinbox);
		holder.rinbox = (RelativeLayout) convertView
				.findViewById(R.id.rel_inbox);
		holder.rsent = (RelativeLayout) convertView.findViewById(R.id.rel_sent);
		holder.body_inbox = (TextView) convertView
				.findViewById(R.id.text_inbox);
		holder.body_sent = (TextView) convertView.findViewById(R.id.text_sent);
		holder.imgInbox = (ImageView) convertView.findViewById(R.id.mms_inbox);
		holder.imgSent = (ImageView) convertView.findViewById(R.id.mms_sent);
		holder.txtTime_inbox = (TextView) convertView.findViewById(R.id.date_1);
		holder.txtTime_sent = (TextView) convertView
				.findViewById(R.id.date_sent);
		convertView.setTag(convertView);

		check = prefs.getBoolean("checkbackup", false);
		{
			ImageView left = (ImageView) convertView
					.findViewById(R.id.inbox_left);
			ImageView right = (ImageView) convertView
					.findViewById(R.id.inbox_right);

			Map<String, ?> item = listitem.get(position);
			final String[] values = (String[]) item.get("values");

			if (values[3].equals("1")) {

				holder.rinbox.setVisibility(View.VISIBLE);
				holder.rsent.setVisibility(View.GONE);
				if (values[6].equals("sms")) {
					holder.pbinbox.setVisibility(View.GONE);
					holder.imgInbox.setVisibility(View.GONE);
					holder.body_inbox.setVisibility(View.VISIBLE);

					if (values[0].length() < 20) {
						holder.body_inbox.setMaxLines(1);
					}
					holder.body_inbox.setText(values[0] + "      ");
				} else {

					if (values[7].startsWith("0")) {
					} else {

						holder.mmstextinbox.setVisibility(View.VISIBLE);
						holder.mmstextinbox.setText(values[7]);
					}

					holder.pbinbox.setVisibility(View.VISIBLE);

					holder.imgInbox.setVisibility(View.VISIBLE);
					holder.body_inbox.setVisibility(View.GONE);

					if (check == true
							|| ((lastbackup.trim().length() != 0) && (!lastbackup
									.trim().equalsIgnoreCase("null")))) {

						imageloader.DisplayImage(values[0], holder.imgInbox);

					} else {

						holder.imgInbox.setTag(values[0]);

						displaybase64.DisplayImage(values[0], "profilepic",
								context, holder.imgInbox);
						checkavataer = true;

					}
				}
				holder.txtTime_inbox.setText(values[2]);

				if (values[4].equals("1")) {
					left.setBackgroundResource(R.drawable.msg_redbtn);
				} else if (values[4].equals("2")) {
					left.setBackgroundResource(R.drawable.msg_greenbtn);
				}

			} else {

				holder.rinbox.setVisibility(View.GONE);
				holder.rsent.setVisibility(View.VISIBLE);
				if (values[6].equals("sms")) {

					holder.pbsentbox.setVisibility(View.GONE);
					holder.imgSent.setVisibility(View.GONE);
					holder.body_sent.setVisibility(View.VISIBLE);

					if (values[0].length() < 20) {
						holder.body_sent.setMaxLines(1);
					}

					holder.body_sent.setText(values[0] + "     ");
				} else {

					holder.pbsentbox.setVisibility(View.VISIBLE);

					if (values[7].startsWith("0")) {

					} else {

						holder.mmstextsent.setVisibility(View.VISIBLE);

						holder.mmstextsent.setText(values[7]);
					}
					holder.imgSent.setVisibility(View.VISIBLE);
					holder.body_sent.setVisibility(View.GONE);

					if (check == true
							|| ((lastbackup.trim().length() != 0) && (!lastbackup
									.trim().equalsIgnoreCase("null")))) {

						imageloader.DisplayImage(values[0], holder.imgSent);

					} else {

						holder.imgSent.setTag(values[0]);

						displaybase64.DisplayImage(values[0], "profilepic",
								context, holder.imgSent);

					}
				}
				holder.txtTime_sent.setText(values[2]);

				if (values[4].equals("2")) {
					right.setBackgroundResource(R.drawable.msg_greenbtn);
				} else if (values[4].equals("1")) {
					right.setBackgroundResource(R.drawable.msg_redbtn);
				}
			}

		}
		views.add(convertView);
		return convertView;

	}

	static class ViewHolder {

		TextView body_inbox, body_sent, txtSubname, txtDescription,
				txtTime_inbox, txtTime_sent;
		ImageView imgInbox;
		ImageView imgSent;

		TextView mmstextsent, mmstextinbox;
		RelativeLayout rinbox, rsent;
		RadioButton rbSelect;
		RadioButton rbSelect2;
		ProgressBar pbsentbox, pbinbox;

	}
}