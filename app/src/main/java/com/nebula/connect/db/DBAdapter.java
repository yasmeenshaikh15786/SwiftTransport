package com.nebula.connect.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nebula.connect.tables.DEALER_TBL;
import com.nebula.connect.tables.DEVICE_TBL;
import com.nebula.connect.tables.MEETING_TBL;
import com.nebula.connect.tables.PAINTER_TBL;
import com.nebula.connect.tables.PLANNING_TBL;
import com.nebula.connect.tables.PRODUCT_SKU_TBL;
import com.nebula.connect.tables.PURCHASE_SUB_ORDER_TBL;
import com.nebula.connect.tables.PURCHASE_TBL;
import com.nebula.connect.tables.ROUTE_PLAN_TBL;
import com.nebula.connect.tables.SALE_METADATA_TBL;
import com.nebula.connect.tables.STARTDAY_TBL;
import com.nebula.connect.tables.SUB_ORDER_TBL;

import com.nebula.connect.tables.SETTINGS_TBL;


public class DBAdapter {
	private static final String TAG="DBAdapter";
	public static final String DB_NAME="NEROLAC";
	private static int dbVersion=2;
	private static DBAdapter adapter ;
	

//    private final Context context;    
    private final DatabaseHelper dbHelper;
    private SQLiteDatabase dbObject;
    
    public SQLiteDatabase getDB(){
    	return dbObject;
    }

    private DBAdapter(final Context ctx) 
    {
//        this.context = ctx;
        dbHelper = new DatabaseHelper(ctx);
    }

	public static DBAdapter getInstance(final Context context){
		Log.d(TAG,"adapter : " + adapter + " : context : " + context);

		synchronized (context) {
			if(adapter == null){
				adapter=new DBAdapter(context);
			}
			
			return adapter;	
		}
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(final Context context) 
        {
            //super(context, Constants.fldr+ "/mydb.db", null, dbVersion);
            super(context, DB_NAME, null, dbVersion);
        }

		@Override
		public void onCreate(final SQLiteDatabase dbObj) {
			Log.d(TAG, "Inside onCreate");
            dbObj.execSQL(DEVICE_TBL.createTable());
            dbObj.execSQL(PRODUCT_SKU_TBL.createTable());
            dbObj.execSQL(PURCHASE_SUB_ORDER_TBL.createTable());
            dbObj.execSQL(ROUTE_PLAN_TBL.createTable());
            dbObj.execSQL(PURCHASE_TBL.createTable());
            dbObj.execSQL(SALE_METADATA_TBL.createTable());
            dbObj.execSQL(SUB_ORDER_TBL.createTable());
            dbObj.execSQL(SETTINGS_TBL.createTable());
            dbObj.execSQL(STARTDAY_TBL.createTable());
            dbObj.execSQL(MEETING_TBL.createTable());
            dbObj.execSQL(PLANNING_TBL.createTable());
            dbObj.execSQL(DEALER_TBL.createTable());
            dbObj.execSQL(PAINTER_TBL.createTable());
		}

		@Override
		public void onUpgrade(final SQLiteDatabase dbObj,final int oldVersion, final int newVersion) {
			dbVersion=newVersion;
            dbObj.execSQL(alterStartDayTbl());
		}
    }

    public static String alterStartDayTbl(){
        final String STARTDAY_TBL_ALTER =
                (new StringBuffer()).append("ALTER TABLE " ).append( STARTDAY_TBL.TABLE_NAME ).append(" ADD COLUMN ").append(STARTDAY_TBL.MOBILE)
                        .append( " text").toString();

        return STARTDAY_TBL_ALTER;

    }

    //---opens the database---
    public DBAdapter open() throws SQLException 
    {
        dbObject = dbHelper.getWritableDatabase();        
        return this;
    }

    //---closes the database---    
    public void close() 
    {
        dbHelper.close();
    }

	
}
