package com.mylaneza.jamarte.forms;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.R;
import com.mylaneza.jamarte.SecuenciaPasos;

import com.mylaneza.jamarte.database.DBHelper;

import com.mylaneza.jamarte.entities.Leccion;
import com.mylaneza.jamarte.entities.Secuencia;


public class NewSecuencia extends AppCompatActivity {

    long id;

    EditText name;
    TextView leccion;
    Leccion l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_secuencia);
        name = (EditText)findViewById(R.id.secName);
        leccion = (TextView)findViewById(R.id.secLeccion);
        Intent intent = getIntent();
        id = intent.getLongExtra("com.mylaneza.jamarte.ID",-1);

        if(id > -1){

            DBHelper db = new DBHelper(this);
            //l = db.getLeccion(id);
            Secuencia s = db.getSecuencia(id);
            l = db.getLeccion(s.leccion);
            name.setText(s.name);
            leccion.setText(""+l.nivel+"-"+l.nombre);
        }else{
            long idlec = intent.getLongExtra("com.mylaneza.jamarte.LECCION",-1);
            DBHelper db = new DBHelper(this);
            l = db.getLeccion(idlec);
            leccion.setText(""+l.nivel+"-"+l.nombre);
        }
    }

    public void steps(View v){
        if(id > -1) {
            Intent intent = new Intent(this, SecuenciaPasos.class);
            intent.putExtra("com.mylaneza.jamarte.ID", id);
            startActivity(intent);
        }else{
            Toast.makeText(this,"Primero se debe crear la secuencia",Toast.LENGTH_SHORT).show();
        }
    }

    public void salvar(View v){
        String nombre = name.getText().toString().trim();
        if(nombre.length() > 0){
            Secuencia s = new Secuencia();
            s.leccion = l.id;
            s.name = nombre;
            DBHelper db = new DBHelper(this);
            if(id > -1){
                if(db.updateSecuencia(s)){
                    Toast.makeText(this, "Secuencia actualizada", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Secuencia no actualizada", Toast.LENGTH_SHORT).show();
                }
                finish();
            }else{
                if(db.insertaSecuencia(s)> -1){
                    Toast.makeText(this, "Secuencia creada", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Secuencia no creada", Toast.LENGTH_SHORT).show();
                }
                finish();

            }
        }else{
            Toast.makeText(this,"Debes elegir un nombre para la secuencia",Toast.LENGTH_SHORT).show();
        }
    }
}
