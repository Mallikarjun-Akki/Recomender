package com.android.recomender;

import com.android.recomender.R;
import com.android.recomender.R.id;
import com.android.recomender.R.layout;
import com.android.recomender.beans.GroupBean;
import com.android.recomender.beans.ItemBean;
import com.android.recomender.beans.UserBean;
import com.android.recomender.classes.ServerService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class ItemViewActivity extends Activity {
	
	/**
	 * @uml.property  name="id_user"
	 */
	int id_user;
	/**
	 * @uml.property  name="rateItemDialog"
	 * @uml.associationEnd  
	 */
	AlertDialog rateItemDialog;
	/**
	 * @uml.property  name="itemObj"
	 * @uml.associationEnd  
	 */
	ItemBean itemObj;
	/**
	 * @uml.property  name="rating_bar"
	 * @uml.associationEnd  
	 */
	RatingBar rating_bar;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_view);
		
		Bundle bundle = this.getIntent().getExtras();
		itemObj = ServerService.getItemByID(this,new Long(bundle.getInt(ItemBean.KEY_ID)));
		id_user = bundle.getInt(UserBean.KEY_ID);
		TextView title = (TextView)findViewById(R.id.title_item_view);
		TextView description = (TextView) findViewById(R.id.item_description);
		description.setText(itemObj.getDescription());
		title.setText(itemObj.getName());
		Log.d("DEBUG","avg rate= "+ServerService.getRateItemOfGroup(ItemViewActivity.this,itemObj.getID()));
		
		rating_bar = (RatingBar) findViewById(R.id.ratingBar_item);
		
		 
		LinearLayout layout_rate = (LinearLayout)findViewById(R.id.rate_layout);
		
		layout_rate.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				ItemViewActivity.this.onPause();
				rateItemDialog.show();
				rating_bar.setRating(ServerService.getRateItemOfGroup(ItemViewActivity.this,itemObj.getID()));
				
				
			}});
		
		rateItemDialog = createDialog();
		
	}
	
	@Override
	public void onStart(){
		Log.d("estado", "START");
		super.onStart();
	}
	
	@Override
	public void onResume(){
		Log.d("estado", "RESUME");
		super.onResume();
		rating_bar.setRating(ServerService.getRateItemOfGroup(this,itemObj.getID()));
	}
	
	@Override
	public void onRestart(){
		Log.d("estado", "RESTART");
		super.onRestart();
	}
	
	
	@Override
	public void onPause(){
		Log.d("estado", "PAUSE");
		super.onPause();
	}
	
	
	private AlertDialog createDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Elije tu preferencia");
		String[] items = {"Lo odio","no me gusta", "Me da igual","Me gusta", "Me encanta"};
		builder.setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ServerService.RateItem(ItemViewActivity.this,id_user,itemObj.getID(),which+1);
				ItemViewActivity.this.onResume();
			}
		});
		return builder.create();
	}

}
