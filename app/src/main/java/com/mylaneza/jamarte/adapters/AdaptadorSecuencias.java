package com.mylaneza.jamarte.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mylaneza.jamarte.R;
import com.mylaneza.jamarte.entities.Sequence;

/**
 * Created by mylaneza on 12/08/2018.
 */
public class AdaptadorSecuencias extends BaseAdapter {

    Sequence secuencias[];
    Context ctx;

    public AdaptadorSecuencias(Context ctx, Sequence secuencias[]){
        this.ctx = ctx;
        this.secuencias = secuencias;
    }

    @Override
    public int getCount() {
        return secuencias.length;
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
            row = inflater.inflate(R.layout.rowsecuencia , viewGroup, false);
        }else{
            row = view;
        }
        TextView nombre = row.findViewById(R.id.rsnombre);
        Sequence s = secuencias[i];
        //Log.i("Paso",p.toString());
        nombre.setText(s.name);

        return row;
    }
}
