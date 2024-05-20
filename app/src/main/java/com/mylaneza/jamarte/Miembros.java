package com.mylaneza.jamarte;


import android.content.Intent;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.adapters.AdaptadorMiembros;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Member;

import com.mylaneza.jamarte.forms.NewMiembro;

public class Miembros extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Member[] miembros;
    ListView list;

    TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miembros);
        list = findViewById(R.id.listMiembros);
        DBHelper db = new DBHelper(this);
        miembros = db.getMiembros();
        list.setAdapter( new AdaptadorMiembros(this,miembros));
        list.setOnItemClickListener(this);
        TextView v = findViewById(R.id.miembrosTotal);
        v.setText("Total: "+miembros.length);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        /*if( item.getItemId() == R.id.item_new ){

            startActivityForResult(new Intent(this, NewMiembro.class),0);
            return true;
        }else*/
            return false;
    }

    public void openNewMiembro(View v){
        startActivityForResult(new Intent(this, NewMiembro.class),0);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Member m = miembros[i];
        Intent intent = new Intent(this, NewMiembro.class);
        intent.putExtra("com.mylaneza.jamarte.ID",m.id);
        startActivityForResult( intent, 0);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK) {
            DBHelper db = new DBHelper(this);
            miembros = db.getMiembros();
            AdaptadorMiembros ap = (AdaptadorMiembros) list.getAdapter();
            ap.miembros = miembros;
            ap.notifyDataSetChanged();
            TextView v = findViewById(R.id.miembrosTotal);
            v.setText("Total: "+miembros.length);
        }
    }
}
