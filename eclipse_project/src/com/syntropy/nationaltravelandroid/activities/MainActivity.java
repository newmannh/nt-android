package com.syntropy.nationaltravelandroid.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.nationaltravelandroid.R;
import com.syntropy.nationaltravelandroid.datamodel.FlightManager;
import com.syntropy.nationaltravelandroid.notifications.NotificationSender;
import com.syntropy.nationaltravelandroid.services.GCMHelper;

public class MainActivity extends Activity {

	private Button timerButton = null;
	private boolean off = true;
	
	private NotificationSender notificationSender = null;
	
	//runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = null; 
    Runnable timerRunnable = null; 
	
    private long lastStart = 0;
    
    int numSent=0;
    boolean sent = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        new GCMHelper(this).registerForGCM();
        
        FlightManager.getFlightManager();
        
        notificationSender = new NotificationSender(this);
        
        timerButton = (Button) findViewById(R.id.timerButton);
        timerHandler = new Handler();
        timerRunnable = new Runnable() {

            @Override
            public void run() {
                long millis = System.currentTimeMillis() - lastStart;
                int seconds = (int) (millis / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;

                timerButton.setText(String.format("%d:%02d", minutes, seconds));
                
                if(seconds%15==0) {
                	if(sent){
                		timerButton.setTextSize(90);
                		sent = false;
                	} else {
                		notificationSender.createNotification();
                		timerButton.setTextSize(45);
                		timerButton.setText("Notification "+(++numSent)+" sent, fool!");
                		sent = true;
                	}
                }
                
                timerHandler.postDelayed(this, 500);
            }
        };
        FlightManager.getFlightManager().getFlights(false);
    }
    
    public void toggleTimer(View view){
    	if(off){
    		timerButton.setTextColor(getResources().getColor(R.color.Green));
    		lastStart = System.currentTimeMillis();
    		timerHandler.postDelayed(timerRunnable, 0);
    	} else {
    		timerHandler.removeCallbacks(timerRunnable);
    		timerButton.setTextColor(getResources().getColor(R.color.Gray));
    	}
    	off = !off;
    }
}
