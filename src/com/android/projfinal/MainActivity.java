package com.android.projfinal;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;

public class MainActivity extends Activity
{
	Button b;

	String number, name, contact_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		PutData();
		
	}
	
	private void PutData() 
	{
		ArrayList<String> contacts = new ArrayList<String>();
		Uri content = ContactsContract.Contacts.CONTENT_URI;
		Uri phonecontent = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		ContentResolver contentresolver = getContentResolver();
		Cursor c = contentresolver.query(content, null, null, null, "UPPER("+ ContactsContract.Contacts.DISPLAY_NAME + ") ASC");
		while (c.moveToNext()) 
		{
			contact_id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
			name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			int phonenumber = Integer.parseInt(c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
			if (phonenumber > 0) {
				Cursor pcur = contentresolver.query(phonecontent, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ " = ?", new String[] { contact_id }, null);
				while (pcur.moveToNext()) {
					number = pcur.getString(pcur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					pcur.getString(pcur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					break;
				}
			}
			contacts.add(name + "\n" + number);
}
		c.close();
		Intent i=new Intent(MainActivity.this,MyList.class);
		i.putStringArrayListExtra("contacts",contacts);
		startActivity(i);
		finish();
	}

	
}
