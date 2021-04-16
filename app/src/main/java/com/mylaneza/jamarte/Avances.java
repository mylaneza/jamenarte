package com.mylaneza.jamarte;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mylaneza.jamarte.adapters.AdaptadorAvances;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Avance;
import com.mylaneza.jamarte.entities.Leccion;
import com.mylaneza.jamarte.forms.NewAvance;
import com.mylaneza.jamarte.forms.NewLesson;

public class Avances extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    Avance avances[];
    ListView list;
    Leccion lecciones[];

    long memberId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avances);
        Intent intent  = getIntent();
        memberId = getIntent().getLongExtra("com.mylaneza.jamarte.ID",-1);
        list = (ListView) findViewById(R.id.listAvances);
        DBHelper db = new DBHelper(this);
        Avance avances[] = db.getAvances(memberId);
        Log.i("Avances",""+avances.length);
        lecciones = db.getLeccionesDeEscuela("Jam en Arte");
        list.setAdapter(new AdaptadorAvances(this,avances,lecciones,memberId));
        list.setOnItemClickListener(this);
        list.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Leccion leccion = lecciones[i];
        AdaptadorAvances adaptador = (AdaptadorAvances)list.getAdapter();
        Avance avance = adaptador.getAvance(leccion.id);
        if(avance.rol == 3)
            avance.rol = 0;
        else
            avance.rol++;
        switch(avance.rol){
            case 0:
                view.setBackgroundColor(Color.WHITE);
                break;
            case 1:
                view.setBackgroundColor(Color.RED);
                break;
            case 2:
                view.setBackgroundColor(Color.BLUE);
                break;
            case 3:
                view.setBackgroundColor(Color.MAGENTA);
                break;

        }
        DBHelper dbHelper = new DBHelper(this);
        if(!dbHelper.updateAvance(avance))
            Toast.makeText(this,"No se actualizo el avance",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Leccion lesson = lecciones[i];
        Intent intent = new Intent(this, NewLesson.class);
        intent.putExtra("com.mylaneza.jamarte.ID",lesson.id);
        startActivity( intent);
        return true;
    }
}
