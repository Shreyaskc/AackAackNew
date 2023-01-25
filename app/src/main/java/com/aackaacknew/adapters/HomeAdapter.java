package com.aackaacknew.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aackaacknew.activities.R;
import com.aackaacknew.utils.ImageLoader_homeGrid;
import com.aackaacknew.utils.NewImageLoader2;
import com.squareup.picasso.Picasso;

public class HomeAdapter extends ArrayAdapter<String> {

	ArrayList<String> name, subname, time, pic, conversation, caption, aack,
			sharetype;

	private LayoutInflater inflater;
	private int resId = 0;
	private Activity context;
	private NewImageLoader2 loader;
	public static ImageLoader_homeGrid loader_tumb;
	ViewHolder holder;
	Typeface typeFace, typeFace2;

	public HomeAdapter(Activity c, int smsadapter, ArrayList<String> name,
			ArrayList<String> subname, ArrayList<String> time,
			ArrayList<String> img, ArrayList<String> conversation,
			ArrayList<String> caption, ArrayList<String> aack,
			ArrayList<String> sharetype) {
		super(c, smsadapter, name);

		this.name = name;
		this.subname = subname;
		this.time = time;
		this.conversation = conversation;
		this.caption = caption;
		this.aack = aack;
		this.resId = smsadapter;
		this.pic = img;
		this.sharetype = sharetype;
		this.context = c;

		loader = new NewImageLoader2(context);
		loader_tumb = new ImageLoader_homeGrid(context);
		typeFace = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Regular.ttf");
		typeFace2 = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Medium.ttf");
		inflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;

		holder = new ViewHolder();

		view = inflater.inflate(resId, parent, false);

		holder.txtName = (TextView) view.findViewById(R.id.name);
		holder.txtSubname = (TextView) view.findViewById(R.id.subname);
		holder.txtcaption = (TextView) view.findViewById(R.id.caption);
		holder.txtConversation = (TextView) view
				.findViewById(R.id.conversation);
		holder.txtTime = (TextView) view.findViewById(R.id.time);
		holder.imgPro = (ImageView) view.findViewById(R.id.image);
		holder.imgAack = (ImageView) view.findViewById(R.id.aack);
		holder.reaackimage = (ImageView) view.findViewById(R.id.reaack);
		try {
			holder.txtName.setTypeface(typeFace);
			holder.txtSubname.setTypeface(typeFace);
			holder.txtcaption.setTypeface(typeFace);
			holder.txtTime.setTypeface(typeFace);
			holder.txtConversation.setTypeface(typeFace2);
			holder.txtName.setText(name.get(position));
			holder.txtSubname.setText("@" + subname.get(position));

			holder.txtcaption.setText(caption.get(position));
			holder.txtConversation.setText(conversation.get(position));
			holder.txtTime.setText(time.get(position));

			if (sharetype.get(position).equals("myshare")) {
				holder.reaackimage.setVisibility(View.GONE);
			} else if (sharetype.get(position).equals("reshare")) {
				holder.reaackimage.setVisibility(View.VISIBLE);
			}

			if (pic.get(position).equals("null")) {

			} else {
				try {
					loader.DisplayImage(pic.get(position), holder.imgPro);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			try {
				Picasso.with(context).load(aack.get(position))
						.into(holder.imgAack);
				// loader_tumb.DisplayImage(aack.get(position), holder.imgAack);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

	public static class ViewHolder {

		TextView txtName, txtTime, txtSubname, txtcaption, txtConversation;
		ImageView imgPro, imgAack, reaackimage;

	}
}