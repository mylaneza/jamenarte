package com.mylaneza.jamarte.forms;


import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.R;
import com.mylaneza.jamarte.Secuencias;
import com.mylaneza.jamarte.adapters.AdaptadorPasos;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Lesson;
import com.mylaneza.jamarte.entities.Step;

public class NewLesson extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    EditText nombre;
    EditText nivel;
    EditText escuela;
    EditText objetivo;
    EditText descripcion;
    Spinner spPasos;
    ListView listaPasos;
    long id;

    Lesson leccion;

    Step[] pasos;

    String[] nombresPasos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_lesson);
        nombre = findViewById(R.id.etLeccionNombre);
        nivel = findViewById(R.id.etLeccionNivel);
        escuela = findViewById(R.id.etLeccionEscuela);
        objetivo = findViewById(R.id.etLeccionObjetivo);
        descripcion = findViewById(R.id.etDescripcion);
        spPasos = findViewById(R.id.spPasos);

        DBHelper db = new DBHelper(this);
        pasos = db.getPasos();
        getNombresPasos();
        ArrayAdapter<String> oAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresPasos);
        oAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spPasos.setAdapter(oAdapter);


        listaPasos = findViewById(R.id.lvLeccionPasos);
        listaPasos.setOnItemLongClickListener(this);
        id = getIntent().getLongExtra("com.mylaneza.jamarte.ID",-1);
        if(id > -1) {

            leccion = db.getLeccion(id);

            if(leccion != null){
                //setTitle(leccion.objetivo);
                setTitle(leccion.level +"-"+leccion.name);
                nombre.setText(leccion.name);
                nivel.setText(leccion.level);
                escuela.setText(leccion.school);
                objetivo.setText(leccion.objective);
                descripcion.setText(leccion.description);
                Step[] pasos1 = new Step[0];
                pasos1 = leccion.steps.toArray(pasos1);
                listaPasos.setAdapter(new AdaptadorPasos(this,pasos1));
            }
        }else{
            Step[] pasos1 = new Step[0];

            listaPasos.setAdapter(new AdaptadorPasos(this,pasos1));
            leccion = new Lesson();
        }
    }

    private void getNombresPasos(){
        nombresPasos = new String[pasos.length];
        for(int i = 0 ; i < pasos.length ; i++){
            nombresPasos[i] = pasos[i].name +" "+pasos[i].base+" "+pasos[i].count;
        }
    }


    public void agregaPaso(View v){
        leccion.steps.add(pasos[spPasos.getSelectedItemPosition()]);
        AdaptadorPasos ap = (AdaptadorPasos) listaPasos.getAdapter();
        Step[] pasos1 = new Step[0];
        pasos1 = leccion.steps.toArray(pasos1);
        ap.pasos =  pasos1;
        //Log.i("PASOS",""+pasos1.length);
        ap.notifyDataSetChanged();
        findViewById(R.id.newLessonView).invalidate();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l){
        //Borrar paso
        leccion.steps.remove(i);
        AdaptadorPasos ap = (AdaptadorPasos) listaPasos.getAdapter();
        Step[] pasos1 = new Step[0];
        pasos1 = leccion.steps.toArray(pasos1);
        ap.pasos =  pasos1;
        ap.notifyDataSetChanged();
        return true;
    }

    public void salvar(View v){
        leccion.name = this.nombre.getText().toString();
        leccion.level = this.nivel.getText().toString();
        leccion.school = this.escuela.getText().toString();
        leccion.objective = this.objetivo.getText().toString();
        leccion.description = this.descripcion.getText().toString();
        DBHelper db = new DBHelper(this);
        if(this.id < 0){

            long  id = db.insertaLeccion(leccion);
            if(id > -1) {
                setResult(RESULT_OK);
                Toast.makeText(this, "Leccion creada", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this,"Leccion no creada",Toast.LENGTH_SHORT).show();
            }
        }else{
            if(db.updateLeccion(leccion)){
                setResult(RESULT_OK);
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
