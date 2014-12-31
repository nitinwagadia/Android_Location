package com.android.projfinal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class Reciever extends BroadcastReceiver 

{

	final SmsManager sms=SmsManager.getDefault();	
	String senderno,msg;
	
	LocationManager lm;
	LocationListener ll;
	SQLiteDatabase db;
	
	@Override
	public void onReceive(Context con, Intent intent) 
	{
		final Bundle bundle = intent.getExtras();
		if(bundle!=null)
	    {
	    	Object[] pdus=(Object[])bundle.get("pdus");
	    	for (int i=0; i<pdus.length; i++)
            {
               SmsMessage cmsg = SmsMessage.createFromPdu((byte[])pdus[i]);                
               String phone=cmsg.getDisplayOriginatingAddress();  
               senderno=phone;
             msg=cmsg.getMessageBody().trim();
            }	    	   	
	    }
	    
	    if(msg.equalsIgnoreCase("Location"))
	    {
	    	abortBroadcast();
	    	Intent i=new Intent(con,Locations.class);
	        i.putExtra("phonenumber",senderno);
	        con.startService(i);
	    }
	    
	    	
	}
}
