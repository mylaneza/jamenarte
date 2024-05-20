package com.mylaneza.jamarte.forms;


import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.database.DBHelper;

import com.mylaneza.jamarte.R;
import com.mylaneza.jamarte.entities.Session;
import com.mylaneza.jamarte.SesionesEstudiantes;


public class NewSesion extends AppCompatActivity {
    long id;
    EditText numero;
    EditText fecha;
    EditText monto;
    EditText escuela;

    Session sesion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sesion);
        id = getIntent().getLongExtra("com.mylaneza.jamarte.ID",-1);
        numero = (EditText) findViewById(R.id.sesionNumero);
        fecha = (EditText) findViewById(R.id.sesionFecha);
        monto = (EditText)findViewById(R.id.sesionMonto);
        escuela = (EditText)findViewById(R.id.sesionEscuela);
        if(id > -1) {
            //Log.i("ID",""+id);
            DBHelper db = new DBHelper(this);
            sesion = db.getSesion(id);
            if(sesion != null){
                numero.setText(""+sesion.number);
                fecha.setText(sesion.date);
                monto.setText(""+sesion.amount);
                escuela.setText(""+sesion.school);
            }
        }else{
            sesion = new Session();
        }
    }

    public void salvar(View v){
        try {
            sesion.number = Integer.parseInt(numero.getText().toString());
        }catch(Exception e){
            Toast.makeText(this, "El numero de la leccion debe ser entero", Toast.LENGTH_SHORT).show();
            return;
        }

        try{
            sesion.amount = Double.parseDouble(monto.getText().toString());
        }catch (Exception e){
            Toast.makeText(this, "El monto debe ser doble", Toast.LENGTH_SHORT).show();
            return;
        }

        sesion.date = fecha.getText().toString();
        sesion.school = escuela.getText().toString();
        DBHelper db = new DBHelper(this);
        if(this.id < 0){

            long  id = db.insertaSesion(sesion);
            if(id > -1) {
                Toast.makeText(this, "Sesion creada", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }else{
                Toast.makeText(this,"Sesion no creada",Toast.LENGTH_SHORT).show();
            }
        }else{
            if(db.updateSesion(sesion)){
                Toast.makeText(this, "Sesion actualizada", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }else{
                Toast.makeText(this,"Sesion no actualizada",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void listaAsistencia(View v){

        if(id > -1){
            Intent i = new Intent(this, SesionesEstudiantes.class);
            i.putExtra("com.mylaneza.jamarte.ID",id);
            startActivity(i);
        }else{
            Toast.makeText(this,"Tienes que crear la sesion.",Toast.LENGTH_SHORT).show();
        }
    }


}
