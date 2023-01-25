package com.aackaacknew.activities;

import java.io.InputStream;
import java.net.URL;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;

import com.aackaacknew.pojo.Contact;

public class ContactsService extends Service {
	public void onCreate() {
	}

	public IBinder onBind(Intent intent) {
		return null;
	}

	public void onDestroy() {
		super.onDestroy();
	}

	@SuppressWarnings("deprecation")
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		try {
			SignUp si = new SignUp();
			si.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	class SignUp extends AsyncTask<URL, Integer, Long> {
		protected Long doInBackground(URL... arg0) {
			try {
				// To get contacts
				getContacts();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	// To get contacts from device
	public void getContacts() {
		Bitmap bit = null;
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String[] projection = new String[] {
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.NUMBER,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID };
		Cursor people = getContentResolver().query(uri, projection, null, null,
				null);
		int indexName = people
				.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
		int indexNumber = people
				.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
		int id = people
				.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);

		Log.d("Contact servicee", "contacts sizeee" + people.getCount());

		people.moveToFirst();
		do {
			long id2 = 0;
			Contact contact = new Contact();
			String name = people.getString(indexName);
			String number = people.getString(indexNumber);
			try {
				id2 = people.getLong(id);
				bit = loadContactPhoto(getContentResolver(), id2);
			} catch (Exception e) {
				e.printStackTrace();
			}
			number = number.replaceAll("[\\W]", "");
			if (number.trim().length() > 10) {
				number = number
						.substring(number.length() - 10, number.length());
			}
			contact.setName(name);
			contact.setNo(number);
			contact.setProfileImage(bit);
			Splash.contacts.add(contact);
		} while (people.moveToNext());
	}

	// To get contact photo from device
	@SuppressWarnings("deprecation")
	public static Bitmap loadContactPhoto(ContentResolver cr, long id) {
		Uri uri = ContentUris.withAppendedId(
				ContactsContract.Contacts.CONTENT_URI, id);
		InputStream input = ContactsContract.Contacts
				.openContactPhotoInputStream(cr, uri);
		if (input == null) {
			return null;
		}
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = 2;
		o2.inPurgeable = true;
		Bitmap bit2 = BitmapFactory.decodeStream(input, null, o2);
		return bit2;
	}
}
