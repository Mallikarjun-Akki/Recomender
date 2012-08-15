package com.android.recomender.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataSource {
	
	/**
	 * @uml.property  name="database"
	 * @uml.associationEnd  
	 */
	private SQLiteDatabase database;
	/**
	 * @uml.property  name="helper"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private RecomenderDB helper;
	
	public DataSource(Context c){
		helper = new RecomenderDB(c);
	}
	
	public void open(){
		database = helper.getWritableDatabase();
	}
	
	

}
