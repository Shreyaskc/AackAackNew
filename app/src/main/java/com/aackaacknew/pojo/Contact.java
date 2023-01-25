package com.aackaacknew.pojo;

import android.graphics.Bitmap;

public class Contact {
	String name, no;
	Bitmap profileImage;

	public Bitmap getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(Bitmap profileImage) {
		this.profileImage = profileImage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

}
