package com.mylaneza.jamarte.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.mylaneza.jamarte.R;
import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Progress;
import com.mylaneza.jamarte.entities.Lesson;

import java.util.Hashtable;

/**
 * Created by mylaneza on 12/08/2018.
 */
public class ProgressAdapter extends BaseAdapter  {

    private static final int NONE = 0;
    private static final int FOLLOWER = 1;
    private static final int LEADER = 2;
    private static final int BOTH = 3;

    //lesson,progress
    Hashtable<Long, Progress> progressTable;

    Context ctx;
    public Lesson[] lessons;
    long memberId;

    public ProgressAdapter(Context ctx , Progress[] progressArray, Lesson[] lessons, long memberId){
        setProgressTable(progressArray);
        this.ctx = ctx;
        this.lessons = lessons;
        this.memberId = memberId;
    }

    public void setProgressTable(Progress[] progressTable){
        this.progressTable = new Hashtable<>();
        for (Progress progress : progressTable) this.progressTable.put(progress.lessonId, progress);
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
            row = inflater.inflate(R.layout.rowavance , viewGroup, false);
        }else{
            row = view;
        }
        TextView name = row.findViewById(R.id.rowAvanceNivelNombre);

        TextView objective = row.findViewById(R.id.rowAvanceObjetivo);

        Lesson p = lessons[i];

        name.setText(ctx.getString(R.string.strLessonRow, p.level,p.name));
        objective.setText(p.objective);

        Progress progress = progressTable.get(p.id);
        if(progress != null){
            switch (progress.rol){
                case NONE:
                    row.setBackgroundColor(Color.WHITE);
                    name.setTextColor(Color.BLACK);
                    objective.setTextColor(Color.BLACK);
                    break;
                case FOLLOWER:
                    row.setBackgroundColor(Color.RED);
                    name.setTextColor(Color.WHITE);
                    objective.setTextColor(Color.WHITE);
                    break;
                case LEADER:
                    row.setBackgroundColor(Color.BLUE);
                    name.setTextColor(Color.WHITE);
                    objective.setTextColor(Color.WHITE);
                    break;
                case BOTH:
                    row.setBackgroundColor(Color.MAGENTA);
                    name.setTextColor(Color.BLACK);
                    objective.setTextColor(Color.BLACK);
                    break;
                default:
                    break;
            }

        }else{

            progress = new Progress();
            progress.rol = 0;
            progress.lessonId = p.id;
            progress.memberId = memberId;
            try (DBHelper db = new DBHelper(ctx)){
                db.insertaAvance(progress);
                row.setBackgroundColor(Color.WHITE);
                progressTable.put(p.id,progress);
            }
        }

        return row;
    }

    public Progress getProgress(long lessonId){
        return progressTable.get(lessonId);
    }


}
