package com.syntropy.nationaltravelandroid.services;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.provider.Settings.Secure;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.syntropy.nationaltravelandroid.exception.NTException;

public class GCMHelper {


	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id"; 
	private static final String PROPERTY_APP_VERSION = "appVersion"; 
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	final static String TAG = "GCMHelper";

	/**
	 * Substitute you own sender ID here. This is the project number you got
	 * from the API Console, as described in "Getting Started."
	 */
	String SENDER_ID = "51940419731";

	private Activity activityContext;

	public GCMHelper(Activity activity){
		this.activityContext = activity;
	}


	/**
	 * Register for Google Cloud Messaging
	 * @return true if success, otherwise false
	 */
	public boolean registerForGCM(){
		if (checkPlayServices()) {
			if (getRegistrationId().isEmpty()) {
				registerInBackground();
			}
			return true;
		} else {
			Log.i(TAG, "No valid Google Play Services APK found.");
			return false;
		}
	}

	/*
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activityContext);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, activityContext,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				activityContext.finish();
			}
			return false;
		}
		return true;
	}



	/*
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 *
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId() {
		final SharedPreferences prefs = getGCMPreferences(activityContext);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		} 
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(activityContext);
		if (registeredVersion != currentVersion) {
			Log.i("", "App version changed.");
			return "";
		}
		Log.i(TAG, "registration id:"+registrationId);
		return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences, but
		// how you store the regID in your app is up to you.
		return context.getSharedPreferences(context.getClass().getSimpleName(),
				Context.MODE_PRIVATE);
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}


	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		new AsyncTask<Void,String,String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					GoogleCloudMessaging  gcm = GoogleCloudMessaging.getInstance(activityContext);
					String regid = gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + regid;

					// You should send the registration ID to your server over HTTP,
					// so it can use GCM/HTTP or CCS to send messages to your app.
					// The request to your server should be authenticated if your app
					// is using accounts.
					sendRegistrationIdToBackend(regid);

					// For this demo: we don't need to send it because the device
					// will send upstream messages to a server that echo back the
					// message using the 'from' address in the message.

					// Persist the regID - no need to register again.
					storeRegistrationId(activityContext, regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				Log.d(TAG,"onPostExecute: "+msg);
			}
		}.execute(null, null, null);
		//	    ...
	}

	/**
	 * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
	 * or CCS to send messages to your app. Not needed for this demo since the
	 * device sends upstream messages to a server that echoes back the message
	 * using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend(String registrationId) {

		Log.d("C2DM", "Sending registration ID to my application server:"+registrationId);
		
		URL url;
	    HttpURLConnection connection = null;  
	    String urlString = "0.0.0.0/0";
	    try {
	      //Create connection
	      url = new URL(urlString);
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");
	      connection.setDoInput(true);
	      connection.setDoOutput(true);

	      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	      String deviceId = Secure.getString(activityContext.getContentResolver(), Secure.ANDROID_ID); 
	      nameValuePairs.add(new BasicNameValuePair("deviceid", deviceId));
	      nameValuePairs.add(new BasicNameValuePair("registrationid", registrationId));
	      
	      //Send request
	      boolean first = true;
	      OutputStreamWriter wr= new OutputStreamWriter(connection.getOutputStream());
	      for(NameValuePair pair : nameValuePairs){
	    	  if(first) first = false;
	    	  else wr.write("&");
	    	  wr.write(pair.getName());
	    	  wr.write("=");
	    	  wr.write(URLEncoder.encode(pair.getValue(),"UTF-8"));
	      }
	      wr.close();

	      //Get Response	
	      String response = IOUtils.toString(connection.getInputStream());
	      Log.d("HTTP POST", "[POST "+urlString+"] (deviceId and regId) with response "+response);

	    } catch (Exception e) {
	    	new NTException(e).withLog("ServerRequestManager", "Exception when attempting to POST to "+urlString+": "+e.getMessage()).printStackTrace();
	    } finally {
	    	if(connection != null) connection.disconnect(); 
	    }
		

	}


	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 *
	 * @param context application's context.
	 * @param regId registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}


}
