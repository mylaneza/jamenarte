package com.mylaneza.jamarte.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mylaneza.jamarte.entities.Lesson;
import com.mylaneza.jamarte.R;

/**
 * Created by mylaneza on 16/08/2018.
 */
public class LessonsAdapter extends BaseAdapter {

    public Lesson[] lessons;
    Activity ctx;


    public LessonsAdapter(Activity ctx, Lesson[] lessons){
        this.ctx = ctx;
        this.lessons = lessons;
    }
    @Override
    public int getCount() {
        return lessons.length;
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
        TextView name = row.findViewById(R.id.rowLeccionNivelNombre);


        TextView school = row.findViewById(R.id.rowLeccionEscuela);


        TextView objective = row.findViewById(R.id.rowLeccionObjetivo);


        Lesson p = lessons[i];

        name.setText(ctx.getString(R.string.strLessonRow, p.level,p.name));
        school.setText(p.school);
        objective.setText(p.objective);

        return row;
    }
}
