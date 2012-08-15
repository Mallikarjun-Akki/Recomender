package jGroupRecomenderImplementations;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import com.android.recomender.database.*;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jGroupRecommender.impl.dataModel.DataModel;
import com.jGroupRecommender.impl.dataModel.JDBCDataModel;
import com.jGroupRecommender.impl.domain.preference.GenericItemPreferenceArray;
import com.jGroupRecommender.impl.domain.preference.GenericPreference;
import com.jGroupRecommender.impl.domain.preference.Preference;
import com.jGroupRecommender.impl.domain.preference.PreferenceArray;
import com.jGroupRecommender.impl.utils.iterator.LongPrimitiveArrayIterator;
import com.jGroupRecommender.impl.utils.iterator.LongPrimitiveIterator;
import com.jGroupRecommender.impl.utils.map.FastByIDMap;
import com.jGroupRecommender.impl.utils.map.FastIDSet;

public class DBDataModel implements DataModel {

	/**
	 * @uml.property  name="_db"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SQLiteDatabase _db;
	/**
	 * @uml.property  name="_groupid"
	 */
	private int _groupid;
	/**
	 * @uml.property  name="_where"
	 */
	private String _where;
	
	public DBDataModel(SQLiteDatabase db, int group_id){
		_db = db;
		_groupid= group_id;
		_where = RecomenderDB.TABLE_RATE_FIELD_GROUPID+" = "+group_id;
	}
	
	@Override
	public LongPrimitiveIterator getItemIDs() {
		
		Cursor cursor = _db.rawQuery("Select "+RecomenderDB.TABLE_RATE_FIELD_ITEMID+" from "+RecomenderDB.TABLE_RATE+
				" WHERE "+_where, null);
		long[] data = new long[cursor.getCount()];
		for(cursor.moveToFirst();cursor.getPosition()<cursor.getCount();cursor.moveToNext()){
			data[cursor.getPosition()] = (long)(cursor.getInt(cursor.getColumnIndex(RecomenderDB.TABLE_RATE_FIELD_ITEMID))); 
		}
		LongPrimitiveIterator iterator = new LongPrimitiveArrayIterator(data);
		cursor.close();
		return iterator;
	}

	@Override
	public FastIDSet getItemIDsFromUser(long arg0) {
		Cursor cursor = _db.rawQuery("Select "+RecomenderDB.TABLE_RATE_FIELD_ITEMID+" from "+RecomenderDB.TABLE_RATE+
									" WHERE "+RecomenderDB.TABLE_RATE_FIELD_USERID+"="+arg0+" AND "+_where, null);
		
		long[] data = new long[cursor.getCount()];
		for(cursor.moveToFirst();cursor.getPosition()<cursor.getCount();cursor.moveToNext()){
			data[cursor.getPosition()] = (long)(cursor.getInt(cursor.getColumnIndex(RecomenderDB.TABLE_RATE_FIELD_ITEMID))); 
		}
		FastIDSet idSet = new FastIDSet(data);
		cursor.close();
		return idSet;
	}

	@Override
	public double getMaxPreference() {
		Cursor cursor = _db.rawQuery("Select max("+RecomenderDB.TABLE_RATE_FIELD_NRATE+") as max_rate from "+RecomenderDB.TABLE_RATE+
				" WHERE "+_where, null);
		if(cursor.moveToFirst()){
			int r = cursor.getInt(cursor.getColumnIndex("max_rate"));
			cursor.close();
			return r;
		}
		cursor.close();
		return 0;
	}

	@Override
	public double getMinPreference() {
		Cursor cursor = _db.rawQuery("Select min("+RecomenderDB.TABLE_RATE_FIELD_NRATE+") as min_rate from "+RecomenderDB.TABLE_RATE+
				" WHERE "+_where, null);
		if(cursor.moveToFirst()){
			int r =cursor.getInt(cursor.getColumnIndex("min_rate"));
			cursor.close();
			return r;
		}
		cursor.close();
		return 0;
	}

	@Override
	public int getNumItems() {
		Cursor cursor = _db.rawQuery("Select count(distinct "+RecomenderDB.TABLE_RATE_FIELD_NRATE+") as count_items from "+RecomenderDB.TABLE_RATE+
				" WHERE "+_where, null);
		if(cursor.moveToFirst()){
			int r =cursor.getInt(cursor.getColumnIndex("count_items"));
			cursor.close(); 
			return r;
		}
		cursor.close();
		return 0;
	}

	@Override
	public int getNumUsers() {
		Cursor cursor = _db.rawQuery("Select count(distinct "+RecomenderDB.TABLE_RATE_FIELD_USERID+") as count_users from "+RecomenderDB.TABLE_RATE+
				" WHERE "+_where, null);
		if(cursor.moveToFirst()){
			int r =cursor.getInt(cursor.getColumnIndex("count_users"));
			cursor.close(); 
			return r;
		}
		cursor.close();
		return 0;
	}

	@Override
	public int getNumUsersWithPreferenceFor(long... arg0) {
		String where ="0";
		for(int i=0; i<arg0.length;i++){
			where +=" OR "+RecomenderDB.TABLE_RATE_FIELD_ITEMID+"="+arg0[i];
		}
		Cursor cursor = _db.rawQuery("Select count(distinct "+RecomenderDB.TABLE_RATE_FIELD_USERID+") as count_itemswpref from "+RecomenderDB.TABLE_RATE+
					"( where "+where+" )AND "+_where, null);
		if(cursor.moveToFirst()){
			int r =cursor.getInt(cursor.getColumnIndex("count_itemswpref"));
			cursor.close(); 
			return r;
		}
		cursor.close();
		return 0;
	}

