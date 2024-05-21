package com.mylaneza.jamarte;


import android.content.Intent;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.adapters.MembersAdapter;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Member;

import com.mylaneza.jamarte.forms.NewMember;

public class Members extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Member[] members;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miembros);
        list = findViewById(R.id.listMiembros);
        try(DBHelper db = new DBHelper(this)){
            members = db.getMiembros();
            list.setAdapter( new MembersAdapter(this, members));
            list.setOnItemClickListener(this);
            TextView v = findViewById(R.id.miembrosTotal);
            v.setText(getString(R.string.strTotal,members.length));
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

    public void openNewMember(View v){
        startActivityForResult(new Intent(this, NewMember.class),0);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Member m = members[i];
        Intent intent = new Intent(this, NewMember.class);
        intent.putExtra("com.mylaneza.jamarte.ID",m.id);
        startActivityForResult( intent, 0);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK) {
            try(DBHelper db = new DBHelper(this)){
                members = db.getMiembros();
                MembersAdapter ap = (MembersAdapter) list.getAdapter();
                ap.members = members;
                ap.notifyDataSetChanged();
                TextView v = findViewById(R.id.miembrosTotal);
                v.setText(getString(R.string.strTotal,members.length));
            }

        }
    }
}
