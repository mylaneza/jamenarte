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

import com.mylaneza.jamarte.adapters.SequenceAdapter;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Sequence;

import com.mylaneza.jamarte.forms.NewSequence;

public class Sequences extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Sequence[] sequences;
    ListView list;
    long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secuencias);
        list = findViewById(R.id.listSecuencias);
        try(DBHelper db = new DBHelper(this)){
            id = getIntent().getLongExtra("com.mylaneza.jamarte.ID",-1);

            sequences = db.getSecuencias(id);

            list.setAdapter(new SequenceAdapter(this, sequences));
            list.setOnItemClickListener(this);
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

    public void openNewSequence(View v){
        Intent intent = new Intent(this, NewSequence.class);
        intent.putExtra("com.mylaneza.jamarte.LECCION", id);
        startActivityForResult(intent,0);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, NewSequence.class);
        intent.putExtra("com.mylaneza.jamarte.ID", sequences[i].id);
        startActivityForResult(intent,0);
    }
}
