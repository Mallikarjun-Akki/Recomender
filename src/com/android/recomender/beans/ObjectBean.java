package com.android.recomender.beans;

import java.util.HashMap;
/**
 * Clase que administra los metodos de un objeto en particular
 * @author lmarquez
 *
 */
public  class ObjectBean {
	
	/**
	 * @uml.property  name="_data"
	 * @uml.associationEnd  qualifier="KEY_NAME:java.lang.String java.lang.String"
	 */
	HashMap _data;
	
	public ObjectBean(HashMap d) {
		_data = d;
	}
	
	
}
