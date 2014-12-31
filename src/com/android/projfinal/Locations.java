package com.android.projfinal;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class Locations extends Service 
{
		String no,mes,acmes;
		LocationManager lm;
		LocationListener ll;
		String Add="";
		SQLiteDatabase db;
		Cursor c;
		NotificationManager nm;
		boolean flag=false;

	@Override
	public IBinder onBind(Intent arg0) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onStart(Intent intent,int StartId)
	{
		Toast.makeText(Locations.this,"I am Retreiveing Location", Toast.LENGTH_SHORT).show();
		no=intent.getStringExtra("phonenumber");
		db=openOrCreateDatabase("Conts", SQLiteDatabase.CREATE_IF_NECESSARY,null);
		c=db.rawQuery("Select numbers from Contacts", null);
		nm=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		outer: for(int i=0;i<c.getCount();i++)
		{	
			c.moveToNext();
			if(c.getString(0).equalsIgnoreCase(no))
				{
				flag=true;
				break outer;
				}
		}
		c.close();
		flag=true;
		Log.i("i REached Here","Sending Loation");
		if(flag)
		{
		lm=(LocationManager)Locations.this.getSystemService(Context.LOCATION_SERVICE);
		ll=new MyLocation();
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,ll);
		Toast.makeText(Locations.this,"Retreiveing Location", Toast.LENGTH_SHORT).show();
		//permittednotification(no);

		}
		else
		{
			//deniednotification(no);
			
		}
	
		
	}
	
	
	private void deniednotification(String no2) 
	{
		Notification n1=new Notification(R.drawable.ic_launcher,"Location denied to "+no2,System.currentTimeMillis()+1000);
		nm.notify(0, n1);
	}

	private void permittednotification(String no2) 
	{
		Notification n1=new Notification(R.drawable.ic_launcher,"Location Given to "+no2,System.currentTimeMillis()+1000);
		nm.notify(0, n1);
	}


	private class MyLocation implements LocationListener
	{
		@Override
		public void onLocationChanged(Location loc) 
		{	
			String Add="";
	
			if(loc!=null)
			{
				Add="Latitude is "+loc.getLatitude()+"\n Longitude is :"+loc.getLongitude()+"\n";
				Geocoder geocoder=new Geocoder(getBaseContext(),Locale.getDefault());
				List<Address> address = null;
				try {
					address = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(address.size() > 0)
				{
					for(int i=0;i<address.get(0).getMaxAddressLineIndex();i++)
					Add+=address.get(0).getAddressLine(i)+"\n";
					
				}
				Toast.makeText(Locations.this, Add, Toast.LENGTH_LONG).show();
				Log.i("Address", Add);
				SmsManager sms=SmsManager.getDefault();
				sms.sendTextMessage(no,null,"Location is  \n"+Add, null, null);

				Toast.makeText(Locations.this,"Location sent to "+no , Toast.LENGTH_SHORT).show();
				lm.removeUpdates(ll);
				
			}
			Locations.this.stopSelf();
			
			
		}

		@Override
		public void onProviderDisabled(String provider) 
		{
			// TODO Auto-generated method stub
		}

		@Override
		public void onProviderEnabled(String provider) 
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		
		
	}
	
}
