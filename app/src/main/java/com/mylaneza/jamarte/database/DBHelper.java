package com.mylaneza.jamarte.database;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;


import com.mylaneza.jamarte.entities.Avance;
import com.mylaneza.jamarte.entities.Leccion;
import com.mylaneza.jamarte.entities.Miembro;
import com.mylaneza.jamarte.entities.Paso;
import com.mylaneza.jamarte.entities.Secuencia;
import com.mylaneza.jamarte.entities.SecuenciaPaso;
import com.mylaneza.jamarte.entities.Sesion;
import com.mylaneza.jamarte.entities.SesionEstudiante;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Vector;


/**
 * Created by mylaneza on 07/07/2018.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "JamArte";
    //private static final String NUMERIC_TYPE = " NUMERIC";
    //private static final String TEXT_TYPE = " TEXT";
    //private static final String COMMA_SEP = ",";

    private static final String SQL_CREA_LECCION =
            "CREATE TABLE " + DBContract.Lesson.TABLE + " (" +
                    DBContract.Lesson._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    DBContract.Lesson.COL_NOMBRE + " TEXT,"+
                    DBContract.Lesson.COL_ESCUELA+ " TEXT,"+
                    DBContract.Lesson.COL_OBJETIVO + " TEXT,"+
                    DBContract.Lesson.COL_DESCRIPCION + " TEXT,"+
                    DBContract.Lesson.COL_NIVEL+" TEXT)";

    private static final String SQL_CREA_LISTAPASOSLECCION =
            "CREATE TABLE " + DBContract.LessonStepList.TABLE + " (" +
                    DBContract.LessonStepList.COL_LECCION + " INTEGER,"+
                    DBContract.LessonStepList.COL_STEP + " INTEGER)";


    private static final String SQL_CREA_MIEMBRO =
            "CREATE TABLE " + DBContract.Member.TABLE + " (" +
                    DBContract.Member._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    DBContract.Member.COL_NOMBRE + " TEXT,"+
                    DBContract.Member.COL_GENERO + " INTEGER,"+
                    DBContract.Member.COL_APELLIDOP+ " TEXT,"+
                    DBContract.Member.COL_APELLIDOM+" TEXT,"+
                    DBContract.Member.COL_CUMPLE+" TEXT,"+
                    DBContract.Member.COL_NICK+" TEXT)";

    private static final String SQL_CREA_PASO =
            "CREATE TABLE " + DBContract.Steps.TABLE + " (" +
                    DBContract.Steps._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    DBContract.Steps.COL_NOMBRE + " TEXT,"+
                    DBContract.Steps.COL_BASE+ " TEXT,"+
                    DBContract.Steps.COL_CUENTA+" INTEGER,"+
                    DBContract.Steps.COL_DESCRIPCION_LIDER+" TEXT,"+
                    DBContract.Steps.COL_DESCRIPCION_FOLLOWER+" TEXT,"+
                    DBContract.Steps.COL_PATH+" TEXT)";

    private static final String SQL_CREA_SESION =
            "CREATE TABLE " + DBContract.Sessions.TABLE + " (" +
                    DBContract.Sessions._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    DBContract.Sessions.COL_FECHA + " TEXT,"+
                    DBContract.Sessions.COL_AHORRO+ " NUMERIC,"+
                    DBContract.Sessions.COL_ESCUELA+ " TEXT,"+
                    DBContract.Sessions.COL_NUMERO+" INTEGER)";

    private static final String SQL_CREA_AVANCE =
            "CREATE TABLE " + DBContract.Advance.TABLE + " (" +
                    DBContract.Advance.COL_MIEMBRO + " INTEGER,"+
                    DBContract.Advance.COL_LECCION+ " INTEGER,"+
                    DBContract.Advance.COL_ROL+" INTEGER)"; //0 - ninguno, 1 - follower, 2 - lider, 3 - ambos

    private static final String SQL_CREA_LISTA =
            "CREATE TABLE " + DBContract.AsistanceList.TABLE + " (" +
                    DBContract.AsistanceList.COL_MIEMBRO + " INTEGER,"+
                    DBContract.AsistanceList.COL_SESION+ " INTEGER)";

    private static final String SQL_CREA_SECUENCIAS =
            "CREATE TABLE " + DBContract.Sequence.TABLE + " (" +
                    DBContract.Sequence._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    DBContract.Sequence.COL_LECCION + " INTEGER,"+
                    DBContract.Sequence.COL_NAME+" TEXT)";

    private static final String SQL_CREA_SECUENCIA_PASO =
            "CREATE TABLE " + DBContract.SequenceStep.TABLE + " (" +
                    DBContract.SequenceStep._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    DBContract.SequenceStep.COL_SECUENCIA+" INTEGER,"+
                    DBContract.SequenceStep.COL_ORDEN+" INTEGER,"+
                    DBContract.SequenceStep.COL_REPETICION+" INTEGER,"+
                    DBContract.SequenceStep.COL_DETALLES+" TEXT,"+
                    DBContract.SequenceStep.COL_PASO+" INTEGER)";


    private static final String SQL_CREA_VIEW_AVANCES =
            "CREATE VIEW V_AVANCES AS" +
                    " SELECT a._ID AS MIEMBRO_ID,a.NICKNAME," +
                    "b._id as LECCION_ID," +
                    "b.nivel AS LECCION_NIVEL," +
                    "b.nombre AS LECCION_NOMBRE," +
                    "b.escuela AS LECCION_ESCUELA,"+
                    "c.rol AS AVANCE_ROL "+
                    "FROM MIEMBROS AS a, LECCIONES as b, AVANCES as c WHERE a._id = c.miembro and b._id == c.leccion";

    private static final String SQL_DESTRUYE_VIEW_AVANCES =
            "DROP VIEW IF EXISTS V_AVANCES";
    private static final String SQL_DESTRUYE_MIEMBROS =
            "DROP TABLE IF EXISTS " + DBContract.Member.TABLE;

    private static final String SQL_DESTRUYE_PASOS =
            "DROP TABLE IF EXISTS " + DBContract.Steps.TABLE;
    private static final String SQL_DESTRUYE_SESIONES =
            "DROP TABLE IF EXISTS " + DBContract.Sessions.TABLE;
    private static final String SQL_DESTRUYE_AVANCES =
            "DROP TABLE IF EXISTS " + DBContract.Advance.TABLE;
    private static final String SQL_DESTRUYE_LISTAS =
            "DROP TABLE IF EXISTS " + DBContract.AsistanceList.TABLE;
    private static final String SQL_DESTRUYE_SECUENCIAS =
            "DROP TABLE IF EXISTS " +DBContract.Sequence.TABLE;
    private static final String SQL_DESTRUYE_LISTAPASOSLECCION =
            "DROP TABLE IF EXISTS " +DBContract.LessonStepList.TABLE;
    private static final String SQL_DESTRUYE_LECCIONES =
            "DROP TABLE IF EXISTS " +DBContract.Lesson.TABLE;
    private static final String SQL_DESTRUYE_SECUENCIA_PASO =
            "DROP TABLE IF EXISTS " +DBContract.SequenceStep.TABLE;



    public DBHelper(Context context ) {
        super(context, DATABASE_NAME +  ".db", null, DATABASE_VERSION);
        //alterTable();
    }

    public void updateDatabase(){
        SQLiteDatabase db = getReadableDatabase();

        //destruye
        db.execSQL(SQL_DESTRUYE_AVANCES);
        db.execSQL(SQL_DESTRUYE_PASOS);
        db.execSQL(SQL_DESTRUYE_SESIONES);
        db.execSQL(SQL_DESTRUYE_LISTAS);
        db.execSQL( SQL_DESTRUYE_SECUENCIAS);
        db.execSQL( SQL_DESTRUYE_LECCIONES );
        db.execSQL( SQL_DESTRUYE_LISTAPASOSLECCION );
        db.execSQL( SQL_DESTRUYE_SECUENCIA_PASO );
        //crea

        db.execSQL( SQL_CREA_PASO );
        db.execSQL( SQL_CREA_SESION );
        db.execSQL( SQL_CREA_AVANCE );
        db.execSQL( SQL_CREA_LISTA );
        db.execSQL( SQL_CREA_SECUENCIAS);
        db.execSQL( SQL_CREA_LECCION );
        db.execSQL( SQL_CREA_LISTAPASOSLECCION );
        db.execSQL( SQL_CREA_SECUENCIA_PASO);
        close();

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL( SQL_CREA_MIEMBRO );
        sqLiteDatabase.execSQL( SQL_CREA_PASO );
        sqLiteDatabase.execSQL( SQL_CREA_SESION );
        sqLiteDatabase.execSQL( SQL_CREA_AVANCE );
        sqLiteDatabase.execSQL( SQL_CREA_LISTA );
        sqLiteDatabase.execSQL( SQL_CREA_SECUENCIAS);
        sqLiteDatabase.execSQL( SQL_CREA_LECCION );
        sqLiteDatabase.execSQL( SQL_CREA_LISTAPASOSLECCION );
        sqLiteDatabase.execSQL( SQL_CREA_SECUENCIA_PASO );
        sqLiteDatabase.execSQL(SQL_CREA_VIEW_AVANCES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DESTRUYE_AVANCES);
        sqLiteDatabase.execSQL(SQL_DESTRUYE_PASOS);
        sqLiteDatabase.execSQL(SQL_DESTRUYE_MIEMBROS);
        sqLiteDatabase.execSQL(SQL_DESTRUYE_SESIONES);
        sqLiteDatabase.execSQL(SQL_DESTRUYE_LISTAS);
        sqLiteDatabase.execSQL( SQL_DESTRUYE_SECUENCIAS);
        sqLiteDatabase.execSQL( SQL_DESTRUYE_LECCIONES );
        sqLiteDatabase.execSQL( SQL_DESTRUYE_LISTAPASOSLECCION );
        onCreate(sqLiteDatabase);
    }

    public long insertaMiembro(Miembro miembro){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Member.COL_NOMBRE, miembro.nombre );
        values.put(DBContract.Member.COL_APELLIDOP, miembro.apellidop);
        values.put(DBContract.Member.COL_APELLIDOM,miembro.apellidom);
        values.put(DBContract.Member.COL_NICK,miembro.nickname);
        values.put(DBContract.Member.COL_CUMPLE,miembro.cumple);
        values.put(DBContract.Member.COL_GENERO,miembro.genero);
        long id = db.insert( DBContract.Member.TABLE, null, values);
        close();
        return id;
    }


    public Miembro[] getMiembros(){

        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        String[] projection = {
                DBContract.Member._ID,
                DBContract.Member.COL_NOMBRE,
                DBContract.Member.COL_APELLIDOP,
                DBContract.Member.COL_APELLIDOM,
                DBContract.Member.COL_NICK,
                DBContract.Member.COL_CUMPLE,
                DBContract.Member.COL_GENERO


        };

        String orderBy = DBContract.Member.COL_NICK+" ASC";

        Cursor c = db.query(
                DBContract.Member.TABLE,  // The table to query
                projection,                               // The columns to return
                null,// The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                orderBy// The sort order
        );
        int length = c.getCount();
        Miembro[] miembros = new Miembro[length];
        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            Miembro m = new Miembro();
            m.id = c.getInt(0);
            m.nombre = c.getString(1);
            m.apellidop = c.getString(2);
            m.apellidom = c.getString(3);
            m.nickname = c.getString(4);
            m.cumple = c.getString(5);
            m.genero = c.getInt(6);
            miembros[i]= m;
        }
        c.close();
        close();
        return miembros;
    }

    public Miembro getMiembro(long id){

        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {

                DBContract.Member.COL_NOMBRE,
                DBContract.Member.COL_APELLIDOP,
                DBContract.Member.COL_APELLIDOM,
                DBContract.Member.COL_NICK,
                DBContract.Member.COL_CUMPLE,
                DBContract.Member.COL_GENERO


        };

        Cursor c = db.query(
                DBContract.Member.TABLE,  // The table to query
                projection,                               // The columns to return
                DBContract.Member._ID + "="+id,// The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null// The sort order
        );
        Miembro m = null;
        if(c.moveToNext()){
            m = new Miembro();
            m.id = id;
            m.nombre = c.getString(0);
            m.apellidop = c.getString(1);
            m.apellidom = c.getString(2);
            m.nickname = c.getString(3);
            m.cumple = c.getString(4);
            m.genero = c.getInt(5);
        }
        c.close();
        close();
        return m;
    }

    public boolean updateMiembro(Miembro m ) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( DBContract.Member.COL_NICK, m.nickname );
        values.put(DBContract.Member.COL_NOMBRE, m.nombre);
        values.put(DBContract.Member.COL_APELLIDOP,m.apellidop);
        values.put(DBContract.Member.COL_APELLIDOM,m.apellidom);
        values.put(DBContract.Member.COL_CUMPLE,m.cumple);
        values.put(DBContract.Member.COL_GENERO,m.genero);
        int rows = db.update(DBContract.Member.TABLE , values, DBContract.Member._ID + "=" + m.id , null);
        close();
        return rows == 1;
    }

    public long insertaPaso(Paso paso){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Steps.COL_NOMBRE, paso.nombre );
        values.put(DBContract.Steps.COL_BASE,paso.base);
        values.put(DBContract.Steps.COL_CUENTA,paso.cuenta);
        values.put(DBContract.Steps.COL_DESCRIPCION_FOLLOWER,paso.descripcionFollower);
        values.put(DBContract.Steps.COL_DESCRIPCION_LIDER,paso.descripcionLider);
        values.put(DBContract.Steps.COL_PATH,paso.path);
        long id = db.insert( DBContract.Steps.TABLE, null, values);
        close();
        return id;
    }

    public boolean updatePaso(Paso paso){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Steps.COL_NOMBRE, paso.nombre );
        values.put(DBContract.Steps.COL_BASE,paso.base);
        values.put(DBContract.Steps.COL_CUENTA,paso.cuenta);
        values.put(DBContract.Steps.COL_DESCRIPCION_FOLLOWER,paso.descripcionFollower);
        values.put(DBContract.Steps.COL_DESCRIPCION_LIDER,paso.descripcionLider);
        values.put(DBContract.Steps.COL_PATH,paso.path);
        int rows = db.update( DBContract.Steps.TABLE, values, DBContract.Steps._ID+"="+paso.id,null);
        close();
        return rows==1;
    }

    public Paso[] getPasos(){

        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        String[] projection = {
                DBContract.Steps._ID,
                DBContract.Steps.COL_NOMBRE,
                DBContract.Steps.COL_CUENTA,
                DBContract.Steps.COL_PATH,
                DBContract.Steps.COL_BASE,
                DBContract.Steps.COL_DESCRIPCION_LIDER,
                DBContract.Steps.COL_DESCRIPCION_FOLLOWER
        };

        String orderBy = DBContract.Steps.COL_NOMBRE+" ASC,"+ DBContract.Steps.COL_CUENTA+" DESC";

        Cursor c = db.query(
                DBContract.Steps.TABLE,  // The table to query
                projection,                               // The columns to return
                null,// The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                orderBy// The sort order
        );
        int length = c.getCount();
        Paso[] pasos = new Paso[length];
        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            Paso p = new Paso();
            p.id = c.getInt(0);
            p.nombre = c.getString(1);
            p.cuenta = c.getInt(2);
            p.path = c.getString(3);
            p.base = c.getString(4);
            p.descripcionLider = c.getString(5);
            p.descripcionFollower = c.getString(6);
            pasos[i]= p;
        }
        c.close();
        close();
        return pasos;
    }

    public Paso[] getPasosBy(String column,String value,boolean isStringValue){

        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        String[] projection = {
                DBContract.Steps._ID,
                DBContract.Steps.COL_NOMBRE,
                DBContract.Steps.COL_CUENTA,
                DBContract.Steps.COL_PATH,
                DBContract.Steps.COL_BASE,
                DBContract.Steps.COL_DESCRIPCION_LIDER,
                DBContract.Steps.COL_DESCRIPCION_FOLLOWER
        };

        String orderBy = DBContract.Steps.COL_NOMBRE+" ASC,"+ DBContract.Steps.COL_CUENTA+" DESC";

        String sqlValue = value;
        if(isStringValue){
            sqlValue = "\""+value+"\"";
        }

        Cursor c = db.query(
                DBContract.Steps.TABLE,  // The table to query
                projection,                               // The columns to return
                column+"="+sqlValue,// The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                orderBy// The sort order
        );
        int length = c.getCount();
        Paso[] pasos = new Paso[length];
        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            Paso p = new Paso();
            p.id = c.getInt(0);
            p.nombre = c.getString(1);
            p.cuenta = c.getInt(2);
            p.path = c.getString(3);
            p.base = c.getString(4);
            p.descripcionLider = c.getString(5);
            p.descripcionFollower = c.getString(6);
            pasos[i]= p;
        }
        c.close();
        close();
        return pasos;
    }

    public long[] getPasosDeLeccionIds(long id){
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                DBContract.LessonStepList.COL_STEP
        };
        String orderBy = DBContract.LessonStepList.COL_STEP+" ASC";
        Cursor c = db.query(
                DBContract.LessonStepList.TABLE,  // The table to query
                projection,                               // The columns to return
                DBContract.LessonStepList.COL_LECCION+"="+id,// The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                orderBy// The sort order
        );
        int length = c.getCount();
        long[] pasos = new long[length];
        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            pasos[i] = c.getInt(0);
        }
        c.close();
        close();
        return pasos;
    }


    public Vector<Paso> getPasosDeLeccion(long id){
        long[] stepsIds = getPasosDeLeccionIds(id);
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        Vector<Paso> pasos = new Vector<>();
        for( long stepId : stepsIds){
            pasos.add(getPaso(stepId));
        }

        return pasos;
    }


    public Paso getPaso(long id){

        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        String[] projection = {

                DBContract.Steps.COL_NOMBRE,
                DBContract.Steps.COL_CUENTA,
                DBContract.Steps.COL_PATH,
                DBContract.Steps.COL_BASE,
                DBContract.Steps.COL_DESCRIPCION_LIDER,
                DBContract.Steps.COL_DESCRIPCION_FOLLOWER



        };


        Cursor c = db.query(
                DBContract.Steps.TABLE,  // The table to query
                projection,                               // The columns to return
                DBContract.Steps._ID+"="+id,// The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null// The sort order
        );

        Paso p = null;
           if( c.moveToNext()){
            p = new Paso();
            p.id = id;
               p.nombre = c.getString(0);
            p.cuenta = c.getInt(1);
            p.path = c.getString(2);
            p.base = c.getString(3);
            p.descripcionLider = c.getString(4);
            p.descripcionFollower = c.getString(5);

        }
        c.close();
        close();
        return p;
    }

    public long insertaSesion(Sesion sesion){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Sessions.COL_NUMERO, sesion.numero );
        values.put(DBContract.Sessions.COL_AHORRO, sesion.monto);
        values.put(DBContract.Sessions.COL_FECHA,sesion.fecha);
        values.put(DBContract.Sessions.COL_ESCUELA,sesion.escuela);
        long id = db.insert( DBContract.Sessions.TABLE, null, values);
        close();
        return id;
    }

    public boolean updateSesion(Sesion sesion) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( DBContract.Sessions.COL_NUMERO, sesion.numero);
        values.put(DBContract.Sessions.COL_AHORRO, sesion.monto);
        values.put(DBContract.Sessions.COL_FECHA,sesion.fecha);
        values.put(DBContract.Sessions.COL_ESCUELA,sesion.escuela);
        int rows = db.update(DBContract.Sessions.TABLE , values, DBContract.Sessions._ID + "=" + sesion.id , null);
        close();

        return rows == 1;
    }

    public Sesion getSesion(long id){

        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {

                DBContract.Sessions.COL_NUMERO,
                DBContract.Sessions.COL_FECHA,
                DBContract.Sessions.COL_AHORRO,
                DBContract.Sessions.COL_ESCUELA


        };

        Cursor c = db.query(
                DBContract.Sessions.TABLE,  // The table to query
                projection,                               // The columns to return
                DBContract.Sessions._ID + "="+id,// The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null// The sort order
        );
        Sesion m = null;
        if(c.moveToNext()){
            m = new Sesion();
            m.id = id;
            m.numero = c.getInt(0);
            m.fecha = c.getString(1);
            m.monto = c.getDouble(2);
            m.escuela = c.getString(3);
        }
        c.close();
        close();
        return m;
    }

    public Sesion[] getSesiones(){

        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        String[] projection = {
                DBContract.Sessions._ID,
                DBContract.Sessions.COL_AHORRO,
                DBContract.Sessions.COL_FECHA,
                DBContract.Sessions.COL_NUMERO,
                DBContract.Sessions.COL_ESCUELA
        };

        String orderBy = DBContract.Sessions.COL_NUMERO+" DESC";

        Cursor c = db.query(
                DBContract.Sessions.TABLE,  // The table to query
                projection,                               // The columns to return
                null,// The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                orderBy// The sort order
        );
        int length = c.getCount();
        Sesion[] sesiones = new Sesion[length];
        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            Sesion m = new Sesion();
            m.id = c.getInt(0);
            m.monto = c.getDouble(1);
            m.fecha = c.getString(2);
            m.numero = c.getInt(3);
            m.escuela = c.getString(4);


            sesiones[i]= m;
        }
        c.close();
        close();
        return sesiones;
    }

    public Sesion[] getSesionesBy(String escuela){

        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        String[] projection = {
                DBContract.Sessions._ID,
                DBContract.Sessions.COL_AHORRO,
                DBContract.Sessions.COL_FECHA,
                DBContract.Sessions.COL_NUMERO
        };

        String orderBy = DBContract.Sessions.COL_NUMERO+" DESC";

        Cursor c = db.query(
                DBContract.Sessions.TABLE,  // The table to query
                projection,                               // The columns to return
                DBContract.Sessions.COL_ESCUELA + "=\""+escuela+"\"",// The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                orderBy// The sort order
        );
        int length = c.getCount();
        Sesion[] sesiones = new Sesion[length];
        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            Sesion m = new Sesion();
            m.id = c.getInt(0);
            m.monto = c.getDouble(1);
            m.fecha = c.getString(2);
            m.numero = c.getInt(3);
            m.escuela = escuela;


            sesiones[i]= m;
        }
        c.close();
        close();
        return sesiones;
    }

    public long insertaAvance(Avance avance){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Advance.COL_LECCION, avance.leccion );
        values.put(DBContract.Advance.COL_MIEMBRO, avance.miembro);
        values.put(DBContract.Advance.COL_ROL,avance.rol);
        long id = db.insert( DBContract.Advance.TABLE, null, values);
        close();
        return id;
    }

    public boolean updateAvance(Avance avance){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( DBContract.Advance.COL_ROL, avance.rol );

        //Log.i("Avance", DBContract.Advance.COL_LECCION + "=" + avance.leccion +" AND "+DBContract.Advance.COL_MIEMBRO+"="+avance.miembro);
        int rows = db.update(DBContract.Advance.TABLE , values, DBContract.Advance.COL_LECCION + "=" + avance.leccion +" AND "+DBContract.Advance.COL_MIEMBRO+"="+avance.miembro, null);
        close();
        //Log.i("ROWS",""+rows);
        return rows == 1;
    }

    public int getTotalAvancesGenero(long miembroId, int genero){
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        String[] projection = {
                DBContract.Advance.COL_LECCION,
                DBContract.Advance.COL_ROL
        };

        String orderBy = DBContract.Advance.COL_LECCION+" DESC";

        String ROLQUERY = null;
        if(genero == 0){
            ROLQUERY = "("+ DBContract.Advance.COL_ROL+"=1 OR "+DBContract.Advance.COL_ROL+"=3 ) ";
        }else{
            ROLQUERY = "("+ DBContract.Advance.COL_ROL+"=2 OR "+DBContract.Advance.COL_ROL+"=3 ) ";
        }

        Cursor c = db.query(
                DBContract.Advance.TABLE,  // The table to query
                projection,                               // The columns to return
                DBContract.Advance.COL_MIEMBRO+"="+miembroId+" AND "+ROLQUERY,// The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                orderBy// The sort order
        );
        int length = c.getCount();
        Avance[] avances = new Avance[length];
        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            Avance m = new Avance();
            m.miembro = miembroId;
            m.leccion = c.getInt(0);
            m.rol = c.getInt(1);

            avances[i]= m;
        }
        c.close();
        close();
        return avances.length;
    }

    public Avance[] getAvances(long miembroId){

        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        String[] projection = {
                DBContract.Advance.COL_LECCION,
                DBContract.Advance.COL_ROL
        };

        String orderBy = DBContract.Advance.COL_LECCION+" DESC";

        Cursor c = db.query(
                DBContract.Advance.TABLE,  // The table to query
                projection,                               // The columns to return
                DBContract.Advance.COL_MIEMBRO+"="+miembroId,// The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                orderBy// The sort order
        );
        int length = c.getCount();
        Avance[] avances = new Avance[length];
        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            Avance m = new Avance();
            m.miembro = miembroId;
            m.leccion = c.getInt(0);
            m.rol = c.getInt(1);

            avances[i]= m;
        }
        c.close();
        close();
        return avances;
    }

    public long insertaLista(SesionEstudiante lista){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.AsistanceList.COL_MIEMBRO, lista.miembro );
        values.put(DBContract.AsistanceList.COL_SESION, lista.sesion);
        long id = db.insert( DBContract.AsistanceList.TABLE, null, values);
        close();
        return id;
    }

    public SesionEstudiante[] getListas(){
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        String[] projection = {
                DBContract.AsistanceList.COL_MIEMBRO,
                DBContract.AsistanceList.COL_SESION
        };

        String orderBy = DBContract.AsistanceList.COL_MIEMBRO+" DESC";

        Cursor c = db.query(
                DBContract.AsistanceList.TABLE,  // The table to query
                projection,                               // The columns to return
                null,// The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                orderBy// The sort order
        );
        int length = c.getCount();
        SesionEstudiante[] lista = new SesionEstudiante[length];

        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            lista[i] = new SesionEstudiante();
            lista[i].miembro = c.getLong(0);
            lista[i].sesion = c.getLong(1);
        }
        c.close();
        close();
        return lista;
    }

    /**
     *
     * @param sesionId
     * @return
     */
    public Miembro[] getListas(long sesionId){

        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        String[] projection = {
                DBContract.AsistanceList.COL_MIEMBRO
        };

        String orderBy = DBContract.AsistanceList.COL_MIEMBRO+" DESC";

        Cursor c = db.query(
                DBContract.AsistanceList.TABLE,  // The table to query
                projection,                               // The columns to return
                DBContract.AsistanceList.COL_SESION+"="+sesionId,// The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                orderBy// The sort order
        );
        int length = c.getCount();
        long[] lista = new long[length];
        Miembro[] miembros = new Miembro[length];

        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            lista[i] = c.getLong(0);
            miembros[i] = getMiembro(lista[i]);
        }
        c.close();
        close();
        return miembros;
    }

    public long insertaSecuencia(Secuencia secuencia){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Sequence.COL_LECCION, secuencia.leccion);
        values.put(DBContract.Sequence.COL_NAME , secuencia.name);
        long count = db.insert( DBContract.Sequence.TABLE, null, values);
        close();
        return count;
    }

    public boolean updateSecuencia(Secuencia secuencia){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( DBContract.Sequence.COL_LECCION, secuencia.leccion );
        values.put( DBContract.Sequence.COL_NAME, secuencia.name );

        //Log.i("Avance", DBContract.Advance.COL_LECCION + "=" + avance.leccion +" AND "+DBContract.Advance.COL_MIEMBRO+"="+avance.miembro);
        int rows = db.update(DBContract.Sequence.TABLE , values, DBContract.Sequence._ID + "=" + secuencia.id,null);
        close();
        //Log.i("ROWS",""+rows);
        return rows == 1;
    }



    public Secuencia getSecuencia( long secuencia){

        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        String[] projection = {

                DBContract.Sequence.COL_LECCION,
                DBContract.Sequence.COL_NAME
        };

        String orderBy = DBContract.Sequence.COL_NAME+" ASC";

        Cursor c = db.query(
                DBContract.Sequence.TABLE,  // The table to query
                projection,                               // The columns to return
                DBContract.Sequence._ID+"="+secuencia,// The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                orderBy// The sort order
        );


        Secuencia s = null;
        if(c.moveToNext()){

            s = new Secuencia();

            s.id = secuencia;
            s.leccion = c.getLong(0);
            s.name = c.getString(1);

        }
        c.close();
        close();
        return s;
    }

    public Secuencia[] getSecuencias( long leccion){

        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        String[] projection = {

                DBContract.Sequence._ID,
                DBContract.Sequence.COL_NAME
        };

        String orderBy = DBContract.Sequence.COL_NAME+" ASC";

        Cursor c = db.query(
                DBContract.Sequence.TABLE,  // The table to query
                projection,                               // The columns to return
                DBContract.Sequence.COL_LECCION+"="+leccion,// The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                orderBy// The sort order
        );
        int length = c.getCount();

        Secuencia[] secuencias = new Secuencia[length];
        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            Secuencia s = new Secuencia();

            s.id = c.getLong(0);
            s.leccion = leccion;
            s.name = c.getString(1);


            secuencias[i]= s;
        }
        c.close();
        close();
        return secuencias;
    }

    public long insertaSecuenciaPaso(SecuenciaPaso paso){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.SequenceStep.COL_PASO, paso.paso );
        values.put(DBContract.SequenceStep.COL_SECUENCIA, paso.secuencia);
        values.put(DBContract.SequenceStep.COL_DETALLES, paso.detalle);
        values.put(DBContract.SequenceStep.COL_ORDEN, paso.orden);
        values.put(DBContract.SequenceStep.COL_REPETICION,paso.repeticion);
        long id = db.insert( DBContract.SequenceStep.TABLE, null, values);

        return id;
    }

    public boolean updateSecuenciaPaso(SecuenciaPaso paso) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.SequenceStep.COL_PASO, paso.paso );
        values.put(DBContract.SequenceStep.COL_SECUENCIA, paso.secuencia);
        values.put(DBContract.SequenceStep.COL_DETALLES, paso.detalle);
        values.put(DBContract.SequenceStep.COL_ORDEN, paso.orden);
        values.put(DBContract.SequenceStep.COL_REPETICION,paso.repeticion);
        int rows = db.update(DBContract.SequenceStep.TABLE , values, DBContract.SequenceStep._ID + "=" + paso.id , null);
        close();


        return rows == 1;
    }

    public SecuenciaPaso getPasoDeSecuencia(long id) {
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        String[] projection = {

                DBContract.SequenceStep.COL_SECUENCIA,
                DBContract.SequenceStep.COL_ORDEN,
                DBContract.SequenceStep.COL_PASO,
                DBContract.SequenceStep.COL_DETALLES,
                DBContract.SequenceStep.COL_REPETICION
        };

        //String orderBy = DBContract.SequenceStep.COL_ORDEN+" ASC";

        Cursor c = db.query(
                DBContract.SequenceStep.TABLE,  // The table to query
                projection,                               // The columns to return
                DBContract.SequenceStep._ID+"="+id,// The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null// The sort order
        );


        SecuenciaPaso s = new SecuenciaPaso();
        if(c.moveToNext()){
            s.id = id;
            s.secuencia = c.getLong(0);
            s.orden = c.getInt(1);
            s.paso = c.getLong(2);
            s.detalle = c.getString(3);
            s.repeticion = c.getInt(4);
            s.setStep(getPaso(s.paso));
        }else
            s = null;









        c.close();


        close();
        return s;
    }

    public SecuenciaPaso[] getPasosDeSecuencia(long secuencia) {
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        String[] projection = {

                DBContract.SequenceStep._ID,
                DBContract.SequenceStep.COL_ORDEN,
                DBContract.SequenceStep.COL_PASO,
                DBContract.SequenceStep.COL_DETALLES,
                DBContract.SequenceStep.COL_REPETICION
        };

        String orderBy = DBContract.SequenceStep.COL_ORDEN+" ASC";

        Cursor c = db.query(
                DBContract.SequenceStep.TABLE,  // The table to query
                projection,                               // The columns to return
                DBContract.SequenceStep.COL_SECUENCIA+"="+secuencia,// The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                orderBy// The sort order
        );
        int length = c.getCount();

        SecuenciaPaso secuencias[] = new SecuenciaPaso[length];
        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            SecuenciaPaso s = new SecuenciaPaso();

            s.id = c.getLong(0);
            s.orden = c.getInt(1);
            s.paso = c.getLong(2);
            s.detalle = c.getString(3);
            s.repeticion = c.getInt(4);



            secuencias[i]= s;
        }
        c.close();

        for( int i = 0 ; i < length ; i++ ){
            secuencias[i].setStep(getPaso(secuencias[i].paso));
        }
        close();
        return secuencias;
    }

    public long insertaLeccion(Leccion leccion){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Lesson.COL_NOMBRE, leccion.nombre );
        values.put(DBContract.Lesson.COL_ESCUELA, leccion.escuela);
        values.put(DBContract.Lesson.COL_NIVEL, leccion.nivel);
        values.put(DBContract.Lesson.COL_OBJETIVO, leccion.objetivo);
        values.put(DBContract.Lesson.COL_DESCRIPCION, leccion.descripcion);
        long id = db.insert( DBContract.Lesson.TABLE, null, values);
        if(id > -1){
            int pasos = leccion.pasos.size();
            for(int i = 0; i < pasos ; i++){
                insertaPasoLeccion(id,leccion.pasos.elementAt(i).id);
            }
        }
        close();
        return id;
    }

    public void insertaPasoLeccion(long leccionId,long pasoId){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.LessonStepList.COL_LECCION, leccionId );
        values.put(DBContract.LessonStepList.COL_STEP, pasoId);
        db.insert( DBContract.LessonStepList.TABLE, null, values);
        close();
    }

    public boolean updateLeccion(Leccion leccion) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( DBContract.Lesson.COL_NOMBRE, leccion.nombre );
        values.put(DBContract.Lesson.COL_NIVEL, leccion.nivel);
        values.put(DBContract.Lesson.COL_OBJETIVO,leccion.objetivo);
        values.put(DBContract.Lesson.COL_ESCUELA,leccion.escuela);
        values.put(DBContract.Lesson.COL_DESCRIPCION, leccion.descripcion);
        int rows = db.update(DBContract.Lesson.TABLE , values, DBContract.Lesson._ID + "=" + leccion.id , null);
        close();
        deleteStepLessonList(leccion.id);
        int pasos = leccion.pasos.size();
        for(int i = 0; i < pasos ; i++){
            insertaPasoLeccion(leccion.id,leccion.pasos.elementAt(i).id);
        }

        return rows == 1;
    }

    public void deleteStepLessonList(long leccionId){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DBContract.LessonStepList.TABLE,DBContract.LessonStepList.COL_LECCION+"="+leccionId,null);
        close();
    }

    public Leccion getLeccion(long id){

        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {

                DBContract.Lesson.COL_NOMBRE,
                DBContract.Lesson.COL_OBJETIVO,
                DBContract.Lesson.COL_ESCUELA,
                DBContract.Lesson.COL_NIVEL,
                DBContract.Lesson.COL_DESCRIPCION



        };

        Cursor c = db.query(
                DBContract.Lesson.TABLE,  // The table to query
                projection,                               // The columns to return
                DBContract.Lesson._ID + "="+id,// The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null// The sort order
        );
        Leccion m = null;
        if(c.moveToNext()){
            m = new Leccion();
            m.id = id;
            m.nombre = c.getString(0);
            m.objetivo = c.getString(1);
            m.escuela = c.getString(2);
            m.nivel = c.getString(3);
            m.descripcion = c.getString(4);
        }
        c.close();
        m.pasos = getPasosDeLeccion(m.id);

        close();
        return m;
    }

    public Leccion[] getLeccionesDeEscuela(String escuela){

        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        String[] projection = {
                DBContract.Lesson._ID,
                DBContract.Lesson.COL_NIVEL,
                DBContract.Lesson.COL_NOMBRE,
                DBContract.Lesson.COL_OBJETIVO

        };

        String orderBy = DBContract.Lesson._ID+" ASC";

        Cursor c = db.query(
                DBContract.Lesson.TABLE,  // The table to query
                projection,                               // The columns to return
                DBContract.Lesson.COL_ESCUELA+"=\'"+escuela+"\'",// The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                orderBy// The sort order
        );
        int length = c.getCount();

        Leccion[] lecciones = new Leccion[length];

        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            Leccion s = new Leccion();
            s.id = c.getInt(0);
            s.nivel = c.getString(1);
            s.escuela = escuela;
            s.nombre = c.getString(2);
            s.objetivo = c.getString(3);
            lecciones[i]= s;
        }
        c.close();
        close();
        return lecciones;
    }

    public Leccion[] getLecciones(){

        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        String[] projection = {
                DBContract.Lesson._ID,
                DBContract.Lesson.COL_NIVEL,
                DBContract.Lesson.COL_ESCUELA,
                DBContract.Lesson.COL_NOMBRE,
                DBContract.Lesson.COL_OBJETIVO

        };

        String orderBy = DBContract.Lesson.COL_NIVEL+" ASC,"+DBContract.Lesson.COL_NOMBRE+" ASC";

        Cursor c = db.query(
                DBContract.Lesson.TABLE,  // The table to query
                projection,                               // The columns to return
                null,// The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                orderBy// The sort order
        );
        int length = c.getCount();

        Leccion[] lecciones = new Leccion[length];
        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            Leccion s = new Leccion();
            s.id = c.getInt(0);
            s.nivel = c.getString(1);
            s.escuela = c.getString(2);
            s.nombre = c.getString(3);
            s.objetivo = c.getString(4);
            lecciones[i]= s;
        }
        c.close();
        close();
        return lecciones;
    }

    public void exportar(){
        Miembro[] miembros = getMiembros();
        Paso[] pasos = getPasos();
        Leccion[] lecciones = getLecciones();
        //Secuencia[] secuencias = getSecuencias();
        Sesion[] sesions = getSesiones();

        SesionEstudiante[] listas = getListas();
        Vector<Avance[]> avances = new Vector<>();
        File dir = new File(Environment.getExternalStorageDirectory() + "/jamarte");
        if(!dir.exists())
            dir.mkdir();
        File f =new File(Environment.getExternalStorageDirectory() + "/jamarte", "db.txt");
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(f));

            //Backup miembros
            for (Miembro m : miembros) {
                avances.add(getAvances(m.id));
                writer.println(DBContract.Member.TABLE + "\t" + m.id + "\t" + m.nickname + "\t" + m.nombre + "\t" + m.apellidop + "\t" + m.apellidom + "\t" + m.cumple + "\t" + m.genero + "\r\n");
            }

            //Backup pasos
            for( int i = 0 ; i < pasos.length ; i++ ) {
                Paso p = pasos[i];
                writer.println(DBContract.Steps.TABLE+"\t"+p.id+"\t"+p.nombre+"\t"+p.cuenta+"\t"+p.base+"\t"+p.path+"\t"+p.descripcionFollower+"\t"+p.descripcionLider+"\r\n");
            }

            //Backup lecciones
            for( int i = 0 ; i < lecciones.length ; i++ ) {
                Leccion l = lecciones[i];
                writer.println(DBContract.Lesson.TABLE+"\t"+l.id+"\t"+l.nombre+"\t"+l.escuela+"\t"+l.nivel+"\t"+l.objetivo+"\t"+l.descripcion+"\r\n");
            }

            //Backup Lista de Pasos Leccion
            for( int i = 0 ; i < lecciones.length ; i++ ) {
                Leccion l = lecciones[i];
                int length = l.pasos.size();
                for(int j = 0 ; j < length ; j++){
                    Paso p = l.pasos.elementAt(j);
                    writer.println(DBContract.LessonStepList.TABLE+"\t"+l.id+"\t"+p.id+"\r\n");
                }
            }

            //Backup Avances
            int length = avances.size();
            for( int i = 0 ; i < length ; i++ ) {
                Avance[] as = avances.elementAt(i);
                for(int j = 0 ; j < as.length ; j++){
                    Avance a = as[j];
                    writer.println(DBContract.Advance.TABLE+"\t"+a.miembro+"\t"+a.leccion+"\t"+a.rol+"\r\n");
                }

            }

            //Backup Sesiones
            for( int i = 0 ; i < sesions.length ; i++ ) {
                Sesion s = sesions[i];
                writer.println(DBContract.Sessions.TABLE+"\t"+s.id+"\t"+s.numero+"\t"+s.monto+"\t"+s.fecha+"\t"+s.escuela+"\r\n");
            }

            //Backup Lista de Asistencia
            for( int i = 0 ; i < listas.length ; i++ ) {
                SesionEstudiante s = listas[i];
                writer.println(DBContract.AsistanceList.TABLE+"\t"+s.miembro+"\t"+s.sesion+"\r\n");
            }


            //Backup Secuencias
            /*for( int i = 0 ; i < lecciones.length ; i++ ) {
                Leccion l = lecciones[i];
                writer.println(DBContract.Lesson.TABLE+","+l.id+","+l.nombre+","+l.escuela+","+l.nivel+","+l.objetivo+","+l.descripcion);
            }*/


            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Avance[] getListaAvances(String school,long miembro_id){
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        String[] projection = {
                "MIEMBRO_ID",
                "NICKNAME",
                "LECCION_ID",
                "LECCION_NIVEL",
                "LECCION_NOMBRE",
                "LECCION_ESCUELA",
                "AVANCE_ROL"

        };
        String orderBy = "LECCION_ESCUELA ASC,LECCION_NIVEL ASC,LECCION_NOMBRE ASC, NICKNAME ASC";

        Cursor c = db.query(
                "V_AVANCES",  // The table to query
                projection,                               // The columns to return
                "MIEMBRO_ID="+miembro_id+" AND LECCION_ESCUELA=\'"+school+"\'",// The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                orderBy// The sort order
        );
        int length = c.getCount();
        Avance[] avances = new Avance[length];
        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            Avance a = new Avance();
            //a.id = c.getInt(6);
            a.leccion = c.getInt(2);
            a.miembro = c.getInt(0);
            a.rol = c.getInt(6);
            avances[i]= a;
            //Log.i("Avance",a.toString());
        }
        c.close();
        close();
        return avances;
    }

    public void alterTable(){
        SQLiteDatabase db = getReadableDatabase();

        //COLUMNA ESCUELA AGREGADA A LA TABLA SESION 2018-10-05
        //"alter table "+DBContract.Sessions.TABLE+" ADD "+DBContract.Sessions.COL_ESCUELA+" TEXT"

        //COLUMNA GENERO AGREGADA A LA TABLA MIEMBRO 2018-10-06
        //"alter table "+DBContract.Member.TABLE+" ADD "+DBContract.Member.COL_GENERO+" TEXT"

        //
        //db.execSQL( SQL_DESTRUYE_SECUENCIA_PASO);
        //db.execSQL( SQL_CREA_SECUENCIAS);
        //db.execSQL( SQL_CREA_SECUENCIA_PASO );
        db.execSQL(SQL_DESTRUYE_VIEW_AVANCES);
        db.execSQL(SQL_CREA_VIEW_AVANCES);
    }


}
