package com.android.recomender;

import com.android.recomender.R;
import com.android.recomender.R.drawable;
import com.android.recomender.R.layout;
import com.android.recomender.database.RecomenderDB;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


/**
 * Clase que se encarga de crear y poblar la base de datos en caso que no exista
 * @author lmarquez
 *
 */
public class LoadDataActivity extends Activity {
		
	/**
	 * @uml.property  name="thread"
	 */
	Thread thread;
    /**
	 * @uml.property  name="progressDialog"
	 * @uml.associationEnd  
	 */
    ProgressDialog progressDialog;
	/**
	 * @uml.property  name="helper"
	 * @uml.associationEnd  
	 */
	RecomenderDB helper;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.login);
		 helper = new RecomenderDB(this); 
		 
		 Intent intent= new Intent(this,LoginActivity.class);
		 startActivity(intent);
		 progressDialog = new ProgressDialog(this);
		 
		 progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		 progressDialog.setTitle("Loading data from database...");
		 progressDialog.show();
		 thread = new Thread(new Runnable(){
		  		 public void run(){
				     loadData();
	        	     progressDialog.dismiss();
				 }
			});
		 thread.run();
	}
	
	/**
	 * Metodo para poblar la Base de Datos
	 */
	public void loadData(){
		helper.onUpgrade(helper.getWritableDatabase(), 0, 0);
		
		 SQLiteDatabase db = helper.getWritableDatabase();
		int version = db.getVersion();
		 String[] fields = new String[3];
		 String[] values = new String[3];
		 
		 fields[0] = RecomenderDB.TABLE_USER_FIELD_EMAIL;
		 fields[1] = RecomenderDB.TABLE_USER_FIELD_NAME;
		 fields[2] = RecomenderDB.TABLE_USER_FIELD_PASS;
		 
		 values[0] = "lucas@email.com";
		 values[1] = "lucas";
		 values[2]="123456";
		 
		 RecomenderDB.put(db,RecomenderDB.TABLE_USER,fields , values);
//		 
		 
		 values[0] = "lucia@email.com";
		 values[1] = "lucia";
		 values[2]="123456";
		 RecomenderDB.put(db,RecomenderDB.TABLE_USER,fields , values);
		 
		 values[0] = "marcos@email.com";
		 values[1] = "marcos";
		 values[2]="123456";
		 RecomenderDB.put(db,RecomenderDB.TABLE_USER,fields , values);
//		 
		 values[0] = "martin@email.com";
		 values[1] = "martin";
		 values[2]="123456";
		 RecomenderDB.put(db,RecomenderDB.TABLE_USER,fields , values);
//		 
		 values[0] = "carla@email.com";
		 values[1] = "carla";
		 values[2]="123456";
		 RecomenderDB.put(db,RecomenderDB.TABLE_USER,fields , values);
//		 
		 values[0] = "ezequiel@email.com";
		 values[1] = "ezequiel";
		 values[2]="123456";
//		 
		 RecomenderDB.put(db,RecomenderDB.TABLE_USER,fields , values);
		 values[0] = "mariela@email.com";
		 values[1] = "mariela";
		 values[2]="123456";
		 RecomenderDB.put(db,RecomenderDB.TABLE_USER,fields , values);
//		 
		 values[0] = "julian@email.com";
		 values[1] = "julian";
		 values[2]="123456";
		 RecomenderDB.put(db,RecomenderDB.TABLE_USER,fields , values);
//		 
		 values[0] = "Luis@email.com";
		 values[1] = "Luis";
		 values[2]="123456";
		 RecomenderDB.put(db,RecomenderDB.TABLE_USER,fields , values);
//      END LOAD USERS
		 
		 
		 
//		 LOAD GROUPS
		 
		 fields = new String[2];
		 values = new String[2];
		 
		 fields[1] = RecomenderDB.TABLE_GROUP_FIELD_DSCRIPTION;
		 fields[0] = RecomenderDB.TABLE_GROUP_FIELD_NAME;

		 values[0] = "Restaurantes";
		 values[1] = "los mejores restaurantes de la ciudad de Mar del Plata";
		 RecomenderDB.put(db,RecomenderDB.TABLE_GROUP,fields , values);
//      
		 values[0] = "Peliculas";
		 values[1] = "elegi las peliculas que mas te gusten";
		 RecomenderDB.put(db,RecomenderDB.TABLE_GROUP,fields , values);
//      	
		 
		 values[0] = "viajes";
		 values[1] = "Busca el mejor viaje para vos y tus amigos";
		 RecomenderDB.put(db,RecomenderDB.TABLE_GROUP,fields , values);
//      
//		 FIN DE CARGA DE GRUPOS
//		 INICIO DE CARGA DE GROUPUSER
		 
		 fields = new String[2];
		 values = new String[2];

		 fields[0]=RecomenderDB.TABLE_USERGROUP_FIELD_GROUPID;
		 fields[1]=RecomenderDB.TABLE_USERGROUP_FIELD_USER_ID;
		 
		 for(int i=1; i<=10;i++){
			 for(int j=1; j<=3; j++){
				 values[0] = j+"";
		 		 values[1] = i+"";
		 		 RecomenderDB.put(db, RecomenderDB.TABLE_USERGROUP, fields, values);
			 }
		 }
		 
//		 FIN CARGA GROUPUSER
//		 INICIO CARGA ITEMS
		 fields = new String[5];
		 fields[0] = RecomenderDB.TABLE_ITEM_FIELD_GROUPID;
		 fields[1] = RecomenderDB.TABLE_ITEM_FIELD_NAME;
		 fields[2] = RecomenderDB.TABLE_ITEM_FIELD_DESCRIOTION;
		 fields[3] = RecomenderDB.TABLE_ITEM_FIELD_SHORTDESCRIPTION;
		 fields[4] = RecomenderDB.TABLE_ITEM_FIELD_IMGSRC;
		 values = new String[5];
		 values[0] = "1";
		 values[1] = "La pulperia";
		 values[2] = "Nuestro restaurante en Tandil, Restó María, se sitúa dentro del complejo de Cabañas y Spa y es un espacio para degustar de exquisitos platos caseros en cualquier época del año. Su capacidad para sólo 35 personas genera un ambiente íntimo y confortable, que invita a la charla placentera entre amigos y familiares. Se encuentra abierto, todos los días, mediodía y noche y pueden hacer uso del mismo, tanto las personas que se hospedan en el complejo como las que solo desean disfrutar de nuestras deliciosas comidas. Le invitamos a deleitarse cualquiera de nuestros platos de: Carnes rojas: • Lomo con hongos con papas rústicas • Miñones de lomo con puré especiado • Bondiola de cerdo a la mostaza y miel con puré de batata y nuez• Otros Carnes blancas: • Posta de salmón con rúcula • Parmesano y tomates secos, salteado de pollo con vegetales  • Pechuguitas con crema de hongos• Otros,Pastas caseras, Variedad de ensaladas Postres caseros.  Restó María, también se encuentra disponible para la realización de eventos sociales y empresariales.";
		 values[3] = "Parrilla al mejor estilo gauchesco";
		 values[4] = R.drawable.pulperia+"";
		 RecomenderDB.put(db, RecomenderDB.TABLE_ITEM, fields, values);
		 
		 values[0] = "1";
		 values[1] = "Brisas serranas";
		 values[2] = "Nuestro restaurante en Tandil, Restó María, se sitúa dentro del complejo de Cabañas y Spa y es un espacio para degustar de exquisitos platos caseros en cualquier época del año. Su capacidad para sólo 35 personas genera un ambiente íntimo y confortable, que invita a la charla placentera entre amigos y familiares. Se encuentra abierto, todos los días, mediodía y noche y pueden hacer uso del mismo, tanto las personas que se hospedan en el complejo como las que solo desean disfrutar de nuestras deliciosas comidas. Le invitamos a deleitarse cualquiera de nuestros platos de: Carnes rojas: • Lomo con hongos con papas rústicas • Miñones de lomo con puré especiado • Bondiola de cerdo a la mostaza y miel con puré de batata y nuez• Otros Carnes blancas: • Posta de salmón con rúcula • Parmesano y tomates secos, salteado de pollo con vegetales  • Pechuguitas con crema de hongos• Otros,Pastas caseras, Variedad de ensaladas Postres caseros.  Restó María, también se encuentra disponible para la realización de eventos sociales y empresariales.";
		 values[3] = "Cocina Gourmet personalizada y exclusiva";
		 values[4] =  R.drawable.restaurant+"";
		 RecomenderDB.put(db, RecomenderDB.TABLE_ITEM, fields, values);
		 
		 values[0] = "1";
		 values[1] = "La Casa de la Reserva";
		 values[2] = "En este café nos proponemos construir un espacio integral, que reúna a quienes vienen a practicar la arquería, a visitar la reserva o simplemente a compartir una merienda; es esa pluralidad justamente la que evoca lo que para nosotros siempre fue la casa de la chacra";
		 values[3] = "#Descripcion";
		 values[4] = R.drawable.restaurant+"";
		 RecomenderDB.put(db, RecomenderDB.TABLE_ITEM, fields, values);
		 
		 values[0] = "1";
		 values[1] = "Ulises Resto";
		 values[2] = "En este café nos proponemos construir un espacio integral, que reúna a quienes vienen a practicar la arquería, a visitar la reserva o simplemente a compartir una merienda; es esa pluralidad justamente la que evoca lo que para nosotros siempre fue la casa de la chacra";
		 values[3] = "Cocina a nivel internacional";
		 values[4] =  R.drawable.restaurant+"";
		 RecomenderDB.put(db, RecomenderDB.TABLE_ITEM, fields, values);
		 
		 values[0] = "1";
		 values[1] = "Cangrejo";
		 values[2] = "La mejor cocina marina \n Mariscos, paella y otras exquiciteses";
		 values[3] = "Cocina marina";
		 values[4] = R.drawable.cangrejo+"";
		 RecomenderDB.put(db, RecomenderDB.TABLE_ITEM, fields, values);
		 
		 values[0] = "1";
		 values[1] = "Carajo";
		 values[2] = "Restaurante, parrilla";
		 values[3] = "Restaurante, parrilla";
		 values[4] = R.drawable.carajo+"";
		 RecomenderDB.put(db, RecomenderDB.TABLE_ITEM, fields, values);
		 
		 values[0] = "1";
		 values[1] = "Don Jose";
		 values[2] = "restaurante-Pizzas-Empanadas";
		 values[3] = "restaurante-Pizzas-Empanadas";
		 values[4] = R.drawable.don_jose+"";
		 RecomenderDB.put(db, RecomenderDB.TABLE_ITEM, fields, values);

		 values[0] = "1";
		 values[1] = "El Tebol";
		 values[2] = "Un lugar tradicional de Tandil, desde hace muchos años, atendido por su dueño, donde podrá disfrutar de riquisimas carnes vacunas, de cerdo o cordero, con el sabor y el toque criollo de nuestros asadores especializados." +
		 		"	  El Trébol esta ubicada en la esquina de Mitre y 14 de Julio , con lugar para estacionar, cómodamente, frente mismo al local.y disfrutar de nuestro asador criollo, empanadas, ensaladas, fritas.";
		 values[3] = "Parrilla";
		 values[4] = R.drawable.el_trebol+"";
		 RecomenderDB.put(db, RecomenderDB.TABLE_ITEM, fields, values);
		 
		 values[0] = "1";
		 values[1] = "Frawens";
		 values[2] = "Un lugar para pasar buenos momentos acompañado de una cafeteria especial y buenos platos";
		 values[3] = "Restaurant & Drinks";
		 values[4] = R.drawable.frawens+"";
		 RecomenderDB.put(db, RecomenderDB.TABLE_ITEM, fields, values);
		 
		 values[0] = "1";
		 values[1] = "La Barraca";
		 values[2] = "La Barraca es un restaurante ubicado en una de las principales avenidas de la ciudad de Tandil. Éste se especializa en carnes a la parrilla, aunque también se destacan sus pastas, pescados y mariscos. Además ofrece una amplia carta de vinos. El local cuenta con una decoración rústica, de ladrillo visto y mesas típicas de madera con un estilo campestre. Se puede llegar hasta quí en la línea de ómnibus número 500";
		 values[3] = "Restaurant";
		 values[4] = R.drawable.la_barraca+"";
		 RecomenderDB.put(db, RecomenderDB.TABLE_ITEM, fields, values);
		 
		 values[0] = "1";
		 values[1] = "La Roca";
		 values[2] = "Sin Descripcion disponible";
		 values[3] = "Restaurant";
		 values[4] = R.drawable.la_roca+"";
		 RecomenderDB.put(db, RecomenderDB.TABLE_ITEM, fields, values);
		 
		 values[0] = "1";
		 values[1] = "La Rueda";
		 values[2] = "Ubicada en el acceso principal de Tandil la Parrilla Parador La Rueda ofrece un autentico asador criollo con años de experiencia gastronómica en la ciudad de Tandil. Cómodos estacionamientos, juegos para niños, la excelente atención de sus dueños y una carne elegida entre la que se pueden encontrar una variedad de cortes al punto justo.";
		 values[3] = "Casa de comidas, parrilla y parador";
		 values[4] = R.drawable.la_rueda+"";
		 RecomenderDB.put(db, RecomenderDB.TABLE_ITEM, fields, values);
		 
		 values[0] = "1";
		 values[1] = "Pizzuela";
		 values[2] = "Taberna dedicada a la elaboracion de pizzas con los mas exquicitos ingredientes";
		 values[3] = "pizzeria-Taberna";
		 values[4] = R.drawable.pizzuela+"";
		 RecomenderDB.put(db, RecomenderDB.TABLE_ITEM, fields, values);
		 
		 values[0] = "1";
		 values[1] = "Sukalde";
		 values[2] = "En esta taberna nos proponemos a brindarle el mejor servicio acompañado de una excelente cocina etnica como es la cocina vasca";
		 values[3] = "Restaurant vasco";
		 values[4] = R.drawable.sukalde+"";
		 RecomenderDB.put(db, RecomenderDB.TABLE_ITEM, fields, values);
		 
//		 Fin Carga Items
//		 INICIO CARGA VOTACIONES
		 
		 fields = new String[4];
		 fields[0] = RecomenderDB.TABLE_RATE_FIELD_GROUPID;
		 fields[1] = RecomenderDB.TABLE_RATE_FIELD_USERID;
		 fields[2] = RecomenderDB.TABLE_RATE_FIELD_NRATE;
		 fields[3] = RecomenderDB.TABLE_RATE_FIELD_ITEMID;
		 values = new String[4];
		 for(int j=1;j<=10;j++){//se cargan los usuarios
			 values[0] = "1";
			 values[1] = j+"";
			 for(int k=1;k<=14;k++){
				 values[2] = ((int) (Math.random()*5))+"";
				 values[3] = k+"";
				 RecomenderDB.put(db, RecomenderDB.TABLE_RATE, fields, values);
			 }
		 }
		 
		 
		 
		 db.close();
	
	}
	
	    
	
	     
	
	
}
