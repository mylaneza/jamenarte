package com.mylaneza.jamarte;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mylaneza.jamarte.adapters.AdaptadorLecciones;
import com.mylaneza.jamarte.adapters.AdaptadorMiembros;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Miembro;
import com.mylaneza.jamarte.forms.NewAvance;
import com.mylaneza.jamarte.forms.NewMiembro;

public class Miembros extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Miembro[] miembros;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miembros);
        list = (ListView) findViewById(R.id.listMiembros);
        DBHelper db = new DBHelper(this);
        miembros = db.getMiembros();
        list.setAdapter( new AdaptadorMiembros(this,miembros));
        list.setOnItemClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if( item.getItemId() == R.id.item_new ){

            startActivityForResult(new Intent(this, NewMiembro.class),0);
            return true;
        }else
            return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Miembro m = miembros[i];
        Intent intent = new Intent(this, NewMiembro.class);
        intent.putExtra("com.mylaneza.jamarte.ID",m.id);
        startActivity( intent);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        DBHelper db = new DBHelper(this);
        miembros = db.getMiembros();
        AdaptadorMiembros ap = (AdaptadorMiembros) list.getAdapter();
        ap.miembros = miembros;
        ap.notifyDataSetChanged();
    }
}
