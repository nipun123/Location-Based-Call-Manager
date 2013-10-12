package locationhandler;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class LocationReader {

	private Context context;
	private String provider;
	LocationManager locationManager;
	public MyLocation location;
	public static LocationReader reader;
	
	public static LocationReader getInstance(Context context)                        // get a instance of locationreader
	{
		if(reader==null)
		{
			reader=new LocationReader(context);
		}
		return reader;
	}

	public LocationReader(Context context) {
		this.context = context; 
		this.location= new MyLocation();
		LocListner locList = new LocListner();
		locationManager = (LocationManager) context                                 // get the location manager
				.getSystemService(Context.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,     // request location updates on loclist locationlistner
				0, locList);
		Location loca = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		updateLocation(loca);
	}
	
	
	public void turnGPSOn()                                                          // turn on GPS
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
	
	public void turnGPSOff()                                                            // turn off GPS
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

	public void updateLocation(Location loca) {                                          // update Mylocation object when new locationupdates come
		String latLongString;

		if (loca != null) {
			double lat = loca.getLatitude();
			double lng = loca.getLongitude();
			
			location.setLatitude(lat);
			location.setLongitude(lng);
			latLongString = "Lat:" + lat + "\nLong:" + lng;
			Log.v("","updated location lati="+location.getLatitude()+" long= "+location.getLongitude());
		} else {
			latLongString = "No Location";
		}

		Toast.makeText(context,
				latLongString,
				Toast.LENGTH_SHORT).show();
	}
	
	public MyLocation getLocation()                                                     // get the MyLocation object 
	{
		Location loca = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (loca != null) {
			double lat = loca.getLatitude();
			double lng = loca.getLongitude();
			
			location.setLatitude(lat);
			location.setLongitude(lng);
			Log.v("","latitude= "+lat+" longitude= "+lng);
		}
		return location;
	}

	private class LocListner implements LocationListener {                             // create private class from locationListner                 

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
