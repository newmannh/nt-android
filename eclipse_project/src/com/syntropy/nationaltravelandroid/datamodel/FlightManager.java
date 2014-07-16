package com.syntropy.nationaltravelandroid.datamodel;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.syntropy.nationaltravelandroid.exception.NTException;

public class FlightManager {
	
	private static FlightManager INSTANCE;
	private ServerRequestManager serverRequestManager = null;
	
	private Flight[] flights = null;
	private Map<String,Airline> airlines = null;
	
	
	private FlightManager(){
		airlines = new HashMap<String, Airline>();
		serverRequestManager = new ServerRequestManager();
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					refreshFlightData();
				} catch (NTException e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute(new Void[]{});
		
	}
	
	
	/**
	 * Singleton instance accessor 
	 */
	public static FlightManager getFlightManager(){
		if(INSTANCE == null) INSTANCE = new FlightManager();
		return INSTANCE;
	}
	
	public Airline[] getStaticAirlineData(Context c){
		InputStream in;
		try {
			in = c.getAssets().open("test_data.json");
			String jsonString = IOUtils.toString(in);
			parseFlightData(jsonString);
			return airlines.values().toArray(new Airline[airlines.size()]);
		} catch (Exception e) {
			new NTException(e).withLog("FlightManager", e.getMessage());
			e.printStackTrace();
			return new Airline[]{};
		}
	}
	
	private void parseFlightData(String rawJson){
		Gson gson = new Gson();
		flights = gson.fromJson(rawJson, Flight[].class);
		airlines.clear();
		for(Flight flight: flights){
			Airline airline = airlines.get(flight.getAirlineCode());
			if(airline!=null){
				airline.addFlight(flight);
			} else {
				airline = new Airline(flight.getAirlineCode(), flight.getAirlineName());
				airline.addFlight(flight);
				airlines.put(airline.getCode(), airline);
			}
		}
		for(Airline airline : airlines.values()){
			Log.w("ServerRequestManager", "Airline "+airline.getCode()+" has "+airline.getFlights().length+" flight(s)");
		}
	}
	
	
	private void refreshFlightData() throws NTException{
		String flightsJson = serverRequestManager.getJSON("flights", null);
		parseFlightData(flightsJson);
	}
	
	/**
	 * Get the flights.  If waitIfNecessary is true, this method may block in order to fetch
	 * data from the network if necessary.  Thus, if blocking is a possibility, this method
	 * should be called asynchronously (on a non-ui thread)
	 * 
	 * @param waitIfNecessary whether or not to wait for a refresh
	 * @return an array of all flights
	 */
	public Flight[] getFlights(boolean waitIfNecessary){
		if(flights==null && waitIfNecessary){ 
			try {
				refreshFlightData();
			} catch (NTException e) {
				e.printStackTrace();
				return new Flight[]{};
			}
		}
		return flights;
	}
	
	public Airline[] getAirlines(boolean waitIfNecessary){
		if(airlines.isEmpty() && waitIfNecessary){ 
			try {
				refreshFlightData();
			} catch (NTException e) {
				e.printStackTrace();
			}
		}
		return airlines.values().toArray(new Airline[airlines.size()]);
	}
	
}
