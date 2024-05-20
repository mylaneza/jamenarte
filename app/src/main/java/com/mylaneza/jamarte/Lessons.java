package com.mylaneza.jamarte;


import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.adapters.LessonsAdapter;

import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Lesson;

import com.mylaneza.jamarte.forms.NewLesson;



public class Lessons extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView list;
    Lesson[] lessons;

    String query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecciones);
        list = findViewById(R.id.listLecciones);
        SharedPreferences sp = getSharedPreferences("com.mylaneza.jamarte.PREFERENCIAS",MODE_PRIVATE);
        query = sp.getString("com.mylaneza.jamarte.SP_LECCION_ESCUELA",null);

        try(DBHelper db = new DBHelper(this)){
            if(query == null)
                lessons = db.getLecciones();
            else if(!query.equals("Todos"))
                lessons = db.getLeccionesDeEscuela(query);
            else
                lessons = db.getLecciones();
            list.setAdapter(new LessonsAdapter(this, lessons));
            list.setOnItemClickListener(this);
        }


        /* Activity Register */
         /*arl = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();

                        }
                    }
                });*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
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

    public void openNewLesson(View v){
        startActivityForResult(new Intent(this, NewLesson.class),0);
        //arl.launch(new Intent(this, NewLesson.class));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Lesson lesson = lessons[i];
        Intent intent = new Intent(this, NewLesson.class);
        intent.putExtra("com.mylaneza.jamarte.ID",lesson.id);
        startActivityForResult( intent,0);
        //arl.launch(intent);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK){
            if(requestCode == 0) { //Opcion de cambio de escuela
                try(DBHelper db = new DBHelper(this)){
                    if(query == null)
                        lessons = db.getLecciones();
                    else if(!query.equals("Todos"))
                        lessons = db.getLeccionesDeEscuela(query);
                    else
                        lessons = db.getLecciones();
                    LessonsAdapter ap = (LessonsAdapter) list.getAdapter();
                    ap.lessons = lessons;
                    ap.notifyDataSetChanged();
                }
            }else{
                String school = data.getStringExtra("com.mylaneza.jamarte.OPCION");
                try(DBHelper db = new DBHelper(this)){
                    SharedPreferences sp = getSharedPreferences("com.mylaneza.jamarte.PREFERENCIAS",MODE_PRIVATE);
                    SharedPreferences.Editor  editor = sp.edit();
                    editor.putString("com.mylaneza.jamarte.SP_LECCION_ESCUELA",school);
                    editor.apply();
                    if(school != null && !"Todos".equals(school)){
                        Lesson[] lessons = db.getLeccionesDeEscuela(school);
                        if(lessons.length > 0){
                            this.lessons = lessons;
                            LessonsAdapter ap = (LessonsAdapter) list.getAdapter();
                            ap.lessons = this.lessons;
                            ap.notifyDataSetChanged();
                        }else{
                            Toast.makeText(this,"No se encontraron sesiones para esa escuela.",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        this.lessons = db.getLecciones();
                        LessonsAdapter ap = (LessonsAdapter) list.getAdapter();
                        ap.lessons = this.lessons;
                        ap.notifyDataSetChanged();
                    }
                }


            }
        }
    }
}