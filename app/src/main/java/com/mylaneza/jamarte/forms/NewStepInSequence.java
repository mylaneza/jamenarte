package com.mylaneza.jamarte.forms;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.R;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Step;
import com.mylaneza.jamarte.entities.StepInSequence;

public class NewStepInSequence extends AppCompatActivity {

    Spinner sp;
    EditText seqNo;
    EditText repetitions;
    EditText detail;

    Step[] steps;

    String[] stepNames;

    long id;
    StepInSequence stepInSequence;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_secuencia_paso);
        sp =  findViewById(R.id.spPaso);
        seqNo = findViewById(R.id.etOrden);
        repetitions = findViewById(R.id.etRepeticion);
        detail = findViewById(R.id.etDetalle);
        try(DBHelper db = new DBHelper(this)){
            steps = db.getPasos();
            getStepsNames();
            ArrayAdapter<String> oAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item , stepNames);
            oAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
            sp.setAdapter(oAdapter);

            id = getIntent().getLongExtra("com.mylaneza.jamarte.ID",-1);
            if(id > -1){
                stepInSequence = db.getPasoDeSecuencia(id);
                sp.setSelection(getStepPosition());

                seqNo.setText(getString(R.string.strInteger,stepInSequence.seqNo));
                detail.setText(stepInSequence.detail);
                repetitions.setText(getString(R.string.strInteger,stepInSequence.repetitions));
            }else{
                stepInSequence = new StepInSequence();
                stepInSequence.sequenceId = getIntent().getLongExtra("com.mylaneza.jamarte.SECUENCIA",-1);
            }
        }

    }

    private int getStepPosition() {
        for(int i = 0; i < stepNames.length ; i++){
            if(stepNames[i].equals(stepInSequence.getName())){
                return i;
            }
        }
        return 0;
    }

    private void getStepsNames(){
        stepNames = new String[steps.length];
        for(int i = 0; i < steps.length ; i++){
            stepNames[i] = steps[i].name +" "+ steps[i].base+" "+ steps[i].count;
        }
    }

    public void save(View v){
        int seqNo;
        try{
            seqNo = Integer.parseInt(this.seqNo.getText().toString());
        }catch(Exception e){
            Toast.makeText(this,"Seq. No. must be an integer",Toast.LENGTH_SHORT).show();
            return;
        }
        int repetitions;
        try{
            repetitions = Integer.parseInt(this.repetitions.getText().toString());
        }catch(Exception e){
            Toast.makeText(this,"Repetitions must be an integer",Toast.LENGTH_SHORT).show();
            return;
        }
        String detail = this.detail.getText().toString();
        try(DBHelper db = new DBHelper(this)){
            stepInSequence.repetitions = repetitions;
            stepInSequence.seqNo = seqNo;
            stepInSequence.detail = detail;
            stepInSequence.stepId = steps[sp.getSelectedItemPosition()].id;

            if(id  > -1){
                stepInSequence.id = id;
                db.updateSecuenciaPaso(stepInSequence);
            }else{
                if(db.insertaSecuenciaPaso(stepInSequence)  > -1){
                    Toast.makeText(this, "Step in sequence added", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Step in sequence not added", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        }


    }
}
