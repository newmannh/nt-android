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
	
	private String airlineCode;
	private String airlineName;
	
	public String getAirlineCode() {
		return airlineCode;
	}
	public String getAirlineName() {
		return airlineName;
	}
	
	public int getMiles() {
		return miles;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public String getDepartureTimeOffset() {
		return departureTimeOffset;
	}

	public Date getArrivalDate() {
		return arrivalDate;
	}

	public String getArrivalTimeOffset() {
		return arrivalTimeOffset;
	}

	public String getFlightType() {
		return flightType;
	}

	public int getNumOfLegs() {
		return numOfLegs;
	}

	public FlightLeg[] getFlightLegs() {
		return flightLegs;
	}

	public String getDepartureAirportCode() {
		return departureAirportCode;
	}

	public String getDepartureAirportName() {
		return departureAirportName;
	}

	public String getArrivalAirportCode() {
		return arrivalAirportCode;
	}

	public String getArrivalAirportName() {
		return arrivalAirportName;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this, Flight.class);
	}
	
	public static class FlightLeg {
		
		private Date departureDate;
		private String departureTimeOffset;
		private Date arrivalDate;
		private String arrivalTimeOffset;
		private String flightNumber;
		private int sequenceNumber;
		private int miles;
		public Date getDepartureDate() {
			return departureDate;
		}
		public String getDepartureTimeOffset() {
			return departureTimeOffset;
		}
		public Date getArrivalDate() {
			return arrivalDate;
		}
		public String getArrivalTimeOffset() {
			return arrivalTimeOffset;
		}
		public String getFlightNumber() {
			return flightNumber;
		}
		public int getSequenceNumber() {
			return sequenceNumber;
		}
		public int getMiles() {
			return miles;
		}
		
		
		
	}
	
}
