package com.mylaneza.jamarte.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mylaneza.jamarte.entities.Paso;
import com.mylaneza.jamarte.R;
import com.mylaneza.jamarte.entities.Sesion;

/**
 * Created by mylaneza on 12/08/2018.
 */
public class AdaptadorSesiones extends BaseAdapter{

    public Sesion sesiones[];
    Context ctx;

    public  AdaptadorSesiones(Context ctx, Sesion sesiones[]){
        this.ctx = ctx;
        this.sesiones = sesiones;
    }
    @Override
    public int getCount() {
        return sesiones.length;
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
            row = inflater.inflate(R.layout.rowsesion , viewGroup, false);
        }else{
            row = view;
        }
        TextView numero = row.findViewById(R.id.tvSesionNumero);
        TextView monto = row.findViewById(R.id.tvSesionMonto);
        TextView fecha = row.findViewById(R.id.tvSesionFecha);
        TextView escuela = row.findViewById(R.id.tvSesionEscuela);

        Sesion p = sesiones[i];
        //Log.i("Paso",p.toString());
        numero.setText(""+p.numero);
        monto.setText(""+p.monto);
        fecha.setText(p.fecha);
        escuela.setText(""+p.escuela);
        return row;
    }
}
