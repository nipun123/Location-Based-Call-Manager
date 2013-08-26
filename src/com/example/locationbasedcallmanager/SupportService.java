package com.example.locationbasedcallmanager;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class SupportService extends Service{
	
	private Timer timer=new Timer();
	private int counter;

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v("","Running comes here to stop");
		Toast.makeText(this,"service destroyed", Toast.LENGTH_LONG).show();
		if(timer!=null)
		{
			timer.cancel();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v("","Running comes here to start");
		Toast.makeText(this,"service started", Toast.LENGTH_LONG).show();
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.v("","Running comes here to start");
		timer.scheduleAtFixedRate(new TimerTask(){ public void run() {onTimerTick();}}, 0, 1000L);
		counter=0;
		
	}
	
	public void onTimerTick()
	{
		Log.v("","Timer tick at time"+counter);
		counter++;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
