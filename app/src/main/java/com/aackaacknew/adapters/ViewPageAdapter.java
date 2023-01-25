package com.aackaacknew.adapters;

import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ViewPageAdapter extends PagerAdapter {
	FragmentActivity activity;
	int imageArray[];

	public ViewPageAdapter(FragmentActivity act, int[] imgArra) {
		imageArray = imgArra;
		activity = act;

	}

	public int getCount() {
		return imageArray.length;
	}

	@SuppressWarnings("deprecation")
	public Object instantiateItem(View collection, int position) {
		ImageView view = new ImageView(activity);
		view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		view.setScaleType(ScaleType.FIT_XY);
		view.setBackgroundResource(imageArray[position]);
		((ViewPager) collection).addView(view, 0);
		return view;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}
}
