package com.mylaneza.jamarte.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mylaneza.jamarte.entities.Step;
import com.mylaneza.jamarte.R;

/**
 * Created by mylaneza on 12/08/2018.
 */
public class AdaptadorPasos extends BaseAdapter{

    public Step pasos[];
    Activity ctx;

    public AdaptadorPasos(Activity ctx, Step pasos[]){
        this.ctx = ctx;
        this.pasos = pasos;
    }

    @Override
    public int getCount() {
        return pasos.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row;
        if( view == null ){
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.rowpaso , viewGroup, false);
        }else{
            row = view;
        }
        TextView nombre = row.findViewById(R.id.rowPasoNombre);

        TextView cuenta = row.findViewById(R.id.rowPasoCuenta);
        TextView base = row.findViewById(R.id.rowPasoBase);

        TextView lider = row.findViewById(R.id.rowPasoLider);
        TextView follower = row.findViewById(R.id.rowPasoFollower);
        TextView id = row.findViewById(R.id.rowPasoId);
        Step p = pasos[i];
        //Log.i("Paso",p.toString());
        nombre.setText(p.name);


        cuenta.setText(""+p.count);
        base.setText(p.base);

        lider.setText(p.descriptionLeader);
        follower.setText(p.descriptionFollower);
        id.setText(""+p.id);
        return row;
    }
}
