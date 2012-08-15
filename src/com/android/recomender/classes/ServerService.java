package com.android.recomender.classes;

import jGroupRecomenderImplementations.DBDataModel;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.internal.util.*;
import com.android.recomender.beans.GroupBean;
import com.android.recomender.beans.ItemBean;
import com.android.recomender.beans.UserBean;
import com.android.recomender.database.RecomenderDB;
import com.jGroupRecommender.impl.GroupRecommender;
import com.jGroupRecommender.impl.dataModel.DataModel;
import com.jGroupRecommender.impl.domain.Group;
import com.jGroupRecommender.impl.estimator.Estimator;
import com.jGroupRecommender.impl.estimator.UserCorrelationEstimator;
import com.jGroupRecommender.impl.merging.MergingGroupRecommender;
import com.jGroupRecommender.impl.similarity.CollaborativeUserCorrelation;
import com.jGroupRecommender.impl.similarity.UserCorrelation;
import com.jGroupRecommender.impl.utils.iterator.LongPrimitiveIterator;

public class ServerService {

	
	/**
	 * 
	 * @param email: user email
	 * @param pass: user password
	 * @return 
	 * 			true if valid user, else false
	 */			
	public static boolean login(Context c, String user, String pass) {
		RecomenderDB helper = new RecomenderDB(c);
		SQLiteDatabase db = helper.getWritableDatabase();
		user.trim();
		String sql = "select "+RecomenderDB.TABLE_USER_FIELD_USERID+" from "+RecomenderDB.TABLE_USER+
				" where "+RecomenderDB.TABLE_USER_FIELD_EMAIL+"=\""+user+"\" and "+RecomenderDB.TABLE_USER_FIELD_PASS+
				"="+pass;
		 Cursor cursor = db.rawQuery(sql, new String[]{});
		 boolean no_empty = cursor.moveToFirst();
		 cursor.close();
		 db.close();
		 return no_empty;
	
	}



	public static UserBean getUserByEmail(Context c,String email) {
		 
		UserBean userObj = null;
		RecomenderDB helper = new RecomenderDB(c); 
		SQLiteDatabase db = helper.getWritableDatabase();
		String[] cols =new String[]{RecomenderDB.TABLE_USER_FIELD_USERID+" AS _id ",
			 	   RecomenderDB.TABLE_USER_FIELD_EMAIL+" AS email",
			 	   RecomenderDB.TABLE_USER_FIELD_NAME+" AS name"}; 
		
		String where = RecomenderDB.TABLE_USER_FIELD_EMAIL+" = \""+email+"\"";
	    Cursor cursor = db.query(RecomenderDB.TABLE_USER, cols, where, null, null, null, null);
        if(cursor.moveToFirst()){
        	cursor.moveToFirst();
        	HashMap data = new HashMap();
    		data.put(UserBean.KEY_EMAIL, cursor.getString(cursor.getColumnIndex(RecomenderDB.TABLE_USER_FIELD_EMAIL)));
    		data.put(UserBean.KEY_ID,new Integer(cursor.getInt(cursor.getColumnIndex("_id"))));
    		data.put(UserBean.KEY_NAME,cursor.getString(cursor.getColumnIndex(RecomenderDB.TABLE_USER_FIELD_NAME)));
    		userObj = new UserBean(data);
    	}
        cursor.close();
		db.close();
		return userObj;
	}



	public static Integer[] getGroupsOfUser(Context c,int id) {

		RecomenderDB helper = new RecomenderDB(c); 
		SQLiteDatabase db = helper.getWritableDatabase();
		
		String[] cols =new String[]{RecomenderDB.TABLE_USERGROUP_FIELD_GROUPID+" AS _id "}; 
		
		String where = RecomenderDB.TABLE_USERGROUP_FIELD_USER_ID+" = "+id;
	    Cursor cursor = db.query(RecomenderDB.TABLE_USERGROUP, cols, where, null, null, null, null);
     
		Integer[] values = new Integer[cursor.getCount()];
		int counter = 0;
	    for(cursor.moveToFirst(); cursor.getPosition()<cursor.getCount(); cursor.moveToNext()){
			values[counter] = new Integer(cursor.getInt(cursor.getColumnIndex("_id")));
			counter++;
	    }
	    cursor.close();
	    db.close();
		return values;
	}



