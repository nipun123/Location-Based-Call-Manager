package com.example.locationbasedcallmanager;


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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class AddLocation extends Activity {

	private Button createButton;
	private Button backButton;
	private Button setVolumeButton;
	private Button setAutoMessage;
	private CheckBox callBlock;
	private CheckBox volumeChange;
	private DataBaseHelper helper;
	private int volumeLevel = 4;
	private String autoSendMessage;
	private EditText name;
	private EditText latitude;
	private EditText longitude;
	private EditText forwardNumber;
	private Context context=this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_location);
		
		this.latitude = (EditText) findViewById(R.id.latitud);
		this.longitude = (EditText) findViewById(R.id.longitude);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			String lat=bundle.getString("lat");
			String lng=bundle.getString("lng");
			
			Log.v("","latitude= "+lat+" Longitude "+lng);
			latitude.setText("71.436578");
			longitude.setText(lng);

		}else{
			Log.v("","Bundle is empty haloooo");
		}

		this.createButton = (Button) findViewById(R.id.createButton);
		this.setAutoMessage=(Button)findViewById(R.id.set_reply);
		this.backButton = (Button) findViewById(R.id.back_button);
		Log.v("","this button is okkkk");
		
		this.setVolumeButton = (Button) findViewById(R.id.set_volume);
		this.callBlock = (CheckBox) findViewById(R.id.call_block);
		callBlock.setChecked(true);
		this.volumeChange = (CheckBox) findViewById(R.id.volume_change);
		volumeChange.setChecked(true);
		this.helper = DataBaseHelper.getInstance(getApplicationContext());                    //assign databaseHelper instance

		this.name = (EditText) findViewById(R.id.lon);
		this.forwardNumber = (EditText) findViewById(R.id.forwardNumber);
		
		Log.v("","execution comes here");
		createButtonAction();
		backButtonaction();
		setRingingVolume();
		setTheAutoSendMessage();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_location, menu);
		return true;
	}

	public void backButtonaction() {
		backButton.setOnClickListener(new OnClickListener() {                              // set onclicklistner for backButton 

			@Override
			public void onClick(View arg0) {
				finish();
				Intent indent = new Intent(getApplicationContext(), LocationSelection.class);             // start mainActivity 
				startActivity(indent);
			}
		});
	}

	public void createButtonAction() {
		
		createButton.setOnClickListener(new OnClickListener() {                             // set onclicklistner for createButton

			@Override
			public void onClick(View arg0) {
				
				String profileName = name.getText().toString();
				String callForwardingNumber = forwardNumber.getText().toString();
				boolean callblocking=callBlock.isChecked();
				boolean volumeChanging=volumeChange.isChecked();

				if(profileName.equals("") || profileName==null)
				{
					Toast.makeText(AddLocation.this, "Profilename is not given",
							Toast.LENGTH_SHORT).show();
				}
				else if(latitude.getText().toString().equals("") || longitude.getText().toString().equals(""))
				{
					Log.v("","create button works okayyy....");
					Toast.makeText(AddLocation.this, "Location cordinates are not given",
							Toast.LENGTH_SHORT).show();
				}
				else{
					
					double latitudevalue = Double.parseDouble(latitude.getText().toString());
					double longitudevalue = Double.parseDouble(longitude.getText().toString());
				try {
					if (helper.addProfile(profileName, latitudevalue, longitudevalue,                // add profile 
							callForwardingNumber, autoSendMessage, volumeLevel,
							callblocking, volumeChanging)) {
						Toast.makeText(AddLocation.this, "Profile added successfully",
								Toast.LENGTH_SHORT).show();
					} else
						Toast.makeText(AddLocation.this,
								"Profile name already exist use another name",
								Toast.LENGTH_SHORT).show();

					name.setText("");                                                          // reset the TextViews
					latitude.setText("");
					longitude.setText("");
					forwardNumber.setText("");
				} catch (Exception e) {
					Toast.makeText(AddLocation.this,
							"Invalid inputs for Latitide and Longitude",
							Toast.LENGTH_SHORT).show();
				   }                                                                   // commit the changes
			    }
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
}
