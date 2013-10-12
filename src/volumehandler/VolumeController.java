package volumehandler;

import android.content.Context;
import android.media.AudioManager;

public class VolumeController {

	private AudioManager manager;
	public static VolumeController controller;

	public static VolumeController getInstance(Context context) {
		if (controller == null) {
			controller = new VolumeController(context);
			return controller;
		} else
			return controller;
	}

	public VolumeController(Context context) {
		manager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
	}

	public void setVolume( int progress) {
		manager.setStreamVolume(AudioManager.STREAM_RING, progress,AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
	}

}
