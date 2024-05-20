package com.mylaneza.jamarte.forms;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Member;
import com.mylaneza.jamarte.R;

import com.mylaneza.jamarte.entities.MemberSession;

import java.util.Vector;


public class NewAssistanceList extends AppCompatActivity {

    Spinner spinner;
    Member[] members;
    String[] nicknames;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_lista);

        Intent intent = getIntent();
        id = intent.getLongExtra("com.mylaneza.jamarte.ID",-1);
        long[] list_ids = intent.getLongArrayExtra("com.mylaneza.jamarte.LIST_IDS");
        try(DBHelper db = new DBHelper(this)){
            members = db.getMiembros();
        }

        if(list_ids != null && list_ids.length > 0){
            Vector<Member> members = new Vector<>();

            for (Member member : this.members) {
                boolean alreadyInList = false;
                for (long listId : list_ids) {
                    if (member.id == listId) {
                        alreadyInList = true;
                        break;
                    }
                }
                if (!alreadyInList) {
                    members.add(member);
                }
            }
            this.members = new Member[0];
            this.members = members.toArray(this.members);

        }

        nicknames = new String[members.length];
        //Log.i("NICKNAMES",""+nicknames.length);
        for(int i = 0 ; i < nicknames.length ; i++){
            nicknames[i] = members[i].nickname;
        }



        spinner = findViewById(R.id.spMiembros);
        ArrayAdapter<String> oAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item , nicknames );
        oAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinner.setAdapter(oAdapter);
    }

    public void save(View v){
        try(DBHelper db = new DBHelper(this)){
            MemberSession se = new MemberSession();
            Member m =  members[spinner.getSelectedItemPosition()];
            se.memberId = m.id;
            se.sessionId = id;
            if(db.insertaLista(se)> -1) {
                Toast.makeText(this, m.nickname + " added.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
            }else{
                setResult(RESULT_CANCELED);
                Toast.makeText(this,"Not added "+m.nickname,Toast.LENGTH_SHORT).show();

            }
        }


        finish();
    }
}
