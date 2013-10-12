package smshandler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

	private String senderNumber;
	private String message;

	@Override
	public void onReceive(Context context, Intent intent) {

		Bundle bundle = intent.getExtras();
		try {

			if (bundle != null) {
				Object[] pdusObj = (Object[]) bundle.get("pdus");
				for (int i = 0; i < pdusObj.length; i++) {
					SmsMessage currentMessage = SmsMessage
							.createFromPdu((byte[]) pdusObj[i]);                               // create a sms message from byte array
					senderNumber = currentMessage
							.getDisplayOriginatingAddress();                                   // get message orginating address

					message = currentMessage.getDisplayMessageBody();                          // get messsage body
					Log.v("SmsReceiver ", "Sender number: " + senderNumber
							+ "; message: " + message);

					Toast.makeText(
							context,
							"Sms Received from:" + senderNumber
									+ "\n Message: " + message,
							Toast.LENGTH_LONG).show();

					Intent inte = new Intent(context, SmsHandlingService.class);

					inte.putExtra("incomingNumber", senderNumber);                            // put sms details to intent
					inte.putExtra("message", message);
					context.startService(inte);                                               // start sms service
					this.abortBroadcast();                                                    // stop broadcasting sms

				}
			}

		} catch (Exception except) {
			Log.v("SmsReceiver error: ", "" + except);
		}
	}

}
