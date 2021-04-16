package com.mylaneza.jamarte.forms;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;


import com.mylaneza.jamarte.R;
import com.mylaneza.jamarte.Secuencias;
import com.mylaneza.jamarte.adapters.AdaptadorPasos;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Leccion;
import com.mylaneza.jamarte.entities.Paso;

public class NewLesson extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    EditText nombre;
    EditText nivel;
    EditText escuela;
    EditText objetivo;
    EditText descripcion;
    Spinner spPasos;
    ListView listaPasos;
    long id;

    Leccion leccion;

    Paso[] pasos;

    String nombresPasos[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_lesson);
        nombre = (EditText) findViewById(R.id.etLeccionNombre);
        nivel = (EditText)findViewById(R.id.etLeccionNivel);
        escuela = (EditText)findViewById(R.id.etLeccionEscuela);
        objetivo = (EditText)findViewById(R.id.etLeccionObjetivo);
        descripcion = (EditText)findViewById(R.id.etDescripcion);
        spPasos = (Spinner)findViewById(R.id.spPasos);

        DBHelper db = new DBHelper(this);
        pasos = db.getPasos();
        getNombresPasos();
        ArrayAdapter<String> oAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item , nombresPasos );
        oAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spPasos.setAdapter(oAdapter);


        listaPasos = (ListView)findViewById(R.id.lvLeccionPasos);
        listaPasos.setOnItemLongClickListener(this);
        id = getIntent().getLongExtra("com.mylaneza.jamarte.ID",-1);
        if(id > -1) {

            leccion = db.getLeccion(id);

            if(leccion != null){
                setTitle(leccion.objetivo);
                nombre.setText(leccion.nombre);
                nivel.setText(leccion.nivel);
                escuela.setText(leccion.escuela);
                objetivo.setText(leccion.objetivo);
                descripcion.setText(leccion.descripcion);
                Paso[] pasos1 = new Paso[0];
                pasos1 = leccion.pasos.toArray(pasos1);
                listaPasos.setAdapter(new AdaptadorPasos(this,pasos1));
            }
        }else{
            Paso[] pasos1 = new Paso[0];

            listaPasos.setAdapter(new AdaptadorPasos(this,pasos1));
            leccion = new Leccion();
        }
    }

    private void getNombresPasos(){
        nombresPasos = new String[pasos.length];
        for(int i = 0 ; i < pasos.length ; i++){
            nombresPasos[i] = pasos[i].nombre+" "+pasos[i].base+" "+pasos[i].cuenta;
        }
    }


    public void agregaPaso(View v){
        leccion.pasos.add(pasos[spPasos.getSelectedItemPosition()]);
        AdaptadorPasos ap = (AdaptadorPasos) listaPasos.getAdapter();
        Paso[] pasos1 = new Paso[0];
        pasos1 = leccion.pasos.toArray(pasos1);
        ap.pasos =  pasos1;
        //Log.i("PASOS",""+pasos1.length);
        ap.notifyDataSetChanged();
        findViewById(R.id.newLessonView).invalidate();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l){
        //Borrar paso
        leccion.pasos.remove(i);
        AdaptadorPasos ap = (AdaptadorPasos) listaPasos.getAdapter();
        Paso[] pasos1 = new Paso[0];
        pasos1 = leccion.pasos.toArray(pasos1);
        ap.pasos =  pasos1;
        ap.notifyDataSetChanged();
        return true;
    }

    public void salvar(View v){
        leccion.nombre = this.nombre.getText().toString();
        leccion.nivel = this.nivel.getText().toString();
        leccion.escuela = this.escuela.getText().toString();
        leccion.objetivo = this.objetivo.getText().toString();
        leccion.descripcion = this.descripcion.getText().toString();
        DBHelper db = new DBHelper(this);
        if(this.id < 0){

            long  id = db.insertaLeccion(leccion);
            if(id > -1) {
                Toast.makeText(this, "Leccion creada", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this,"Leccion no creada",Toast.LENGTH_SHORT).show();
            }
        }else{
            if(db.updateLeccion(leccion)){
                Toast.makeText(this, "Leccion actualizada", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this,"Leccion no actualizada",Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void secuencias(View v){
        if(id > -1) {
            Intent intent = new Intent(this, Secuencias.class);
            intent.putExtra("com.mylaneza.jamarte.ID", id);
            startActivity(intent);
        }else{
            Toast.makeText(this,"Primero se debe crear la leccion",Toast.LENGTH_SHORT).show();
        }
    }


}
