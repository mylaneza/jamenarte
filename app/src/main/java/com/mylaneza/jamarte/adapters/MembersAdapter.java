package com.mylaneza.jamarte.adapters;

import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mylaneza.jamarte.entities.Member;
import com.mylaneza.jamarte.R;

/**
 * Created by mylaneza on 12/08/2018.
 */
public class MembersAdapter extends BaseAdapter{

    private static final int MALE = 0;
    public Member[] members;
    Activity ctx;
    public MembersAdapter(Activity ctx, Member[] members){
        this.ctx = ctx;
        this.members = members;
    }

    @Override
    public int getCount() {
        return members.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public View getRow(int i, View view, ViewGroup viewGroup){
        View row;
        if( view == null ){
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.rowmiembro , viewGroup, false);
        }else{
            row = view;
        }
        TextView nick = row.findViewById(R.id.txtNick);
        TextView name = row.findViewById(R.id.txtNombre);
        TextView lastNameParent = row.findViewById(R.id.txtApellidoP);
        TextView lastNameMother = row.findViewById(R.id.txtApellidoM);
        TextView birthday = row.findViewById(R.id.txtCumple);
        TextView id = row.findViewById(R.id.txtId);

        Member m = members[i];
        nick.setText(m.nickname);
        name.setText(m.name);
        lastNameParent.setText(m.lastNameParent);
        lastNameMother.setText(m.lastNameMother);
        birthday.setText(m.birthday);
        id.setText(""+m.id);
        return row;
    }

    public View getCell(int i, View view, ViewGroup viewGroup){
        View cell;
        if( view == null ){
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            cell = inflater.inflate(R.layout.cellmiembro , viewGroup, false);
        }else{
            cell = view;
        }
        TextView member = cell.findViewById(R.id.miembroAsistente);
        ImageView image = cell.findViewById(R.id.imgDancer);

        Member m = members[i];
        member.setText(m.nickname);
        if(m.gender == MALE){
            image.setImageResource(R.drawable.dancer);
        }else{
            image.setImageResource(R.drawable.dancerf);
        }
        return cell;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if(parent instanceof GridView){
            return getCell(i,view,parent);
        }else{
            return getRow(i,view,parent);
        }
    }
}
