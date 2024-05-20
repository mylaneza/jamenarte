package com.mylaneza.jamarte.forms;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.database.DBHelper;

import com.mylaneza.jamarte.entities.Step;
import com.mylaneza.jamarte.R;

public class NewPaso extends AppCompatActivity implements View.OnClickListener{

    EditText nombre;
    EditText cuenta;
    EditText base;
    EditText lider;
    EditText follower;
    EditText path;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_paso);
        nombre =  findViewById(R.id.txtPasoNombre);

        cuenta =  findViewById(R.id.txtPasoCuenta);
        base =  findViewById(R.id.txtPasoBase);

        lider =  findViewById(R.id.txtLider);
        follower = findViewById(R.id.txtFollower);
        path = findViewById(R.id.newPasoPath);
        findViewById(R.id.savePaso).setOnClickListener(this);
        id = getIntent().getLongExtra("com.mylaneza.jamarte.ID",-1);
        if(id > -1) {
            DBHelper db = new DBHelper(this);
            Step p = db.getPaso(id);

            if(p != null){
                nombre.setText(p.name);

                cuenta.setText(""+p.count);
                base.setText(p.base);

                lider.setText(p.descriptionLeader);
                follower.setText(p.descriptionFollower);
                path.setText(p.videoFilePath);
            }
        }
    }

    public void salvar(View v){
        Step paso = new Step();
        paso.id = id;
        paso.name = this.nombre.getText().toString();

        try {
            paso.count = Integer.parseInt(this.cuenta.getText().toString());
        }catch(Exception e){
            Toast.makeText(this,"Solo numeros enteros para la cuenta",Toast.LENGTH_SHORT).show();
            return;
        }
        paso.base = this.base.getText().toString();
        paso.descriptionLeader = this.lider.getText().toString();
        paso.descriptionFollower = this.follower.getText().toString();

        String pathString = path.getText().toString();
        if(pathString != null && !pathString.isEmpty())
            paso.videoFilePath = pathString;
        DBHelper db = new DBHelper(this);

        if(id < 0 ){
            if(db.insertaPaso(paso)> -1) {
                setResult(RESULT_OK);
                Toast.makeText(this, "Registro creado", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(this,"No se creo al registro",Toast.LENGTH_SHORT).show();
        }else{
            if(db.updatePaso(paso)) {
                setResult(RESULT_OK);
                Toast.makeText(this, "Registro actualizado", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(this,"No se actualizo el registro",Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        Log.i("PASO","NUEVO");
        if(v.getId() == R.id.savePaso)
            salvar(v);
    }
}
