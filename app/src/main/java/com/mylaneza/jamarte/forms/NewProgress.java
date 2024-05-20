package com.mylaneza.jamarte.forms;



import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.R;

public class NewProgress extends AppCompatActivity {

    EditText level;
    EditText lesson;
    Spinner rol;
    Spinner member;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_avance);
        level = findViewById(R.id.txtNivel);
        lesson = findViewById(R.id.txtLeccion);
        rol = findViewById(R.id.spRol);
        member = findViewById(R.id.spMiembro);
    }

    public void save(View v){
        finish();
    }
}
