package com.example.locationbasedcallmanager;

import locationhandler.LocationReader;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import databasehandler.DataBaseHelper;

public class MainActivity extends Activity {

	private Button addContact;
	private Button addLocation;
	private Button editLocation;
	private EditText contactNumber;
	private EditText contactName;
	private EditText allowedLocation;
	private DataBaseHelper helper;
	private LocationReader reader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("Execution comes here");
		setContentView(R.layout.activity_main);

		addContact = (Button) findViewById(R.id.add_contact);
		addLocation = (Button) findViewById(R.id.location);
		editLocation = (Button) findViewById(R.id.editlocation);

		helper = new DataBaseHelper(getApplicationContext());
		reader=new LocationReader(getApplicationContext());
		

		addContact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//addContactAction();
				reader.turnGPSOn();
				
			}
		});

		addLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//addLocationAction();
				reader.turnGPSOff();
			}
		});

		editLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				editLocationAction();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void addContactAction() {

		String number, name, location;

		contactNumber = (EditText) findViewById(R.id.contact_number);
		contactName = (EditText) findViewById(R.id.contact_name);
		allowedLocation = (EditText) findViewById(R.id.location_name);

		number = contactNumber.getText().toString();
		name = contactName.getText().toString();
		location = allowedLocation.getText().toString();
		
		contactNumber.setText("");
		contactName.setText("");
		allowedLocation.setText("");
		
		helper.addContact(number,location,name);
		Toast.makeText(MainActivity.this,"Number added successfully",Toast.LENGTH_SHORT).show();

	}

	public void addLocationAction() {

		Intent indent = new Intent(getApplicationContext(), AddLocation.class);
		startActivity(indent);
	}

	public void editLocationAction() {
		Intent indent = new Intent(getApplicationContext(), EditLocation.class);
		startActivity(indent);
	}

}
