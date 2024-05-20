package com.mylaneza.jamarte;


import android.content.Intent;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.adapters.AdaptadorSecuencias;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Secuencia;

import com.mylaneza.jamarte.forms.NewSecuencia;

public class Secuencias extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Secuencia[] secuencias;
    ListView list;
    long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secuencias);
        list = findViewById(R.id.listSecuencias);
        DBHelper db = new DBHelper(this);
        id = getIntent().getLongExtra("com.mylaneza.jamarte.ID",-1);

        secuencias = db.getSecuencias(id);

        list.setAdapter(new AdaptadorSecuencias(this,secuencias));
        list.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        /*if( item.getItemId() == R.id.item_new ){
            Intent intent = new Intent(this, NewSecuencia.class);
            intent.putExtra("com.mylaneza.jamarte.LECCION", id);
            startActivityForResult(intent,0);
            return true;
        }else*/
            return false;
    }

    public void openNewSecuencia(View v){
        Intent intent = new Intent(this, NewSecuencia.class);
        intent.putExtra("com.mylaneza.jamarte.LECCION", id);
        startActivityForResult(intent,0);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, NewSecuencia.class);
        intent.putExtra("com.mylaneza.jamarte.ID", secuencias[i].id);
        startActivityForResult(intent,0);
        //return true;
    }
}
