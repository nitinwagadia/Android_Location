package com.android.projfinal;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyList extends Activity {

	ListView lv, lst;
	ArrayAdapter<String> ad, adt;
	int flag[];
	ArrayList<String> contacts = new ArrayList<String>();
	TextView tv;
	SQLiteDatabase db;
	String number, name, contact_id;
	

	int count;
	String[] f;
	int i;
	SparseBooleanArray checklist = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.listbutton);
		Intent i=getIntent();
		contacts=i.getStringArrayListExtra("contacts");
		tv = (TextView) findViewById(R.id.textView1);
		lv = (ListView) findViewById(R.id.listView1);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		//MyList.this.deleteDatabase("Conts");
		db = openOrCreateDatabase("Conts", SQLiteDatabase.CREATE_IF_NECESSARY,null);
		db.execSQL("Create table if not exists Contacts(names varchar,numbers varchar)");
		
		ad = new ArrayAdapter<String>(MyList.this,android.R.layout.simple_list_item_multiple_choice, contacts);
		lv.setAdapter(ad);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

			}
		});
	
		//PutData();

	}

		public void AddRecords(View v) throws Exception
	{
		Async a=new Async();
		a.execute();
		String toast=a.get();
		Toast.makeText(MyList.this,toast+" are now permitted to access your Location", Toast.LENGTH_SHORT).show();
		
	}
	
	public void DeleteRecords(View v)
	{
		Intent in=new Intent(MyList.this,DeleteRecord.class);
		startActivity(in);
		//finish();
	}

	
	
	class Async extends AsyncTask<Void,Void, String>
	{
		int end=0;
		SQLiteDatabase db;
		SparseBooleanArray checklist = null;
		Cursor c=null;
		ArrayList<String> checkeditems =null;
		
		
		@Override
		protected String doInBackground(Void... params) 
		{	
			//MyList list=new MyList();
			String toast="";
			checkeditems = new ArrayList<String>();
			db = openOrCreateDatabase("Conts", SQLiteDatabase.CREATE_IF_NECESSARY,null);
			db.execSQL("Create table if not exists Contacts(names varchar,numbers varchar)");
			checklist = new SparseBooleanArray();
			checklist.clear();
			checklist = lv.getCheckedItemPositions();
			checkeditems.clear();
			
			
				
				for (int i = 0; i <checklist.size(); i++) 
				{
					int position = checklist.keyAt(i);
					if (checklist.valueAt(i)) 
					{
						checkeditems.add(ad.getItem(position));
						Log.i("DAtaaaa", checkeditems.get(i)+"added");
					}
					
				}
			
			
			flag=new int[checkeditems.size()];
			for(i=0;i<checkeditems.size();i++)
			flag[i]=0;
			
			c=db.rawQuery("Select numbers from Contacts", null);
			
			if(c.getCount()>0)
			{
				i=0;
				String data[]=new String[c.getCount()];
				Log.i("Database Data","I reached");
				while(c.moveToNext())
				{
					data[i]=c.getString(c.getColumnIndex("numbers"));
					Log.i("Database Data",data[i]);
					i++;
				}
				
					
				for(int j=0;j<checkeditems.size();j++)
						for(i=0;i<data.length;i++)
					{
						if(data[i].equalsIgnoreCase(checkeditems.get(j).substring(checkeditems.get(j).indexOf("\n")).trim()))
						{
							flag[j]=1;
							break;
						}
					}
					
					
					
					Log.i("I have the ", "Data");
					
					
				int k=0;
				for(i=0;i<flag.length;i++)
				{	
					if(flag[i]!=1)
					{	
						k++;
						end = checkeditems.get(i).indexOf("\n");
						String s = "insert into Contacts values('"+ checkeditems.get(i).substring(0, end) + "','"+ checkeditems.get(i).substring(end).trim() + "')";
						db.execSQL(s);
						toast+=checkeditems.get(i).substring(0, end)+"\n";
						
					}
					
				}
				if(k==0)
				toast="All record are already present";
				c.close();
				db.close();
			}
			else
			{
				for (i = 0; i < checkeditems.size(); i++) 
				{
					end = checkeditems.get(i).indexOf("\n");
					String s = "insert into Contacts values('"+ checkeditems.get(i).substring(0, end) + "','"+ checkeditems.get(i).substring(end).trim() + "')";
					db.execSQL(s);
					toast+=checkeditems.get(i).substring(0, end)+"\n";
				}
				c.close();
				db.close();
			}
			
			return toast;
		
		}
	}
}
