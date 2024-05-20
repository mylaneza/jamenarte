package com.mylaneza.jamarte.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mylaneza.jamarte.R;
import com.mylaneza.jamarte.entities.Session;

/**
 * Created by mylaneza on 12/08/2018.
 */
public class SessionsAdapter extends BaseAdapter{

    public Session[] sessions;
    Context ctx;

    public SessionsAdapter(Context ctx, Session[] sessions){
        this.ctx = ctx;
        this.sessions = sessions;
    }
    @Override
    public int getCount() {
        return sessions.length;
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
        TextView number = row.findViewById(R.id.tvSesionNumero);
        TextView amount = row.findViewById(R.id.tvSesionMonto);
        TextView date = row.findViewById(R.id.tvSesionFecha);
        TextView school = row.findViewById(R.id.tvSesionEscuela);

        Session p = sessions[i];

        number.setText(ctx.getString(R.string.strInteger,p.number));
        amount.setText(ctx.getString(R.string.strDouble,p.amount));
        date.setText(p.date);
        school.setText(p.school);
        return row;
    }
}
