package com.mylaneza.jamarte;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mylaneza.jamarte.adapters.AdaptadorMiembros;
import com.mylaneza.jamarte.adapters.AdaptadorSesiones;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Sesion;
import com.mylaneza.jamarte.forms.NewAvance;
import com.mylaneza.jamarte.forms.NewPaso;
import com.mylaneza.jamarte.forms.NewSesion;

public class Sesiones extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Sesion sesiones[];
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
        if( itemId == R.id.item_new ){
            startActivityForResult(new Intent(this, NewSesion.class),0);
            return true;
        }else if(itemId == R.id.item_query){
            Intent i = new Intent(this,QueryAct.class);
            i.putExtra("com.mylaneza.jamarte.PARENT",0);
            startActivityForResult(i,1);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Sesion p = sesiones[i];
        Intent intent = new Intent(this, NewSesion.class);
        intent.putExtra("com.mylaneza.jamarte.ID",p.id);
        startActivity( intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0) {
            DBHelper db = new DBHelper(this);
            sesiones = db.getSesiones();
            AdaptadorSesiones ap = (AdaptadorSesiones) list.getAdapter();
            ap.sesiones = sesiones;
            ap.notifyDataSetChanged();
        }else{
            if(resultCode == RESULT_OK){
                String escuela = data.getStringExtra("com.mylaneza.jamarte.OPCION");
                DBHelper db = new DBHelper(this);
                SharedPreferences sp = getSharedPreferences("com.mylaneza.jamarte.PREFERENCIAS",MODE_PRIVATE);
                SharedPreferences.Editor  editor = sp.edit();
                editor.putString("com.mylaneza.jamarte.SP_SESION_ESCUELA",escuela);
                editor.commit();
                if(escuela != null && !"Todos".equals(escuela)){

                    Sesion sesiones[] = db.getSesionesBy(escuela);
                    if(sesiones.length > 0){
                        this.sesiones = sesiones;
                        AdaptadorSesiones ap = (AdaptadorSesiones) list.getAdapter();
                        ap.sesiones = this.sesiones;
                        ap.notifyDataSetChanged();
                    }else{
                        Toast.makeText(this,"No se encontraron sesiones para esa escuela.",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    this.sesiones = db.getSesiones();
                    AdaptadorSesiones ap = (AdaptadorSesiones) list.getAdapter();
                    ap.sesiones = this.sesiones;
                    ap.notifyDataSetChanged();
                }
                calculaTotal();
            }


        }
    }
}
