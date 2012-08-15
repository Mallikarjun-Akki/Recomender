package com.android.recomender.beans;

import java.util.HashMap;

/**
 * Clase que se encarga de administrar los atributos un grupo
 * en particular
 * @author lmarquez
 *
 */
public class GroupBean extends ObjectBean{
	
	public static String KEY_ID = "group_id";
	public static String KEY_NAME= "group_name";
	public static String KEY_DESCRIPTION = "group_descripction";
	
	/**
	 * Constructor
	 * @param d HashMap que contiene los datos del grupo
	 */
	public GroupBean(HashMap d) {
		super(d);
	}

	/**
	 * retorna el nombre del grupo
	 * @return nombre de grupo
	 */
	public String getName() {
		// TODO Auto-generated method stub
		return (String) _data.get(KEY_NAME);
	}

	/**
	 * retorna la descripcion que posee el grupo
	 * @return descripcion del grupo
	 */
	public String getDescription() {
		// TODO Auto-generated method stub
		return (String) _data.get(KEY_DESCRIPTION);
	}

	/**
	 * Retorna el id unico del grupo
	 * @return id del grupo
	 */
	public Integer getID() {
		String a = (String)_data.get(KEY_ID);
		return new Integer(Integer.parseInt(a));
	}

}
