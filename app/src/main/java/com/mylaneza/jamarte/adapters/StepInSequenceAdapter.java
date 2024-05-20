package com.mylaneza.jamarte.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mylaneza.jamarte.R;
import com.mylaneza.jamarte.entities.StepInSequence;

/**
 * Created by mylaneza on 28/10/2018.
 */
public class StepInSequenceAdapter extends BaseAdapter {

    public StepInSequence[] steps;
    Activity ctx;

    public StepInSequenceAdapter(Activity ctx, StepInSequence[] steps){
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
            row = inflater.inflate(R.layout.rowsecuenciapaso , viewGroup, false);
        }else{
            row = view;
        }
        TextView name = row.findViewById(R.id.spNombre);
        TextView repetitions = row.findViewById(R.id.spRepeticion);
        TextView seqNo = row.findViewById(R.id.spOrden);
        TextView detail = row.findViewById(R.id.spDetalle);
        TextView times = row.findViewById(R.id.spTiempos);
        StepInSequence p = steps[i];
        //Log.i("Paso",p.toString());
        name.setText(p.getName());
        repetitions.setText(ctx.getString(R.string.strInteger,p.repetitions));
        seqNo.setText(ctx.getString(R.string.strInteger,p.seqNo));
        detail.setText(p.detail);
        times.setText(ctx.getString(R.string.strInteger,(p.repetitions *p.getCount())));
        return row;
    }
}
