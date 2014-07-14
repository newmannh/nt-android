package com.syntropy.nationaltravelandroid.datamodel;

import java.util.Date;

import com.google.gson.Gson;

public class Flight {

	/**
	[  
	   {  
	      "miles":2472,
	      "DepartureDate":"2014-07-13T06:00:00Z",
	      "departureTimeOffset":"-0400",
	      "ArrivalDate":"2014-07-13T09:00:00Z",
	      "arrivalTimeOffset":"-0700",
	      "flightType":"NonStop",
	      "numOfLegs":1,
	      "flightLegs":[  
	         {  
	            "DepartureDate":"2014-07-13T06:00:00Z",
	            "departureTimeOffset":"-0400",
	            "ArrivalDate":"2014-07-13T09:00:00Z",
	            "arrivalTimeOffset":"-0700",
	            "flightNumber":"171",
	            "sequenceNumber":1,
	            "miles":2472,
	            "AirlineCode":"AA"
	            //??"airlineName":"Bob"
	         }
	      ],
	      "departureAirportCode":"JFK",
	      "departureAirportName":"New York JFK",
	      "arrivalAirportCode":"LAX",
	      "arrivalAirportName":"Los Angeles"
	   },
	   ...
	]
	*/
	
	private int miles;
	private Date departureDate;
	private String departureTimeOffset;
	private Date arrivalDate;
	private String arrivalTimeOffset;
	private String flightType;
	private int numOfLegs;
	
	private FlightLeg[] flightLegs;
	
	private String departureAirportCode;
	private String departureAirportName;
	private String arrivalAirportCode;
	private String arrivalAirportName;
	
	public static class FlightLeg {
		
		private Date departureDate;
		private String departureTimeOffset;
		private Date arrivalDate;
		private String arrivalTimeOffset;
		private String flightNumber;
		private int sequenceNumber;
		private int miles;
		private String airlineCode;
		private String airlineName;
		
		
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this, Flight.class);
	}
	
	
}
