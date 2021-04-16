package com.mylaneza.jamarte.forms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.mylaneza.jamarte.R;

public class NewAvance extends AppCompatActivity {

    EditText nivel;
    EditText leccion;
    Spinner rol;
    Spinner miembro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_avance);
        nivel = (EditText)findViewById(R.id.txtNivel);
        leccion = (EditText)findViewById(R.id.txtLeccion);
        rol = (Spinner) findViewById(R.id.spRol);
        miembro = (Spinner)findViewById(R.id.spMiembro);
    }

    public void salvar(View v){
        finish();
    }
}
