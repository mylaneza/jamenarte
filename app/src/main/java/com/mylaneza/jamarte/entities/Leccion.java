package com.mylaneza.jamarte.entities;

import java.util.Vector;

/**
 * Created by mylaneza on 16/08/2018.
 */
public class Leccion {

    public long id;
    public String nivel;
    public String nombre;
    public String escuela;
    public String objetivo;
    public String descripcion;

    public Vector<Paso> pasos = new Vector<Paso>();
}
