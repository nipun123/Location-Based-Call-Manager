package locationhandler;

public class MyLocation {

	public double latitude=10.00;
	public double longitude=10.00;
	public static MyLocation location;

	public static MyLocation getInstance() {
		if(location==null)
		{
			location=new MyLocation();
			return location;
		}
		else
		    return location;
	}
	
	public MyLocation()
	{
		location=this;
	}

	public void setLatitude(double lati) {
		this.latitude = lati;
	}

	public void setLongitude(double longi) {
		this.longitude = longi;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

}
