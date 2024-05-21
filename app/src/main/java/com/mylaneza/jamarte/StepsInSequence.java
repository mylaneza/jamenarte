package com.mylaneza.jamarte;

import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.adapters.StepInSequenceAdapter;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.StepInSequence;
import com.mylaneza.jamarte.forms.NewStepInSequence;




public class StepsInSequence extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView sp;
    long id;

    StepInSequence[] steps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secuencia_pasos);
        sp = findViewById(R.id.listSecuenciaPaso);
        id = getIntent().getLongExtra("com.mylaneza.jamarte.ID",-1);

        try(DBHelper db = new DBHelper(this)){
            steps = db.getPasosDeSecuencia(id);
            sp.setAdapter(new StepInSequenceAdapter(this, steps));
            sp.setOnItemClickListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        return false;
    }

    public void openNewStepInSequence(View v){
        Intent intent = new Intent(this, NewStepInSequence.class);
        intent.putExtra("com.mylaneza.jamarte.SECUENCIA", id);
        startActivityForResult(intent,0);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, NewStepInSequence.class);
        intent.putExtra("com.mylaneza.jamarte.ID", steps[i].id);
        startActivityForResult(intent,0);
    }
}
