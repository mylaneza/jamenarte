package com.mylaneza.jamarte.forms;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.R;
import com.mylaneza.jamarte.SecuenciaPasos;

import com.mylaneza.jamarte.database.DBHelper;

import com.mylaneza.jamarte.entities.Lesson;
import com.mylaneza.jamarte.entities.Sequence;


public class NewSequence extends AppCompatActivity {

    long id;

    EditText name;
    TextView lesson;
    Lesson l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_secuencia);
        name = findViewById(R.id.secName);
        lesson = findViewById(R.id.secLeccion);
        Intent intent = getIntent();
        id = intent.getLongExtra("com.mylaneza.jamarte.ID",-1);

        if(id > -1){
            try(DBHelper db = new DBHelper(this)){
                Sequence s = db.getSecuencia(id);
                l = db.getLeccion(s.lessonId);
                name.setText(s.name);
                lesson.setText(getString(R.string.strLessonRow,l.level,l.name));
            }
        }else{
            long lessonId = intent.getLongExtra("com.mylaneza.jamarte.LECCION",-1);
            try(DBHelper db = new DBHelper(this)){
                l = db.getLeccion(lessonId);
                lesson.setText(getString(R.string.strLessonRow,l.level,l.name));
            }

        }
    }

    public void steps(View v){
        if(id > -1) {
            Intent intent = new Intent(this, SecuenciaPasos.class);
            intent.putExtra("com.mylaneza.jamarte.ID", id);
            startActivity(intent);
        }else{
            Toast.makeText(this,"First add a sequence",Toast.LENGTH_SHORT).show();
        }
    }

    public void save(View v){
        String name = this.name.getText().toString().trim();
        if(name.length() > 0){
            Sequence s = new Sequence();
            s.lessonId = l.id;
            s.name = name;
            try(DBHelper db = new DBHelper(this)){
                if(id > -1){
                    if(db.updateSecuencia(s)){
                        Toast.makeText(this, "Sequence updated", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "Sequence not updated", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }else{
                    if(db.insertaSecuencia(s)> -1){
                        Toast.makeText(this, "Sequence added", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "Sequence not added", Toast.LENGTH_SHORT).show();
                    }
                    finish();

                }
            }

        }else{
            Toast.makeText(this,"You must fill sequence name",Toast.LENGTH_SHORT).show();
        }
    }
}
