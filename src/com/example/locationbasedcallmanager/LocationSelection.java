package com.example.locationbasedcallmanager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class LocationSelection extends Activity {

	private GoogleMap googleMap;
	private double latitude = 0;
	private double longitude = 0;
	private MarkerOptions marker;
	private Button backButton;
	private Button okButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_selection);

		backButton = (Button) findViewById(R.id.back);
		okButton = (Button) findViewById(R.id.ok);

		try {
			initializeMap();
		} catch (Exception e) {
			Log.v("error occured ", "" + e);
		}
		
		backButtonAction();
		okButtonaction();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location_selection, menu);
		return true;
	}

	private void initializeMap() {

		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());
		if (status != ConnectionResult.SUCCESS) { // Google Play Services are
													// not available

			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
					requestCode);
			dialog.show();
		}

		else if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			marker = new MarkerOptions().position(
					new LatLng(latitude, longitude)).title("Hello Maps ");
			googleMap.addMarker(marker);
			googleMap.setMyLocationEnabled(true);

			googleMap.getUiSettings().setCompassEnabled(true);
			googleMap.getUiSettings().setZoomControlsEnabled(true);
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);

			googleMap.setOnMapClickListener(new OnMapClickListener() {

				@Override
				public void onMapClick(LatLng latlang) {

					latitude = latlang.latitude;
					longitude = latlang.longitude;
					googleMap.clear();
					marker = new MarkerOptions()
							.position(latlang)
							.title("Selected Location")
							.snippet(
									"latitude= " + latitude + " longitude="
											+ longitude);
					googleMap.addMarker(marker).showInfoWindow();

				}
			});

			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initializeMap();
	}

	public void backButtonAction() {
		
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);

			}
		});
	}

	public void okButtonaction() {
		
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getApplicationContext(), AddLocation.class);
				intent.putExtra("lat",Double.toString(latitude));
				intent.putExtra("lng",Double.toString(longitude));
				startActivity(intent);
			}
		});
	}

}
