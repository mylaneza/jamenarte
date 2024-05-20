package com.mylaneza.jamarte.forms;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.Miembros;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Miembro;
import com.mylaneza.jamarte.R;

import com.mylaneza.jamarte.entities.SesionEstudiante;

import java.util.Vector;


public class NewLista extends AppCompatActivity {

    Spinner spinner;
    Miembro[] miembros;
    String[] nicknames;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_lista);

        Intent intent = getIntent();
        id = intent.getLongExtra("com.mylaneza.jamarte.ID",-1);
        long[] list_ids = intent.getLongArrayExtra("com.mylaneza.jamarte.LIST_IDS");
        DBHelper db = new DBHelper(this);
        miembros = db.getMiembros();
        if(list_ids != null && list_ids.length > 0){

            //Elimina de la lista los miembros que ya fueron agregados a la sesion
            Vector<Miembro> members = new Vector<Miembro>();

            for(int i = 0 ; i < miembros.length ; i++){
                boolean alreadyInList = false;
                for(int j = 0 ; j < list_ids.length ; j++) {
                    if (miembros[i].id == list_ids[j]) {
                        alreadyInList = true;
                        break;
                    }
                }
                if(alreadyInList){
                    //No desplegar
                }else{
                    members.add(miembros[i]);
                }
            }
            miembros = new Miembro[0];
            miembros = members.toArray(miembros);

        }

        nicknames = new String[miembros.length];
        //Log.i("NICKNAMES",""+nicknames.length);
        for(int i = 0 ; i < nicknames.length ; i++){
            nicknames[i] = miembros[i].nickname;
        }



        spinner = (Spinner)findViewById(R.id.spMiembros);
        ArrayAdapter<String> oAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item , nicknames );
        oAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinner.setAdapter(oAdapter);
    }

    public void salvar(View v){

        //Toast.makeText(this,miembros[spinner.getSelectedItemPosition()].nickname,Toast.LENGTH_SHORT).show();
        DBHelper db = new DBHelper(this);
        SesionEstudiante se = new SesionEstudiante();
        Miembro m =  miembros[spinner.getSelectedItemPosition()];
        se.miembro = m.id;
        se.sesion = id;
        if(db.insertaLista(se)> -1) {
            Toast.makeText(this, m.nickname + " agregado.", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
        }else{
            setResult(RESULT_CANCELED);
            Toast.makeText(this,"No se agrego a "+m.nickname,Toast.LENGTH_SHORT).show();

        }

        finish();
    }
}
