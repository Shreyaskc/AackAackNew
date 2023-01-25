package com.aackaacknew.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

public class WebService {

	private DataOutputStream outputStream = null;
	private String lineEnd = "\r\n";
	private String twoHyphens = "--";
	private String boundary = "*****";
	private String dQ = "\"";
	private String serverResponse;
	private int orientation;
	private String httpPath;
	private HttpURLConnection connection = null;
	private boolean lastLine = false;
	DataInputStream inputStream = null;
	int bytesRead, bytesAvailable, bufferSize;
	byte[] buffer;
	int maxBufferSize = 1 * 1024 * 1024;
	int serverResponseCode;
	String serverResponseMessage;

	public WebService(String url) {
		httpPath = url;
	}

	public void openConnection() {
		URL url;
		try {
			url = new URL(httpPath);
			connection = (HttpURLConnection) url.openConnection();

			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			// Enable POST method
			connection.setRequestMethod("POST");

			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("ENCTYPE", "multipart/form-data");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}

	}
// To close the HttpUrl connection
	public void closeConnection() {

		try {

			InputStream is;
			is = connection.getInputStream();
			serverResponse = WebServiceUtils.convertStreamToString(is);
			outputStream.flush();
			outputStream.close();
			connection.disconnect();

		} catch (IOException e) {

		}
	}

	public String getResult() {
		return serverResponse;
	}

	// to set the string parameter to the webservice
	public void setStringParameter(String name, String value) {

		String pCon = "Content-Disposition: form-data;name=" + dQ + name + dQ
				+ lineEnd;
		try {
			outputStream.writeBytes(pCon);
			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(value);
			setLastLine();

		} catch (IOException e) {

		}

	}

	// to set the image parameter to the webservice
	public void setImageParameter(String name, String path, boolean compress) {

		String req = "Content-Disposition: form-data;name=" + dQ + name + dQ
				+ ";filename=" + dQ + path + dQ + lineEnd;

		try {
			outputStream.writeBytes(req);
			outputStream.writeBytes(lineEnd);
			if (compress) {
				File myFile = null;

				if (path != null)
					myFile = new File(path);
				if (myFile != null) {
					if (DataUrls.from.equals("share")) {
						decodeFile(myFile, path).compress(CompressFormat.PNG,
								90, outputStream);
					} else {
						decodeFile(myFile, path).compress(CompressFormat.JPEG,
								90, outputStream);
					}
					// SET BITMAP TO IMAGEVIEW
				}

			} else {
				int bytesRead, bytesAvailable, bufferSize;
				byte[] buffer;
				int maxBufferSize = 1 * 1024 * 1024;
				FileInputStream fileInputStream = new FileInputStream(new File(
						path));
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, bytesAvailable); // for
																		// getting
																		// complete
																		// image
																		// for
																		// posting
																		// to
																		// web...
				buffer = new byte[bufferSize];
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {
					outputStream.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}

				fileInputStream.close();

			}
			setLastLine();

		} catch (Exception e) {

		}

	}

	public void setLastLine(boolean lastLine) {
		this.lastLine = lastLine;
	}

	private void setLastLine() {

		try {
			outputStream.writeBytes(lineEnd);
			if (lastLine) {
				outputStream.writeBytes(twoHyphens + boundary + twoHyphens
						+ lineEnd);
			} else {
				outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			}
		} catch (IOException e) {
		}
	}
// To get the Bitmap using path
	public Bitmap getBitmap(String path, int value) {

		try {
			// Decode image size

			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;

			URL url = new URL(path);

			// Read the image ...
			InputStream inputStream = url.openStream();

			BitmapFactory.decodeStream(inputStream, null, o);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 70;

			// Find the correct scale value. It should be the power of 2.
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(inputStream, null, o2);
		} catch (Exception e) {
		}
		return null;

	}

	// To decode the File using path
	private Bitmap decodeFile(File f, String path) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.

			int scale = 2;

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			Bitmap bm = BitmapFactory.decodeStream(new FileInputStream(f),
					null, o2);

			Bitmap bitmap = bm;

			ExifInterface exif = new ExifInterface(path);

			orientation = exif
					.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

			Matrix m = new Matrix();

			if ((orientation == 3)) {

				m.postRotate(180);
				m.postScale((float) bm.getWidth(), (float) bm.getHeight());

				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bitmap;
			} else if (orientation == 6) {

				m.postRotate(90);

				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bitmap;
			}

			else if (orientation == 8) {

				m.postRotate(270);

				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);

				return bitmap;
			}
			return bitmap;
		} catch (Exception e) {
		}
		return null;
	}

	public static String openGetWebService(String url) {

		InputStream inputStream;
		try {
			if (url.contains(" ")) {
				url = url.replace(" ", "%20");
			}

			inputStream = (InputStream) new URL(url).getContent();
			return WebServiceUtils.convertStreamToString(inputStream);

		} catch (IOException e) {
			return e.toString();
		}
	}
// To get Orientation of image(Uri)
	public static int getOrientation(Context context, Uri photoUri) {
		/* it's on the external media. */
		Cursor cursor = context.getContentResolver().query(photoUri,
				new String[] { MediaStore.Images.ImageColumns.ORIENTATION },
				null, null, null);

		if (cursor.getCount() != 1) {
			return -1;
		}

		cursor.moveToFirst();
		return cursor.getInt(0);
	}

	public static Bitmap getCorrectlyOrientedImage(Context context, Uri photoUri)
			throws IOException {
		InputStream is = context.getContentResolver().openInputStream(photoUri);
		BitmapFactory.Options dbo = new BitmapFactory.Options();
		dbo.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(is, null, dbo);
		is.close();

		int rotatedWidth, rotatedHeight;
		int orientation = getOrientation(context, photoUri);

		int MAX_IMAGE_DIMENSION = 70;

		if (orientation == 90 || orientation == 270) {
			rotatedWidth = dbo.outHeight;
			rotatedHeight = dbo.outWidth;
		} else {
			rotatedWidth = dbo.outWidth;
			rotatedHeight = dbo.outHeight;
		}

		Bitmap srcBitmap;
		is = context.getContentResolver().openInputStream(photoUri);
		if (rotatedWidth > MAX_IMAGE_DIMENSION
				|| rotatedHeight > MAX_IMAGE_DIMENSION) {
			float widthRatio = ((float) rotatedWidth)
					/ ((float) MAX_IMAGE_DIMENSION);
			float heightRatio = ((float) rotatedHeight)
					/ ((float) MAX_IMAGE_DIMENSION);
			float maxRatio = Math.max(widthRatio, heightRatio);

			// Create the bitmap from file
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = (int) maxRatio;
			srcBitmap = BitmapFactory.decodeStream(is, null, options);
		} else {
			srcBitmap = BitmapFactory.decodeStream(is);
		}
		is.close();
		if (orientation > 0) {
			Matrix matrix = new Matrix();
			matrix.postRotate(orientation);

			srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
					srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
		}

		return srcBitmap;
	}

}
