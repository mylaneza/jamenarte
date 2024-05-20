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

import com.mylaneza.jamarte.entities.Miembro;
import com.mylaneza.jamarte.R;

/**
 * Created by mylaneza on 12/08/2018.
 */
public class AdaptadorMiembros extends BaseAdapter{

    public Miembro[] miembros;
    Activity ctx;
    public AdaptadorMiembros(Activity ctx, Miembro[] miembros){
        this.ctx = ctx;
        this.miembros = miembros;
    }

    @Override
    public int getCount() {
        return miembros.length;
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
        TextView nombre = row.findViewById(R.id.txtNombre);
        TextView apellidop = row.findViewById(R.id.txtApellidoP);
        TextView apellidom = row.findViewById(R.id.txtApellidoM);
        TextView cumple = row.findViewById(R.id.txtCumple);
        TextView id = row.findViewById(R.id.txtId);

        Miembro m = miembros[i];
        //Log.i("Miembro",m.toString());
        nick.setText(m.nickname);
        nombre.setText(m.nombre);
        apellidop.setText(m.apellidop);
        apellidom.setText(m.apellidom);
        cumple.setText(m.cumple);
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
        TextView miembro = cell.findViewById(R.id.miembroAsistente);
        ImageView imagen = cell.findViewById(R.id.imgDancer);

        Miembro m = miembros[i];
        //Log.i("Miembro",m.toString());
        miembro.setText(m.nickname);
        if(m.genero == 0){
            imagen.setImageResource(R.drawable.dancer);
        }else{
            imagen.setImageResource(R.drawable.dancerf);
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
