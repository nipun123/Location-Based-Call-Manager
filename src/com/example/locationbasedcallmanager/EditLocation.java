package com.example.locationbasedcallmanager;

import java.util.HashMap;

import databasehandler.DataBaseHelper;
import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class EditLocation extends Activity {
	
	private Button backButton;
	private Button saveChangesButton;
	private Button deleteButton;
	private Button setVolumeButton;
	private Button setAutoMessage;
	private CheckBox callBlock;
	private CheckBox volumeChange;
	private DataBaseHelper helper;
	private int volumeLevel = 4;
	private String autoSendMessage;
	private String profileName;
	private EditText name;
	private EditText latitude;
	private EditText longitude;
	private EditText forwardNumber;
	private Context context=this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_location);
		
		this.latitude = (EditText) findViewById(R.id.lati);
		this.longitude = (EditText) findViewById(R.id.lon);
		this.name = (EditText) findViewById(R.id.profilename);
		this.forwardNumber= (EditText) findViewById(R.id.forward);   
		
		backButton=(Button) findViewById(R.id.backbuton);  
		saveChangesButton=(Button) findViewById(R.id.savechange);  
		deleteButton=(Button) findViewById(R.id.deletebutton); 
		setVolumeButton=(Button) findViewById(R.id.change_volume); 
		setAutoMessage=(Button) findViewById(R.id.auto_message); 
	
		callBlock=(CheckBox) findViewById(R.id.call_block); 
		volumeChange=(CheckBox) findViewById(R.id.volume_change); 
		
		helper=DataBaseHelper.getInstance(context);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			profileName=bundle.getString("Name");
			name.setText(profileName); 
		}else{
			Log.v("","Bundle is empty haloooo");
		}
		
		LoadProfile();
		setTheAutoSendMessage();
		setRingingVolume();
		backButtonAction();
		deleteButtonAction();
		saveChangesButtonAction();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_location, menu);
		return true;
	}
    
	public void setTheAutoSendMessage()
	{
		setAutoMessage.setOnClickListener(new OnClickListener(){

			private Dialog dialog;
			private EditText message;
			private Button okButton;
			@Override
			public void onClick(View arg0) {
                dialog=new Dialog(context);
				
			    dialog.setContentView(R.layout.auto_message);
				dialog.setTitle("Set the Auto send Message");
				okButton = (Button) dialog.findViewById(R.id.ok_button);
				message=(EditText)dialog.findViewById(R.id.message);
				if(autoSendMessage!=null)
				{
					message.setText(autoSendMessage);
				}
				OkButtonAction();
				dialog.show();
			}
			
			public void OkButtonAction()
			{
				okButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
				autoSendMessage=message.getText().toString();
				dialog.dismiss();
			 }});
				
			}
		});
	}
	
	public void setRingingVolume() {
		setVolumeButton.setOnClickListener(new OnClickListener(){
			private SeekBar volumeBar;
			private AudioManager manager;
			private TextView volume;
			private Button okButton;
			private Button backButton;
			private Dialog dialog;
			@Override
			public void onClick(View arg0) {
				
			    dialog=new Dialog(context);
				
			    dialog.setContentView(R.layout.activity_volume);
				dialog.setTitle("Set the ringing volume for location");
				okButton = (Button) dialog.findViewById(R.id.auto_message);
				backButton = (Button) dialog.findViewById(R.id.savechange);
				
				volume = (TextView) dialog.findViewById(R.id.volumelevel);

				WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			    lp.copyFrom(dialog.getWindow().getAttributes());
			    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
			    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
			    dialog.getWindow().setAttributes(lp);

				okButtonaction();
				backButtonaction();
				volumeBar = (SeekBar) dialog.findViewById(R.id.volumebar);
				manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
				volumeBar.setMax(manager.getStreamMaxVolume(AudioManager.STREAM_RING));

				volumeBar.setKeyProgressIncrement(1);
				volumeBar
						.setProgress(manager.getStreamVolume(AudioManager.STREAM_RING));

				volume.setText(volumeBar.getProgress() + "");
			    setVolumebarAction();
				dialog.show();
				
			}
			
            public void okButtonaction() {
				
				okButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						volumeLevel=volumeBar.getProgress();
						dialog.dismiss();
					}
				});
			}

     public void backButtonaction() {
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				 dialog.dismiss();
			}
		});

	}
            
			public void setVolumebarAction()
			{
				volumeBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onProgressChanged(SeekBar seekbar, int progress,
							boolean arg2) {
						manager.setStreamVolume(AudioManager.STREAM_RING, progress,
								AudioManager.FLAG_SHOW_UI
										+ AudioManager.FLAG_PLAY_SOUND);
						volume.setText(progress + "");
					}
					@Override
					public void onStartTrackingTouch(SeekBar arg0) {
						// TODO Auto-generated method stub
					}
					@Override
					public void onStopTrackingTouch(SeekBar arg0) {
						// TODO Auto-generated method stub
					}
				});
			}
			
			@SuppressWarnings("unused")
			public boolean onKeyDown(int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
						|| keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
					volumeBar.setProgress(manager
							.getStreamVolume(AudioManager.STREAM_RING));
				}
				return ((Activity) context).onKeyDown(keyCode, event);
			}
			
		  });
		}
	
	public void backButtonAction()
	{
		backButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
				Intent intent=new Intent(getApplicationContext(),ProfileList.class);
				startActivity(intent);
				
			}});
	}
	
	public void deleteButtonAction(){
		deleteButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
				
				if(profileName!=null)
				{
					helper.deleteProfile(profileName);
				}
				Intent intent=new Intent(getApplicationContext(),MainActivity.class);
				startActivity(intent);
				
			}});
	}
	
	public void saveChangesButtonAction()
	{
		saveChangesButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				String profile=name.getText().toString();
				String  callForwardingNumber = forwardNumber.getText().toString();
				boolean callblocking=callBlock.isChecked();
				boolean volumeChanging=volumeChange.isChecked();
				double latitudevalue = Double.parseDouble(latitude.getText().toString());
				double longitudevalue = Double.parseDouble(longitude.getText().toString());
				
				try{
					if(!profileName.equals(profile) && !profile.equals(""))
					{
						helper.updateProfileName(profileName,profile);
						profileName=profile;
					}
					else if(profile.equals(""))
					{
						Toast.makeText(EditLocation.this,
								"Enter a valid profileName",
								Toast.LENGTH_SHORT).show();
					}
					
					if(!latitude.getText().toString().equals("") || !longitude.getText().toString().equals(""))
					{	
					    helper.updateProfilePosition(profileName,latitudevalue,longitudevalue);
					}
					else{
						Toast.makeText(EditLocation.this, "Location cordinates are not given",
								Toast.LENGTH_SHORT).show();
					}
					
					helper.updateRingingVolume(profileName,volumeLevel);
					helper.updateAutoSendmessage(profileName,autoSendMessage);
					helper.updateCallBlockState(profileName,callblocking);
					helper.updateVolumeControlState(profileName,volumeChanging);
						
				}catch(Exception e)
				{}
				
				finish();
				Intent intent=new Intent(getApplicationContext(),MainActivity.class);
				startActivity(intent);
				
			}});
	}
	
	public void LoadProfile()
	{
		HashMap<String, String> profile=new HashMap<String, String>();
        String detail;
		
		if(profileName!=null)
		{
			
			try{
				Log.v("","load profile function calls ");
			profile=helper.getProfileInfo(profileName);

			detail=profile.get("Latitude");
			
			latitude.setText(detail);
			detail=profile.get("Longitude");
			longitude.setText(detail);
			detail=profile.get("ForwardingNumber");
			forwardNumber.setText(detail);
			detail=profile.get("AutoMessage");
			autoSendMessage=detail;
			detail=profile.get("Volume");
			volumeLevel=Integer.parseInt(detail);
			detail=profile.get("BlockState");
			if(detail.equals("1"))
			{
				callBlock.setChecked(true);
			}else{callBlock.setChecked(false);}
			detail=profile.get("VolumeState");
			if(detail.equals("1"))
			{
				volumeChange.setChecked(true);
			}else{volumeChange.setChecked(false);}
			
			}catch(Exception e)
			{
				Log.v(""," error occured"+e);
			}
		}	
	}

}
