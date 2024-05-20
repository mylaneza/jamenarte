package com.mylaneza.jamarte;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.mylaneza.jamarte.adapters.MembersAdapter;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Member;
import com.mylaneza.jamarte.entities.Session;
import com.mylaneza.jamarte.forms.NewAssistanceList;

import java.util.Vector;
import android.util.Log;

public class SesionesEstudiantes extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener/*, PopupMenu.OnMenuItemClickListener*/ {
    //Haber
    Member[] lista;
    GridView list;
    long id;
    Session sesion;
    long selectedMember;
    Member nacho;


    Vector<Member> followers = new Vector<Member>();
    Vector<Member> lideres = new Vector<Member>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesiones_estudiantes);
        list =  findViewById(R.id.listAsistencia);
        findViewById(R.id.seNewMember).setOnClickListener(this);
        DBHelper db = new DBHelper(this);

        id = getIntent().getLongExtra("com.mylaneza.jamarte.ID",-1);
        if(id == -1){
            Toast.makeText(this,"Esta sesion no existe.", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            sesion = db.getSesion(id);
            setTitle(sesion.school +" "+sesion.number);
        }
        lista = actualizaLista(db);
        TextView totalTextView = findViewById(R.id.seTotal);
        totalTextView.setText("Total:"+lista.length);
        quickSort(lista, 0 , lista.length-1);
        //asignaParejas();
        list.setAdapter(new MembersAdapter(this,lista));
        list.setOnItemClickListener(this);

    }

    private void quickSort(Member[] arr, int low, int high) {
        if(low < high){
            int pi = partition(arr,low,high);
            Log.i("Partition Index",""+pi);
            quickSort(arr,low,pi-1);
            quickSort(arr,pi+1, high);
        }
    }

    private int partition(Member[] arr, int low, int high) {
        Member pivot = arr[high];
        int i = low -1;
        for(int j= low; j<=high-1;j++){
            if(arr[j].nickname.compareTo(pivot.nickname) < 0){
                i++;
                Member temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        Member temp = arr[i+1];
        arr[i+1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }

    private Member[] actualizaLista(DBHelper db){
        Member[] lista = db.getListas(id);
        for(Member miembro : lista)
            miembro.progressByGender = db.getTotalAvancesGenero(miembro.id,miembro.gender);
        return lista;
    }

    private void asignaParejas(){
        Vector<Member> mushashas = new Vector<Member>();
        Vector<Member> mushashos = new Vector<Member>();

        for(int i = 0 ; i < lista.length ; i++){
            if(lista[i].gender == 0) {
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

    private void ordenaLista(Vector<Member> mushashos, Vector<Member> mushashas, boolean sobra){
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

    private void remueveNacho(Vector<Member> mushashos){
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
        /*if( item.getItemId() == R.id.item_new ){
            Intent intent = new Intent(this, NewLista.class);
            intent.putExtra("com.mylaneza.jamarte.ID",id);
            startActivityForResult(intent,0);
            return true;
        }else*/
            return false;
    }



    public void openNewLista(View v){
        Intent intent = new Intent(this, NewAssistanceList.class);
        //ID de la sesion
        intent.putExtra("com.mylaneza.jamarte.ID",id);
        //Agregar la lista de participantes ya agregados a la lista
        long[] list_ids = new long[lista.length];
        for(int i = 0; i < lista.length ; i++){
            list_ids[i] = lista[i].id;
        }
        intent.putExtra("com.mylaneza.jamarte.LIST_IDS",list_ids);
        startActivityForResult(intent,0);
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

        Intent intent = new Intent(this, ProgressScreen.class);
        intent.putExtra("com.mylaneza.jamarte.ID",lista[i].id);
        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK ) {
            DBHelper db = new DBHelper(this);
            lista = db.getListas(id);

            TextView totalTextView = findViewById(R.id.seTotal);
            totalTextView.setText("Total:"+lista.length);
            
            quickSort(lista, 0 , lista.length-1);
            MembersAdapter ap = (MembersAdapter) list.getAdapter();
            ap.members = lista;
            ap.notifyDataSetChanged();
        }
    }

    public boolean onMenuItemClick(MenuItem item) {

        switch( item.getItemId() ){
            case R.id.item_lider:

                return true;
            case R.id.item_follower:
                return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.seNewMember){
            openNewLista(v);
        }
    }
}
