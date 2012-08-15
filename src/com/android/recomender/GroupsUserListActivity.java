package com.android.recomender;

import com.android.recomender.R;
import com.android.recomender.R.id;
import com.android.recomender.R.layout;
import com.android.recomender.adapters.GroupsArrayAdapter;
import com.android.recomender.beans.GroupBean;
import com.android.recomender.beans.UserBean;
import com.android.recomender.classes.ServerService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GroupsUserListActivity extends Activity {
	
	/**
	 * @uml.property  name="groups"
	 */
	Integer[] groups;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.groups_list);
		 Bundle bundle = this.getIntent().getExtras();
		 String email = bundle.getString("email");
		 final UserBean userObj = ServerService.getUserByEmail(this, email);
		 groups = ServerService.getGroupsOfUser(this,userObj.getID());
		 TextView pData = (TextView)findViewById(R.id.personal_data);
		 String name = (String)pData.getText();
		 name = name.replace("[USER]", userObj.getName());
		 name = name.replace("[EMAIL]", userObj.getEmail());
		 pData.setText(name);
		 ArrayAdapter<Integer> adapter = new GroupsArrayAdapter(this, groups);
		 ListView list_view = (ListView) findViewById(R.id.groups_list);
		 list_view.setAdapter(adapter);
		 
		 list_view.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
					
				Intent intent = new Intent(GroupsUserListActivity.this,GroupActivity.class);
				intent.putExtra(GroupBean.KEY_ID, groups[position].intValue());
				intent.putExtra(UserBean.KEY_ID,userObj.getID().intValue());
				startActivity(intent);
			}
			 
		 });
	}
	
	/**
	 * @return
	 * @uml.property  name="groups"
	 */
	public Integer[] getGroups(){
		return groups;
	}

}
