package com.android.recomender.adapters;

import java.util.Arrays;

import com.android.recomender.R;
import com.android.recomender.R.id;
import com.android.recomender.R.layout;
import com.android.recomender.beans.GroupBean;
import com.android.recomender.beans.ItemBean;
import com.android.recomender.classes.ServerService;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ItemsArrayAdapter extends ArrayAdapter<Integer> {

	/**
	 * @uml.property  name="vals" multiplicity="(0 -1)" dimension="1"
	 */
	Integer[] vals;
	/**
	 * @uml.property  name="recomendaitons" multiplicity="(0 -1)" dimension="1"
	 */
	Long[] recomendaitons;
	
	public ItemsArrayAdapter(Context context, Integer[] items ){
		super(context,R.layout.item_row,items);
		vals = items;
	}
	
	public void setRecomendations(Long[] rec){
		recomendaitons = rec;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemRowView = inflater.inflate(R.layout.item_row, parent,false);
		ItemBean itemObj = ServerService.getItemByID(super.getContext(),new Long(vals[position]));
		TextView title = (TextView) itemRowView.findViewById(R.id.item_row_title);
		TextView description = (TextView) itemRowView.findViewById(R.id.item_row_description);
		title.setText(itemObj.getName());
		description.setText(itemObj.getShortDescription()); 
		
		
		return itemRowView;
		
	}
}
