package com.syntropy.nationaltravelandroid.datamodel;

import java.util.HashMap;
import java.util.Map;

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
	
	private void refreshFlightData() throws NTException{
		String flightsJson = serverRequestManager.getJSON("flights", null);
		Gson gson = new Gson();
		flights = gson.fromJson(flightsJson, Flight[].class);
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
