package com.mylaneza.jamarte;


import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.database.DBHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void members(View v){
        Intent i = new Intent(this, Members.class);
        startActivity(i);
    }

    public void steps(View v){
        Intent i = new Intent(this, Steps.class);
        startActivity(i);
    }

    public void sessions(View v){
        Intent i = new Intent(this, Sessions.class);
        startActivity(i);
    }

    public void progress(View v){
        Intent i = new Intent(this, ProgressList.class);
        startActivity(i);

    }
    
    public void lessons(View v){
        Intent i = new Intent(this, Lessons.class);
        startActivity(i);
    }

    public void export(View v){
        try(DBHelper helper = new DBHelper(this)){
            helper.exportar();
        }

    }

    public void update(View v){
        try(DBHelper helper = new DBHelper(this)){
            helper.alterTable();
        }
    }

}
