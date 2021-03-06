package com.syntropy.nationaltravelandroid.activities;

import org.joda.time.DateTime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.syntropy.nationaltravelandroid.util.FormattingUtils;

public class AirlineOptionActivity extends Activity {

	private ListView listView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_airline_option);
		
		Airline[] airlines = ServerRequestManager.NO_TALKING_TO_SERVER? 
				 FlightManager.getFlightManager().getStaticAirlineData(this) : 
					 FlightManager.getFlightManager().getAirlines(false);
		
		
		listView = (ListView)findViewById(R.id.airlinesListView);
		final AirlineArrayAdapter adapter = 
				new AirlineArrayAdapter(this, airlines);
		listView.setAdapter(adapter);
		
		final Context context = this;
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				view.setSelected(true);
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
		
		static class AirlineViewHolder {
			TextView flightNumView, airlineNameView, timeView, timePeriodView;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			AirlineViewHolder holder;
			
			if(view==null){
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.airline_row, parent,false);
				holder = new AirlineViewHolder();
				holder.flightNumView = (TextView)view.findViewById(R.id.flightNum);
				holder.airlineNameView = (TextView)view.findViewById(R.id.airlineName);
				holder.timeView = (TextView)view.findViewById(R.id.time);
				holder.timePeriodView = (TextView)view.findViewById(R.id.timePeriod);
				view.setTag(holder);
			} else {
				holder = (AirlineViewHolder) view.getTag();
			}
			
			Airline airline = (Airline) getItem(position);
			if(airline!=null){
				FormattingUtils utils = new FormattingUtils();

				holder.flightNumView.setText(airline.getFlights().length+"");
				holder.airlineNameView.setText(" "+airline.getName());
				DateTime dateTime = airline.getFlights()[0].getDepartureDate();
				holder.timeView.setText(utils.getHrsMinsString(dateTime));
				holder.timePeriodView.setText(FormattingUtils.getPeriodString(dateTime));

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
