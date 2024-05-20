package com.mylaneza.jamarte;


import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.database.DBHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void miembros(View v){
        Intent i = new Intent(this, Miembros.class);
        startActivity(i);
    }

    public void pasos(View v){
        Intent i = new Intent(this, Pasos.class);
        startActivity(i);
    }

    public void sesiones(View v){
        Intent i = new Intent(this, Sesiones.class);
        startActivity(i);
    }

    public void avances(View v){
        //Haber
        //Log.i("Avances","AVANCES");
        Intent i = new Intent(this, ListaAvances.class);
        startActivity(i);

    }
    
    public void lecciones(View v){
        Intent i = new Intent(this,Lecciones.class);
        startActivity(i);
    }

    public void exportar(View v){
        DBHelper helper = new DBHelper(this);
        helper.exportar();
    }

    public void actualizar(View v){
        DBHelper helper = new DBHelper(this);
        helper.alterTable();
    }

}
