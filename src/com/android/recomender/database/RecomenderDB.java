package com.android.recomender.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.android.recomender.RecomenderActivity;
import com.android.recomender.beans.UserBean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class RecomenderDB extends SQLiteOpenHelper {

	
	public static final String DB_NAME = "recomender_db";
	public static final int DB_MODE_WRITE = 0;
	private static final int DB_MODE_READ = 1;
	
	public static final String TABLE_USER = "user";
	public static final String TABLE_USER_FIELD_USERID = "_id";
	public static final String TABLE_USER_FIELD_EMAIL = "email";
	public static final String TABLE_USER_FIELD_NAME = "name";
	public static final String TABLE_USER_FIELD_PASS = "password";
	
	public static final String TABLE_USERGROUP= "user_group";
	public static final String TABLE_USERGROUP_FIELD_USER_ID = "user_id";
	public static final String TABLE_USERGROUP_FIELD_GROUPID = "group_id";
	
	public static final String TABLE_GROUP = "groups";
	public static final String TABLE_GROUP_FIELD_GRUOPID = "_id";
	public static final String TABLE_GROUP_FIELD_NAME = "name";
	public static final String TABLE_GROUP_FIELD_DSCRIPTION = "description";
	
	public static final String TABLE_RATE = "rate";
	public static final String TABLE_RATE_FIELD_USERID = "user_id";
	public static final String TABLE_RATE_FIELD_GROUPID = "groupid";
	public static final String TABLE_RATE_FIELD_NRATE = "nrate";
	public static final String TABLE_RATE_FIELD_ITEMID = "item_id";
	
	public static final String TABLE_ITEM = "item";
	public static final String TABLE_ITEM_FIELD_ITEMID = "_id";
	public static final String TABLE_ITEM_FIELD_GROUPID = "group_id";
	public static final String TABLE_ITEM_FIELD_NAME = "name";
	public static final String TABLE_ITEM_FIELD_DESCRIOTION = "description";
	public static final String TABLE_ITEM_FIELD_SHORTDESCRIPTION= "short_description";
	public static final String TABLE_ITEM_FIELD_IMGSRC = "image_src";
	static final int VERSION = 1;
	
	
	
	
	public RecomenderDB(Context context) {
		super(context, DB_NAME, null, VERSION);
		
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE "+TABLE_USER+
				"( "+TABLE_USER_FIELD_USERID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
				     TABLE_USER_FIELD_EMAIL+" TEXT ,"+
				     TABLE_USER_FIELD_PASS+" TEXT NOT NULL ,"+
					 TABLE_USER_FIELD_NAME+" TEXT)");
		db.execSQL("CREATE TABLE "+TABLE_GROUP+
				"( "+TABLE_GROUP_FIELD_GRUOPID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
				     TABLE_GROUP_FIELD_DSCRIPTION+" TEXT ,"+
					 TABLE_GROUP_FIELD_NAME+" TEXT)");
		db.execSQL("CREATE TABLE "+TABLE_USERGROUP+
				"( "+TABLE_USERGROUP_FIELD_GROUPID+" INTEGER,"+
					 TABLE_USERGROUP_FIELD_USER_ID+" INTEGER,"+
					 "PRIMARY KEY ("+TABLE_USERGROUP_FIELD_GROUPID+","+TABLE_USERGROUP_FIELD_USER_ID+"))");
		db.execSQL("CREATE TABLE "+TABLE_ITEM+
				"( "+TABLE_ITEM_FIELD_ITEMID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
					TABLE_ITEM_FIELD_GROUPID+" INTEGER,"+
					TABLE_ITEM_FIELD_NAME+" TEXT,"+
					TABLE_ITEM_FIELD_SHORTDESCRIPTION+" TEXT,"+
					TABLE_ITEM_FIELD_IMGSRC+" TEXT,"+
					TABLE_ITEM_FIELD_DESCRIOTION+" TEXT)");		 
		db.execSQL("CREATE TABLE "+TABLE_RATE+
				"( "+TABLE_RATE_FIELD_GROUPID+" INTEGER,"+
					 TABLE_RATE_FIELD_USERID+" INTEGER,"+
					 TABLE_RATE_FIELD_NRATE+" INTEGER,"+
					 TABLE_RATE_FIELD_ITEMID+" INTEGER,"+
					 "PRIMARY KEY ("+TABLE_RATE_FIELD_GROUPID+","+TABLE_RATE_FIELD_USERID+","+TABLE_RATE_FIELD_ITEMID+"))");
					 
		
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int old_version, int new_version) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_USER+";");
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_GROUP+";");
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_USERGROUP+";");
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_ITEM+";");
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_RATE);
		onCreate(db);
	}
	
	public static boolean put(SQLiteDatabase db,String table, String[] fields, String[] values){
		try{
			ContentValues cv = new ContentValues();
		
			for(int i=0; i<fields.length;i++){
				cv.put(fields[i], values[i]);
			}
			db.insert(table, TABLE_USER_FIELD_EMAIL, cv);
			return true;
		}
		catch (Exception e) {
			return false;
		}
		
		
	}

	public File getCSVTable(SQLiteDatabase db,String table, String[] fields, Context c){
		
		try{
			File f = new File(c.getFilesDir(),"data.csv");
		
			OutputStreamWriter outw = new OutputStreamWriter(new FileOutputStream(f));
			Cursor cursor = db.query(table, fields, null, null, null, null, null);
			String data = "";
			for(cursor.moveToFirst(); cursor.getPosition()<cursor.getCount();cursor.moveToNext()){
				int i=0;
				for(i=0; i<fields.length-1;i++){
					data+= cursor.getString(cursor.getColumnIndex(fields[i]))+",";
				}
				data+=cursor.getString(cursor.getColumnIndex(fields[i]))+"\n";
			}
			outw.write(data);
			outw.close();
			return f;
		}
		catch (Exception e) {
			return null;
		}
		
	}
	
	
	public static Cursor get(SQLiteDatabase db, String table, String columns,String[] whereClause ){
		
		Cursor data = db.rawQuery("SELECT " + columns +
				" FROM "+table, new String[]{});
		return data;
	}
	
	

}
