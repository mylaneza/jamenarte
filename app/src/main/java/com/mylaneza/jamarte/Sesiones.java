package com.mylaneza.jamarte;


import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.GridView;

import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.adapters.AdaptadorSesiones;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Sesion;

import com.mylaneza.jamarte.forms.NewSesion;

public class Sesiones extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Sesion[] sesiones;
    GridView list;
    TextView monto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesiones);
        list = (GridView) findViewById(R.id.listSesiones);
        monto = (TextView) findViewById(R.id.montoTotal);
        SharedPreferences sp = getSharedPreferences("com.mylaneza.jamarte.PREFERENCIAS",MODE_PRIVATE);
        String query = sp.getString("com.mylaneza.jamarte.SP_SESION_ESCUELA",null);

        DBHelper db = new DBHelper(this);
        if(query == null ){
            sesiones = db.getSesiones();
        }else if(!query.equals("Todos") ){
            sesiones = db.getSesionesBy(query);
        }else{
            sesiones = db.getSesiones();
        }

        calculaTotal();

        list.setAdapter(new AdaptadorSesiones(this,sesiones));
        list.setOnItemClickListener(this);

    }

    private void calculaTotal(){
        int total = 0;
        for( int i = 0 ; i < sesiones.length ; i++){
            total+=(int)sesiones[i].monto;
        }
        monto.setText("Monto total $"+total);
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
            startActivityForResult(new Intent(this, NewSesion.class),0);
            return true;
        }else*/ if(itemId == R.id.item_query){
            Intent i = new Intent(this,QueryAct.class);
            i.putExtra("com.mylaneza.jamarte.PARENT",0);
            startActivityForResult(i,1);
            return true;
        }else {
            return false;
        }
    }

    public void openNewSesion(View v){
        startActivityForResult(new Intent(this, NewSesion.class),0);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Sesion p = sesiones[i];
        Intent intent = new Intent(this, NewSesion.class);
        intent.putExtra("com.mylaneza.jamarte.ID",p.id);
        startActivityForResult( intent, 0 );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK){
            DBHelper db = new DBHelper(this);
            SharedPreferences sp = getSharedPreferences("com.mylaneza.jamarte.PREFERENCIAS", MODE_PRIVATE);
            if(requestCode == 0){ //NewSession
                String query = sp.getString("com.mylaneza.jamarte.SP_SESION_ESCUELA", null);
                sesiones = db.getSesionesBy(query);
            }else{ //QueryAct
                String escuela = data.getStringExtra("com.mylaneza.jamarte.OPCION");
                SharedPreferences.Editor  editor = sp.edit();
                editor.putString("com.mylaneza.jamarte.SP_SESION_ESCUELA",escuela);
                editor.commit();
                if(escuela != null && !"Todos".equals(escuela)){
                    this.sesiones = db.getSesionesBy(escuela);
                }else{
                    this.sesiones = db.getSesiones();
                }
            }
            AdaptadorSesiones ap = (AdaptadorSesiones) list.getAdapter();
            ap.sesiones = sesiones;
            ap.notifyDataSetChanged();
            calculaTotal();
        }else{
            Log.i("RESULT CODE",""+resultCode);
        }

    }
}
