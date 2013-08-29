package callhandler;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class CallReceiver extends BroadcastReceiver  {
	
	Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context=context;
		try{
			
			TelephonyManager manager= (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			PhoneStateListner listner=new PhoneStateListner();
			manager.listen(listner,PhoneStateListener.LISTEN_CALL_STATE);
			
		}catch(Exception except)
		{
			Log.v("Phone Receiver error ",""+except);
		}
	}
	
	private class PhoneStateListner extends PhoneStateListener
	{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			Log.v("Phone State listner",state+"incoming number: "+incomingNumber);
			
			if(state==1)
			{
				String text="New phone call Event incoming number"+incomingNumber;
			    Toast.makeText(context,text,Toast.LENGTH_LONG).show();
			    
			    Intent intent = new Intent(context,CallHandlingService.class);
				context.startService(intent);
			}
		}
		
	}

}
