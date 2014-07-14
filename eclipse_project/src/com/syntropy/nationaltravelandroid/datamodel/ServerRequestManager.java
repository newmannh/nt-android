package com.syntropy.nationaltravelandroid.datamodel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import android.util.Log;

import com.syntropy.nationaltravelandroid.exception.NTException;

public class ServerRequestManager { 

	public final boolean NO_TALKING_TO_SERVER = false;
	
	private final static String SERVER_URL = "https://national-travel.appspot.com/api/v1/";
			//flights?departureAirportCode=JFK&arrivalAirportCode=LAX&year=2014&month=7&day=10&inFuture=true
	
	
	// constructor
	public ServerRequestManager() {}

	public String getJSON(String urlExtension, Map<String,String> parameters) throws NTException {
		
		String fullUrlString = SERVER_URL+urlExtension;
		if(parameters!=null && parameters.size()>0){
			String s = "?";
			for(Map.Entry<String, String> entry : parameters.entrySet()){
				if(s.length()>1) s+="&";
				s+=entry.getKey()+"="+entry.getValue();
			}
		}
		
		if(NO_TALKING_TO_SERVER){
			InputStream in = null;
			try {
				in = new FileInputStream("test_data.json");
				String json = IOUtils.toString(in);
				Log.w("ServerRequestManager", "(TEST DATA USED): "+json);
				return json;
			} catch (Exception e) {
				throw new NTException(e).withLog("ServerRequestManager", e.getMessage());
			} finally {
				IOUtils.closeQuietly(in);
			}
			
		} else {
			
			HttpURLConnection con=null;
			String json = "";
			
			// Making HTTP request
			try {
				URL url = new URL(fullUrlString);
				con = (HttpURLConnection) url.openConnection();
				json = IOUtils.toString(con.getInputStream());
				
				Log.d("HTTP GET", "[GET "+url+"] with response \""+json+"\"");
			} catch (Exception e) {
				throw new NTException(e).withLog("ServerRequestManager", "Exception when attempting to GET "+fullUrlString+": "+e.getMessage());
			} finally {
				if(con!=null) con.disconnect();
			}
			return json;
			
		}
		
	}


	

//	public String postJSONToUrl(String urlString, String json) throws NTException{
//
//		URL url;
//	    HttpURLConnection connection = null;  
//	    try {
//	      //Create connection
//	      url = new URL(urlString);
//	      connection = (HttpURLConnection)url.openConnection();
//	      connection.setRequestMethod("POST");
//	      connection.setDoInput(true);
//	      connection.setDoOutput(true);
//
//	      //Send request
//	      OutputStreamWriter wr= new OutputStreamWriter(connection.getOutputStream());
//	      wr.write(json);
//	      wr.close();
//
//	      //Get Response	
//	      String response = IOUtils.toString(connection.getInputStream());
//	      Log.d("HTTP POST", "[POST "+urlString+" with content "+json+"] with response "+response);
//	      return response;
//
//	    } catch (Exception e) {
//	    	throw new NTException(e).withLog("ServerRequestManager", "Exception when attempting to POST "+json+" to "+urlString+": "+e.getMessage());
//	    } finally {
//	    	if(connection != null) connection.disconnect(); 
//	    }
//	}

	public static boolean isResponseIndicativeOfSuccess(String httpResponse){
		return httpResponse!=null 
				&& !httpResponse.startsWith("40") 
				&& !httpResponse.startsWith("{\"status\":40");
	}


}