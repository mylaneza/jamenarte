package com.mylaneza.jamarte.database;

import android.provider.BaseColumns;

import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by mylaneza on 07/07/2018.
 */
public class DBContract {

    public static abstract class Lesson implements BaseColumns{
        public static final String TABLE= "lecciones";
        public static final String COL_NIVEL = "nivel";
        public static final String COL_NOMBRE = "nombre";
        public static final String COL_ESCUELA = "escuela";
        public static final String COL_OBJETIVO = "objetivo";
        public static final String COL_DESCRIPCION = "descripcion";
    }

    public static abstract class LessonStepList implements BaseColumns{
        public static final String TABLE = "listadepasosporleccion";
        public static final String COL_LECCION = "leccion";
        public static final String COL_STEP = "step";
    }


    public static abstract class Member implements BaseColumns {
        public static final String TABLE = "miembros";
        public static final String COL_NOMBRE = "nombre";
        public static final String COL_APELLIDOP = "apellidop";
        public static final String COL_APELLIDOM = "apellidom";
        public static final String COL_CUMPLE = "cumple";
        public static final String COL_NICK = "nickname";
        public static final String COL_GENERO = "genero";
    }

    public static abstract class Advance implements BaseColumns {
        public static final String TABLE = "avances";
        public static final String COL_LECCION = "leccion";
        public static final String COL_ROL = "rol";
        public static final String COL_MIEMBRO = "miembro";
    }

    public static abstract class Steps  implements BaseColumns {
        public static final String TABLE = "pasos";
        public static final String COL_NOMBRE = "nombre";
        public static final String COL_CUENTA = "cuenta";
        public static final String COL_BASE = "base";
        public static final String COL_DESCRIPCION_LIDER = "lider";
        public static final String COL_DESCRIPCION_FOLLOWER = "follower";
        public static final String COL_PATH = "path";
    }

    public static abstract class Sessions implements  BaseColumns {
        public static final String TABLE = "jams";
        public static final String COL_FECHA = "fecha";
        public static final String COL_AHORRO = "monto";
        public static final String COL_NUMERO = "numero";
        public static final String COL_ESCUELA = "escuela";
    }

    public static abstract class AsistanceList implements BaseColumns {
        public static final String TABLE = "lista";
        public static final String COL_SESION = "sesion";
        public static final String COL_MIEMBRO = "miembro";
    }

    public static abstract class Sequence implements BaseColumns{
        public static final String TABLE = "secuencias";
        public static final String COL_LECCION = "leccion";
        public static final String COL_NAME = "nombre";

    }

    public static abstract class SequenceStep implements BaseColumns{
        public static final String TABLE = "pasosecuencia";
        public static final String COL_SECUENCIA = "secuencia";
        public static final String COL_PASO = "paso";
        public static final String COL_REPETICION = "repeticion";
        public static final String COL_DETALLES = "detalle";
        public static final String COL_ORDEN = "orden";
    }
}
