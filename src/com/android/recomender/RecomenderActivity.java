package com.android.recomender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.android.recomender.R;
import com.android.recomender.R.id;
import com.android.recomender.R.layout;
import com.jGroupRecommender.impl.GroupRecommender;
import com.jGroupRecommender.impl.dataModel.DataModel;
import com.jGroupRecommender.impl.dataModel.FileDataModel;
import com.jGroupRecommender.impl.domain.Group;
import com.jGroupRecommender.impl.estimator.Estimator;
import com.jGroupRecommender.impl.estimator.UserCorrelationEstimator;
import com.jGroupRecommender.impl.merging.MergingGroupRecommender;
import com.jGroupRecommender.impl.similarity.CollaborativeUserCorrelation;
import com.jGroupRecommender.impl.similarity.UserCorrelation;
import com.jGroupRecommender.impl.utils.iterator.LongPrimitiveIterator;

import android.app.Activity;
import android.content.res.AssetManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RecomenderActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		Button b1 = (Button)findViewById(R.id.button1);
		b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				exec();
			}

			private void exec() {
				DataModel model;
				try {
				
				File f = RecomenderActivity.this.getFile("aac");
				String a = f.toString();
				
				 model = new FileDataModel(f);
					long[] users=new long[5];
					LongPrimitiveIterator iterator=model.getUserIDs(); 
					//Create a group with the first 5 users of the data model.
					users[0]=iterator.nextLong();
					users[1]=iterator.nextLong();
					users[2]=iterator.nextLong();
					users[3]=iterator.nextLong();
					Group group=new Group(23, "name", users);
					//A specific userCorrelation
					UserCorrelation userSimilarity=new CollaborativeUserCorrelation(model);
					//A specific Estimator
					Estimator estimator=new UserCorrelationEstimator(model, userSimilarity);
					//Choose the group recommendation technique
					GroupRecommender r=new MergingGroupRecommender(model, group, estimator);
					//Get the results (by default all the techniques gets a list with 10 recommended items)
					Log.d("Al inicio", r.recommend().toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
	}
	
	public File getFile(String name){
		File f = RecomenderActivity.this.getFilesDir();
		boolean exists = false;
		if(f.exists()){
			File[] files = f.listFiles();
			for(int i=0; i<files.length; i++){
				if(files[i].getName().equals(name))
					exists=true;
			}
		}
		
//		se crea el directorio en memoria interna para la aplicacion
		else f.mkdir();
		
//		se genera el archivo a partir de un asset que esta por default
		if(!exists)try{
			
//			Se captura el asset
			AssetManager am = RecomenderActivity.this.getAssets();
			InputStream is = am.open("Default");
//			Estto es de prueba
			int size = is.available(); 
	            byte[] buffer = new byte[size]; 
	            is.read(buffer); 
	            // Convert the buffer into a string. 
	            String text = new String(buffer); 
	            // Finally stick the string into the text view. 
	           TextView tv = (TextView)findViewById(R.id.text1); 
	            tv.setText(text); 
			
			//Aqui le dan el nombre y/o con la ruta del archivo salida
		   OutputStream salida= openFileOutput(name, RecomenderActivity.MODE_PRIVATE);
		   salida.write(buffer);
		   salida.close();
		   is.close();
		   Log.d("Al inicio", "Se realizo la conversion con exito");
		   
		  }catch(IOException e){
	
			   Log.d("Al inicio", "Se produjo error");
		  }
		return new File(f.getAbsolutePath()+"/"+name);
		}
	
}

