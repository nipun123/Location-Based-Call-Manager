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

public class ProfileList extends Activity {
	
	private DataBaseHelper helper;
	private TextView profile;
	private Button okButton;
	private Button backButton;
	private String profilename;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_list);
		
		setupActionBar();
		helper=DataBaseHelper.getInstance(getApplicationContext());
		backButtonAction();
		okButtonaction();
		fillProfileList();
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile_list, menu);
		return true;
	}
	
	public void backButtonAction()
	{
		backButton=(Button)findViewById(R.id.back_button);
		backButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);         // start  new EditLocation Activity
				startActivity(intent);
			}});
	}
	
	public void okButtonaction()
	{
		okButton=(Button)findViewById(R.id.okButton);
		okButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
				Intent intent = new Intent(getApplicationContext(), EditLocation.class);  // start  new EditLocation Activity
				intent.putExtra("Name",profilename);
				startActivity(intent);
			
			}});	
	}
	
	public void fillProfileList()
	{
		ArrayList<HashMap<String, String>> profileList = helper.getAllProfiles();
		
		if (profileList .size() != 0) {
			ListView lv = (ListView)findViewById(R.id.profile_list);
			lv.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long arg3) {
					profile = (TextView) view.findViewById(R.id.profile);
					profilename=profile.getText().toString();
					
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
			
			ListAdapter adapter = new SimpleAdapter(ProfileList.this,
					profileList, R.layout.profile, new String[] {"Name" }, new int[] {R.id.profile});
			lv.setAdapter(adapter);
			
		}
		else
		{
			Toast.makeText(this, "No Allowed contacts for this profile", Toast.LENGTH_LONG).show();
			backButtonAction();
		}
	}

}
