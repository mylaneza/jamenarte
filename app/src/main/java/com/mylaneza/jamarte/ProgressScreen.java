package com.mylaneza.jamarte;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.os.Bundle;

import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.adapters.ProgressAdapter;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Progress;
import com.mylaneza.jamarte.entities.Lesson;

import com.mylaneza.jamarte.forms.NewLesson;

public class ProgressScreen extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    ListView list;
    Lesson[] lessons;

    long memberId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avances);
        memberId = getIntent().getLongExtra("com.mylaneza.jamarte.ID",-1);

        SharedPreferences sp = getSharedPreferences("com.mylaneza.jamarte.PREFERENCIAS",MODE_PRIVATE);
        String school = sp.getString("com.mylaneza.jamarte.SP_LECCION_ESCUELA",null);
        list = findViewById(R.id.listAvances);
        DBHelper db = new DBHelper(this);
        Progress[] avances = db.getAvances(memberId);
        lessons = db.getLeccionesDeEscuela(school);
        list.setAdapter(new ProgressAdapter(this,avances,lessons,memberId));
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
        Lesson leccion = lessons[i];
        ProgressAdapter adaptador = (ProgressAdapter)list.getAdapter();
        Progress avance = adaptador.getProgress(leccion.id);
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
        Lesson lesson = lessons[i];
        Intent intent = new Intent(this, NewLesson.class);
        intent.putExtra("com.mylaneza.jamarte.ID",lesson.id);
        startActivity( intent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();
        /*if( itemId == R.id.item_new ){
            startActivityForResult(new Intent(this, NewLesson.class),0);
            return true;
        }*/ if(itemId == R.id.item_query){
            Intent i = new Intent(this,QueryAct.class);
            i.putExtra("com.mylaneza.jamarte.PARENT",2);
            startActivityForResult(i,1);
            //arl.launch(i);
            return true;
        }else
            return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 0) {
            /*DBHelper db = new DBHelper(this);
            lecciones = db.getLecciones();
            AdaptadorLecciones ap = (AdaptadorLecciones) list.getAdapter();
            ap.lecciones = lecciones;
            ap.notifyDataSetChanged();

            DBHelper db = new DBHelper(this);
            Avance[] avances = db.getAvances(memberId);
            //Log.i("Avances",""+avances.length);
            lecciones = db.getLeccionesDeEscuela(school);
            list.setAdapter(new AdaptadorAvances(this,avances,lecciones,memberId));
            list.setOnItemClickListener(this);
            list.setOnItemLongClickListener(this);*/

        }else{
            if(resultCode == RESULT_OK){
                String escuela = data.getStringExtra("com.mylaneza.jamarte.OPCION");
                DBHelper db = new DBHelper(this);
                SharedPreferences sp = getSharedPreferences("com.mylaneza.jamarte.PREFERENCIAS",MODE_PRIVATE);
                SharedPreferences.Editor  editor = sp.edit();
                editor.putString("com.mylaneza.jamarte.SP_LECCION_ESCUELA",escuela);
                editor.commit();
                if(escuela != null && !"Todos".equals(escuela)){
                    Lesson[] lecciones = db.getLeccionesDeEscuela(escuela);
                    if(lecciones.length > 0){
                        this.lessons = lecciones;
                        ProgressAdapter ap = (ProgressAdapter) list.getAdapter();
                        ap.lessons = this.lessons;
                        ap.notifyDataSetChanged();
                    }else{
                        Toast.makeText(this,"No se encontraron sesiones para esa escuela.",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    this.lessons = db.getLecciones();
                    ProgressAdapter ap = (ProgressAdapter) list.getAdapter();
                    ap.lessons = this.lessons;
                    ap.notifyDataSetChanged();
                }
            }


        }
    }
}
