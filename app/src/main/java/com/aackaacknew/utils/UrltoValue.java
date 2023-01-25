package com.aackaacknew.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class UrltoValue {
	private static final String USER_AGENT = "Mozilla/5.0";
	// To get the Response from when calling the URL
	public static String getValuefromUrl(String url) {
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.addRequestProperty("Cache-Control", "no-cache");
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			return response.toString();
		} catch (Exception ex) {

			ex.printStackTrace();
			return "zero";
		}
	}
}
