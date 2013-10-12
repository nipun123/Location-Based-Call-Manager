package volumehandler;

import com.example.locationbasedcallmanager.AddLocation;
import com.example.locationbasedcallmanager.R;

import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class VolumeActivity extends Activity {


	private SeekBar volumeBar;
	private AudioManager manager;
	private TextView volume;
	private Button okButton;
	private Button backButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_volume);

		okButton = (Button) findViewById(R.id.auto_message);
		backButton = (Button) findViewById(R.id.savechange);
		volume = (TextView) findViewById(R.id.volumelevel);

		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				okButtonaction();
			}
		});

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				backButtonaction();
			}
		});

		volumeBar = (SeekBar) findViewById(R.id.volumebar);

		manager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		volumeBar.setMax(manager.getStreamMaxVolume(AudioManager.STREAM_RING));

		volumeBar.setKeyProgressIncrement(1);
		volumeBar
				.setProgress(manager.getStreamVolume(AudioManager.STREAM_RING));

		volume.setText(volumeBar.getProgress() + "");

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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
				|| keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			volumeBar.setProgress(manager
					.getStreamVolume(AudioManager.STREAM_RING));
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.volume, menu);
		return true;
	}

	public void okButtonaction() {
		Intent intent = new Intent(getApplicationContext(), AddLocation.class);
		intent.putExtra("ringVolume", volumeBar.getProgress());
		this.startActivity(intent);
	}

	public void backButtonaction() {
		Intent intent = new Intent(getApplicationContext(), AddLocation.class);
		this.startActivity(intent);
	}

}
