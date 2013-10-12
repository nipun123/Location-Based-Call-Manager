package com.example.locationbasedcallmanager;

import java.util.ArrayList;
import java.util.HashMap;

import databasehandler.DataBaseHelper;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class AllowedContacts extends Activity {
	private DataBaseHelper helper;
	private String profileName;
	private TextView name;
	private TextView number;
	private String contactName,contactNumber;
	private Button backButton;
	private Button deleteButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_allowed_contacts);
		setupActionBar();
		
		Intent intent=this.getIntent();
		Bundle extras = intent.getExtras();
		profileName=extras.getString("Name");
		
		TextView text=(TextView)findViewById(R.id.location);
		text.setText("Allowed contacts at "+profileName);	
		helper=DataBaseHelper.getInstance(getApplicationContext());
		fillcontactList();
		
		backButtonAction();
		deleteButtonaction();
		
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	public void fillcontactList(){
		ArrayList<HashMap<String, String>> contactList = helper.getAllAllowedContacts(profileName);
		
		if (contactList.size() != 0) {
			ListView lv = (ListView)findViewById(R.id.list);
			lv.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long arg3) {
					name = (TextView) view.findViewById(R.id.lon);
					number=(TextView) view.findViewById(R.id.number);
					contactName=name.getText().toString();
					contactNumber=number.getText().toString();
					
					for(int i=0; i<parent.getChildCount(); i++)
					{
					     if(i == position)
					     {
					               parent.getChildAt(i).setBackgroundColor(Color.CYAN);
					     }
					     else
					     {
					               parent.getChildAt(i).setBackgroundColor(Color.WHITE);
					     }

					 }
					
				}
			});
			
			ListAdapter adapter = new SimpleAdapter(AllowedContacts.this,
					contactList, R.layout.details, new String[] {
							"Name", "Number" }, new int[] {
							R.id.lon, R.id.number });
			
			lv.setAdapter(adapter);
			
		}
		else
		{
			Toast.makeText(this, "No Allowed contacts for this profile", Toast.LENGTH_LONG).show();
			backButtonAction();
		}
	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.allowed_contacts, menu);
		return true;
	}
	
	public void backButtonAction()
	{
		backButton=(Button)findViewById(R.id.allowed_contacts);
		backButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
				Intent indent = new Intent(getApplicationContext(), MainActivity.class);         // start  new EditLocation Activity
				startActivity(indent);
			}});
		
	}
	
	public void deleteButtonaction()
	{
		deleteButton=(Button)findViewById(R.id.Delete);
		deleteButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(contactNumber!=null)
				{
					helper.deleteContact(contactNumber,profileName);
					Toast.makeText(AllowedContacts.this, "contact successfully deleted", Toast.LENGTH_LONG).show();
					fillcontactList();
					
				}
				else
				{
					Toast.makeText(AllowedContacts.this, "Select a item to delete", Toast.LENGTH_LONG).show();
				}
			}});
		
	}

}
