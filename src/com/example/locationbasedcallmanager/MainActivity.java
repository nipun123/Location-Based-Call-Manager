package com.example.locationbasedcallmanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import locationhandler.LocationReader;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import databasehandler.DataBaseHelper;
import databasehandler.TestDataInsert;

public class MainActivity extends Activity {

	private Button addContact;
	private Button addLocation;
	private Button editLocation;
	private Button allowedContacts;
	private EditText contactNumber;
	private EditText allowedLocation;
	private DataBaseHelper helper;
	private LocationReader reader;
	private TestDataInsert tester;
	private ArrayList<Map<String, String>> contactList;
	private SimpleAdapter adapter;
	private AutoCompleteTextView phonecontacts;
	private Spinner locations;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("Execution comes here");
		setContentView(R.layout.activity_main);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()	.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		       addContact = (Button) findViewById(R.id.add_contact); // get the values
		       addLocation = (Button) findViewById(R.id.location);
		       editLocation = (Button) findViewById(R.id.editlocation);
		       allowedContacts=(Button) findViewById(R.id.allowed);
		       contactNumber = (EditText) findViewById(R.id.contact_num);
		       phonecontacts = (AutoCompleteTextView) findViewById(R.id.contactlist);

		       contactList = new ArrayList<Map<String, String>>();
		       
		       helper = DataBaseHelper.getInstance(getApplicationContext());
		       reader = LocationReader.getInstance(getApplicationContext());
		       tester = new TestDataInsert(helper);
		       
		       AddItemsToLocations();
		       adapter = new SimpleAdapter(this, contactList, R.layout.details,new String[] { "Name", "Phone" }, new int[] { R.id.lon,R.id.number });
		       phonecontacts.setAdapter(adapter);
		       
		       populateContactList();
		       addContactAction();
		       addLocationAction();
		       editLocationAction();
		       allowedContacts();

		phonecontacts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> av, View arg1, int index,long arg3) {Map<String, String> map = (Map<String, String>) av.getItemAtPosition(index);
				String name = map.get("Name");
				String number = map.get("Phone");

				phonecontacts.setText(name);
				contactNumber.setText(number);
			}
		});

	}

	@SuppressWarnings("deprecation")
	public void populateContactList() {
		contactList.clear();

		Cursor contacts = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		while (contacts.moveToNext()) {
			String contactName = contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			String hasPhone = contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			String contactId = contacts.getString(contacts.getColumnIndex(BaseColumns._ID));

			if (Integer.parseInt(hasPhone) > 0) {
				Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ " = " + contactId, null, null);

				while (phones.moveToNext()) {
					String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					String numberType = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
					Map<String, String> NamePhone = new HashMap<String, String>();

					NamePhone.put("Name", contactName);
					NamePhone.put("Phone", phoneNumber);

					contactList.add(NamePhone);
				}
				phones.close();
			}
		}
		contacts.close();
	    startManagingCursor(contacts);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void addContactAction() {

		addContact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String number, name, location,message="";
				number = contactNumber.getText().toString();                                 // get String value from EditText
				name=phonecontacts.getText().toString();  
				location =String.valueOf(locations.getSelectedItem()) ;                                                         //allowedLocation.getText().toString();
				Log.v("","run comes to here");
				
		        
				if(number==null || number.equals(""))
				{
					message+=" Contactnumber is not specified";
				}
				if(name==null || name.equals(""))
				{
					message+=" Contactname is not specified";
				}
				if(location==null || location.equals(""))
				{
					message+=" location is not given";
				}
				
				if(!message.equals(""))
				{
					Toast.makeText(MainActivity.this, message,Toast.LENGTH_SHORT).show();
				}
				else{
				try {
					if (helper.addContact(number, location, name)) {                         // call database addcontact method to add contact
						Toast.makeText(MainActivity.this, "Contact added successfully",Toast.LENGTH_SHORT).show();
					} else
						Toast.makeText(MainActivity.this, "Contact already exist"+number+" "+name+" "+location,Toast.LENGTH_SHORT).show();

					contactNumber.setText("");                                               // clear the EditText values
					phonecontacts.setText("");
					
					                                                                         //allowedLocation.setText("");
				} catch (Exception e) {
					Toast.makeText(MainActivity.this, "Invalid inputs",Toast.LENGTH_SHORT).show();
				}
             }
				
			}
		});

	}
	
	public void AddItemsToLocations()
	{
		locations=(Spinner)findViewById(R.id.location_spinner);
		
		ArrayList<HashMap<String, String>> profilDetails=helper.getAllProfiles();
		Iterator<HashMap<String, String>> iterator = profilDetails.iterator();
		List<String> locationList=new ArrayList<String>();
		
		
		while(iterator.hasNext())
		{
			String locationName=iterator.next().get("Name");
			locationList.add(locationName);
		}
		
		Collections.sort(locationList, new Comparator<String>() {
	        @Override
	        public int compare(String s1, String s2) {
	            return s1.compareToIgnoreCase(s2);
	        }
	    });
		
		if(locationList.size()==0)
		{
			locationList.add("No Location exit");
		}
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, locationList);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		locations.setAdapter(dataAdapter);
	}

	public void addLocationAction() {
		
		addLocation.setOnClickListener(new OnClickListener() {         // set onclickListner to addLocation Button

			@Override
			public void onClick(View arg0) {
				finish();
				Intent intent = new Intent(getApplicationContext(), LocationSelection.class);
				startActivity(intent);
				Log.v("","activity starts");
				
			}
		});	
	}

	public void editLocationAction() {
		
		editLocation.setOnClickListener(new OnClickListener() {        // set onclickListner to editLocation Button

			@Override
			public void onClick(View arg0) {
				finish();
				Intent indent = new Intent(getApplicationContext(), ProfileList.class);         // start  new EditLocation Activity
				startActivity(indent);
			}
		});
	}
	
	public void allowedContacts()
	{
		allowedContacts.setOnClickListener(new OnClickListener() {        // set onclickListner to editLocation Button

			@Override
			public void onClick(View arg0) {
				String name=String.valueOf(locations.getSelectedItem()) ;   
				Log.v("name of the profile",""+name);
				
				if(!name.equals("No Location exist") && name!=null)
				{
					finish();
					Intent indent = new Intent(getApplicationContext(), AllowedContacts.class);         // start  new EditLocation Activity
					indent.putExtra("Name",name);
					startActivity(indent);	
				}
				else
				{
					Toast.makeText(MainActivity.this, "No Location selected",Toast.LENGTH_SHORT).show();	
				}
			}
		});
	}
	
	

}
