package com.mylaneza.jamarte.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import com.mylaneza.jamarte.R;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Avance;
import com.mylaneza.jamarte.entities.Leccion;

import java.util.Hashtable;

/**
 * Created by mylaneza on 12/08/2018.
 */
public class AdaptadorAvances extends BaseAdapter  {


    //Leccion,Avance
    Hashtable<Long,Avance> avances;

    Context ctx;
    public Leccion[] lecciones;
    long memberId;

    public AdaptadorAvances(Context ctx , Avance[] avances,Leccion lecciones[],long memberId){
        setAvances(avances);
        this.ctx = ctx;
        this.lecciones = lecciones;
        this.memberId = memberId;
    }

    public void setAvances(Avance[] avances){
        this.avances = new Hashtable<Long,Avance>();
        for(int i = 0; i < avances.length;i++)
            this.avances.put(avances[i].leccion,avances[i]);
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
            row = inflater.inflate(R.layout.rowavance , viewGroup, false);
        }else{
            row = view;
        }
        TextView nombre = row.findViewById(R.id.rowAvanceNivelNombre);

        TextView objetivo = row.findViewById(R.id.rowAvanceObjetivo);


        Leccion p = lecciones[i];

        //Log.i("Paso",p.toString());
        nombre.setText(p.nivel+"-"+p.nombre);
        objetivo.setText(p.objetivo);

        Avance avance = avances.get(p.id);
        if(avance != null){
            switch (avance.rol){
                case 0: // ni lider ni follower
                    row.setBackgroundColor(Color.WHITE);
                    nombre.setTextColor(Color.BLACK);
                    objetivo.setTextColor(Color.BLACK);
                    //lider.setChecked(false);
                    //follower.setChecked(false);
                    break;
                case 1: //solo follower
                    row.setBackgroundColor(Color.RED);
                    nombre.setTextColor(Color.WHITE);
                    objetivo.setTextColor(Color.WHITE);
                    //follower.setChecked(true);
                    //lider.setChecked(false);
                    break;
                case 2: // solo lider
                    row.setBackgroundColor(Color.BLUE);
                    nombre.setTextColor(Color.WHITE);
                    objetivo.setTextColor(Color.WHITE);
                    break;
                case 3: // lider y follower
                    row.setBackgroundColor(Color.MAGENTA);
                    nombre.setTextColor(Color.BLACK);
                    objetivo.setTextColor(Color.BLACK);
                    break;
                default:
                    break;
            }

        }else{

            avance = new Avance();
            avance.rol = 0;
            avance.leccion = p.id;
            avance.miembro = memberId;
            DBHelper db = new DBHelper(ctx);
            db.insertaAvance(avance);
            row.setBackgroundColor(Color.WHITE);
            avances.put(p.id,avance);
        }

        return row;
    }

    public Avance getAvance(long leccionId){
        return avances.get(leccionId);
    }


}
