package com.mylaneza.jamarte.forms;


import android.content.Intent;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.Avances;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Miembro;
import com.mylaneza.jamarte.R;

public class NewMiembro extends AppCompatActivity {

    EditText nick;
    EditText nombre;
    EditText apellidom;
    EditText apellidop;
    EditText cumple;
    ToggleButton genero;

    long id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_miembro);
        nick = findViewById(R.id.txtNick);
        nombre = findViewById(R.id.txtName);
        apellidom = findViewById(R.id.txtApellidoM);
        apellidop = findViewById(R.id.txtApellidoP);
        cumple = findViewById(R.id.txtCumple);
        genero = findViewById(R.id.tgGenero);
        id = getIntent().getLongExtra("com.mylaneza.jamarte.ID",-1);
        if(id > -1) {
            //Log.i("ID",""+id);
            DBHelper db = new DBHelper(this);
            Miembro m = db.getMiembro(id);
            if(m != null){
                nick.setText(m.nickname);
                nombre.setText(m.nombre);
                apellidom.setText(m.apellidom);
                apellidop.setText(m.apellidop);
                cumple.setText(m.cumple);
                genero.setChecked(m.genero == 1);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    public void salvar(View v){
        Miembro mt = new Miembro();
        mt.id = id;
        mt.nickname = this.nick.getText().toString();
        mt.nombre = this.nombre.getText().toString();
        mt.apellidom = this.apellidom.getText().toString();
        mt.apellidop  = this.apellidop.getText().toString();
        mt.cumple = this.cumple.getText().toString();
        mt.genero = this.genero.isChecked() ? 1 : 0;
        DBHelper db = new DBHelper(this);
        if(id < 0 ){
            if(db.insertaMiembro(mt)> -1) {
                setResult(RESULT_OK);
                Toast.makeText(this, "Registro creado", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(this,"No se creo al registro",Toast.LENGTH_SHORT).show();
        }else{
            if(db.updateMiembro(mt)) {
                setResult(RESULT_OK);
                Toast.makeText(this, "Registro actualizado", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(this,"No se actualizo el registro",Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if( item.getItemId() == R.id.item_avances ){
            if(id > -1) {
                Intent i = new Intent(this, Avances.class);
                i.putExtra("com.mylaneza.jamarte.ID",id);
                startActivityForResult(i, 0);
                return true;
            }else{
                Toast.makeText(this,"Debes crear primero el registro del miembro",Toast.LENGTH_SHORT).show();
                return false;
            }
        }else
            return false;
    }


}
