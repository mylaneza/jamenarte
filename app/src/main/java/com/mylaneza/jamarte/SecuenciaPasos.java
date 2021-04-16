package com.mylaneza.jamarte;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mylaneza.jamarte.adapters.AdaptadorPasos;
import com.mylaneza.jamarte.adapters.AdaptadorSecuenciaPaso;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Paso;
import com.mylaneza.jamarte.entities.Secuencia;
import com.mylaneza.jamarte.entities.SecuenciaPaso;
import com.mylaneza.jamarte.forms.NewSecuencia;
import com.mylaneza.jamarte.forms.NewSecuenciaPaso;

import java.util.Vector;


public class SecuenciaPasos extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView sp;
    long id;

    SecuenciaPaso pasos[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secuencia_pasos);
        sp = (ListView) findViewById(R.id.listSecuenciaPaso);
        id = getIntent().getLongExtra("com.mylaneza.jamarte.ID",-1);

        DBHelper db = new DBHelper(this);
        pasos = db.getPasosDeSecuencia(id);
        sp.setAdapter(new AdaptadorSecuenciaPaso(this,pasos));
        sp.setOnItemClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if( item.getItemId() == R.id.item_new ){
            Intent intent = new Intent(this, NewSecuenciaPaso.class);
            intent.putExtra("com.mylaneza.jamarte.SECUENCIA", id);
            startActivityForResult(intent,0);
            return true;
        }else
            return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, NewSecuenciaPaso.class);
        intent.putExtra("com.mylaneza.jamarte.ID", pasos[i].id);
        startActivityForResult(intent,0);
    }
}
