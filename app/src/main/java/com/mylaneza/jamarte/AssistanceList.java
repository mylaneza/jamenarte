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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.mylaneza.jamarte.adapters.MembersAdapter;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Member;
import com.mylaneza.jamarte.entities.Session;
import com.mylaneza.jamarte.forms.NewAssistanceList;

import java.util.Vector;

public class AssistanceList extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener/*, PopupMenu.OnMenuItemClickListener*/ {
    //Haber
    Member[] membersList;
    GridView list;
    long id;
    Session session;
    long selectedMember;
    Member nacho;


    Vector<Member> followers = new Vector<>();
    Vector<Member> leaders = new Vector<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesiones_estudiantes);
        list =  findViewById(R.id.listAsistencia);
        findViewById(R.id.seNewMember).setOnClickListener(this);
        DBHelper db = new DBHelper(this);

        id = getIntent().getLongExtra("com.mylaneza.jamarte.ID",-1);
        if(id == -1){
            Toast.makeText(this,"Session does not exist", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            session = db.getSesion(id);
            setTitle(session.school +" "+ session.number);
        }
        membersList = updateList(db);
        TextView totalTextView = findViewById(R.id.seTotal);
        totalTextView.setText(getString(R.string.strTotal,membersList.length));
        quickSort(membersList, 0 , membersList.length-1);
        //setCouples();
        list.setAdapter(new MembersAdapter(this, membersList));
        list.setOnItemClickListener(this);

    }

    private void quickSort(Member[] arr, int low, int high) {
        if(low < high){
            int pi = partition(arr,low,high);
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

    private Member[] updateList(DBHelper db){
        Member[] list = db.getListas(id);
        for(Member member : list)
            member.progressByGender = db.getTotalAvancesGenero(member.id,member.gender);
        return list;
    }

    private void setCouples(){
        Vector<Member> women = new Vector<>();
        Vector<Member> men = new Vector<>();

        for (Member member : membersList) {
            if (member.gender == 0) {
                men.add(member);
            } else {
                women.add(member);
            }
        }

        boolean extra = false;
        if((membersList.length % 2) != 0){
            extra = true;
            removeTeacher(men);
        }
        sortList(men,women,extra);
    }

    private void sortList(Vector<Member> men, Vector<Member> women, boolean extra){
        int womenCount = women.size();
        int menCount = men.size();

        if(menCount > womenCount){

        }else if(womenCount > menCount){

        }else{
            for(int i = 0 ; i < menCount ; i++){
                membersList[i] = men.elementAt(i);
                membersList[i+1] = women.elementAt(i);
            }
        }

        if(extra){
            membersList[membersList.length-1] = nacho;
        }
    }

    private void removeTeacher(Vector<Member> men){
        int length = men.size();
        for(int i = 0 ; i < length ; i++ ){
            if("Nacho".equals(men.elementAt(i).nickname)){
                nacho = men.remove(i);
                return;
            }
        }
    }

    private void changeLeaders(){

    }

    private void changeFollowers(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        /*if( item.getItemId() == R.id.item_new ){
            Intent intent = new Intent(this, NewAssistanceList.class);
            intent.putExtra("com.mylaneza.jamarte.ID",id);
            startActivityForResult(intent,0);
            return true;
        }else*/
            return false;
    }



    public void openNewAssistanceList(View v){
        Intent intent = new Intent(this, NewAssistanceList.class);
        intent.putExtra("com.mylaneza.jamarte.ID",id);

        long[] list_ids = new long[membersList.length];
        for(int i = 0; i < membersList.length ; i++){
            list_ids[i] = membersList[i].id;
        }
        intent.putExtra("com.mylaneza.jamarte.LIST_IDS",list_ids);
        startActivityForResult(intent,0);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectedMember = membersList[i].id;
        //Toast.makeText(this,list[i].nickname,Toast.LENGTH_SHORT).show();
        //DBHelper db = new DBHelper(this);
        /*PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.roll, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();*/

        Intent intent = new Intent(this, ProgressScreen.class);
        intent.putExtra("com.mylaneza.jamarte.ID", membersList[i].id);
        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK ) {
            try(DBHelper db = new DBHelper(this)){
                membersList = db.getListas(id);

                TextView totalTextView = findViewById(R.id.seTotal);
                totalTextView.setText(getString(R.string.strTotal,membersList.length));

                quickSort(membersList, 0 , membersList.length-1);
                MembersAdapter ap = (MembersAdapter) list.getAdapter();
                ap.members = membersList;
                ap.notifyDataSetChanged();
            }

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
            openNewAssistanceList(v);
        }
    }
}
