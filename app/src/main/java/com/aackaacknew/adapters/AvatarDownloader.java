package com.aackaacknew.adapters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.widget.ImageView;

public class AvatarDownloader {

	private HashMap<String, Bitmap> cache = new HashMap<String, Bitmap>();

	private File cacheDir;
	String path = "";
	static Context con;

	public AvatarDownloader(Context context) {
		photoLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);

		con = context;
		// Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					"/download/myApp/avatars/");
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	// final int stub_id=R.drawable.icon_contact_small;
	public void DisplayImage(String url, String profilePic, Activity activity,
			ImageView imageView) {
		if (cache.containsKey(url))
			imageView.setImageBitmap(cache.get(url));
		else {
			queuePhoto(url, activity, imageView, profilePic);
		}
	}

	private void queuePhoto(String url, Activity activity, ImageView imageView,
			String profilePic) {
		// This ImageView may be used for other images before. So there may be
		// some old tasks in the queue. We need to discard them.
		photosQueue.Clean(imageView);
		PhotoToLoad p = new PhotoToLoad(url, imageView, profilePic);
		synchronized (photosQueue.photosToLoad) {
			photosQueue.photosToLoad.push(p);
			photosQueue.photosToLoad.notifyAll();
		}

		if (photoLoaderThread.getState() == Thread.State.NEW)
			photoLoaderThread.start();
	}

	// Task for the queue
	class PhotoToLoad {
		public String url;
		public ImageView imageView;
		public String profilePic;

		public PhotoToLoad(String u, ImageView i, String profilePic) {
			this.url = u;
			this.imageView = i;
			this.profilePic = profilePic;
		}
	}

	PhotosQueue photosQueue = new PhotosQueue();

	public void stopThread() {
		photoLoaderThread.interrupt();
	}

	// stores list of photos to download
	class PhotosQueue {
		private Stack<PhotoToLoad> photosToLoad = new Stack<PhotoToLoad>();

		// removes all instances of this ImageView
		public void Clean(ImageView image) {
			for (int j = 0; j < photosToLoad.size();) {
				if (photosToLoad.get(j).imageView == image)
					photosToLoad.remove(j);
				else
					++j;
			}
		}
	}

	class PhotosLoader extends Thread {
		public void run() {
			try {
				while (true) {
					// thread waits until there are any images to load in the
					// queue
					if (photosQueue.photosToLoad.size() == 0)
						synchronized (photosQueue.photosToLoad) {
							photosQueue.photosToLoad.wait();
						}
					if (photosQueue.photosToLoad.size() != 0) {
						PhotoToLoad photoToLoad;
						synchronized (photosQueue.photosToLoad) {
							photoToLoad = photosQueue.photosToLoad.pop();
						}

						Bitmap path = GetMmsAttachment(photoToLoad.url);

						if (path != null) {

							cache.put(photoToLoad.url, path);
							Object tag = photoToLoad.imageView.getTag();
							if (tag != null
									&& ((String) tag).equals(photoToLoad.url)) {
								BitmapDisplayer bd = new BitmapDisplayer(path,
										photoToLoad.imageView);
								Activity a = (Activity) photoToLoad.imageView
										.getContext();
								a.runOnUiThread(bd);
							}
						}
					}
					if (Thread.interrupted())
						break;
				}
			} catch (InterruptedException e) {
			}
		}
	}

	PhotosLoader photoLoaderThread = new PhotosLoader();

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		ImageView imageView;

		public BitmapDisplayer(Bitmap b, ImageView i) {
			bitmap = b;
			imageView = i;
		}

		public void run() {
			if (bitmap != null)
				imageView.setImageBitmap(bitmap);
			else {

			}
		}
	}

	public void clearCache() {
		// clear memory cache
		cache.clear();

		// clear SD cache
		File[] files = cacheDir.listFiles();
		for (File f : files)
			f.delete();
	}

	public static Bitmap decodeBase64(String input) {

		int scale = 3;
		byte[] decodedByte = Base64.decode(input, 0);
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inSampleSize = scale;
		Bitmap bts = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(
				new ByteArrayInputStream(decodedByte), null, o), 200, 200,
				false);

		return bts;

	}

	public Bitmap base64imageformat(String path) {

		Bitmap bm = BitmapFactory.decodeFile(path);

		if (bm != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 90, baos); // bm is the
																// bitmap
																// object

		}
		return bm;
	}

	public static Bitmap GetMmsAttachment(String _id) {

		Bitmap bitmap = null;
		Uri partURI = Uri.parse("content://mms/part/" + _id);
		InputStream is = null;
		try {
			is = con.getContentResolver().openInputStream(partURI);
			bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(is),
					130, 130, false);

		} catch (Exception e) {
			e.printStackTrace();

		}
		return bitmap;
	}

}
