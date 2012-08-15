package com.android.recomender.adapters;

import com.android.recomender.R;
import com.android.recomender.R.id;
import com.android.recomender.R.layout;
import com.android.recomender.beans.GroupBean;
import com.android.recomender.classes.ServerService;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GroupsArrayAdapter extends ArrayAdapter<Integer>{

	/**
	 * @uml.property  name="vals" multiplicity="(0 -1)" dimension="1"
	 */
	Integer[] vals;
	
	public GroupsArrayAdapter(Context context, Integer[] values ){
		super(context,R.layout.group_row,values);
		vals = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View groupRowView = inflater.inflate(R.layout.group_row, parent,false);
		GroupBean groupObj = ServerService.getGoupByID(super.getContext(),vals[position]);
		TextView title = (TextView) groupRowView.findViewById(R.id.group_row_title);
		TextView description = (TextView) groupRowView.findViewById(R.id.group_row_description);
		title.setText(groupObj.getName());
		description.setText(groupObj.getDescription()); 
		
		return groupRowView;
		
	}
}
