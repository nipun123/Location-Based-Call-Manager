package locationhandler;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

public class LocationListner extends Activity implements LocationListener {
	
	MyLocation location;

	@Override
	public void onLocationChanged(Location loca) {
		location=new MyLocation();
		location.setLatitude(loca.getLatitude());
		location.setLongitude(loca.getLongitude());
		String text="My Current location test: "+"Longitude = "+location.getLatitude()+" Longitude = "+location.getLongitude();
		System.out.println(text);
	}

	@Override
	public void onProviderDisabled(String arg0) {
		Toast.makeText(getApplicationContext(),"GPS Disabled",Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		Toast.makeText(getApplicationContext(),"GPS Enabled",Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) 
	{
		
	}

}
