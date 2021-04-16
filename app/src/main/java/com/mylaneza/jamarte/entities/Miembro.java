package com.mylaneza.jamarte.entities;

/**
 * Created by mylaneza on 07/07/2018.
 */
public class Miembro {
    public long id;
    public String nombre;
    public String nickname;
    public String apellidop;
    public String apellidom;
    public String cumple;
    public int genero;
    public int rol;
    public int avancesGenero;

    public String toString(){
        return nombre+","+nickname+","+apellidop+","+apellidom+","+cumple;
    }
}
