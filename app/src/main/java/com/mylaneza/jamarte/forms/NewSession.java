package com.mylaneza.jamarte.forms;


import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.database.DBHelper;

import com.mylaneza.jamarte.R;
import com.mylaneza.jamarte.entities.Session;
import com.mylaneza.jamarte.AssistanceList;


public class NewSession extends AppCompatActivity {
    long id;
    EditText number;
    EditText date;
    EditText amount;
    EditText school;

    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sesion);
        id = getIntent().getLongExtra("com.mylaneza.jamarte.ID",-1);
        number = findViewById(R.id.sesionNumero);
        date = findViewById(R.id.sesionFecha);
        amount = findViewById(R.id.sesionMonto);
        school = findViewById(R.id.sesionEscuela);
        if(id > -1) {
            //Log.i("ID",""+id);
            try(DBHelper db = new DBHelper(this)){
                session = db.getSesion(id);
                if(session != null){
                    number.setText(getString(R.string.strInteger,session.number));
                    date.setText(session.date);
                    amount.setText(getString(R.string.strDouble,session.amount));
                    school.setText(session.school);
                }
            }

        }else{
            session = new Session();
        }
    }

    public void save(View v){
        try {
            session.number = Integer.parseInt(number.getText().toString());
        }catch(Exception e){
            Toast.makeText(this, "Lesson number must be an integer", Toast.LENGTH_SHORT).show();
            return;
        }

        try{
            session.amount = Double.parseDouble(amount.getText().toString());
        }catch (Exception e){
            Toast.makeText(this, "Amount must be a double", Toast.LENGTH_SHORT).show();
            return;
        }

        session.date = date.getText().toString();
        session.school = school.getText().toString();
        try(DBHelper db = new DBHelper(this)){
            if(this.id < 0){

                long  id = db.insertaSesion(session);
                if(id > -1) {
                    Toast.makeText(this, "Session added", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }else{
                    Toast.makeText(this,"Session not added",Toast.LENGTH_SHORT).show();
                }
            }else{
                if(db.updateSesion(session)){
                    Toast.makeText(this, "Session updated", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }else{
                    Toast.makeText(this,"Session not updated",Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    public void goToAssistanceList(View v){

        if(id > -1){
            Intent i = new Intent(this, AssistanceList.class);
            i.putExtra("com.mylaneza.jamarte.ID",id);
            startActivity(i);
        }else{
            Toast.makeText(this,"You must add a session",Toast.LENGTH_SHORT).show();
        }
    }


}
