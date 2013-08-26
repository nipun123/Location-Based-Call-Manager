package com.example.locationbasedcallmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AutoStart extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent inte) {
		if ("android.intent.action.BOOT_COMPLETED".equals(inte.getAction()))
		{
			Intent intent = new Intent(context,SupportService.class);
			context.startService(intent);
			
			Log.v("","When phone boots this was called");
		}
		
	}

}
