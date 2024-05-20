package com.mylaneza.jamarte.entities;

/**
 * Created by mylaneza on 09/10/2018.
 */
public class SecuenciaPaso {
    public long id;
    public long secuencia;
    public long paso;
    public int orden;
    public int repeticion;
    public String detalle;

    Step p;

    public String getNombre(){
        return p.name;
    }

    public int getCuenta(){
        return p.count;
    }

    public String getBase(){
        return p.base;
    }

    public void setStep(Step paso) {
        p = paso;
    }
}
