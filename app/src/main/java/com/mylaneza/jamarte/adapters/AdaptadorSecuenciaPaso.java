package com.mylaneza.jamarte.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mylaneza.jamarte.R;
import com.mylaneza.jamarte.entities.Paso;
import com.mylaneza.jamarte.entities.Secuencia;
import com.mylaneza.jamarte.entities.SecuenciaPaso;

import java.util.Vector;

/**
 * Created by mylaneza on 28/10/2018.
 */
public class AdaptadorSecuenciaPaso extends BaseAdapter {

    public SecuenciaPaso pasos[];
    Activity ctx;

    public AdaptadorSecuenciaPaso(Activity ctx, SecuenciaPaso[] pasos){
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
            row = inflater.inflate(R.layout.rowsecuenciapaso , viewGroup, false);
        }else{
            row = view;
        }
        TextView nombre = row.findViewById(R.id.spNombre);
        TextView repeticion = row.findViewById(R.id.spRepeticion);


        SecuenciaPaso p = pasos[i];
        //Log.i("Paso",p.toString());
        nombre.setText(p.getNombre());
        repeticion.setText(""+p.repeticion);

        return row;
    }
}
