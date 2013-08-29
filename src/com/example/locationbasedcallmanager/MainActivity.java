package com.example.locationbasedcallmanager;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import locationhandler.LocationListner;

public class MainActivity extends Activity {
	
	Button start;
	Button stop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		start=(Button)findViewById(R.id.start);
		stop=(Button)findViewById(R.id.stop);
		
		start.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				Intent intent = new Intent(getApplicationContext(),SupportService.class);
				startService(intent);
				
			}});
		
		stop.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				Intent intent = new Intent(getApplicationContext(),SupportService.class);
				stopService(intent );
				Log.v("running comes here","upto this correct");
				Intent locIntent= new Intent(getApplicationContext(),LocationListner.class);
				startActivity(locIntent);
				
				
				
			}});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
