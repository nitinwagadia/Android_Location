package com.android.projfinal;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteRecord extends Activity {
	ListView lv;
	ArrayAdapter<String> ad, adt;
	ArrayList<String> contacts = new ArrayList<String>();
	TextView tv;
	SQLiteDatabase db;
	Cursor c = null;
	SparseBooleanArray checklist = null;
	Intent in;
	int i = 0;
	int end = 0;
	ArrayList<String> checkeditems = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deleterecords);
		in = getIntent();
		lv = (ListView) findViewById(R.id.listView1);
		tv = (TextView) findViewById(R.id.textView1);
		tv.setText("Select Contacts to Deny Permission");
		db = openOrCreateDatabase("Conts", SQLiteDatabase.CREATE_IF_NECESSARY,
				null);
		setData();

	}

	private void setData() {
		c = db.rawQuery("Select * from contacts", null);
		while (c.moveToNext()) {
			String name = c.getString(0);
			String number = c.getString(1);
			String result = name + "\n" + number;
			contacts.add(result);

		}

		ad = new ArrayAdapter<String>(DeleteRecord.this,
				android.R.layout.simple_list_item_multiple_choice, contacts);
		lv.setAdapter(ad);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

			}
		});
	} // end of setdata

	public void DeleteRecords(View v) 
	{

		String toast = "";
		checkeditems = new ArrayList<String>();
		db = openOrCreateDatabase("Conts", SQLiteDatabase.CREATE_IF_NECESSARY,
				null);
		db.execSQL("Create table if not exists Contacts(names varchar,numbers varchar)");
		checklist = new SparseBooleanArray();
		checklist.clear();
		checklist = lv.getCheckedItemPositions();

		for (int i = 0; i < checklist.size(); i++) {
			int position = checklist.keyAt(i);
			if (checklist.valueAt(i)) {
				checkeditems.add(ad.getItem(position));
			}

		}

		if (checkeditems.size() > 0) {
			i = 0;
			String data[] = new String[checkeditems.size()];
			String names[] = new String[checkeditems.size()];

			for (int i = 0; i < data.length; i++) {
				data[i] = checkeditems.get(i)
						.substring(checkeditems.get(i).indexOf("\n")).trim();
				names[i] = checkeditems.get(i)
						.substring(0, checkeditems.get(i).indexOf("\n")).trim();
			}

			for (i = 0; i < data.length; i++) {

				String s = "delete from  Contacts where numbers='" + data[i]+"'";
				db.execSQL(s);
				toast += names[i] + "\n";

			}
			Toast.makeText(DeleteRecord.this,
					toast + " denied permission to access your location",
					Toast.LENGTH_SHORT).show();
			finish();
			startActivity(getIntent());
			c.close();
			db.close();

		} else {
			Toast.makeText(DeleteRecord.this,
					"Please Give Permission before Deleting it",
					Toast.LENGTH_SHORT).show();
			finish();
			startActivity(getIntent());
			c.close();
			db.close();
		}

		checkeditems.clear();
	}

	
	public void Cancel(View v)
	{
		//Intent in=new Intent(DeleteRecord.this,MainActivity.class);
		//startActivity(in);
		finish();
	}
}
