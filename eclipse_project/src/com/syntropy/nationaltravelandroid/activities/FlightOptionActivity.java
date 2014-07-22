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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
	
	FlightListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flight_option);
		
		airline = getIntent().getParcelableExtra("AIRLINE");
		final Context c = this;
		
		ListView flightListView = (ListView) findViewById(R.id.flightOptionListView);
		adapter = new FlightListAdapter(this, airline.getFlights());
		flightListView.setAdapter(adapter);
		flightListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				view.setSelected(true);
				adapter.selectFlight(position);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.flight_option, menu);
		return true;
	}
	
	public void bookFlight(View view){
		Flight flight = adapter.getSelectedFlight();
		String flightString = flight==null? "null" : flight.toString();
		Toast.makeText(this, "Booking flight "+flightString, Toast.LENGTH_LONG).show();
	}
	
	
	
	private static class FlightListAdapter extends BaseAdapter{

		private Context context;
		private Flight[] flights;
		
		private Flight selectedFlight = null;
		
		public FlightListAdapter(Context context, Flight[] flights) {
			this.context = context;
			this.flights = flights;
		}
		
		public void selectFlight(int pos){
			this.selectedFlight = flights[pos];
		}
		
		public Flight getSelectedFlight(){
			return selectedFlight;
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			FlightViewHolder holder;
			
			if(view==null){
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.flight_row, parent,false);
				holder = new FlightViewHolder();
//				holder.hlv = (HorizontalListView)view.findViewById(R.id.flightLegsList);
				holder.tv = (TextView)view.findViewById(R.id.flightDetails);
				view.setTag(holder);
			} else {
				holder = (FlightViewHolder) view.getTag();
			}
			
			Flight flight = (Flight) getItem(position);
			if(flight!=null){
				
//				holder.hlv.setAdapter(new FlightLegListAdapter(context, flight.getFlightLegs()));
				
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
				holder.tv.setText(flightDetailsString);
				
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
		
		static class FlightViewHolder{
//			HorizontalListView hlv;
			TextView tv;
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

			static class FlightLegViewHolder{
				TextView startTime, startPeriod, startLoc, endTime, endPeriod, endLoc;
			}
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = convertView;
				FlightLegViewHolder holder;
				
				
				if(view==null){
					LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					view = inflater.inflate(R.layout.flight_leg_list_item, parent,false);
					holder = new FlightLegViewHolder();
					holder.startTime = (TextView) view.findViewById(R.id.startTime);
					holder.startPeriod = (TextView) view.findViewById(R.id.startTimePeriod);
					holder.startLoc = (TextView) view.findViewById(R.id.startBottom);
					holder.endTime = (TextView) view.findViewById(R.id.destTime);
					holder.endPeriod =  (TextView) view.findViewById(R.id.destTimePeriod);
					holder.endLoc = (TextView) view.findViewById(R.id.destBottom);
					view.setTag(holder);
				} else {
					holder = (FlightLegViewHolder) view.getTag();
				}
				
				FlightLeg flightLeg = getItem(position);
				if(flightLeg!=null){
					FormattingUtils utils = new FormattingUtils();
					
					DateTime dateTimeStart = flightLeg.getDepartureDate();
					holder.startTime.setText(utils.getHrsMinsString(dateTimeStart));
					holder.startPeriod.setText(FormattingUtils.getPeriodString(dateTimeStart));
					holder.startLoc.setText("Some Origin"); //TODO
					
					DateTime dateTimeEnd = flightLeg.getArrivalDate();
					holder.endTime.setText(utils.getHrsMinsString(dateTimeEnd));
					holder.endPeriod.setText(FormattingUtils.getPeriodString(dateTimeEnd));
					holder.endLoc.setText("Some Destination"); //TODO

					
				}
				
				
				return view;
			}
			
		}
		
	}
	

}
