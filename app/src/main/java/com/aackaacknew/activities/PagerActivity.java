package com.aackaacknew.activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.aackaacknew.adapters.CustomPagerAdapter;


public class PagerActivity extends FragmentActivity {
	int imageArra[] = { R.drawable.screen1, R.drawable.screen2,
			R.drawable.screen3 };
	ViewPager myPager;
	Button button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pager);
		button = (Button) findViewById(R.id.button);
		myPager = (ViewPager) findViewById(R.id.viewpager);
		myPager.setAdapter(new CustomPagerAdapter(this));
		button.setVisibility(View.GONE);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(PagerActivity.this,
						SignInSignupActivity.class);
				startActivity(i);
				finish();
			}
		});
		myPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {

				if (position == 0) {
					button.setVisibility(View.GONE);
					myPager.setCurrentItem(0);

				} else if (position == 1) {
					myPager.setCurrentItem(1);
					button.setVisibility(View.GONE);

				} else if (position == 2) {
					button.setVisibility(View.VISIBLE);
					myPager.setCurrentItem(2);

				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}
}