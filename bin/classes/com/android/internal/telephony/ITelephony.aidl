package com.android.internal.telephony;

interface ITelephony                                 // provide a interface to method of telephonymanager which normal canot access
{

      boolean endCall();
      void answerRingingCall();

}
