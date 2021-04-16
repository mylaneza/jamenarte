package com.mylaneza.jamarte.forms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Miembro;
import com.mylaneza.jamarte.R;
import com.mylaneza.jamarte.entities.Sesion;
import com.mylaneza.jamarte.entities.SesionEstudiante;
import com.mylaneza.jamarte.SesionesEstudiantes;

public class NewLista extends AppCompatActivity {

    Spinner spinner;
    Miembro miembros[];
    String nicknames[];
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_lista);
        id = getIntent().getLongExtra("com.mylaneza.jamarte.ID",-1);
        DBHelper db = new DBHelper(this);
        miembros = db.getMiembros();
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
        if(db.insertaLista(se)> -1)
            Toast.makeText(this,m.nickname+" agregado.",Toast.LENGTH_SHORT).show();
        else{
            Toast.makeText(this,"No se agrego a "+m.nickname,Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
