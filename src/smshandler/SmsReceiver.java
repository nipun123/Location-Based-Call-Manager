package smshandler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver{

	SmsManager sms=SmsManager.getDefault();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Bundle bundle=intent.getExtras();
		
		try{
			
			if(bundle!=null)
			{
				Object[] pdusObj=(Object[])bundle.get("pdus");
				
				for(int i=0;i<pdusObj.length;i++)
				{
					SmsMessage currentMessage=SmsMessage.createFromPdu((byte[])pdusObj[i]);
					String phoneNumber=currentMessage.getDisplayOriginatingAddress();
					String senderNumber=phoneNumber;
					String message=currentMessage.getDisplayMessageBody();
					Log.v("SmsReceiver ","Sender number: "+senderNumber+"; message: "+message);
					
					Toast.makeText(context,"Sms Received from:"+senderNumber+"\n Message: "+message,Toast.LENGTH_LONG).show();
					
					Intent inte = new Intent(context,SmsHandlingService.class);
					context.startService(inte);
					
				}
			}
			
		}catch(Exception except)
		{
			Log.v("SmsReceiver error: ",""+except);
		}
	}

}
