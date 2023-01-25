package com.aackaacknew.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aackaacknew.activities.R;
import com.aackaacknew.activities.Splash;
import com.aackaacknew.pojo.Contact;
import com.aackaacknew.utils.ImageHelper;
import com.aackaacknew.utils.ImageLoader1;

public class FavsDetailsAdapter extends BaseAdapter {

	ArrayList<String> caption, user_name, conver, desc, date, pic, lastmsg,
			num;
	String nick;
	boolean check;
	ImageHelper img;

	Activity context;
	TextView txtCaptiondesc;
	TextView txtName;
	TextView txtRelation;
	TextView txtDescription, txtConver, txtDate;
	ImageView imgPic;
	ImageLoader1 loader;
	Typeface typeFace;

	public FavsDetailsAdapter(Activity context, int favouritelistdata,
			String nickname, ArrayList<String> caption,
			ArrayList<String> conver, ArrayList<String> user_name,
			ArrayList<String> date, ArrayList<String> pic,
			ArrayList<String> lastmsg, ArrayList<String> num) {

		this.context = context;

		this.caption = caption;
		this.user_name = user_name;
		this.conver = conver;
		this.date = date;
		this.pic = pic;
		this.lastmsg = lastmsg;
		this.nick = nickname;
		this.num = num;
		typeFace = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Regular.ttf");

		loader = new ImageLoader1(context);
		img = new ImageHelper();
	}

	public int getCount() {
		return caption.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;

		LayoutInflater inflater = context.getLayoutInflater();
		row = inflater.inflate(R.layout.favsdetails_item, parent, false);
		txtCaptiondesc = (TextView) row.findViewById(R.id.captiondes);
		txtName = (TextView) row.findViewById(R.id.name1);
		txtRelation = (TextView) row.findViewById(R.id.name2);
		txtConver = (TextView) row.findViewById(R.id.name3);
		txtDate = (TextView) row.findViewById(R.id.date_new);
		txtDescription = (TextView) row.findViewById(R.id.description);
		imgPic = (ImageView) row.findViewById(R.id.image);
		if (!user_name.get(position).equals(null)
				&& user_name.get(position).trim().length() != 0) {
			txtName.setText("@" + user_name.get(position));
		}
		if (!nick.equals(null) && nick.trim().length() != 0) {
			txtRelation.setText(" & " + nick);
		}
		if (!conver.get(position).equals(null)
				&& conver.get(position).trim().length() != 0) {
			txtConver.setText(" (" + conver.get(position) + ")");
		}
		if (caption.get(position).contains("#")) {
			ForegroundColorSpan span = new ForegroundColorSpan(
					Color.parseColor("#41749b"));
			SpannableString s1 = new SpannableString(caption.get(position));
			String[] s2 = caption.get(position).split(" ");
			int currIndex = 0;
			for (String word : s2) {
				if (word.startsWith("#")) {
					s1.setSpan(span, currIndex, currIndex + word.length(), 0);
				}
				currIndex += (word.length() + 1);
			}

			txtCaptiondesc.setText(s1);
		} else {
			txtCaptiondesc.setTextColor(Color.parseColor("#000000"));
			txtCaptiondesc.setText(caption.get(position));
		}
		if (lastmsg.get(position).startsWith("http")) {
			txtDescription.setText("");
		} else {
			txtDescription.setText(lastmsg.get(position));
		}
		if (!date.get(position).equals(null)
				&& date.get(position).trim().length() != 0) {
			txtDate.setText(date.get(position));
		}

		try {
			if (num.get(position).length() < 10) {
				imgPic.setBackgroundResource(R.drawable.default_profilepic);
			}

			Contact con = null;
			for (int i = 0; i < Splash.contacts.size(); i++) {
				con = Splash.contacts.get(i);

				if (con.getNo().equals(num.get(position))) {

					check = true;
					break;

				} else {

					con = null;
				}

			}
			if (!check) {
				imgPic.setBackgroundResource(R.drawable.default_profilepic);
			}
			check = false;
			if (con != null) {

				Bitmap bt = con.getProfileImage();

				if (con.getNo().length() < 10) {
					imgPic.setBackgroundResource(R.drawable.default_profilepic);
				} else {

					if (bt == null) {
						imgPic.setBackgroundResource(R.drawable.default_profilepic);
					} else {

						imgPic.setImageBitmap(ImageHelper
								.getRoundedCornerBitmap(bt, 50));
					}
				}
			}
		} catch (Exception e) {
		}
		txtName.setTypeface(typeFace);
		txtRelation.setTypeface(typeFace);
		txtCaptiondesc.setTypeface(typeFace);
		txtDescription.setTypeface(typeFace);

		return row;
	}
}