	@Override
	public Preference getPreference(long user, long item) {
		Cursor cursor = _db.rawQuery("Select "+RecomenderDB.TABLE_RATE_FIELD_NRATE+" from "+RecomenderDB.TABLE_RATE+
				" where "+RecomenderDB.TABLE_RATE_FIELD_ITEMID+"="+item+" AND "+
							RecomenderDB.TABLE_RATE_FIELD_USERID+"="+user+" AND "+_where, null);
		if(cursor.moveToFirst()){
			long value = (long) cursor.getInt(cursor.getColumnIndex(RecomenderDB.TABLE_RATE_FIELD_NRATE));
			Preference pref = new GenericPreference(user, item, value);
			cursor.close();
			return pref;
		}
		cursor.close();
		return null;
	}

	@Override
	public double getPreferenceValue(long user, long item) {
		return this.getPreference(user, item).getValue();
	}

	@Override
	public PreferenceArray getPreferencesForItem(long item) {
		Cursor cursor = _db.rawQuery("Select "+RecomenderDB.TABLE_RATE_FIELD_NRATE+","+RecomenderDB.TABLE_RATE_FIELD_USERID+" from "+RecomenderDB.TABLE_RATE+
				" where "+RecomenderDB.TABLE_RATE_FIELD_ITEMID+"="+item+" AND "+_where, null);
		List<Preference> prefs = new ArrayList<Preference>();
		for(cursor.moveToFirst();cursor.getPosition()<cursor.getCount();cursor.moveToNext()){
			long value = (long) cursor.getInt(cursor.getColumnIndex(RecomenderDB.TABLE_RATE_FIELD_NRATE));
			long user = (long) cursor.getInt(cursor.getColumnIndex(RecomenderDB.TABLE_RATE_FIELD_USERID));
			prefs.add(new GenericPreference(user, item, value));
		}
		PreferenceArray preference = new GenericItemPreferenceArray(prefs);
		cursor.close();
		return preference;
	}

	@Override
	public PreferenceArray getPreferencesFromUser(long user) {
		Cursor cursor = _db.rawQuery("Select "+RecomenderDB.TABLE_RATE_FIELD_NRATE+","+RecomenderDB.TABLE_RATE_FIELD_ITEMID+" from "+RecomenderDB.TABLE_RATE+
				" where "+RecomenderDB.TABLE_RATE_FIELD_USERID+"="+user+" AND "+_where, null);
		List<Preference> prefs = new ArrayList<Preference>();
		for(cursor.moveToFirst();cursor.getPosition()<cursor.getCount();cursor.moveToNext()){
			long value = (long) cursor.getInt(cursor.getColumnIndex(RecomenderDB.TABLE_RATE_FIELD_NRATE));
			long item = (long) cursor.getInt(cursor.getColumnIndex(RecomenderDB.TABLE_RATE_FIELD_ITEMID));
			prefs.add(new GenericPreference(user, item, value));
		}
		PreferenceArray preference = new GenericItemPreferenceArray(prefs);
		cursor.close();
		return preference;
	}

	@Override
	public LongPrimitiveIterator getUserIDs() {
		String sql = "Select DISTINCT "+RecomenderDB.TABLE_RATE_FIELD_USERID+" from "+RecomenderDB.TABLE_RATE+
				" WHERE "+_where;
		Cursor cursor = _db.rawQuery(sql, null);
		long[] data = new long[cursor.getCount()];
		for(cursor.moveToFirst();cursor.getPosition()<cursor.getCount();cursor.moveToNext()){
			data[cursor.getPosition()] = (long)(cursor.getInt(cursor.getColumnIndex(RecomenderDB.TABLE_RATE_FIELD_USERID))); 
		}
		LongPrimitiveIterator iterator = new LongPrimitiveArrayIterator(data);
		cursor.close();
		return iterator;
	}

	@Override
	public boolean hasPreferenceValues() {
		Cursor cursor = _db.rawQuery("select * from "+RecomenderDB.TABLE_RATE+" WHERE "+_where, null);
		if(cursor.moveToFirst()){
			cursor.close();
			return true;
			
		}
		return false;
	}

	@Override
	public void removePreference(long user, long item) {
		_db.delete(RecomenderDB.TABLE_RATE, RecomenderDB.TABLE_RATE_FIELD_ITEMID+"=? AND"+
						RecomenderDB.TABLE_RATE_FIELD_USERID+"=? AND "+_where, new String[]{item+"", user+""});
	}

	@Override
	public void setPreference(long user, long item, double value) {
		ContentValues values = new ContentValues();
		values.put(RecomenderDB.TABLE_RATE_FIELD_ITEMID, item);
		values.put(RecomenderDB.TABLE_RATE_FIELD_USERID, user);
		values.put(RecomenderDB.TABLE_RATE_FIELD_NRATE, value);
		String where = RecomenderDB.TABLE_RATE_FIELD_ITEMID+"=? AND "+
				RecomenderDB.TABLE_RATE_FIELD_USERID+"=? AND "+_where;
		_db.update(RecomenderDB.TABLE_RATE,
					values, where, new String[]{item+""+user+""});
		
	}

}
