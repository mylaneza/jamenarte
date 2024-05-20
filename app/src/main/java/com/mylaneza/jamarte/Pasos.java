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

import com.mylaneza.jamarte.adapters.StepsAdapter;

import com.mylaneza.jamarte.database.DBContract;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Step;

import com.mylaneza.jamarte.forms.NewPaso;

public class Pasos extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView list;
    Step[] pasos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasos);
        list = (ListView) findViewById(R.id.listPasos);
        DBHelper db = new DBHelper(this);
        pasos = db.getPasos();
        list.setAdapter(new StepsAdapter(this,pasos));
        list.setOnItemClickListener(this);
        TextView totalPasos = findViewById(R.id.pasosTotal);
        totalPasos.setText("Total: "+pasos.length);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();
       /* if( itemId == R.id.item_new ){
            startActivityForResult(new Intent(this, NewPaso.class),0);
            return true;
        }else */if(itemId == R.id.item_query){
            Intent i = new Intent(this,QueryAct.class);
            i.putExtra("com.mylaneza.jamarte.PARENT",1);

            startActivityForResult(i,1);
            return true;
        }else
            return false;
    }

    public void openNewPaso(View v){
        startActivityForResult(new Intent(this, NewPaso.class),0);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Step p = pasos[i];
        Intent intent = new Intent(this, NewPaso.class);
        intent.putExtra("com.mylaneza.jamarte.ID",p.id);
        startActivityForResult( intent,0);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK){
            DBHelper db = new DBHelper(this);
            if(requestCode == 0){ 
                pasos = db.getPasos();
            }else{
                String base = data.getStringExtra("com.mylaneza.jamarte.OPCION");
                String column = data.getStringExtra("com.mylaneza.jamarte.COLUMN");
                if(base != null && !"Todos".equals(base)){
                    pasos = db.getPasosBy(DBContract.Steps.COL_BASE,base,true);
                }
            }
            StepsAdapter ap = (StepsAdapter) list.getAdapter();
            ap.steps = pasos;
            ap.notifyDataSetChanged();
            TextView totalPasos = findViewById(R.id.pasosTotal);
            totalPasos.setText("Total: "+pasos.length);
        }
    }
}
