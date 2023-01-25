package com.aackaacknew.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.aackaacknew.activities.R;
import com.aackaacknew.activities.ui.profile.ProfileFragment;
import com.aackaacknew.pojo.Model;
import com.aackaacknew.utils.ImageLoader_homeGrid;
import com.aackaacknew.utils.MyProgressDialog;
import com.aackaacknew.utils.NewImageLoader2;
import com.squareup.picasso.Picasso;

public class ProfileAdapter extends BaseAdapter {

	Activity context;
	ArrayList<String> subname, desc, date, pic, aackcontent, aackid, sharetype,
			reaackfullname, reaackusername;
	String name, fullname, strResponse = "", strUserid = "", strAackid = "";
	NewImageLoader2 loader;
	ImageLoader_homeGrid loader2;
	MyProgressDialog dialog;
	Typeface typeFace2, typeFace;
	public ArrayList<Integer> array = new ArrayList<Integer>();
	List<Model> list = new ArrayList<Model>();
	ViewHolder viewHolder = null;

	static class ViewHolder {
		protected TextView text, txtName, txtusername, txtRelation2, txtDesc,
				txtDate;
		protected ImageView imgPic, aackcontentimv, reaackimage;
		protected CheckBox checkbox;
	}

	public ProfileAdapter(Activity context, int profilelistdata, String pname,
			ArrayList<String> subname, ArrayList<String> desc,
			ArrayList<String> date, ArrayList<String> pic, String fullname,
			ArrayList<String> aackcontent, ArrayList<String> aackid,
			String userid, ArrayList<String> sharetype,
			ArrayList<String> reaackfullname, ArrayList<String> reaackusername) {
		this.aackcontent = aackcontent;
		this.context = context;
		this.name = pname;
		this.subname = subname;
		this.desc = desc;
		this.date = date;

		this.pic = pic;
		this.fullname = fullname;
		this.aackid = aackid;
		this.strUserid = userid;
		this.sharetype = sharetype;
		this.reaackfullname = reaackfullname;
		this.reaackusername = reaackusername;

		loader = new NewImageLoader2(context);
		loader2 = new ImageLoader_homeGrid(context);
	}

	public int getCount() {
		return subname.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		View row = convertView;
		list.add(new Model(subname.get(position)));

		if (row == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			row = inflator.inflate(R.layout.homelistitem, null);
			viewHolder = new ViewHolder();

			viewHolder.checkbox = (CheckBox) row.findViewById(R.id.checkbox);
			viewHolder.txtName = (TextView) row.findViewById(R.id.name);
			viewHolder.txtusername = (TextView) row.findViewById(R.id.subname);
			viewHolder.txtDesc = (TextView) row.findViewById(R.id.caption);
			viewHolder.aackcontentimv = (ImageView) row.findViewById(R.id.aack);
			viewHolder.txtDate = (TextView) row.findViewById(R.id.time);
			viewHolder.imgPic = (ImageView) row.findViewById(R.id.image);

			viewHolder.txtRelation2 = (TextView) row
					.findViewById(R.id.conversation);
			viewHolder.reaackimage = (ImageView) row.findViewById(R.id.reaack);

			viewHolder.txtName.setTypeface(typeFace);
			viewHolder.txtusername.setTypeface(typeFace);
			viewHolder.txtDesc.setTypeface(typeFace);
			viewHolder.txtRelation2.setTypeface(typeFace2);

			viewHolder.checkbox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {

							int getPosition = (Integer) buttonView.getTag(); // Here
																				// we
																				// get
																				// the
																				// position
																				// that
																				// we
																				// have
																				// set
																				// for
																				// the
																				// checkbox
																				// using
																				// setTag.
							list.get(getPosition).setSelected(
									buttonView.isChecked()); // Set the value of
																// checkbox to
																// maintain its
																// state.

						}
					});

			row.setTag(viewHolder);
			row.setTag(R.id.checkbox, viewHolder.checkbox);

		} else {
			viewHolder = (ViewHolder) row.getTag();
		}

		viewHolder.checkbox.setTag(position); // This line is important.
		viewHolder.checkbox.setChecked(list.get(position).isSelected());

		if (ProfileFragment.delete) {
			viewHolder.checkbox.setVisibility(View.VISIBLE);
		} else {
			viewHolder.checkbox.setVisibility(View.GONE);
		}

		if (sharetype.get(position).equals("myshare")) {
			viewHolder.reaackimage.setVisibility(View.GONE);
		} else if (sharetype.get(position).equals("reshare")) {
			viewHolder.reaackimage.setVisibility(View.VISIBLE);
		}

		try {
			if (ProfileFragment.str.equals("1")) {

				if (sharetype.get(position).equals("myshare")) {
					if (!name.equals("null") && name.trim().length() != 0) {
						viewHolder.txtName.setText(fullname);
					}
				} else if (sharetype.get(position).equals("reshare")) {
					if (!reaackfullname.get(position).equals("null")
							&& reaackfullname.get(position).trim().length() != 0) {
						viewHolder.txtName
								.setText(reaackfullname.get(position));
					}

				}
			} else {

				if (!ProfileFragment.profile_username.get(position).equals(
						"null")
						&& ProfileFragment.profile_username.get(position)
								.trim().length() != 0) {
					viewHolder.txtName.setText(ProfileFragment.profile_username
							.get(position));
				}

			}
		} catch (Exception e) {
		}

		try {
			if (ProfileFragment.str.equals("1")) {

				if (sharetype.get(position).equals("myshare")) {
					if (!subname.get(position).equals("null")
							&& subname.get(position).trim().length() != 0) {
						viewHolder.txtusername.setText("@" + name);
					}
				} else if (sharetype.get(position).equals("reshare")) {
					if (!reaackusername.get(position).equals("null")
							&& reaackusername.get(position).trim().length() != 0) {
						viewHolder.txtusername.setText("@"
								+ reaackusername.get(position));
					}
				}

			} else {

				if (!ProfileFragment.profile_nickname.get(position).equals(
						"null")
						&& ProfileFragment.profile_nickname.get(position)
								.trim().length() != 0) {
					viewHolder.txtusername.setText("@"
							+ ProfileFragment.profile_nickname.get(position));

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (desc.get(position).contains("#")) {
			if (desc.get(position).startsWith("http")) {
				viewHolder.txtDesc.setText("");
			} else {
				ForegroundColorSpan span = new ForegroundColorSpan(
						Color.parseColor("#41749b"));
				SpannableString s1 = new SpannableString(desc.get(position));
				String[] s2 = desc.get(position).split(" ");

				int currIndex = 0;
				for (String word : s2) {
					if (word.startsWith("#")) {
						s1.setSpan(span, currIndex, currIndex + word.length(),
								0);
					}
					currIndex += (word.length() + 1);
				}
				viewHolder.txtDesc.setText(s1);
			}

		} else {

			if (desc.get(position).startsWith("http")) {
				viewHolder.txtDesc.setText("");
			} else {
				viewHolder.txtDesc.setTextColor(Color.parseColor("#999999"));

				viewHolder.txtDesc.setText(desc.get(position));
			}

		}
		viewHolder.txtDate.setText(date.get(position));
		viewHolder.txtRelation2.setText(subname.get(position));

		if (!pic.get(position).equals("null")) {

			try {
				loader.DisplayImage(pic.get(position), viewHolder.imgPic);
				Picasso.with(context).load(aackcontent.get(position))
						.into(viewHolder.aackcontentimv);

				// loader2.DisplayImage(aackcontent.get(position),
				// viewHolder.aackcontentimv);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return row;
	}

}
