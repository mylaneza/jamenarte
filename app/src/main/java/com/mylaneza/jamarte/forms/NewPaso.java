package com.mylaneza.jamarte.forms;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.database.DBHelper;

import com.mylaneza.jamarte.entities.Paso;
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
            Paso p = db.getPaso(id);

            if(p != null){
                nombre.setText(p.nombre);

                cuenta.setText(""+p.cuenta);
                base.setText(p.base);

                lider.setText(p.descripcionLider);
                follower.setText(p.descripcionFollower);
                path.setText(p.path);
            }
        }
    }

    public void salvar(View v){
        Paso paso = new Paso();
        paso.id = id;
        paso.nombre = this.nombre.getText().toString();

        try {
            paso.cuenta = Integer.parseInt(this.cuenta.getText().toString());
        }catch(Exception e){
            Toast.makeText(this,"Solo numeros enteros para la cuenta",Toast.LENGTH_SHORT).show();
            return;
        }
        paso.base = this.base.getText().toString();
        paso.descripcionLider = this.lider.getText().toString();
        paso.descripcionFollower = this.follower.getText().toString();

        String pathString = path.getText().toString();
        if(pathString != null && !pathString.isEmpty())
            paso.path = pathString;
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
