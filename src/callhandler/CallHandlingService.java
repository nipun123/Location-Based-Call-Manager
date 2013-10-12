package callhandler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import com.android.internal.telephony.ITelephony;

import volumehandler.VolumeController;

import databasehandler.DataBaseHelper;
import locationhandler.LocationReader;
import locationhandler.MyLocation;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class CallHandlingService extends Service {

	private MyLocation location;
	private DataBaseHelper helper;
	private double latitude;
	private double longitude;
	private String profileName;
	private String incomingNumber;
	private HashMap<String, String> map;
	private LocationReader reader;
	private VolumeController controller;
	private AudioManager audioManager;

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v("", "Running comes here to stop");
		Toast.makeText(this, "call service destroyed", Toast.LENGTH_LONG)
				.show();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v("", "Running comes here to start");
		Bundle extras = intent.getExtras();
		incomingNumber = extras.getString("incomingNumber");                      // get incomingnumber from bundle
		handleCall();                                                             // call handlecall method
		Toast.makeText(this, "call service started", Toast.LENGTH_LONG).show();
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.helper = DataBaseHelper.getInstance(getApplicationContext());
		this.reader = LocationReader.getInstance(getApplicationContext());
		this.controller = VolumeController.getInstance(getApplicationContext());       // create a volume controller
		audioManager = (AudioManager) getBaseContext().getSystemService(
				Context.AUDIO_SERVICE);                                                // instance
		this.location = reader.getLocation();
		Log.v("", "Latitude= " + location.getLatitude() + " longioitude= "+ location.getLongitude());
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public void handleCall() {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		int volumeLevel;

		profileName = helper.getProfileNameFromcordinates(latitude, longitude);      // get profilename from cordinates
		if (!profileName.equals("")) {

			map = helper.getProfileInfo(profileName);
			volumeLevel = Integer.parseInt(map.get("Volume"));
			controller.setVolume(volumeLevel);

			if (!helper.checkContactAllowed(incomingNumber, profileName)) {

				blockCall();                                                        // block the call
				if (!map.get("AutoMessage").equals("")) {                           // check autosend
															                        // message set
					sendAutoMessage();                                              // send auto message
					if (!map.get("ForwardingNumber").equals("")) {                  // check forwarding number set
						//callForwarding();
					}
				}
			}
		}

		onDestroy();

	}

	public void sendAutoMessage() {
		Log.v("", "Call sendmessage method");
		try {

			SmsManager sms = SmsManager.getDefault();
			ArrayList<String> smsString = sms.divideMessage(map.get("AutoMessage"));
			sms.sendMultipartTextMessage(incomingNumber, null, smsString, null,null);
			
		} catch (Exception e) {
			Log.v("", "Error occurred= " + e);
		}
		Log.v("", "Call sendmessage method");
	}

	public void blockCall() {
		TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

		if (tm.getCallState() == TelephonyManager.CALL_STATE_RINGING) {
			try {
				Class c = Class.forName(tm.getClass().getName());
				Method m = c.getDeclaredMethod("getITelephony");
				m.setAccessible(true);
				com.android.internal.telephony.ITelephony telephonyService = (ITelephony) m.invoke(tm);
				audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
				telephonyService.endCall();
				
			} catch (Exception e) {
				Log.v("", "Error occured = " + e);
			}
		} else {
			audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		}
	}

	public void callForwarding() {

		// #21# to off the call forwarding
		try {
			String number = "*21*" + map.get("ForwardingNumber") + "#";
			Intent intentCallForward = new Intent(Intent.ACTION_CALL);
			intentCallForward.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Uri mmiCode = Uri.fromParts("tel", number, "#");
			intentCallForward.setData(mmiCode);
			startActivity(intentCallForward);
		} catch (Exception e) {
			Log.v("", "error occured in call forwarding " + e);
		}

	}

}
