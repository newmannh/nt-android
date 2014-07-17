package com.syntropy.nationaltravelandroid.datamodel;

import java.util.Comparator;
import java.util.TreeSet;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

public class Airline implements Parcelable{
	
	private final String code;
	private final String name;
	private TreeSet<Flight> flights;
	
	private static final Comparator<Flight> FLIGHT_COMPARATOR = new Comparator<Flight>() {
		@Override
		public int compare(Flight a, Flight b) {
			return a.compareTo(b);
		}
	};
	
	public Airline(String code, String name){
		this.code=code;
		this.name=name;
		flights = new TreeSet<Flight>(FLIGHT_COMPARATOR);
	}
	
	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	public Flight[] getFlights() {
		return flights.toArray(new Flight[flights.size()]);
	}
	
	public void addFlight(Flight flight){
		flights.add(flight);
	}
	
	public void addFlights(Flight[] flightsArray){
		for(Flight flight : flightsArray){
			flights.add(flight);
		}
	}
	
	@Override
	public String toString() {
//		return "~~ "+name+"("+code+") ~~";
		return "<"+name+"("+code+")>: "+new Gson().toJson(getFlights(), Flight[].class);
	}

	
	/////////////////////Boilerplate to Parcel Airline\\\\\\\\\\\\\\\\\\\\\\\\
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(code);
		dest.writeString(name);
		dest.writeTypedArray(getFlights(), 0);
		
	}
	
	public static final Parcelable.Creator<Airline> CREATOR = new Parcelable.Creator<Airline>() {
		@Override
		public Airline createFromParcel(Parcel source) {
			return new Airline(source);
		}
		@Override
		public Airline[] newArray(int size) {
			return new Airline[size];
		}
	}; 
	
	private Airline(Parcel in){
		code = in.readString();
		name = in.readString();
		Flight[] flightsArray = in.createTypedArray(Flight.CREATOR);
		flights = new TreeSet<Flight>(FLIGHT_COMPARATOR);
		for(Flight flight : flightsArray) flights.add(flight);
	}
	

	
	
}
