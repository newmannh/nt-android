package com.syntropy.nationaltravelandroid.activities;

import org.joda.time.DateTime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nationaltravelandroid.R;
import com.syntropy.nationaltravelandroid.datamodel.Airline;
import com.syntropy.nationaltravelandroid.datamodel.FlightManager;
import com.syntropy.nationaltravelandroid.datamodel.ServerRequestManager;

public class AirlineOptionActivity extends Activity {

	private ListView listView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_airline_option);
		
		Airline[] airlines = ServerRequestManager.NO_TALKING_TO_SERVER? 
				 FlightManager.getFlightManager().getStaticAirlineData(this) : 
					 FlightManager.getFlightManager().getAirlines(false);
		
		
		listView = (ListView)findViewById(R.id.listView);
		final AirlineArrayAdapter adapter = 
				new AirlineArrayAdapter(this, airlines);
		listView.setAdapter(adapter);
		
		final Context context = this;
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(context,FlightOptionActivity.class);
				intent.putExtra("AIRLINE", adapter.getItem(position));
				startActivity(intent);
			}
		});
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
				airlineNameView.setText(" "+airline.getName());
				DateTime dateTime = airline.getFlights()[0].getDepartureDate();
				timeView.setText(dateTime.getHourOfDay()%12+":"+dateTime.getMinuteOfHour());
				timePeriodView.setText(dateTime.getHourOfDay()>12? "PM" : "AM");

			}
			
			return view;
		}


		@Override
		public int getCount() {
			return airlines.length;
		}

		@Override
		public Airline getItem(int position) {
			return airlines[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
	}
	
}
