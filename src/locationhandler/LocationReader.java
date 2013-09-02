package locationhandler;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

public class LocationReader {

	private Context context;
	private String provider;
	LocationManager locationManager;
	MyLocation location;
	

	public LocationReader(Context context) {
		this.context = context;
		location = MyLocation.getInstance();
		LocListner locList = new LocListner();
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, locList);
		Location loca = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		updateLocation(loca);
		
	}
	
	public void turnGPSOn()
	{
		String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps")){
            final Intent intent = new Intent();
            intent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
            intent.setData(Uri.parse("3"));
            context.sendBroadcast(intent);
        }
        Toast.makeText(context,
				"GPS turn On successfully",
				Toast.LENGTH_SHORT).show();
	}
	
	public void turnGPSOff()
	{
		  String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	        if(provider.contains("gps")){
	            final Intent intent = new Intent();
	            intent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
	            intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
	            intent.setData(Uri.parse("3"));
	            context.sendBroadcast(intent);
	        }
	        Toast.makeText(context,
					"GPS turn off successfully",
					Toast.LENGTH_SHORT).show();
	}

	public void updateLocation(Location location) {
		String latLongString;

		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			
			location.setLatitude(lat);
			location.setLongitude(lng);
			latLongString = "Lat:" + lat + "\nLong:" + lng;
		} else {
			latLongString = "No Location";
		}

		Toast.makeText(context,
				latLongString,
				Toast.LENGTH_SHORT).show();
	}

	public class LocListner implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {		
			updateLocation(location);
		}

		@Override
		public void onProviderDisabled(String arg0) {
			Toast.makeText(context, "Disabled provider " + provider,
					Toast.LENGTH_SHORT).show();

		}

		@Override
		public void onProviderEnabled(String arg0) {
			Toast.makeText(context, "Enabled new provider " + provider,
					Toast.LENGTH_SHORT).show();

		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub

		}

	}

}