	public static GroupBean getGoupByID(Context c,Integer id) {
		
		RecomenderDB helper = new RecomenderDB(c); 
		SQLiteDatabase db = helper.getWritableDatabase();
		String[] cols =new String[]{RecomenderDB.TABLE_GROUP_FIELD_GRUOPID,
								RecomenderDB.TABLE_GROUP_FIELD_DSCRIPTION,
								RecomenderDB.TABLE_GROUP_FIELD_NAME}; 
		String where = RecomenderDB.TABLE_GROUP_FIELD_GRUOPID+" = "+id.intValue();
	    Cursor cursor = db.query(RecomenderDB.TABLE_GROUP, cols, where, null, null, null, null);
	    HashMap data = new HashMap();
	    if(cursor.moveToFirst()){
			String a =cursor.getString(cursor.getColumnIndex("_id"));
			data.put(GroupBean.KEY_ID, cursor.getString(cursor.getColumnIndex("_id")));
			a=cursor.getString(cursor.getColumnIndex(RecomenderDB.TABLE_GROUP_FIELD_NAME));
			data.put(GroupBean.KEY_NAME, cursor.getString(cursor.getColumnIndex(RecomenderDB.TABLE_GROUP_FIELD_NAME)));
			a=cursor.getString(cursor.getColumnIndex(RecomenderDB.TABLE_GROUP_FIELD_DSCRIPTION));
			data.put(GroupBean.KEY_DESCRIPTION, cursor.getString(cursor.getColumnIndex(RecomenderDB.TABLE_GROUP_FIELD_DSCRIPTION)));
	    }
	    cursor.close();
	    db.close();
		return new GroupBean(data);
	}



	public static Integer[] getItemsByGroup(Context c,Integer id) {

		RecomenderDB helper = new RecomenderDB(c); 
		SQLiteDatabase db = helper.getWritableDatabase();
		String[] cols =new String[]{RecomenderDB.TABLE_ITEM_FIELD_ITEMID}; 
		String where = RecomenderDB.TABLE_ITEM_FIELD_GROUPID+" = "+id.intValue();
	    Cursor cursor = db.query(RecomenderDB.TABLE_ITEM, cols, where, null, null, null, null);
	    int a;
		Integer[] items = new Integer[cursor.getCount()];
		for(cursor.moveToFirst(); cursor.getPosition()<cursor.getCount();cursor.moveToNext()){
			items[cursor.getPosition()] = new Integer(cursor.getInt(cursor.getColumnIndex(RecomenderDB.TABLE_ITEM_FIELD_ITEMID)));
			a = cursor.getInt(cursor.getColumnIndex(RecomenderDB.TABLE_ITEM_FIELD_ITEMID));
		}
		db.close();
		cursor.close();
		return items;
	}



public static ItemBean getItemByID(Context c,Long long1) {
		
		RecomenderDB helper = new RecomenderDB(c); 
		SQLiteDatabase db = helper.getWritableDatabase();
		String[] cols =new String[]{RecomenderDB.TABLE_ITEM_FIELD_ITEMID,
				RecomenderDB.TABLE_ITEM_FIELD_NAME,
				RecomenderDB.TABLE_ITEM_FIELD_DESCRIOTION,
				RecomenderDB.TABLE_ITEM_FIELD_SHORTDESCRIPTION,
				RecomenderDB.TABLE_ITEM_FIELD_IMGSRC}; 
		String where = RecomenderDB.TABLE_ITEM_FIELD_ITEMID+" = "+long1.intValue();
	    Cursor cursor = db.query(RecomenderDB.TABLE_ITEM, cols, where, null, null, null, null);
	    HashMap data = new HashMap();
	    if(cursor.moveToFirst()){
	    	String a =cursor.getString(cursor.getColumnIndex(RecomenderDB.TABLE_ITEM_FIELD_ITEMID));
			data.put(ItemBean.KEY_ID, cursor.getString(cursor.getColumnIndex(RecomenderDB.TABLE_ITEM_FIELD_ITEMID)));
			data.put(ItemBean.KEY_NAME, cursor.getString(cursor.getColumnIndex(RecomenderDB.TABLE_ITEM_FIELD_NAME)));
			data.put(ItemBean.KEY_DESCRIPTION, cursor.getString(cursor.getColumnIndex(RecomenderDB.TABLE_ITEM_FIELD_DESCRIOTION)));
			data.put(ItemBean.KEY_SDESCRIPTION, cursor.getString(cursor.getColumnIndex(RecomenderDB.TABLE_ITEM_FIELD_SHORTDESCRIPTION)));
			data.put(ItemBean.KEY_LOGO, cursor.getString(cursor.getColumnIndex(RecomenderDB.TABLE_ITEM_FIELD_IMGSRC)));
			
	    }
	    cursor.close();
	    db.close();
		return new ItemBean(data);
	}

