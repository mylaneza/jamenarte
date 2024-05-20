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
import com.mylaneza.jamarte.Sequences;
import com.mylaneza.jamarte.adapters.StepsAdapter;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Lesson;
import com.mylaneza.jamarte.entities.Step;

public class NewLesson extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    EditText name;
    EditText level;
    EditText school;
    EditText objective;
    EditText description;
    Spinner stepsSpinner;
    ListView stepsList;
    long id;

    Lesson lesson;

    Step[] steps;

    String[] stepsNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_lesson);
        name = findViewById(R.id.etLeccionNombre);
        level = findViewById(R.id.etLeccionNivel);
        school = findViewById(R.id.etLeccionEscuela);
        objective = findViewById(R.id.etLeccionObjetivo);
        description = findViewById(R.id.etDescripcion);
        stepsSpinner = findViewById(R.id.spPasos);

        try(DBHelper db = new DBHelper(this)){
            steps = db.getPasos();
            getStepsNames();
            ArrayAdapter<String> oAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stepsNames);
            oAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
            stepsSpinner.setAdapter(oAdapter);

            stepsList = findViewById(R.id.lvLeccionPasos);
            stepsList.setOnItemLongClickListener(this);
            id = getIntent().getLongExtra("com.mylaneza.jamarte.ID",-1);
            if(id > -1) {

                lesson = db.getLeccion(id);

                if(lesson != null){
                    setTitle(lesson.level +"-"+ lesson.name);
                    name.setText(lesson.name);
                    level.setText(lesson.level);
                    school.setText(lesson.school);
                    objective.setText(lesson.objective);
                    description.setText(lesson.description);
                    Step[] step1 = new Step[0];
                    step1 = lesson.steps.toArray(step1);
                    stepsList.setAdapter(new StepsAdapter(this,step1));
                }
            }else{
                Step[] step1 = new Step[0];

                stepsList.setAdapter(new StepsAdapter(this,step1));
                lesson = new Lesson();
            }
        }


    }

    private void getStepsNames(){
        stepsNames = new String[steps.length];
        for(int i = 0; i < steps.length ; i++){
            stepsNames[i] = steps[i].name +" "+ steps[i].base+" "+ steps[i].count;
        }
    }


    public void addStep(View v){
        lesson.steps.add(steps[stepsSpinner.getSelectedItemPosition()]);
        StepsAdapter ap = (StepsAdapter) stepsList.getAdapter();
        Step[] steps1 = new Step[0];
        steps1 = lesson.steps.toArray(steps1);
        ap.steps =  steps1;
        ap.notifyDataSetChanged();
        findViewById(R.id.newLessonView).invalidate();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l){
        lesson.steps.remove(i);
        StepsAdapter ap = (StepsAdapter) stepsList.getAdapter();
        Step[] steps1 = new Step[0];
        steps1 = lesson.steps.toArray(steps1);
        ap.steps =  steps1;
        ap.notifyDataSetChanged();
        return true;
    }

    public void save(View v){
        lesson.name = this.name.getText().toString();
        lesson.level = this.level.getText().toString();
        lesson.school = this.school.getText().toString();
        lesson.objective = this.objective.getText().toString();
        lesson.description = this.description.getText().toString();
        try(DBHelper db = new DBHelper(this)){
            if(this.id < 0){
                long  id = db.insertaLeccion(lesson);
                if(id > -1) {
                    setResult(RESULT_OK);
                    Toast.makeText(this, "Lesson created", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(this,"Lesson not created",Toast.LENGTH_SHORT).show();
                }
            }else{
                if(db.updateLeccion(lesson)){
                    setResult(RESULT_OK);
                    Toast.makeText(this, "Lesson updated", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(this,"Lesson not updated",Toast.LENGTH_SHORT).show();
                }
            }
        }


    }

    public void goToSequences(View v){
        if(id > -1) {
            Intent intent = new Intent(this, Sequences.class);
            intent.putExtra("com.mylaneza.jamarte.ID", id);
            startActivity(intent);
        }else{
            Toast.makeText(this,"Create lesson first",Toast.LENGTH_SHORT).show();
        }
    }


}
