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
public class StepsAdapter extends BaseAdapter{

    public Step[] steps;
    Activity ctx;

    public StepsAdapter(Activity ctx, Step[] steps){
        this.ctx = ctx;
        this.steps = steps;
    }

    @Override
    public int getCount() {
        return steps.length;
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
        TextView name = row.findViewById(R.id.rowPasoNombre);
        TextView count = row.findViewById(R.id.rowPasoCuenta);
        TextView base = row.findViewById(R.id.rowPasoBase);
        TextView leader = row.findViewById(R.id.rowPasoLider);
        TextView follower = row.findViewById(R.id.rowPasoFollower);
        TextView id = row.findViewById(R.id.rowPasoId);
        Step p = steps[i];
        name.setText(p.name);
        count.setText(ctx.getString(R.string.strInteger,p.count));
        base.setText(p.base);
        leader.setText(p.descriptionLeader);
        follower.setText(p.descriptionFollower);
        id.setText(ctx.getString(R.string.strInteger,p.id));
        return row;
    }
}
