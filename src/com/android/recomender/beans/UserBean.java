package com.android.recomender.beans;

import java.util.HashMap;

/**
 * 
 * @author lmarquez
 *
 */
public class UserBean extends ObjectBean{

	public static final String KEY_ID = "user_id";

	public static final Object KEY_NAME = "user_name";

	public static String KEY_EMAIL = "user_email";	
	
	
	
	/**
	 * 
	 * @param d Data of User
	 */
	public UserBean(HashMap d) {
		super(d);
	}

	/**
	 * 
	 * @return User Id
	 */
	public Integer getID() {
		return (Integer) _data.get(KEY_ID);
	}

	/**
	 * 
	 * @return User Name
	 */
	public String getName() {
		return (String)_data.get(KEY_NAME);
	}

	public CharSequence getEmail() {
		return (String)_data.get(KEY_EMAIL);
	}

}
