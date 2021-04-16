package com.mylaneza.jamarte.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mylaneza.jamarte.entities.Leccion;
import com.mylaneza.jamarte.entities.Paso;
import com.mylaneza.jamarte.R;

/**
 * Created by mylaneza on 16/08/2018.
 */
public class AdaptadorLecciones extends BaseAdapter {

    public Leccion lecciones[];
    Activity ctx;


    public AdaptadorLecciones(Activity ctx, Leccion[] lecciones){
        this.ctx = ctx;
        this.lecciones = lecciones;
    }
    @Override
    public int getCount() {
        return lecciones.length;
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
            row = inflater.inflate(R.layout.rowleccion , viewGroup, false);
        }else{
            row = view;
        }
        TextView nombre = row.findViewById(R.id.rowLeccionNivelNombre);


        TextView escuela = row.findViewById(R.id.rowLeccionEscuela);


        TextView objetivo = row.findViewById(R.id.rowLeccionObjetivo);


        Leccion p = lecciones[i];
        //Log.i("Paso",p.toString());
        nombre.setText(p.nivel+"-"+p.nombre);
        escuela.setText(p.escuela);
        objetivo.setText(p.objetivo);


        return row;
    }
}
