package com.mylaneza.jamarte.entities;

/**
 * Created by mylaneza on 07/07/2018.
 */
public class Paso {
    public long id;
    public String nombre;
    public int cuenta;
    public String base;
    public String descripcionLider;
    public String descripcionFollower;
    public String path;

    public String toString(){
        return nombre+","+cuenta+","+base+","+descripcionLider+","+descripcionFollower;
    }

}
