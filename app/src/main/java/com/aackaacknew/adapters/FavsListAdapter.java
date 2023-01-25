package com.aackaacknew.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aackaacknew.activities.R;
import com.aackaacknew.activities.Splash;
import com.aackaacknew.pojo.Contact;
import com.aackaacknew.utils.ImageHelper;
import com.aackaacknew.utils.NewImageLoader2;

public class FavsListAdapter extends ArrayAdapter<String> {
	private Activity context;
	NewImageLoader2 loader;
	ArrayList<String> arl_Name, arl_con, arl_count, arl_pic, arl_num;
	boolean check;
	ImageHelper img;
	Typeface typeFace;

	public FavsListAdapter(Activity context, int textViewResourceId,
			ArrayList<String> arl_Nmae, ArrayList<String> arl_con,
			ArrayList<String> arl_count, ArrayList<String> arl_pic,
			ArrayList<String> arl_num) {
		super(context, textViewResourceId, arl_Nmae);
		this.context = context;
		this.arl_Name = arl_Nmae;
		this.arl_con = arl_con;
		this.arl_count = arl_count;
		this.arl_pic = arl_pic;
		this.arl_num = arl_num;
		loader = new NewImageLoader2(context);
		img = new ImageHelper();
		typeFace = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Bold.ttf");
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		ViewHolder holder = new ViewHolder();

		LayoutInflater inflator = context.getLayoutInflater();
		rowView = inflator.inflate(R.layout.favs_list_item, null);

		holder.txtTitle = (TextView) rowView.findViewById(R.id.title);
		holder.txtName = (TextView) rowView.findViewById(R.id.name);
		holder.txtcount = (TextView) rowView.findViewById(R.id.count);

		holder.img = (ImageView) rowView.findViewById(R.id.image);

		holder.txtTitle.setText(arl_Name.get(position));
		if (arl_con.get(position).trim().length() != 0
				&& !(arl_con.get(position).equals(null))) {
			holder.txtName.setText("(" + arl_con.get(position) + ")");
		}
		holder.txtcount.setText(arl_count.get(position));

		holder.txtTitle.setTypeface(typeFace);
		holder.txtName.setTypeface(typeFace);

		try {

			if (arl_num.get(position).length() < 10) {
				holder.img.setBackgroundResource(R.drawable.default_profilepic);

			}

			Contact con = null;

			for (int i = 0; i < Splash.contacts.size(); i++) {
				con = Splash.contacts.get(i);

				if (con.getNo().equals(arl_num.get(position))) {

					check = true;
					break;

				} else {

					con = null;
				}

			}
			if (!check) {
				holder.img.setBackgroundResource(R.drawable.default_profilepic);
			}
			check = false;
			if (con != null) {

				Bitmap bt = con.getProfileImage();

				if (con.getNo().length() < 10) {
					holder.img
							.setBackgroundResource(R.drawable.default_profilepic);
				} else {

					if (bt == null) {
						holder.img
								.setBackgroundResource(R.drawable.default_profilepic);
					} else {

						holder.img.setImageBitmap(ImageHelper
								.getRoundedCornerBitmap(bt, 50));

						holder.img.getLayoutParams().height = 120;
						holder.img.getLayoutParams().width = 120;
					}

				}

			}
		} catch (Exception e) {

		}

		return rowView;
	}

	class ViewHolder {
		TextView txtTitle, txtName, txtcount;
		ImageView img;
	}

}
