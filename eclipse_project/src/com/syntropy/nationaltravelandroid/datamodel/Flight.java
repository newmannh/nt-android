package com.syntropy.nationaltravelandroid.datamodel;

import org.joda.time.DateTime;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.syntropy.nationaltravelandroid.util.FormattingUtils;

/**
 * Parcelable class that represents a flight
 * @author nathannewman
 *
 */
public class Flight implements Parcelable, Comparable<Flight>{
	
	private int miles;
	private String departureDate;
	private String departureTimeOffset;
	private String arrivalDate;
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
		return Flight.this.airlineCode;
	}
	public String getAirlineName() {
		return Flight.this.airlineName;
	}
	
	public int getMiles() {
		return Flight.this.miles;
	}

	public DateTime getDepartureDate() {
		return new FormattingUtils().parseDateTime(Flight.this.departureDate);
	}

	public String getDepartureTimeOffset() {
		return Flight.this.departureTimeOffset;
	}

	public DateTime getArrivalDate() {
		return new FormattingUtils().parseDateTime(Flight.this.arrivalDate);
	}

	public String getArrivalTimeOffset() {
		return Flight.this.arrivalTimeOffset;
	}

	public String getFlightType() {
		return Flight.this.flightType;
	}

	public int getNumOfLegs() {
		return Flight.this.numOfLegs;
	}

	public FlightLeg[] getFlightLegs() {
		return Flight.this.flightLegs;
	}

	public String getDepartureAirportCode() {
		return Flight.this.departureAirportCode;
	}

	public String getDepartureAirportName() {
		return Flight.this.departureAirportName;
	}

	public String getArrivalAirportCode() {
		return Flight.this.arrivalAirportCode;
	}

	public String getArrivalAirportName() {
		return Flight.this.arrivalAirportName;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this, Flight.class);
	}
	
	
	

	////////////////////Boilerplate for Flight Parceling\\\\\\\\\\\\\\\\\\\\\
	
	private Flight(Parcel in){
		FormattingUtils utils = new FormattingUtils();
		Flight.this.miles = in.readInt();
		Flight.this.departureDate = utils.dateTimeMillisToString(in.readLong());
		Flight.this.departureTimeOffset=in.readString();
		Flight.this.arrivalDate=utils.dateTimeMillisToString(in.readLong());
		Flight.this.arrivalTimeOffset=in.readString();
		Flight.this.flightType=in.readString();
		Flight.this.numOfLegs=in.readInt();
		Flight.this.flightLegs=in.createTypedArray(FlightLeg.CREATOR);
		Flight.this.departureAirportCode=in.readString();
		Flight.this.departureAirportName=in.readString();
		Flight.this.arrivalAirportCode=in.readString();
		Flight.this.arrivalAirportName=in.readString();
		Flight.this.airlineCode=in.readString();
		Flight.this.airlineName=in.readString();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(miles);
		dest.writeLong(getDepartureDate().getMillis());
		dest.writeString(departureTimeOffset);
		dest.writeLong(getArrivalDate().getMillis());
		dest.writeString(arrivalTimeOffset);
		dest.writeString(flightType);
		dest.writeInt(numOfLegs);
		dest.writeTypedArray(flightLegs, 0);
		dest.writeString(departureAirportCode);
		dest.writeString(departureAirportName);
		dest.writeString(arrivalAirportCode);
		dest.writeString(arrivalAirportName);
		dest.writeString(airlineCode);
		dest.writeString(airlineName);
	}
	
	public static final Parcelable.Creator<Flight> CREATOR = new Parcelable.Creator<Flight>() {
		@Override
		public Flight createFromParcel(Parcel source) {
			return new Flight(source);
		}
		
		@Override
		public Flight[] newArray(int size) {
			return new Flight[size];
		}
	};
	
	/**
	 * Inner class representing a flight leg, one or more of which may comprise a flight
	 * This implements Parcelable (as does Flight)
	 * @author nathannewman
	 *
	 */
	public static class FlightLeg implements Parcelable{
		
		private String departureDate;
		private String departureTimeOffset;
		private String arrivalDate;
		private String arrivalTimeOffset;
		private String flightNumber;
		private int sequenceNumber;
		private int miles;
		
		public DateTime getDepartureDate() {
			return new FormattingUtils().parseDateTime(FlightLeg.this.departureDate);
		}
		public String getDepartureTimeOffset() {
			return FlightLeg.this.departureTimeOffset;
		}
		public DateTime getArrivalDate() {
			return new FormattingUtils().parseDateTime(FlightLeg.this.arrivalDate);
		}
		public String getArrivalTimeOffset() {
			return FlightLeg.this.arrivalTimeOffset;
		}
		public String getFlightNumber() {
			return FlightLeg.this.flightNumber;
		}
		public int getSequenceNumber() {
			return FlightLeg.this.sequenceNumber;
		}
		public int getMiles() {
			return FlightLeg.this.miles;
		}
		
		@Override
		public String toString() {
			return new Gson().toJson(FlightLeg.this, FlightLeg.class);
		}
		
		////////////////////Boilerplate for FlightLeg Parceling\\\\\\\\\\\\\\\\\\\\\

		
		@Override
		public int describeContents() {
			return 0;
		}
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeLong(FlightLeg.this.getDepartureDate().getMillis());
			dest.writeString(FlightLeg.this.departureTimeOffset);
			dest.writeLong(FlightLeg.this.getArrivalDate().getMillis());
			dest.writeString(FlightLeg.this.arrivalTimeOffset);
			dest.writeString(FlightLeg.this.flightNumber);
			dest.writeInt(FlightLeg.this.sequenceNumber);
			dest.writeInt(FlightLeg.this.miles);
		}
		
		public static final Parcelable.Creator<FlightLeg> CREATOR = new Parcelable.Creator<Flight.FlightLeg>() {
			@Override
			public FlightLeg createFromParcel(Parcel source) {
				return new FlightLeg(source);
			}
			
			@Override
			public FlightLeg[] newArray(int size) {
				return new FlightLeg[size];
			}
		};
		
		private FlightLeg(Parcel in){
			FormattingUtils utils = new FormattingUtils();
			FlightLeg.this.departureDate=utils.dateTimeMillisToString(in.readLong());
			FlightLeg.this.departureTimeOffset=in.readString();
			FlightLeg.this.arrivalDate=utils.dateTimeMillisToString(in.readLong());
			FlightLeg.this.arrivalTimeOffset=in.readString();
			FlightLeg.this.flightNumber=in.readString();
			FlightLeg.this.sequenceNumber=in.readInt();
			FlightLeg.this.miles=in.readInt();
		}
		
		
		
	}

	
	
	@Override
	public int compareTo(Flight another) {
		int val = getDepartureDate().compareTo(getArrivalDate());
		return val==0? this.toString().compareTo(another.toString()) : val;
	}
	
	@Override
	public boolean equals(Object o) {
		return false;
//		if(!(o instanceof Flight)) return false;
//		return this.toString().equals(o.toString());
	}
	
	
}
