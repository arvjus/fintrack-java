package org.zv.fintrack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import org.zv.fintrack.model.Category;
import org.zv.fintrack.util.Base64;

public class FintrackApplication extends Application {
	private static FintrackApplication singleton;
	private HttpClient httpClient;
	private boolean authorized;
	private List<Category> categories;
	private ArrayList<Map<String,String>> result;

	public static FintrackApplication getInstance() {
		return singleton;
	}
	
	@Override
	public void onCreate() {
		Log.d(getClass().getSimpleName(), "onCreate()..");
		super.onCreate();
		singleton = this;
	}

	@Override
	public void onLowMemory() {
		Log.d(getClass().getSimpleName(), "onLowMemory()..");
		super.onLowMemory();
        httpClient.getConnectionManager().shutdown();
	}

	@Override
	public void onTerminate() {
		Log.d(getClass().getSimpleName(), "onTerminate()..");
		super.onTerminate();
        httpClient.getConnectionManager().shutdown();
        singleton = null;
	}

	public HttpClient getHttpClient() {
		if (httpClient == null) {
			Log.d(getClass().getSimpleName(),"getHttpClient(), creating..");
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);
			
			SchemeRegistry schemaRegistry = new SchemeRegistry();
			schemaRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schemaRegistry);
			
			httpClient = new DefaultHttpClient(conMgr, params);	
		}
		return httpClient;
	}
	
	/**
	 * Connect to server, verify authentication.
	 * 
	 * @return status code
	 */
	public int authenticate() {
		int rc = 0;
		try {  
	        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			HttpGet httpget = new HttpGet(prefs.getString("service_location", ""));  
			httpget.addHeader("Authorization", "Basic " + Base64.encode(prefs.getString("username", "") + ":" + prefs.getString("password", "")));

			// Execute HTTP GET Request  
			HttpResponse response = getHttpClient().execute(httpget);
	        rc = response.getStatusLine().getStatusCode();
		} catch (IOException e) {  
			Log.e(getClass().getSimpleName(), e.toString());
			rc = -1;
		}
		
		authorized = (rc == HttpStatus.SC_OK);
		return rc;
	}
	
	/**
	 * Retrieve resource via GET method.
	 * 
	 * @return status code
	 */
	public int doGet(String uri, StringBuilder out) {
		int rc = 0;
		out.setLength(0);
		if (authorized) {
			BufferedReader in = null;
			try {  
		        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
				HttpGet httpget = new HttpGet(prefs.getString("service_location", "") + uri);  
				httpget.addHeader("Authorization", "Basic " + Base64.encode(prefs.getString("username", "") + ":" + prefs.getString("password", "")));
	
				// Execute HTTP GET Request  
				HttpResponse response = getHttpClient().execute(httpget);
		        rc = response.getStatusLine().getStatusCode();
		        if (rc == HttpStatus.SC_OK) {
					in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			        String line;
			        while ((line = in.readLine()) != null) {
			            out.append(line + "\n");
			        }
		        }
			} catch (IOException e) {  
				Log.e(getClass().getSimpleName(),e.toString());
				rc = -1;
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						Log.i(getClass().getSimpleName(), e.toString());
					}
				}
			}
		} else {
			Toast.makeText(this, "Error: not authorized", Toast.LENGTH_SHORT).show();
		}
		return rc;
	}

	/**
	 * Retrieve resource via POST method.
	 * 
	 * @return status code
	 */
	public int doPost(String uri, Map<String, String> params, StringBuilder out) {
		int rc = 0;
		out.setLength(0);
		if (authorized) {
			BufferedReader in = null;
			try {  
		        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
				HttpPost httppost = new HttpPost(prefs.getString("service_location", "") + uri);  
				httppost.addHeader("Authorization", "Basic " + Base64.encode(prefs.getString("username", "") + ":" + prefs.getString("password", "")));

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(params.size());
				for (Map.Entry<String, String> entry : params.entrySet()) {
					nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));  
				}
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));  				
	
				// Execute HTTP GET Request  
				HttpResponse response = getHttpClient().execute(httppost);
		        rc = response.getStatusLine().getStatusCode();
		        if (rc == HttpStatus.SC_OK) {
					in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			        String line;
			        while ((line = in.readLine()) != null) {
			            out.append(line + "\n");
			        }
		        }
			} catch (IOException e) {  
				Log.e(getClass().getSimpleName(),e.toString());
				rc = -1;
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						Log.i(getClass().getSimpleName(), e.toString());
					}
				}
			}
		} else {
			Toast.makeText(this, "Error: not authorized", Toast.LENGTH_SHORT).show();
		}
		return rc;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public ArrayList<Map<String, String>> getResult() {
		return result;
	}

	public void setResult(ArrayList<Map<String, String>> result) {
		this.result = result;
	}
}
