package com.syntropy.nationaltravelandroid.activities;

import org.joda.time.DateTime;
import org.joda.time.Duration;

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
import com.google.gson.Gson;
import com.syntropy.nationaltravelandroid.datamodel.Airline;
import com.syntropy.nationaltravelandroid.datamodel.Flight;
import com.syntropy.nationaltravelandroid.datamodel.Flight.FlightLeg;
import com.syntropy.nationaltravelandroid.exception.NTException;
import com.syntropy.nationaltravelandroid.util.FormattingUtils;

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
			if(flight!=null){
				
				HorizontalListView flightLegsListView = (HorizontalListView)view.findViewById(R.id.flightLegsList);
				flightLegsListView.setAdapter(new FlightLegListAdapter(context, flight.getFlightLegs()));
				
				TextView details = (TextView)view.findViewById(R.id.flightDetails);
				
				Duration duration = new Duration(flight.getDepartureDate(),flight.getArrivalDate());
				String durationString = duration.getStandardHours()+"hr "+duration.getStandardMinutes()%60+" min";
				int hourTimeDifference = 0;
				try {
					hourTimeDifference = (Integer.parseInt(flight.getDepartureTimeOffset())-Integer.parseInt(flight.getArrivalTimeOffset()))%100;
				} catch (NumberFormatException e) {
					hourTimeDifference = 0;
					new NTException(e).withLog("FlightOptionActivity", e.getMessage());
					Log.e("FlightOptionActivity", "The offending flight is "+new Gson().toJson(flight,Flight.class));
				}
				String timeDifferential = (hourTimeDifference<0? -hourTimeDifference : hourTimeDifference) + " hour time differential";
				
				
				String flightDetailsArray[] = {
						flight.getNumOfLegs()+" stop",
						durationString,
						timeDifferential,
						"Hindenburg-Class Airship",
						"seats left?",
						"gate?"
						
						};
				String flightDetailsString = "";
				for(String flightDetail : flightDetailsArray){
					flightDetailsString += flightDetailsString.length()>0? " \u00B7 "+flightDetail : flightDetail;
				}
				details.setText(flightDetailsString);
				
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
				if(flightLeg!=null){
					FormattingUtils utils = new FormattingUtils();
					
					TextView startTime = (TextView) view.findViewById(R.id.startTime);
					TextView startPeriod =  (TextView) view.findViewById(R.id.startTimePeriod);
					TextView startLoc = (TextView) view.findViewById(R.id.startBottom);
					
					TextView endTime = (TextView) view.findViewById(R.id.destTime);
					TextView endPeriod =  (TextView) view.findViewById(R.id.destTimePeriod);
					TextView endLoc = (TextView) view.findViewById(R.id.destBottom);
					
					DateTime dateTimeStart = flightLeg.getDepartureDate();
					startTime.setText(utils.getHrsMinsString(dateTimeStart));
					startPeriod.setText(FormattingUtils.getPeriodString(dateTimeStart));
					startLoc.setText("Some Origin"); //TODO
					
					DateTime dateTimeEnd = flightLeg.getArrivalDate();
					endTime.setText(utils.getHrsMinsString(dateTimeEnd));
					endPeriod.setText(FormattingUtils.getPeriodString(dateTimeEnd));
					endLoc.setText("Some Destination"); //TODO

					
				}
				
				
				return view;
			}
			
		}
		
	}
	

}