	/**
	 * 
	 * @param id: Item id
	 * @return
	 * 			a number of rating of item that is a average of all ratings
	 */
	public static float getRateItemOfGroup(Context c,Integer id) {
		RecomenderDB helper = new RecomenderDB(c); 
		SQLiteDatabase db = helper.getWritableDatabase();
		String[] cols =new String[]{RecomenderDB.TABLE_RATE_FIELD_NRATE}; 
		String where = RecomenderDB.TABLE_RATE_FIELD_ITEMID+" = "+id.intValue();
	    Cursor cursor = db.query(RecomenderDB.TABLE_RATE, cols, where, null, null, null, null);
	    float avg=0;
	    int cantidad = cursor.getCount();
	    for(cursor.moveToFirst(); cursor.getPosition()<cursor.getCount();cursor.moveToNext()){
	    	avg+=cursor.getFloat(cursor.getColumnIndex(RecomenderDB.TABLE_RATE_FIELD_NRATE));
	    }
	    db.close();
	    cursor.close();
		return (avg/cantidad);
	}



	public static void RateItem(Context c,int id_user, Integer id, int rate) {
		RecomenderDB helper = new RecomenderDB(c); 
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("INSERT OR IGNORE INTO "+RecomenderDB.TABLE_RATE+"("+
					RecomenderDB.TABLE_RATE_FIELD_ITEMID+","+
					RecomenderDB.TABLE_RATE_FIELD_USERID+","+
					RecomenderDB.TABLE_RATE_FIELD_NRATE+")"+
					" VALUES ("+id.intValue()+","+
						id_user+","+
						rate+");" +
					"UPDATE "+RecomenderDB.TABLE_RATE+" SET "+
						RecomenderDB.TABLE_RATE_FIELD_NRATE+"="+rate+
						" WHERE "+RecomenderDB.TABLE_RATE_FIELD_USERID+"="+id_user);
		db.close();
	}
	
	public static int[] getRatesByIDItem(Context c, int iditem){
		RecomenderDB helper = new RecomenderDB(c);
		SQLiteDatabase db = helper.getReadableDatabase();
		
		String cols[] = new String[]{RecomenderDB.TABLE_RATE_FIELD_NRATE};
		String where = RecomenderDB.TABLE_RATE_FIELD_ITEMID+"="+iditem;
		Cursor cursor = db.query(RecomenderDB.TABLE_RATE, cols, where, null, null, null, null, null);
		
		int[] items = new int[cursor.getCount()];
		int pos =0;
		for(cursor.moveToFirst(); cursor.getPosition()<cursor.getCount();cursor.moveToNext()){
			items[pos] = cursor.getInt(cursor.getColumnIndex(RecomenderDB.TABLE_RATE_FIELD_NRATE));
			pos++;
		}
		
		cursor.close();
		db.close();
		return items;
	}
	
	/**
	 * 
	 * @param c context to get a redeable database
	 * @param users list of group users 
	 * @param id_group group id
	 * @return a list with best items rated
	 * 
	 */
	
	public static Long[] getRanking(Context c,int id_group){
		RecomenderDB helper = new RecomenderDB(c);
		SQLiteDatabase db = helper.getReadableDatabase();
	
		DataModel model;
		try {
		
			model = new DBDataModel(db,id_group);
			
			LongPrimitiveIterator iterator= model.getUserIDs(); 
			long[] users=new long[iterator.size()];
			//Create a group with the first 5 users of the data model.
			for(int i =0; i<iterator.size();i++){
				users[i]=iterator.nextLong();
			}
			Group group=new Group(23, "name", users);
			//A specific userCorrelation
			UserCorrelation userSimilarity=new CollaborativeUserCorrelation(model);
			Estimator estimator=new UserCorrelationEstimator(model, userSimilarity);
			//Choose the group recommendation technique
			GroupRecommender r=new MergingGroupRecommender(model, group, estimator);
			//Get the results (by default all the techniques gets a list with 10 recommended items)
			List<Long> list_recomendations = r.recommend();
			Long[] recomendations_array = new Long[list_recomendations.size()];
			return list_recomendations.toArray(recomendations_array);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}



	private static long[] toPrimitive(Long[] recomendations) {
		long[] rec = new long[recomendations.length];
		for(int i = 0; i<recomendations.length;i++){
			rec[i] = recomendations[i];
		}
		return rec;
	}
	
	

}
