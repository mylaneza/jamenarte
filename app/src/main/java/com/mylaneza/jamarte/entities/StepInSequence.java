package com.mylaneza.jamarte.entities;

/**
 * Created by mylaneza on 09/10/2018.
 */
public class StepInSequence {
    public long id;
    public long sequenceId;
    public long stepId;
    public int seqNo;
    public int repetitions;
    public String detail;

    Step p;

    public String getName(){
        return p.name;
    }

    public int getCount(){
        return p.count;
    }

    public String getBase(){
        return p.base;
    }

    public void setStep(Step paso) {
        p = paso;
    }
}
