package com.syntropy.nationaltravelandroid.activities;

import org.joda.time.DateTime;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.devsmart.android.ui.HorizontalListView;
import com.example.nationaltravelandroid.R;
import com.syntropy.nationaltravelandroid.datamodel.Airline;
import com.syntropy.nationaltravelandroid.datamodel.Flight;
import com.syntropy.nationaltravelandroid.datamodel.Flight.FlightLeg;

public class FlightOptionActivity extends Activity {

	private Airline airline = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flight_option);
		
		airline = getIntent().getParcelableExtra("AIRLINE");
		
		ListView flightListView = (ListView) findViewById(R.id.flightOptionListView);
		flightListView.setAdapter(new FlightListAdapter(this, airline.getFlights()));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.flight_option, menu);
		return true;
	}
	
	
	private static class FlightListAdapter extends BaseAdapter{

		private Context context;
		private Flight[] flights;
		
		public FlightListAdapter(Context context, Flight[] flights) {
			this.context = context;
			this.flights = flights;
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if(view==null){
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.flight_row, parent,false);
			}
			
			Flight flight = (Flight) getItem(position);
			Log.w("RescheduleOptionListActivity", flight.toString());
			if(flight!=null){
				
				HorizontalListView flightLegsListView = (HorizontalListView)view.findViewById(R.id.flightLegsList);
				flightLegsListView.setAdapter(new FlightLegListAdapter(context, flight.getFlightLegs()));
				
				TextView details = (TextView)view.findViewById(R.id.flightDetails);
				details.setText("All . Of . The . Details . Go . Here . In . This . Obnoxious . Format . Blahb lah blah blah");
				
			}
			
			return view;
		}


		@Override
		public int getCount() {
			return flights.length;
		}

		@Override
		public Flight getItem(int position) {
			return flights[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	
		
		private static class FlightLegListAdapter extends BaseAdapter {
			
			private Context context = null;
			private FlightLeg[] flightLegs = null;
			
			public FlightLegListAdapter(Context context, FlightLeg[] flightLegs) {
				this.context = context;
				this.flightLegs = flightLegs;
			}

			@Override
			public int getCount() {
				return flightLegs.length;
			}

			@Override
			public FlightLeg getItem(int position) {
				return flightLegs[position];
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = convertView;
				if(view==null){
					LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					view = inflater.inflate(R.layout.flight_leg_list_item, parent,false);
				}
				
				FlightLeg flightLeg = getItem(position);
				Log.w("RescheduleOptionListActivity", flightLeg.toString());
				if(flightLeg!=null){
					TextView startTime = (TextView) view.findViewById(R.id.startTime);
					TextView startPeriod =  (TextView) view.findViewById(R.id.startTimePeriod);
					TextView startLoc = (TextView) view.findViewById(R.id.startBottom);
					
					TextView endTime = (TextView) view.findViewById(R.id.destTime);
					TextView endPeriod =  (TextView) view.findViewById(R.id.destTimePeriod);
					TextView endLoc = (TextView) view.findViewById(R.id.destBottom);
					
					DateTime dateTimeStart = flightLeg.getDepartureDate();
					startTime.setText(dateTimeStart.getHourOfDay()%12+":"+dateTimeStart.getMinuteOfHour());
					startPeriod.setText(dateTimeStart.getHourOfDay()>12? "PM" : "AM");
					startLoc.setText("TODO TODOTODO");
					
					DateTime dateTimeEnd = flightLeg.getArrivalDate();
					endTime.setText(dateTimeEnd.getHourOfDay()%12+":"+dateTimeEnd.getMinuteOfHour());
					endPeriod.setText(dateTimeEnd.getHourOfDay()>12? "PM" : "AM");
					endLoc.setText("TODO I don't know");

					
				}
				
				
				return view;
			}
			
		}
		
	}
	

}
