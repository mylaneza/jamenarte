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

    Paso p;

    public String getNombre(){
        return p.nombre;
    }

    public int getCuenta(){
        return p.cuenta;
    }

    public String getBase(){
        return p.base;
    }

    public void setStep(Paso paso) {
        p = paso;
    }
}
