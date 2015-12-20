package com.borntowindevelopers.whitelogics;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "notifications";

	// Contacts table name
	private static final String TABLE_NAME = "noti";

	// Contacts Table Columns names
	private static final String ID = "id";
	private static final String BODY = "body";
	private static final String SUBJECT = "subject";
	private static final String IN_TIME = "received_time";

	String temp="",temp1="";
	static int count=0;
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_NOTIFICATION_TABLE = " CREATE TABLE " + TABLE_NAME + "("+
				ID+" INTEGER,"+ SUBJECT + " TEXT," + BODY + " TEXT,"
				+ IN_TIME + " DATETIME" + ")";
		db.execSQL(CREATE_NOTIFICATION_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */



    public void newEntry(Notification noti,Context context) {

        int check = CheckIsDataAlreadyInDBorNot(TABLE_NAME,ID,noti.getId());

/*        if(!TextUtils.isEmpty(noti.getSubject()) || !TextUtils.isEmpty(noti.getBody())){

            deleteNotification(noti.getId());

        }
        else*/ if(check==1){

            updateNotification(noti,context);

        }
        else if(check==0){
            addNotification(noti,context);
        }
        else{
            Log.d("TAG","Invalid DB Entry");
        }

       }


    // Updating single Notification
    public void updateNotification(Notification noti,Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(IN_TIME, noti.getTime());

        // updating row
        db.update(TABLE_NAME, values, ID + " = ?",new String[] { String.valueOf(noti.getId()) });
		setNotification(noti,context);
    }

    //Check for db entry already present
    public int CheckIsDataAlreadyInDBorNot(String TableName, String dbfield, int fieldValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + TableName + " where " + dbfield + " = " + fieldValue;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return 0;
        }
        cursor.close();
        return 1;
    }


    // Adding new notification
	void addNotification(Notification noti,Context context) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(ID, noti.getId());
		values.put(SUBJECT, noti.getSubject());
		values.put(BODY, noti.getBody());
		values.put(IN_TIME, noti.getTime());

		// Inserting Row
		db.insert(TABLE_NAME, null, values);

		Log.v("TAG", "" + noti.getId());

		setNotification(noti,context);

	}


	void setNotification(Notification noti,Context context){

		noti = getNotification();

		MainActivity.id = noti.getId();
		MainActivity.body = noti.getBody();
		MainActivity.subject = noti.getSubject();
		MainActivity.time = noti.getTime();
		Log.d("TAG", MainActivity.subject + " " + MainActivity.body + " " + MainActivity.time);

//		db.deleteFirstNotification();

		Calendar calendar = new GregorianCalendar();


		//set notification for date time
		try {
			Date date =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(MainActivity.time);
			calendar.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Intent myIntent = new Intent(context, MyReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);

		AlarmManager alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

	}

	// Getting single notification
	Notification getNotification() {
		SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME +" ORDER BY datetime("+IN_TIME+") ASC";

		Cursor cursor = db.rawQuery(selectQuery, null);

		Notification noti;// = new Notification();
		if (cursor != null && cursor.moveToFirst()) {
			noti = new Notification(Integer.parseInt(cursor.getString(0)),cursor.getString(1), cursor.getString(2), cursor.getString(3));
		}else{
			noti = new Notification(100,"SynCops", "Welcome to SynCops", "2015-12-18 18:44:43");
		}

		// return notification
		return noti;
	}


    public void deleteFirstNotification() {
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME +" ORDER BY datetime("+IN_TIME+") ASC";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {

			db.delete(TABLE_NAME, ID + " = " + cursor.getString(0), null);
		}
		db.close();
    }

    // Deleting single notification
    public void deleteNotification(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " = " + id,null);
        db.close();
    }

/*
	// Getting All Contacts
	public List<Notification> getNotification () {
		List<Notification> notiList = new ArrayList<Notification>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_NAME +" ORDER BY datetime("+IN_TIME+") ASC";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			Notification noti = new Notification();

		//	do {
		//		if(count==0) {

		//			temp = cursor.getString(3).substring(0,16);
		//			Log.v("klk","inside if  "+temp);
					noti.setSubject(cursor.getString(1));
					noti.setBody(cursor.getString(2));
					noti.setTime(cursor.getString(3));
					notiList.add(noti);
					count++;

		//		}
				else {
                        temp1= cursor.getString(3).substring(0,16);
					if (temp.equals(temp1)) {
						Log.v("klk","inside else  "+temp1);

						noti.setSubject(cursor.getString(1));
						noti.setBody(cursor.getString(2));
						noti.setTime(cursor.getString(3));
						notiList.add(noti);
					}
			}

			}//while(cursor.moveToNext());


//		Log.v("klk",""+notiList.size());

//		Log.v("klk",""+notiList.get(0));
//		Log.v("klk",""+notiList.get(1));

        // return contact list
		return notiList;
	}


	// Getting contacts Count
	public int getContactsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}
*/
}
