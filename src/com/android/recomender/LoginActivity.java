package com.android.recomender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.android.recomender.R;
import com.android.recomender.R.id;
import com.android.recomender.R.layout;
import com.android.recomender.beans.UserBean;
import com.android.recomender.classes.ServerService;
import com.android.recomender.database.RecomenderDB;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	/**
	 * @uml.property  name="error_message"
	 * @uml.associationEnd  
	 */
	public Toast error_message;
	
	/**
	 * @uml.property  name="db"
	 * @uml.associationEnd  readOnly="true"
	 */
	SQLiteDatabase db;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.login);
		 
		 Button b_submit = (Button) findViewById(R.id.button1);
		 b_submit.setOnClickListener( new OnClickListener(){
			 
			@Override
			public void onClick(View v) {
				EditText email = (EditText) findViewById(R.id.editText1);
				boolean success = false;
				EditText pass =(EditText) findViewById(R.id.editText2);
				success = verificateData(email.getText().toString(),pass.getText().toString());
				if(success){
					success = ServerService.login(LoginActivity.this,email.getText().toString(), pass.getText().toString());
					if(success){
						Intent intent = new Intent(LoginActivity.this, GroupsUserListActivity.class);
						intent.putExtra("email", email.getText().toString());
						startActivity(intent);
					}
					else{
						error_message = Toast.makeText(LoginActivity.this, "datos incorrectos", Toast.LENGTH_SHORT);
						error_message.show();
					}
				}
				
				else
					error_message.show();
				
				
			}
			 
		 });
	}
	
	public boolean verificateData(String email, String pass){
		boolean error = false;
		String errorMsg = "";
		if(email.equals("")){
			error=true;
			errorMsg="Email: es dato obligatorio"+'\n';
		}
		Pattern pat = null;
        Matcher mat = null;        
        pat = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");
        mat = pat.matcher(email);
        if (!mat.find()) {
        	error = true;
        	errorMsg+="Email: formato invalido"+'\n';
        }
        
        if(pass.equals("")){
        	error=true;
			errorMsg+="Contraseña: es dato obligatorio"+'\n';
        }
        
        if(pass.length()<6){
        	error = true;
        	errorMsg+="Conraseña: minimo 6 caracteres"+'\n'; 
        }
        if(error){
        	error_message = Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT);
        	return false;
        }
        return true;
        
        	
	}

}
