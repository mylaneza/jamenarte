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

import com.mylaneza.jamarte.forms.NewStep;

public class Steps extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView list;
    Step[] steps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasos);
        list =  findViewById(R.id.listPasos);
        try(DBHelper db = new DBHelper(this)){
            steps = db.getPasos();
            list.setAdapter(new StepsAdapter(this, steps));
            list.setOnItemClickListener(this);
            TextView totalSteps = findViewById(R.id.pasosTotal);
            totalSteps.setText(getString(R.string.strTotal,steps.length));
        }

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
        startActivityForResult(new Intent(this, NewStep.class),0);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Step p = steps[i];
        Intent intent = new Intent(this, NewStep.class);
        intent.putExtra("com.mylaneza.jamarte.ID",p.id);
        startActivityForResult( intent,0);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK){
            try(DBHelper db = new DBHelper(this)){
                if(requestCode == 0){
                    steps = db.getPasos();
                }else{
                    String base = data.getStringExtra("com.mylaneza.jamarte.OPCION");
                    if(base != null && !"Todos".equals(base)){
                        steps = db.getPasosBy(DBContract.Steps.COL_BASE,base,true);
                    }
                }
                StepsAdapter ap = (StepsAdapter) list.getAdapter();
                ap.steps = steps;
                ap.notifyDataSetChanged();
                TextView totalSteps = findViewById(R.id.pasosTotal);
                totalSteps.setText(getString(R.string.strTotal,steps.length));
            }

        }
    }
}
