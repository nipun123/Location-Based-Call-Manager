package smshandler;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class SmsHandlingService extends Service {
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v("","Running comes here to stop");
		Toast.makeText(this,"sms service destroyed", Toast.LENGTH_LONG).show();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v("","Running comes here to start");
		Toast.makeText(this,"sms service started", Toast.LENGTH_LONG).show();
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.v("","Running comes here to start");
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
