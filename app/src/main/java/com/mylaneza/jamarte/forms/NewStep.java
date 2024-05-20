package com.mylaneza.jamarte.forms;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.database.DBHelper;

import com.mylaneza.jamarte.entities.Step;
import com.mylaneza.jamarte.R;

public class NewStep extends AppCompatActivity implements View.OnClickListener{

    EditText name;
    EditText count;
    EditText base;
    EditText leaderDetails;
    EditText followerDetails;
    EditText videoFilePath;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_paso);
        name =  findViewById(R.id.txtPasoNombre);

        count =  findViewById(R.id.txtPasoCuenta);
        base =  findViewById(R.id.txtPasoBase);

        leaderDetails =  findViewById(R.id.txtLider);
        followerDetails = findViewById(R.id.txtFollower);
        videoFilePath = findViewById(R.id.newPasoPath);
        findViewById(R.id.savePaso).setOnClickListener(this);
        id = getIntent().getLongExtra("com.mylaneza.jamarte.ID",-1);
        if(id > -1) {
            try(DBHelper db = new DBHelper(this)){
                Step p = db.getPaso(id);
                if(p != null){
                    name.setText(p.name);
                    count.setText(getString(R.string.strInteger,p.count));
                    base.setText(p.base);

                    leaderDetails.setText(p.descriptionLeader);
                    followerDetails.setText(p.descriptionFollower);
                    videoFilePath.setText(p.videoFilePath);
                }
            }

        }
    }

    public void save(View v){
        Step paso = new Step();
        paso.id = id;
        paso.name = this.name.getText().toString();

        try {
            paso.count = Integer.parseInt(this.count.getText().toString());
        }catch(Exception e){
            Toast.makeText(this,"Just integer values for the count",Toast.LENGTH_SHORT).show();
            return;
        }
        paso.base = this.base.getText().toString();
        paso.descriptionLeader = this.leaderDetails.getText().toString();
        paso.descriptionFollower = this.followerDetails.getText().toString();

        String pathString = videoFilePath.getText().toString();
        if(!pathString.isEmpty())
            paso.videoFilePath = pathString;
        try(DBHelper db = new DBHelper(this)){
            if(id < 0 ){
                if(db.insertaPaso(paso)> -1) {
                    setResult(RESULT_OK);
                    Toast.makeText(this, "Step added", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(this,"Step not added",Toast.LENGTH_SHORT).show();
            }else{
                if(db.updatePaso(paso)) {
                    setResult(RESULT_OK);
                    Toast.makeText(this, "Step updated", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(this,"Step not updated",Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.savePaso)
            save(v);
    }
}
