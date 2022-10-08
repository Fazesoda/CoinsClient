package me.mindlessly.notenoughcoins.utils;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Utils {

	static JsonElement getJson(String jsonUrl) {
		try {
			URL url = new URL(jsonUrl);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Connection", "close");
			return new JsonParser().parse(new InputStreamReader(conn.getInputStream()));
		} catch (Exception e) {
			//Reference.logger.error(e.getMessage(), e);
			return null;
		}
	}

}
