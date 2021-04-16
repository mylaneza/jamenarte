package com.mylaneza.jamarte.forms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mylaneza.jamarte.R;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Paso;
import com.mylaneza.jamarte.entities.SecuenciaPaso;

public class NewSecuenciaPaso extends AppCompatActivity {

    Spinner sp;
    EditText orden;
    EditText repeticion;
    EditText detalle;

    Paso[] pasos;

    String nombresPasos[];

    long id;
    SecuenciaPaso secuenciaPaso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_secuencia_paso);
        sp = (Spinner) findViewById(R.id.spPaso);
        orden = (EditText)findViewById(R.id.etOrden);
        repeticion = (EditText)findViewById(R.id.etRepeticion);
        detalle = (EditText)findViewById(R.id.etDetalle);
        DBHelper db = new DBHelper(this);
        pasos = db.getPasos();
        getNombresPasos();
        ArrayAdapter<String> oAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item , nombresPasos );
        oAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        sp.setAdapter(oAdapter);

        id = getIntent().getLongExtra("com.mylaneza.jamarte.ID",-1);
        if(id > -1){
            secuenciaPaso = db.getPasoDeSecuencia(id);
            sp.setSelection(getStepPosition());

            orden.setText(""+secuenciaPaso.orden);
            detalle.setText(secuenciaPaso.detalle);
            repeticion.setText(""+secuenciaPaso.repeticion);
        }else{
            secuenciaPaso = new SecuenciaPaso();
            secuenciaPaso.secuencia = getIntent().getLongExtra("com.mylaneza.jamarte.SECUENCIA",-1);
        }
    }

    private int getStepPosition() {
        for(int i = 0 ; i < nombresPasos.length ; i++){
            if(nombresPasos[i].equals(secuenciaPaso.getNombre())){
                return i;
            }
        }
        return 0;
    }

    private void getNombresPasos(){
        nombresPasos = new String[pasos.length];
        for(int i = 0 ; i < pasos.length ; i++){
            nombresPasos[i] = pasos[i].nombre+" "+pasos[i].base+" "+pasos[i].cuenta;
        }
    }

    public void salvar(View v){
        int orden = 0;
        try{
            orden = Integer.parseInt(this.orden.getText().toString());
        }catch(Exception e){
            Toast.makeText(this,"El orden debe ser entero",Toast.LENGTH_SHORT).show();
            return;
        }
        int repeticion = 0;
        try{
            repeticion = Integer.parseInt(this.repeticion.getText().toString());
        }catch(Exception e){
            Toast.makeText(this,"La repeticion debe ser entero",Toast.LENGTH_SHORT).show();
            return;
        }
        String detalle = this.detalle.getText().toString();
        DBHelper db = new DBHelper(this);

        secuenciaPaso.repeticion = repeticion;
        secuenciaPaso.orden = orden;
        secuenciaPaso.detalle = detalle;
        secuenciaPaso.paso = pasos[sp.getSelectedItemPosition()].id;

        if(id  > -1){
            secuenciaPaso.id = id;
            db.updateSecuenciaPaso(secuenciaPaso);
        }else{
            if(db.insertaSecuenciaPaso(secuenciaPaso)  > -1){
                Toast.makeText(this, "Se agrego el paso a la secuencia", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "No se pudo agregar el paso a la secuencia", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }
}
