package com.mylaneza.jamarte.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mylaneza.jamarte.entities.MemberSession;

/**
 * Created by mylaneza on 12/08/2018.
 */
public class AssistanceListAdapter extends BaseAdapter{

    MemberSession[] list;
    Context ctx;

    public AssistanceListAdapter(Context ctx, MemberSession[] list){
        this.ctx = ctx;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.length;
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
        return null;
    }
}
