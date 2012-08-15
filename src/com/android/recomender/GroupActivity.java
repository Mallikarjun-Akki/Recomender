/**
 * 
 */
package com.android.recomender;

import java.util.List;

import com.android.recomender.R;
import com.android.recomender.R.id;
import com.android.recomender.R.layout;
import com.android.recomender.adapters.ItemsArrayAdapter;
import com.android.recomender.beans.GroupBean;
import com.android.recomender.beans.ItemBean;
import com.android.recomender.beans.UserBean;
import com.android.recomender.classes.ServerService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * @author lmarquez
 *
 */
public class GroupActivity extends Activity {

	/**
	 * @uml.property  name="_iduser"
	 */
	int _iduser;
	/**
	 * @uml.property  name="_items" multiplicity="(0 -1)" dimension="1"
	 */
	Integer[] _items;
	/**
	 * @uml.property  name="_idgroup"
	 */
	int _idgroup;
	@Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.group);
		 
		 Bundle bundle = this.getIntent().getExtras();
		 _idgroup = bundle.getInt(GroupBean.KEY_ID);
		 final GroupBean group = ServerService.getGoupByID(this,new Integer(_idgroup));
		 _iduser = bundle.getInt(UserBean.KEY_ID);
		 String b = group.getName();
		 String a = group.getDescription();
		 Long[] rItems= ServerService.getRanking(this,_idgroup);
		 _items = ServerService.getItemsByGroup(this,group.getID());

		 ItemsArrayAdapter adapter = new ItemsArrayAdapter(this,_items);
		 adapter.setRecomendations(rItems);
		 ListView list_items = (ListView) this.findViewById(R.id.item_list);
		 list_items.setAdapter(adapter);
		 inflateRecomendeds(rItems);
		 list_items.setOnItemClickListener(new OnItemClickListener(){
			 
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				callItemActivity(_items[position], _idgroup, _iduser);
			}
			 
		 });
		 
		 
		 
	}
	
	protected void inflateRecomendeds(Long[] its){
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup container =  (ViewFlipper) findViewById(R.id.recomender_flipper);
		
		for(final Long item :  its){
			View itemRowView = inflater.inflate(R.layout.recomended_row, (ViewGroup)container,false);
			ItemBean itemObj = ServerService.getItemByID(this,item);
			TextView title = (TextView) itemRowView.findViewById(R.id.rec_title);
			String sTitle = (String) title.getText();
			sTitle = sTitle.replace("[ITEM]", itemObj.getName());
			sTitle = sTitle.replace("[DESCRIPTION]", itemObj.getShortDescription());
			title.setText(sTitle);
			container.addView(itemRowView);
			
			//modifico la imagen del logo del item recomendado
			ImageView logo = (ImageView)itemRowView.findViewById(R.id.img_recomended);
			logo.setImageResource(itemObj.getLogo());
			
			RatingBar rating_bar = (RatingBar) itemRowView.findViewById(R.id.ratingBar_item);
			rating_bar.setRating(ServerService.getRateItemOfGroup(this, new Integer(item.intValue())));
			itemRowView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					callItemActivity(item.intValue(), _idgroup, _iduser);
				}
			});
			
			
		}	
	}
	
	
	
	protected void callItemActivity(int item_id,int group_id ,int user_id){
		Intent intent = new Intent(GroupActivity.this,ItemViewActivity.class);
		intent.putExtra(ItemBean.KEY_ID, item_id);
		intent.putExtra(GroupBean.KEY_ID, group_id);
		intent.putExtra(UserBean.KEY_ID,user_id);
		startActivity(intent);
	}
	

}
