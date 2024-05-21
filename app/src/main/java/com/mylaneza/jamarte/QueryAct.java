package com.mylaneza.jamarte;


import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import android.widget.Spinner;

public class QueryAct extends Activity {


    Spinner spinner;

    String[] options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        spinner = (Spinner)findViewById(R.id.qf1);
        int parent = getIntent().getIntExtra("com.mylaneza.jamarte.PARENT",0);
        switch(parent){
            case 0:
                initSessionsOptions();
                break;
            case 1:
                initStepsOptions();
                break;
            case 2:
                initLessonsOptions();
                break;
            case 3:
                initProgressOptions();
                break;
        }
        ArrayAdapter<String> oAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item , options);
        oAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinner.setAdapter(oAdapter);
    }

    private void initSessionsOptions(){
        options = new String[5];
        options[0] = "Todos";
        options[1] = "Bravo Hay Jam";
        options[2] = "Jam en Arte";
        options[3] = "Calavera Swing";
        options[4] = "Jam en Italia";
    }

    private void initProgressOptions(){
        options = new String[4];
        options[0] = "Todos";
        options[2] = "Jam en Arte";
        options[3] = "Calavera Swing";
    }

    private void initStepsOptions(){
        options = new String[11];
        options[0] = "Todos";
        options[1] = "Solo Jazz";
        options[2] = "Charleston";
        options[3] = "Lindy Hop";
        options[4] = "Jive";
        options[5] = "Collegiate Shag";
        options[6] = "St. Louis Shag";
        options[7] = "Balboa";
        options[8] = "Jitterbug";
        options[9] = "West Coast Swing";
        options[10] = "Boogie Woogie";
    }

    private void initLessonsOptions(){
        options = new String[4];
        options[0] = "Todos";
        options[1] = "Jam en Arte";
        options[2] = "Lindy Hop Puebla";
        options[3] = "Calavera Swing";
    }

    public void ok(View v){
        Intent data = new Intent();
        data.putExtra("com.mylaneza.jamarte.OPCION", options[spinner.getSelectedItemPosition()]);
        setResult(RESULT_OK,data);
        finish();
    }
}
