package smshandler;

import locationhandler.LocationReader;
import locationhandler.MyLocation;
import databasehandler.DataBaseHelper;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class SmsHandlingService extends Service {

	private String incomingNumber;
	private String messageBody;
	private MailClient mailer;
	private double latitude, longitude;
	private String profileName;
	private MyLocation location;
	private DataBaseHelper helper;
	private LocationReader reader;

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "sms service destroyed", Toast.LENGTH_LONG).show();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			Bundle extras = intent.getExtras(); // get the bundle from intent
			if (extras != null) {
				this.incomingNumber = extras.getString("incomingNumber"); // get
																			// the
																			// number
																			// and
																			// message
																			// from
																			// the
																			// bundle
				this.messageBody = extras.getString("message");
				handleMessage(); // call handle message method
			} else
				Log.v("", "Bundle is null");

		} catch (Exception e) {
			Log.v("", "Error occured at geting bundle");
		}
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.helper = DataBaseHelper.getInstance(getApplicationContext()); // get
																			// DatabaseHandler
																			// instance
		this.reader = LocationReader.getInstance(getApplicationContext()); // get
																			// location
																			// reader
																			// instance
		this.location = reader.getLocation(); // get the location from
												// locationreader
		Log.v("", "Latitude= " + location.getLatitude() + " longioitude= "
				+ location.getLongitude());
		mailer = new MailClient(); // create a new mailClient
		Toast.makeText(this, "sms service started", Toast.LENGTH_LONG).show();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void handleMessage() {
		latitude = location.getLatitude();
		longitude = location.getLongitude();

		profileName = helper.getProfileNameFromcordinates(latitude, longitude); // get
																				// the
																				// profilename
																				// from
																				// location
		if (!profileName.equals("")) {
			Log.v("", "Correctly read the profile");

			if (!helper.checkContactAllowed(incomingNumber, profileName)) { // check
																			// the
																			// contact
																			// is
																			// allowed
																			// at
																			// the
																			// location
				try {
					Log.v("", "Correctly check the auto message");
					mailer.SendMail(incomingNumber, messageBody); // send the
																	// mail
				} catch (Exception e) {
					Toast.makeText(this, "sms sending to mail failed",
							Toast.LENGTH_LONG).show();

				}
			} else {
				Intent in = new Intent("SmsMessage.intent.MAIN").putExtra( // broadcast
																			// the
																			// message
																			// if
																			// the
																			// number
																			// allowed
																			// at
																			// the
																			// location
						"get_msg", incomingNumber + ":" + messageBody);
				in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				this.sendBroadcast(in);
			}
		}
		onDestroy(); // destroy the service at completion
	}

}
