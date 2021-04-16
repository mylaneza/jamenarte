package com.mylaneza.jamarte;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mylaneza.jamarte.adapters.AdaptadorPasos;
import com.mylaneza.jamarte.adapters.AdaptadorSesiones;
import com.mylaneza.jamarte.database.DBContract;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Paso;
import com.mylaneza.jamarte.entities.Sesion;
import com.mylaneza.jamarte.forms.NewAvance;
import com.mylaneza.jamarte.forms.NewMiembro;
import com.mylaneza.jamarte.forms.NewPaso;

public class Pasos extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView list;
    Paso pasos[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasos);
        list = (ListView) findViewById(R.id.listPasos);
        DBHelper db = new DBHelper(this);
        pasos = db.getPasos();
        list.setAdapter(new AdaptadorPasos(this,pasos));
        list.setOnItemClickListener(this);
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
            startActivityForResult(new Intent(this, NewPaso.class),0);
            return true;
        }else if(itemId == R.id.item_query){
            Intent i = new Intent(this,QueryAct.class);
            i.putExtra("com.mylaneza.jamarte.PARENT",1);
            startActivityForResult(i,1);
            return true;
        }else
            return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Paso p = pasos[i];
        Intent intent = new Intent(this, NewPaso.class);
        intent.putExtra("com.mylaneza.jamarte.ID",p.id);
        startActivityForResult( intent,0);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0) {
            DBHelper db = new DBHelper(this);
            pasos = db.getPasos();
            AdaptadorPasos ap = (AdaptadorPasos) list.getAdapter();
            ap.pasos = pasos;
            ap.notifyDataSetChanged();
        }else{
            if(resultCode == RESULT_OK){
                String base = data.getStringExtra("com.mylaneza.jamarte.OPCION");
                String column = data.getStringExtra("com.mylaneza.jamarte.COLUMN");
                if(base != null && !"Todos".equals(base)){
                    DBHelper db = new DBHelper(this);
                    Paso pasos[] = db.getPasosBy(DBContract.Steps.COL_BASE,base,true);
                    if(pasos.length > 0){
                        this.pasos = pasos;
                        AdaptadorPasos ap = (AdaptadorPasos) list.getAdapter();
                        ap.pasos = this.pasos;
                        ap.notifyDataSetChanged();
                    }else{
                        Toast.makeText(this,"No se encontraron pasos de esta base.",Toast.LENGTH_SHORT).show();
                    }
                }
            }


        }
    }
}
