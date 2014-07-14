package com.syntropy.nationaltravelandroid.activities;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nationaltravelandroid.R;
import com.syntropy.nationaltravelandroid.datamodel.Airline;
import com.syntropy.nationaltravelandroid.datamodel.FlightManager;

public class RescheduleOptionListActivity extends Activity {

	private ListView listView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reschedule_option_list);
		
		listView = (ListView)findViewById(R.id.listView);
		AirlineArrayAdapter adapter = 
				new AirlineArrayAdapter(this, 
						FlightManager.getFlightManager().getAirlines(false));
		listView.setAdapter(adapter);
	}
	
	private static class AirlineArrayAdapter extends BaseAdapter{

		private Context context;
		private Airline[] airlines;
		
		public AirlineArrayAdapter(Context context, Airline[] airlines) {
			this.context = context;
			this.airlines = airlines;
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if(view==null){
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.airline_row, parent,false);
			}
			
			Airline airline = (Airline) getItem(position);
			Log.w("RescheduleOptionListActivity", airline.toString());
			if(airline!=null){
				TextView flightNumView = (TextView)view.findViewById(R.id.flightNum);
				TextView airlineNameView = (TextView)view.findViewById(R.id.airlineName);
				TextView timeView = (TextView)view.findViewById(R.id.time);
				TextView timePeriodView = (TextView)view.findViewById(R.id.timePeriod);

				flightNumView.setText(airline.getFlights().length+"");
				airlineNameView.setText(airline.getName()+"");
				Date date = airline.getFlights()[0].getDepartureDate();
				Calendar calendar = GregorianCalendar.getInstance();
				calendar.setTime(date);
				timeView.setText(calendar.get(Calendar.HOUR)+":00");//TODO
				timePeriodView.setText(calendar.get(Calendar.HOUR_OF_DAY)>12? "PM" : "AM");

			}
			
			return view;
		}


		@Override
		public int getCount() {
			return airlines.length;
		}

		@Override
		public Object getItem(int position) {
			return airlines[0];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
	}
	
}
