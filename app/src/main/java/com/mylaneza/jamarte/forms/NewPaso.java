package com.mylaneza.jamarte.forms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Miembro;
import com.mylaneza.jamarte.entities.Paso;
import com.mylaneza.jamarte.R;

public class NewPaso extends AppCompatActivity {

    EditText nombre;

    EditText cuenta;
    EditText base;

    EditText lider;
    EditText follower;


    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_paso);
        nombre = (EditText) findViewById(R.id.txtPasoNombre);

        cuenta = (EditText) findViewById(R.id.txtPasoCuenta);
        base = (EditText) findViewById(R.id.txtPasoBase);

        lider = (EditText) findViewById(R.id.txtLider);
        follower = (EditText) findViewById(R.id.txtFollower);
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


        DBHelper db = new DBHelper(this);

        if(id < 0 ){
            if(db.insertaPaso(paso)> -1)
                Toast.makeText(this,"Registro creado",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this,"No se creo al registro",Toast.LENGTH_SHORT).show();
        }else{
            if(db.updatePaso(paso))
                Toast.makeText(this,"Registro actualizado",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this,"No se actualizo el registro",Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
