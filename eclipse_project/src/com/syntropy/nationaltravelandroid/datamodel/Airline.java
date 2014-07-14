package com.syntropy.nationaltravelandroid.datamodel;

import java.util.Comparator;
import java.util.TreeSet;

public class Airline {
	
	private final String code;

	private final String name;
	private TreeSet<Flight> flights;
//	private List<Flight> flights;
	
	public Airline(String code, String name){
		this.code=code;
		this.name=name;
		flights = new TreeSet<Flight>(new Comparator<Flight>() {
			@Override
			public int compare(Flight a, Flight b) {
				// TODO Auto-generated method stub
				return a.getDepartureDate().compareTo(b.getDepartureDate());
			}
		});
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
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "This the motherfuckin airline "+name+" ("+code+"). Don't fuck with this shit.";
	}
	
	
}
