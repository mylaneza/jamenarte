package com.mylaneza.jamarte;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.adapters.AdaptadorLecciones;

import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Leccion;

import com.mylaneza.jamarte.forms.NewLesson;



public class Lecciones extends AppCompatActivity implements AdapterView.OnItemClickListener {


    ListView list;
    Leccion[] lecciones;
    //ActivityResultLauncher<Intent> arl;

    String query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecciones);
        list = findViewById(R.id.listLecciones);
        SharedPreferences sp = getSharedPreferences("com.mylaneza.jamarte.PREFERENCIAS",MODE_PRIVATE);
        query = sp.getString("com.mylaneza.jamarte.SP_LECCION_ESCUELA",null);

        DBHelper db = new DBHelper(this);
        if(query == null)
            lecciones = db.getLecciones();
        else if(!query.equals("Todos"))
            lecciones = db.getLeccionesDeEscuela(query);
        else
            lecciones = db.getLecciones();
        list.setAdapter(new AdaptadorLecciones(this,lecciones));
        list.setOnItemClickListener(this);

        /* Activity Register */
         /*arl = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();

                        }
                    }
                });*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();
        /*if( itemId == R.id.item_new ){
            startActivityForResult(new Intent(this, NewLesson.class),0);
            return true;
        }*/ if(itemId == R.id.item_query){
            Intent i = new Intent(this,QueryAct.class);
            i.putExtra("com.mylaneza.jamarte.PARENT",2);
            startActivityForResult(i,1);
            //arl.launch(i);
            return true;
        }else
            return false;
    }

    public void openNewLesson(View v){
        startActivityForResult(new Intent(this, NewLesson.class),0);
        //arl.launch(new Intent(this, NewLesson.class));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Leccion lesson = lecciones[i];
        Intent intent = new Intent(this, NewLesson.class);
        intent.putExtra("com.mylaneza.jamarte.ID",lesson.id);
        startActivityForResult( intent,0);
        //arl.launch(intent);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK){
            if(requestCode == 0) { //Opcion de cambio de escuela
                DBHelper db = new DBHelper(this);
                if(query == null)
                    lecciones = db.getLecciones();
                else if(!query.equals("Todos"))
                    lecciones = db.getLeccionesDeEscuela(query);
                else
                    lecciones = db.getLecciones();
                AdaptadorLecciones ap = (AdaptadorLecciones) list.getAdapter();
                ap.lecciones = lecciones;
                ap.notifyDataSetChanged();
            }else{
                //Regresa de nueva Leccion
                String escuela = data.getStringExtra("com.mylaneza.jamarte.OPCION");
                DBHelper db = new DBHelper(this);
                SharedPreferences sp = getSharedPreferences("com.mylaneza.jamarte.PREFERENCIAS",MODE_PRIVATE);
                SharedPreferences.Editor  editor = sp.edit();
                editor.putString("com.mylaneza.jamarte.SP_LECCION_ESCUELA",escuela);
                editor.commit();
                if(escuela != null && !"Todos".equals(escuela)){
                    Leccion[] lecciones = db.getLeccionesDeEscuela(escuela);
                    if(lecciones.length > 0){
                        this.lecciones = lecciones;
                        AdaptadorLecciones ap = (AdaptadorLecciones) list.getAdapter();
                        ap.lecciones = this.lecciones;
                        ap.notifyDataSetChanged();
                    }else{
                        Toast.makeText(this,"No se encontraron sesiones para esa escuela.",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    this.lecciones = db.getLecciones();
                    AdaptadorLecciones ap = (AdaptadorLecciones) list.getAdapter();
                    ap.lecciones = this.lecciones;
                    ap.notifyDataSetChanged();
                }

            }
        }
    }
}
