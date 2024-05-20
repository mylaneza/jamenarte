package com.mylaneza.jamarte.forms;


import android.content.Intent;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.ProgressScreen;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Member;
import com.mylaneza.jamarte.R;

public class NewMember extends AppCompatActivity {

    EditText nick;
    EditText name;
    EditText lastNameMother;
    EditText lastNameParent;
    EditText birthday;
    ToggleButton gender;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_miembro);
        nick = findViewById(R.id.txtNick);
        name = findViewById(R.id.txtName);
        lastNameMother = findViewById(R.id.txtApellidoM);
        lastNameParent = findViewById(R.id.txtApellidoP);
        birthday = findViewById(R.id.txtCumple);
        gender = findViewById(R.id.tgGenero);
        id = getIntent().getLongExtra("com.mylaneza.jamarte.ID",-1);
        if(id > -1) {
            try(DBHelper db = new DBHelper(this)){
                Member m = db.getMiembro(id);
                if(m != null){
                    nick.setText(m.nickname);
                    name.setText(m.name);
                    lastNameMother.setText(m.lastNameMother);
                    lastNameParent.setText(m.lastNameParent);
                    birthday.setText(m.birthday);
                    gender.setChecked(m.gender == 1);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    public void save(View v){
        Member mt = new Member();
        mt.id = id;
        mt.nickname = this.nick.getText().toString();
        mt.name = this.name.getText().toString();
        mt.lastNameMother = this.lastNameMother.getText().toString();
        mt.lastNameParent = this.lastNameParent.getText().toString();
        mt.birthday = this.birthday.getText().toString();
        mt.gender = this.gender.isChecked() ? 1 : 0;
        try(DBHelper db = new DBHelper(this)){
            if(id < 0 ){
                if(db.insertaMiembro(mt)> -1) {
                    setResult(RESULT_OK);
                    Toast.makeText(this, "Member added", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(this,"Member not added",Toast.LENGTH_SHORT).show();
            }else{
                if(db.updateMiembro(mt)) {
                    setResult(RESULT_OK);
                    Toast.makeText(this, "Member updated", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(this,"Member not updated",Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if( item.getItemId() == R.id.item_avances ){
            if(id > -1) {
                Intent i = new Intent(this, ProgressScreen.class);
                i.putExtra("com.mylaneza.jamarte.ID",id);
                startActivityForResult(i, 0);
                return true;
            }else{
                Toast.makeText(this,"You must add the member first",Toast.LENGTH_SHORT).show();
                return false;
            }
        }else
            return false;
    }


}
