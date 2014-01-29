package com.danegor.wheelyproject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Content extends AsyncTask<Void, Void, String>{

	public static final String ID = "ID";
	public static final String TITLE = "TITLE";
	public static final String TEXT = "TEXT";
	
	public class Item {
		String id, title, text;
	}
	
	
	public static List<Item> ITEMS = new ArrayList<Item>();
	public static Map<String, Item> ITEM_MAP = new HashMap<String, Item>();
	public static List<Map<String, String>> DATA = new ArrayList<Map<String, String>>();
	
	public void update() {
		try {
			new Content().execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected String doInBackground(Void... params) {
		HttpClient client = new DefaultHttpClient();
		String res = null;
		try {
			HttpGet httpget = new HttpGet("http://crazy-dev.wheely.com");
			ResponseHandler<String> rh = new BasicResponseHandler();
			res = client.execute(httpget, rh);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			client.getConnectionManager().shutdown();
		}
		return res;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		DATA.clear();
		ITEM_MAP.clear();
		Gson gson = new Gson();
		ITEMS = gson.fromJson(result, new TypeToken<ArrayList<Item>>(){}.getType());
		for (Item item : ITEMS) {
			ITEM_MAP.put(item.id, item);
			Map<String, String> itemMap = new HashMap<String, String>();
			itemMap.put(ID, item.id);
			itemMap.put(TITLE, item.title);
			itemMap.put(TEXT, item.text);
			DATA.add(itemMap);
		}
	}
}
