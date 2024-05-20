package com.mylaneza.jamarte;


import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import android.widget.Spinner;

public class QueryAct extends Activity {


    Spinner spinner;

    String[] opciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        spinner = (Spinner)findViewById(R.id.qf1);
        int parent = getIntent().getIntExtra("com.mylaneza.jamarte.PARENT",0);
        switch(parent){
            case 0: //Sesiones
                initOpcionesSesiones();
                break;
            case 1: //Pasos
                initOpcionesPasos();
                break;
            case 2: //Lecciones
                initOpcionesLecciones();
                break;
            case 3:
                initOpcionesAvances();
                break;
            default:
                initOpcionesSesiones();
        }


        ArrayAdapter<String> oAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item , opciones );
        oAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinner.setAdapter(oAdapter);
    }

    private void initOpcionesSesiones(){
        opciones = new String[5];
        opciones[0] = "Todos";
        opciones[1] = "Bravo Hay Jam";
        opciones[2] = "Jam en Arte";
        opciones[3] = "Calavera Swing";
        opciones[4] = "Jam en Italia";
    }

    private void initOpcionesAvances(){
        opciones = new String[4];
        opciones[0] = "Todos";
        opciones[2] = "Jam en Arte";
        opciones[3] = "Calavera Swing";
    }

    private void initOpcionesPasos(){
        opciones = new String[11];
        opciones[0] = "Todos";
        opciones[1] = "Solo Jazz";
        opciones[2] = "Charleston";
        opciones[3] = "Lindy Hop";
        opciones[4] = "Jive";
        opciones[5] = "Collegiate Shag";
        opciones[6] = "St. Louis Shag";
        opciones[7] = "Balboa";
        opciones[8] = "Jitterbug";
        opciones[9] = "West Coast Swing";
        opciones[10] = "Boogie Woogie";
    }

    private void initOpcionesLecciones(){
        opciones = new String[4];
        opciones[0] = "Todos";
        opciones[1] = "Jam en Arte";
        opciones[2] = "Lindy Hop Puebla";
        opciones[3] = "Calavera Swing";
    }

    public void ok(View v){
        Intent data = new Intent();
        data.putExtra("com.mylaneza.jamarte.OPCION",opciones[spinner.getSelectedItemPosition()]);
        setResult(RESULT_OK,data);
        finish();
    }
}
