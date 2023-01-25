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

//For listing all the bar pictures.
public class MessagesListAdapter_new extends ArrayAdapter<String> {
	private Activity context;
	ArrayList<String> username, strName, strdesc, strTime, imgs, phonenumber,
			media, messagetype;
	ImageHelper img;
	boolean check;
	Typeface typeFace, typeFace2;

	public MessagesListAdapter_new(Activity context, int textViewResourceId,
			ArrayList<String> arlName, ArrayList<String> fullname,
			ArrayList<String> desc, ArrayList<String> time,
			ArrayList<String> imgs, ArrayList<String> lstArrPhoneNum,
			ArrayList<String> media, ArrayList<String> messagetype) {

		super(context, textViewResourceId, arlName);
		this.context = context;
		this.username = arlName;
		this.strName = fullname;
		this.strdesc = desc;
		this.strTime = time;
		this.imgs = imgs;
		this.media = media;
		this.messagetype = messagetype;
		img = new ImageHelper();
		this.phonenumber = lstArrPhoneNum;
		typeFace = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Regular.ttf");
		typeFace2 = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Bold.ttf");

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		ViewHolder holder = new ViewHolder();

		LayoutInflater inflator = context.getLayoutInflater();

		if (username.get(position).equals("MESSAGEFORBACKUPPROCESS")) {

			rowView = inflator.inflate(R.layout.renderbackupingmessage, null);

			holder.txtBackup = (TextView) rowView.findViewById(R.id.backup);

			holder.txtBackup.setTypeface(typeFace);

			holder.txtBackup
					.setText("We have started to backup your messages. The remaining messages should be backed up in a while. Refresh to get the backup status. You can build Aacks, Share them once done. ");

		} else {
			rowView = inflator.inflate(R.layout.messages_list_item, null);

			holder.txtTitle = (TextView) rowView.findViewById(R.id.title);
			holder.txtName = (TextView) rowView.findViewById(R.id.name);
			holder.txtDesc = (TextView) rowView.findViewById(R.id.desc);
			holder.txtTime = (TextView) rowView.findViewById(R.id.time);
			holder.imgArr = (ImageView) rowView.findViewById(R.id.arr);
			holder.img = (ImageView) rowView.findViewById(R.id.image);

			if (strName.get(position).equals("No Name")
					|| strName.get(position).trim().length() == 0) {
				holder.txtTitle.setText(username.get(position));
			} else {

				holder.txtTitle.setText(username.get(position));
				holder.txtName.setText("@" + strName.get(position));
			}

			if (messagetype.get(position).equals("mms")) {
				holder.txtDesc.setText("");

			} else {
				holder.txtDesc.setText(strdesc.get(position));
			}

			holder.txtTime.setText(strTime.get(position));

			if (phonenumber.get(position).length() < 10) {
				holder.img.setBackgroundResource(R.drawable.default_profilepic);

			}

			Contact con = null;

			for (int i = 0; i < Splash.contacts.size(); i++) {
				con = Splash.contacts.get(i);

				if (con.getNo().equals(phonenumber.get(position))) {

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
					}
				}
			}
			holder.txtTitle.setTypeface(typeFace2);
			holder.txtName.setTypeface(typeFace);
			holder.txtDesc.setTypeface(typeFace);
			holder.txtTime.setTypeface(typeFace);

		} // added for show custom message for Backup..

		return rowView;
	}

	class ViewHolder {
		TextView txtTitle, txtName, txtDesc, txtTime, txtBackup;
		ImageView img, imgArr;
	}

}
