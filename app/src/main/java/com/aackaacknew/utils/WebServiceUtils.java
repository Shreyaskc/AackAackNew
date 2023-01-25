package com.aackaacknew.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import android.util.Log;

public class WebServiceUtils {
	public static String convertStreamToString(InputStream is)
			throws IOException {
		String serverResponse = null;
		try {
			if (is != null) {
				Writer writer = new StringWriter();
				char[] buffer = new char[1024];
				try {
					Reader reader = new BufferedReader(
							new InputStreamReader(is));
					int n;
					while ((n = reader.read(buffer)) != -1) {
						writer.write(buffer, 0, n);
					}
				} finally {
					is.close();
				}
				serverResponse = writer.toString();
			} else {
				serverResponse = "{\"Error\":\"No response\"}";
			}
			return serverResponse;
		} catch (IOException e) {
			Log.e("GrrrUtils converting to string failed", e.toString());
		}
		return serverResponse;
	}
}
