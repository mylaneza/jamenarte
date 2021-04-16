package com.mylaneza.jamarte;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.mylaneza.jamarte.adapters.AdaptadorMiembros;
import com.mylaneza.jamarte.adapters.AdaptadorPasos;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Miembro;
import com.mylaneza.jamarte.entities.Sesion;
import com.mylaneza.jamarte.forms.NewAvance;
import com.mylaneza.jamarte.forms.NewLista;
import com.mylaneza.jamarte.forms.NewSesion;

import java.util.Vector;

public class SesionesEstudiantes extends AppCompatActivity implements AdapterView.OnItemClickListener, PopupMenu.OnMenuItemClickListener {
    //Haber
    Miembro lista[];
    GridView list;
    long id;
    Sesion sesion;
    long selectedMember;
    Miembro nacho;

    Vector<Miembro> followers = new Vector<Miembro>();
    Vector<Miembro> lideres = new Vector<Miembro>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesiones_estudiantes);
        list = (GridView) findViewById(R.id.listAsistencia);
        DBHelper db = new DBHelper(this);

        id = getIntent().getLongExtra("com.mylaneza.jamarte.ID",-1);
        if(id == -1){
            Toast.makeText(this,"Esta sesion no existe.", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            sesion = db.getSesion(id);
            setTitle(sesion.escuela+" "+sesion.numero);
        }
        lista = actualizaLista(db);


        asignaParejas();
        //Log.i("ID",""+id);
        //Log.i("Lista",""+lista.length);
        list.setAdapter(new AdaptadorMiembros(this,lista));
        list.setOnItemClickListener(this);

    }

    private Miembro[] actualizaLista(DBHelper db){
        Miembro lista[] = db.getListas(id);
        for(int i = 0 ; i < lista.length ; i++){
            lista[i].avancesGenero = db.getTotalAvancesGenero(lista[i].id,lista[i].genero);
        }
        return lista;
    }

    private void asignaParejas(){
        Vector<Miembro> mushashas = new Vector<Miembro>();
        Vector<Miembro> mushashos = new Vector<Miembro>();

        for(int i = 0 ; i < lista.length ; i++){
            if(lista[i].genero == 0) {
                mushashos.add(lista[i]);
            }else {
                mushashas.add(lista[i]);
            }
        }

        boolean sobra = false;
        if((lista.length % 2) != 0){
            //sobra una persona
            sobra = true;
            remueveNacho(mushashos);
        }
        ordenaLista(mushashos,mushashas,sobra);
    }

    private void ordenaLista(Vector<Miembro> mushashos,Vector<Miembro> mushashas,boolean sobra){
        int cuantasMushashas = mushashas.size();
        int cuantosMushashos = mushashos.size();

        if(cuantosMushashos > cuantasMushashas){

        }else if(cuantasMushashas > cuantosMushashos){

        }else{
            for(int i = 0 ; i < cuantosMushashos ; i++){
                lista[i] = mushashos.elementAt(i);
                lista[i+1] = mushashas.elementAt(i);
            }
        }

        if(sobra){
            lista[lista.length-1] = nacho;
        }
    }

    private void remueveNacho(Vector<Miembro> mushashos){
        int length = mushashos.size();
        for(int i = 0 ; i < length ; i++ ){
            if("Nacho".equals(mushashos.elementAt(i).nickname)){
                nacho = mushashos.remove(i);
                return;
            }
        }
    }

    private void rotanLideres(){

    }

    private void rotanFollowers(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if( item.getItemId() == R.id.item_new ){
            Intent intent = new Intent(this, NewLista.class);
            intent.putExtra("com.mylaneza.jamarte.ID",id);
            startActivityForResult(intent,0);
            return true;
        }else
            return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectedMember = lista[i].id;
        //Toast.makeText(this,lista[i].nickname,Toast.LENGTH_SHORT).show();
        //DBHelper db = new DBHelper(this);
        /*PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.roll, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();*/

        Intent intent = new Intent(this, Avances.class);
        intent.putExtra("com.mylaneza.jamarte.ID",lista[i].id);
        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        DBHelper db = new DBHelper(this);
        lista = db.getListas(id);
        AdaptadorMiembros ap = (AdaptadorMiembros) list.getAdapter();
        ap.miembros = lista;
        ap.notifyDataSetChanged();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch( item.getItemId() ){
            case R.id.item_lider:

                return true;
            case R.id.item_follower:
                return true;
        }
        return false;
    }
}
