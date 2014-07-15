package com.syntropy.nationaltravelandroid.datamodel;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

/**
 * Parcelable class that represents a flight
 * @author nathannewman
 *
 */
public class Flight implements Parcelable{

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

	////////////////////Boilerplate for Flight Parceling\\\\\\\\\\\\\\\\\\\\\
	
	private Flight(Parcel in){
		this.miles = in.readInt();
		this.departureDate = new Date(in.readLong());
		this.departureTimeOffset=in.readString();
		this.arrivalDate=new Date(in.readLong());
		this.arrivalTimeOffset=in.readString();
		this.flightType=in.readString();
		this.numOfLegs=in.readInt();
		this.flightLegs=in.createTypedArray(FlightLeg.CREATOR);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(miles);
		dest.writeLong(departureDate.getTime());
		dest.writeString(departureTimeOffset);
		dest.writeLong(arrivalDate.getTime());
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
		
		////////////////////Boilerplate for FlightLeg Parceling\\\\\\\\\\\\\\\\\\\\\

		
		@Override
		public int describeContents() {
			return 0;
		}
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeLong(departureDate.getTime());
			dest.writeString(departureTimeOffset);
			dest.writeLong(arrivalDate.getTime());
			dest.writeString(arrivalTimeOffset);
			dest.writeString(flightNumber);
			dest.writeInt(sequenceNumber);
			dest.writeInt(miles);
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
			this.departureDate=new Date(in.readLong());
			this.departureTimeOffset=in.readString();
			this.arrivalDate=new Date(in.readLong());
			this.arrivalTimeOffset=in.readString();
			this.flightNumber=in.readString();
			this.sequenceNumber=in.readInt();
			this.miles=in.readInt();
		}
		
		
		
	}
	
	
	
}
