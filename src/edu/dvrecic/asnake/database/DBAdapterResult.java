package edu.dvrecic.asnake.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class DBAdapterResult implements BaseColumns {
	public static final  String TAG="DBAdapterRezultat";

	public static final  String STANJE = "i_stanje";
	public static final  String NAME = "s_name";
	public static final  String DATE = "s_date";
	public static final  int POS__ID=0;
	public static final  int POS_NAME=1;
	public static final  int POS_STANJE=2;
	public static final  int POS_DATE=3;

	public static final  String TABLE="highscore";

	private final Context context;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBAdapterResult(Context ctx) 
	{
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}


	//---opens the database---
	public DBAdapterResult open() throws SQLException 
	{
		db = DBHelper.getWritableDatabase();
		return this;
	}

	//---closes the database---    
	public void close() 
	{
		DBHelper.close();
	}
	//---deletes a particular title---
	public boolean deleteStevec(long rowId) 
	{
		return db.delete(TABLE, _ID + "=" + rowId, null) > 0;
	}

	//---retrieves all the titles---
	public Cursor getAll() 
	{
		return db.query(TABLE, new String[] {
				_ID,       //POS__ID=0;
				NAME,      //POS_NAME=1
				STANJE,
				DATE},    //POS_VALUE =2
				null, 
				null, 
				null, 
				null, 
				null);
	}
}

