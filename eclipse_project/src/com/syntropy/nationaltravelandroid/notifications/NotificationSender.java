package com.syntropy.nationaltravelandroid.notifications;

import java.util.concurrent.atomic.AtomicLong;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.syntropy.nationaltravelandroid.activities.RescheduleOptionListActivity;

public class NotificationSender {

	private static AtomicLong lastId = new AtomicLong(System.currentTimeMillis());
	
	NotificationCompat.Builder notificationBuilder = null;
	private Context context = null;
	
	@SuppressLint("NewApi") 
	public NotificationSender(Context context){
		this.context = context;
		notificationBuilder = new NotificationCompat.Builder(context)
									.setSmallIcon(android.R.drawable.ic_menu_manage)
									.setContentTitle("National Travel Alert")
									.setContentText("Your flight has been cancelled. Click to view options.");
		
		
		Intent resultIntent = new Intent(context, RescheduleOptionListActivity.class); // create explicit intent for activity in app
		
		PendingIntent resultPendingIntent;
		
		//Call requires API >= 16
	    if (Build.VERSION.SDK_INT >= 16) {
	    	TaskStackBuilder stackBuilder = TaskStackBuilder.create(context); // stack builder object contains an artificial back stack for the started activity
	    	stackBuilder.addParentStack(RescheduleOptionListActivity.class); //adds the back stack for the intent (but not the intent itself)
	    	stackBuilder.addNextIntent(resultIntent);
	    	resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
	    } else {
	    	resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	    	Log.w("NotificationSender", "Warning: this api doesn't support back stack for activities started from notifications");
	    }
		notificationBuilder.setContentIntent(resultPendingIntent);
	}
	
	
	/**
	 * Creates a notification (and id for the notification), sends it, and returns the notification's id 
	 * @return
	 */
	public int createNotification(){
		int id = (int) lastId.incrementAndGet();
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(id,notificationBuilder.build());
		return id;
	}
	
}
