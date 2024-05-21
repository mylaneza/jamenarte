package com.mylaneza.jamarte;


import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.GridView;

import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.adapters.SessionsAdapter;
import com.mylaneza.jamarte.database.DBHelper;

import com.mylaneza.jamarte.entities.Session;
import com.mylaneza.jamarte.forms.NewSession;

public class Sessions extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Session[] sessions;
    GridView list;
    TextView amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesiones);
        list = findViewById(R.id.listSesiones);
        amount =  findViewById(R.id.montoTotal);
        SharedPreferences sp = getSharedPreferences("com.mylaneza.jamarte.PREFERENCIAS",MODE_PRIVATE);
        String query = sp.getString("com.mylaneza.jamarte.SP_SESION_ESCUELA",null);

        try(DBHelper db = new DBHelper(this)){
            if(query == null ){
                sessions = db.getSesiones();
            }else if(!query.equals("Todos") ){
                sessions = db.getSesionesBy(query);
            }else{
                sessions = db.getSesiones();
            }
        }
        calculateTotal();
        list.setAdapter(new SessionsAdapter(this, sessions));
        list.setOnItemClickListener(this);
    }

    private void calculateTotal(){
        int total = 0;
        for (Session session : sessions) {
            total += (int) session.amount;
        }
        amount.setText(getString(R.string.strTotal,total));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();
        if(itemId == R.id.item_query){
            Intent i = new Intent(this,QueryAct.class);
            i.putExtra("com.mylaneza.jamarte.PARENT",0);
            startActivityForResult(i,1);
            return true;
        }else {
            return false;
        }
    }

    public void openNewSession(View v){
        startActivityForResult(new Intent(this, NewSession.class),0);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        com.mylaneza.jamarte.entities.Session p = sessions[i];
        Intent intent = new Intent(this, NewSession.class);
        intent.putExtra("com.mylaneza.jamarte.ID",p.id);
        startActivityForResult( intent, 0 );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK){
            try(DBHelper db = new DBHelper(this)) {
                SharedPreferences sp = getSharedPreferences("com.mylaneza.jamarte.PREFERENCIAS", MODE_PRIVATE);
                if (requestCode == 0) { //NewSession
                    String query = sp.getString("com.mylaneza.jamarte.SP_SESION_ESCUELA", null);
                    sessions = db.getSesionesBy(query);
                } else { //QueryAct
                    String escuela = data.getStringExtra("com.mylaneza.jamarte.OPCION");
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("com.mylaneza.jamarte.SP_SESION_ESCUELA", escuela);
                    editor.apply();
                    if (escuela != null && !"Todos".equals(escuela)) {
                        this.sessions = db.getSesionesBy(escuela);
                    } else {
                        this.sessions = db.getSesiones();
                    }
                }
            }
            SessionsAdapter ap = (SessionsAdapter) list.getAdapter();
            ap.sessions = sessions;
            ap.notifyDataSetChanged();
            calculateTotal();
        }else{
            Log.i("RESULT CODE",""+resultCode);
        }

    }
}
