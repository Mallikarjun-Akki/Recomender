package com.android.recomender.beans;

import java.util.HashMap;

/**
 * Clase que se encarga de administrar los atributos un item
 * @author lmarquez
 *
 */
public class ItemBean extends ObjectBean{
	

	
	public static final Object KEY_SDESCRIPTION = "short_description";
	public static String KEY_ID = "item_id";
	public static String KEY_NAME= "item_name";
	public static String KEY_DESCRIPTION = "item_descripction";
	public static String KEY_LOGO = "logo";


	/**
	 * constructor
	 * @param d datos del item
	 */
	public ItemBean(HashMap d) {
		super(d);
	}
	
	/**
	 * Retorna el nombre del item
	 * @return nombre del item
	 */
	public String getName() {
		return (String) _data.get(KEY_NAME);
	}

	/**
	 * Retorna el la descripcion larga del item 
	 * @return Descripcion del item
	 */
	public String getDescription() {
		return (String) _data.get(KEY_DESCRIPTION) ;
	}

	/**
	 * Retorna una descripcion mas corta del item 
	 * @return descripcion corta del item
	 */
	public String getShortDescription() {
		return (String)_data.get(KEY_SDESCRIPTION);
	}

	/**
	 * Retorna el ID del item
	 * @return id del item
	 */
	public Integer getID() {
		return Integer.parseInt((String)_data.get(KEY_ID));
	}

	/**
	 * Retorna un entero que representa la direccion logica de la imagen
	 * @return direccion logica de la imagen
	 */
	public int getLogo() {	
		return Integer.parseInt((String)_data.get(KEY_LOGO));
	}

}
