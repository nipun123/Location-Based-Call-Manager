package locationhandler;

public class MyLocation {

	public double latitude;
	public double longitude;
	public static MyLocation location;

	public static MyLocation getInstance() {
		if (location == null) {
			location = new MyLocation();
		}
		return location;
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
