package com.mylaneza.jamarte.database;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;


import com.mylaneza.jamarte.entities.Progress;
import com.mylaneza.jamarte.entities.Lesson;
import com.mylaneza.jamarte.entities.Member;
import com.mylaneza.jamarte.entities.Step;
import com.mylaneza.jamarte.entities.Sequence;
import com.mylaneza.jamarte.entities.SecuenciaPaso;
import com.mylaneza.jamarte.entities.Session;
import com.mylaneza.jamarte.entities.MemberSession;

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

    public long insertaMiembro(Member miembro){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Member.COL_NOMBRE, miembro.name);
        values.put(DBContract.Member.COL_APELLIDOP, miembro.lastNameParent);
        values.put(DBContract.Member.COL_APELLIDOM,miembro.lastNameMother);
        values.put(DBContract.Member.COL_NICK,miembro.nickname);
        values.put(DBContract.Member.COL_CUMPLE,miembro.birthday);
        values.put(DBContract.Member.COL_GENERO,miembro.gender);
        long id = db.insert( DBContract.Member.TABLE, null, values);
        close();
        return id;
    }


    public Member[] getMiembros(){

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
        Member[] miembros = new Member[length];
        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            Member m = new Member();
            m.id = c.getInt(0);
            m.name = c.getString(1);
            m.lastNameParent = c.getString(2);
            m.lastNameMother = c.getString(3);
            m.nickname = c.getString(4);
            m.birthday = c.getString(5);
            m.gender = c.getInt(6);
            miembros[i]= m;
        }
        c.close();
        close();
        return miembros;
    }

    public Member getMiembro(long id){

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
        Member m = null;
        if(c.moveToNext()){
            m = new Member();
            m.id = id;
            m.name = c.getString(0);
            m.lastNameParent = c.getString(1);
            m.lastNameMother = c.getString(2);
            m.nickname = c.getString(3);
            m.birthday = c.getString(4);
            m.gender = c.getInt(5);
        }
        c.close();
        close();
        return m;
    }

    public boolean updateMiembro(Member m ) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( DBContract.Member.COL_NICK, m.nickname );
        values.put(DBContract.Member.COL_NOMBRE, m.name);
        values.put(DBContract.Member.COL_APELLIDOP,m.lastNameParent);
        values.put(DBContract.Member.COL_APELLIDOM,m.lastNameMother);
        values.put(DBContract.Member.COL_CUMPLE,m.birthday);
        values.put(DBContract.Member.COL_GENERO,m.gender);
        int rows = db.update(DBContract.Member.TABLE , values, DBContract.Member._ID + "=" + m.id , null);
        close();
        return rows == 1;
    }

    public long insertaPaso(Step paso){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Steps.COL_NOMBRE, paso.name);
        values.put(DBContract.Steps.COL_BASE,paso.base);
        values.put(DBContract.Steps.COL_CUENTA,paso.count);
        values.put(DBContract.Steps.COL_DESCRIPCION_FOLLOWER,paso.descriptionFollower);
        values.put(DBContract.Steps.COL_DESCRIPCION_LIDER,paso.descriptionLeader);
        values.put(DBContract.Steps.COL_PATH,paso.videoFilePath);
        long id = db.insert( DBContract.Steps.TABLE, null, values);
        close();
        return id;
    }

    public boolean updatePaso(Step paso){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Steps.COL_NOMBRE, paso.name);
        values.put(DBContract.Steps.COL_BASE,paso.base);
        values.put(DBContract.Steps.COL_CUENTA,paso.count);
        values.put(DBContract.Steps.COL_DESCRIPCION_FOLLOWER,paso.descriptionFollower);
        values.put(DBContract.Steps.COL_DESCRIPCION_LIDER,paso.descriptionLeader);
        values.put(DBContract.Steps.COL_PATH,paso.videoFilePath);
        int rows = db.update( DBContract.Steps.TABLE, values, DBContract.Steps._ID+"="+paso.id,null);
        close();
        return rows==1;
    }

    public Step[] getPasos(){

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
        Step[] pasos = new Step[length];
        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            Step p = new Step();
            p.id = c.getInt(0);
            p.name = c.getString(1);
            p.count = c.getInt(2);
            p.videoFilePath = c.getString(3);
            p.base = c.getString(4);
            p.descriptionLeader = c.getString(5);
            p.descriptionFollower = c.getString(6);
            pasos[i]= p;
        }
        c.close();
        close();
        return pasos;
    }

    public Step[] getPasosBy(String column, String value, boolean isStringValue){

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
        Step[] pasos = new Step[length];
        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            Step p = new Step();
            p.id = c.getInt(0);
            p.name = c.getString(1);
            p.count = c.getInt(2);
            p.videoFilePath = c.getString(3);
            p.base = c.getString(4);
            p.descriptionLeader = c.getString(5);
            p.descriptionFollower = c.getString(6);
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


    public Vector<Step> getPasosDeLeccion(long id){
        long[] stepsIds = getPasosDeLeccionIds(id);
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        Vector<Step> pasos = new Vector<>();
        for( long stepId : stepsIds){
            pasos.add(getPaso(stepId));
        }

        return pasos;
    }


    public Step getPaso(long id){

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

        Step p = null;
           if( c.moveToNext()){
            p = new Step();
            p.id = id;
               p.name = c.getString(0);
            p.count = c.getInt(1);
            p.videoFilePath = c.getString(2);
            p.base = c.getString(3);
            p.descriptionLeader = c.getString(4);
            p.descriptionFollower = c.getString(5);

        }
        c.close();
        close();
        return p;
    }

    public long insertaSesion(Session sesion){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Sessions.COL_NUMERO, sesion.number);
        values.put(DBContract.Sessions.COL_AHORRO, sesion.amount);
        values.put(DBContract.Sessions.COL_FECHA,sesion.date);
        values.put(DBContract.Sessions.COL_ESCUELA,sesion.school);
        long id = db.insert( DBContract.Sessions.TABLE, null, values);
        close();
        return id;
    }

    public boolean updateSesion(Session sesion) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( DBContract.Sessions.COL_NUMERO, sesion.number);
        values.put(DBContract.Sessions.COL_AHORRO, sesion.amount);
        values.put(DBContract.Sessions.COL_FECHA,sesion.date);
        values.put(DBContract.Sessions.COL_ESCUELA,sesion.school);
        int rows = db.update(DBContract.Sessions.TABLE , values, DBContract.Sessions._ID + "=" + sesion.id , null);
        close();

        return rows == 1;
    }

    public Session getSesion(long id){

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
        Session m = null;
        if(c.moveToNext()){
            m = new Session();
            m.id = id;
            m.number = c.getInt(0);
            m.date = c.getString(1);
            m.amount = c.getDouble(2);
            m.school = c.getString(3);
        }
        c.close();
        close();
        return m;
    }

    public Session[] getSesiones(){

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
        Session[] sesiones = new Session[length];
        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            Session m = new Session();
            m.id = c.getInt(0);
            m.amount = c.getDouble(1);
            m.date = c.getString(2);
            m.number = c.getInt(3);
            m.school = c.getString(4);


            sesiones[i]= m;
        }
        c.close();
        close();
        return sesiones;
    }

    public Session[] getSesionesBy(String escuela){

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
        Session[] sesiones = new Session[length];
        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            Session m = new Session();
            m.id = c.getInt(0);
            m.amount = c.getDouble(1);
            m.date = c.getString(2);
            m.number = c.getInt(3);
            m.school = escuela;


            sesiones[i]= m;
        }
        c.close();
        close();
        return sesiones;
    }

    public long insertaAvance(Progress avance){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Advance.COL_LECCION, avance.lessonId);
        values.put(DBContract.Advance.COL_MIEMBRO, avance.memberId);
        values.put(DBContract.Advance.COL_ROL,avance.rol);
        long id = db.insert( DBContract.Advance.TABLE, null, values);
        close();
        return id;
    }

    public boolean updateAvance(Progress avance){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( DBContract.Advance.COL_ROL, avance.rol );

        //Log.i("Avance", DBContract.Advance.COL_LECCION + "=" + avance.leccion +" AND "+DBContract.Advance.COL_MIEMBRO+"="+avance.miembro);
        int rows = db.update(DBContract.Advance.TABLE , values, DBContract.Advance.COL_LECCION + "=" + avance.lessonId +" AND "+DBContract.Advance.COL_MIEMBRO+"="+avance.memberId, null);
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
        Progress[] avances = new Progress[length];
        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            Progress m = new Progress();
            m.memberId = miembroId;
            m.lessonId = c.getInt(0);
            m.rol = c.getInt(1);

            avances[i]= m;
        }
        c.close();
        close();
        return avances.length;
    }

    public Progress[] getAvances(long miembroId){

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
        Progress[] avances = new Progress[length];
        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            Progress m = new Progress();
            m.memberId = miembroId;
            m.lessonId = c.getInt(0);
            m.rol = c.getInt(1);

            avances[i]= m;
        }
        c.close();
        close();
        return avances;
    }

    public long insertaLista(MemberSession lista){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.AsistanceList.COL_MIEMBRO, lista.memberId);
        values.put(DBContract.AsistanceList.COL_SESION, lista.sessionId);
        long id = db.insert( DBContract.AsistanceList.TABLE, null, values);
        close();
        return id;
    }

    public MemberSession[] getListas(){
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
        MemberSession[] lista = new MemberSession[length];

        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            lista[i] = new MemberSession();
            lista[i].memberId = c.getLong(0);
            lista[i].sessionId = c.getLong(1);
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
    public Member[] getListas(long sesionId){

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
        Member[] miembros = new Member[length];

        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            lista[i] = c.getLong(0);
            miembros[i] = getMiembro(lista[i]);
        }
        c.close();
        close();
        return miembros;
    }

    public long insertaSecuencia(Sequence secuencia){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Sequence.COL_LECCION, secuencia.lessonId);
        values.put(DBContract.Sequence.COL_NAME , secuencia.name);
        long count = db.insert( DBContract.Sequence.TABLE, null, values);
        close();
        return count;
    }

    public boolean updateSecuencia(Sequence secuencia){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( DBContract.Sequence.COL_LECCION, secuencia.lessonId);
        values.put( DBContract.Sequence.COL_NAME, secuencia.name );

        //Log.i("Avance", DBContract.Advance.COL_LECCION + "=" + avance.leccion +" AND "+DBContract.Advance.COL_MIEMBRO+"="+avance.miembro);
        int rows = db.update(DBContract.Sequence.TABLE , values, DBContract.Sequence._ID + "=" + secuencia.id,null);
        close();
        //Log.i("ROWS",""+rows);
        return rows == 1;
    }



    public Sequence getSecuencia(long secuencia){

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


        Sequence s = null;
        if(c.moveToNext()){

            s = new Sequence();

            s.id = secuencia;
            s.lessonId = c.getLong(0);
            s.name = c.getString(1);

        }
        c.close();
        close();
        return s;
    }

    public Sequence[] getSecuencias(long leccion){

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

        Sequence[] secuencias = new Sequence[length];
        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            Sequence s = new Sequence();

            s.id = c.getLong(0);
            s.lessonId = leccion;
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

    public long insertaLeccion(Lesson leccion){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Lesson.COL_NOMBRE, leccion.name);
        values.put(DBContract.Lesson.COL_ESCUELA, leccion.school);
        values.put(DBContract.Lesson.COL_NIVEL, leccion.level);
        values.put(DBContract.Lesson.COL_OBJETIVO, leccion.objective);
        values.put(DBContract.Lesson.COL_DESCRIPCION, leccion.description);
        long id = db.insert( DBContract.Lesson.TABLE, null, values);
        if(id > -1){
            int pasos = leccion.steps.size();
            for(int i = 0; i < pasos ; i++){
                insertaPasoLeccion(id,leccion.steps.elementAt(i).id);
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

    public boolean updateLeccion(Lesson leccion) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( DBContract.Lesson.COL_NOMBRE, leccion.name);
        values.put(DBContract.Lesson.COL_NIVEL, leccion.level);
        values.put(DBContract.Lesson.COL_OBJETIVO,leccion.objective);
        values.put(DBContract.Lesson.COL_ESCUELA,leccion.school);
        values.put(DBContract.Lesson.COL_DESCRIPCION, leccion.description);
        int rows = db.update(DBContract.Lesson.TABLE , values, DBContract.Lesson._ID + "=" + leccion.id , null);
        close();
        deleteStepLessonList(leccion.id);
        int pasos = leccion.steps.size();
        for(int i = 0; i < pasos ; i++){
            insertaPasoLeccion(leccion.id,leccion.steps.elementAt(i).id);
        }

        return rows == 1;
    }

    public void deleteStepLessonList(long leccionId){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DBContract.LessonStepList.TABLE,DBContract.LessonStepList.COL_LECCION+"="+leccionId,null);
        close();
    }

    public Lesson getLeccion(long id){

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
        Lesson m = null;
        if(c.moveToNext()){
            m = new Lesson();
            m.id = id;
            m.name = c.getString(0);
            m.objective = c.getString(1);
            m.school = c.getString(2);
            m.level = c.getString(3);
            m.description = c.getString(4);
        }
        c.close();
        m.steps = getPasosDeLeccion(m.id);

        close();
        return m;
    }

    public Lesson[] getLeccionesDeEscuela(String escuela){

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

        Lesson[] lecciones = new Lesson[length];

        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            Lesson s = new Lesson();
            s.id = c.getInt(0);
            s.level = c.getString(1);
            s.school = escuela;
            s.name = c.getString(2);
            s.objective = c.getString(3);
            lecciones[i]= s;
        }
        c.close();
        close();
        return lecciones;
    }

    public Lesson[] getLecciones(){

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

        Lesson[] lecciones = new Lesson[length];
        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            Lesson s = new Lesson();
            s.id = c.getInt(0);
            s.level = c.getString(1);
            s.school = c.getString(2);
            s.name = c.getString(3);
            s.objective = c.getString(4);
            lecciones[i]= s;
        }
        c.close();
        close();
        return lecciones;
    }

    public void exportar(){
        Member[] miembros = getMiembros();
        Step[] pasos = getPasos();
        Lesson[] lecciones = getLecciones();
        //Secuencia[] secuencias = getSecuencias();
        Session[] sesions = getSesiones();

        MemberSession[] listas = getListas();
        Vector<Progress[]> avances = new Vector<>();
        File dir = new File(Environment.getExternalStorageDirectory() + "/jamarte");
        if(!dir.exists())
            dir.mkdir();
        File f =new File(Environment.getExternalStorageDirectory() + "/jamarte", "db.txt");
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(f));

            //Backup miembros
            for (Member m : miembros) {
                avances.add(getAvances(m.id));
                writer.println(DBContract.Member.TABLE + "\t" + m.id + "\t" + m.nickname + "\t" + m.name + "\t" + m.lastNameParent + "\t" + m.lastNameMother + "\t" + m.birthday + "\t" + m.gender + "\r\n");
            }

            //Backup pasos
            for( int i = 0 ; i < pasos.length ; i++ ) {
                Step p = pasos[i];
                writer.println(DBContract.Steps.TABLE+"\t"+p.id+"\t"+p.name +"\t"+p.count +"\t"+p.base+"\t"+p.videoFilePath +"\t"+p.descriptionFollower +"\t"+p.descriptionLeader +"\r\n");
            }

            //Backup lecciones
            for( int i = 0 ; i < lecciones.length ; i++ ) {
                Lesson l = lecciones[i];
                writer.println(DBContract.Lesson.TABLE+"\t"+l.id+"\t"+l.name +"\t"+l.school +"\t"+l.level +"\t"+l.objective +"\t"+l.description +"\r\n");
            }

            //Backup Lista de Pasos Leccion
            for( int i = 0 ; i < lecciones.length ; i++ ) {
                Lesson l = lecciones[i];
                int length = l.steps.size();
                for(int j = 0 ; j < length ; j++){
                    Step p = l.steps.elementAt(j);
                    writer.println(DBContract.LessonStepList.TABLE+"\t"+l.id+"\t"+p.id+"\r\n");
                }
            }

            //Backup Avances
            int length = avances.size();
            for( int i = 0 ; i < length ; i++ ) {
                Progress[] as = avances.elementAt(i);
                for(int j = 0 ; j < as.length ; j++){
                    Progress a = as[j];
                    writer.println(DBContract.Advance.TABLE+"\t"+a.memberId +"\t"+a.lessonId +"\t"+a.rol+"\r\n");
                }

            }

            //Backup Sesiones
            for( int i = 0 ; i < sesions.length ; i++ ) {
                Session s = sesions[i];
                writer.println(DBContract.Sessions.TABLE+"\t"+s.id+"\t"+s.number +"\t"+s.amount +"\t"+s.date +"\t"+s.school +"\r\n");
            }

            //Backup Lista de Asistencia
            for( int i = 0 ; i < listas.length ; i++ ) {
                MemberSession s = listas[i];
                writer.println(DBContract.AsistanceList.TABLE+"\t"+s.memberId +"\t"+s.sessionId +"\r\n");
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

    public Progress[] getListaAvances(String school, long miembro_id){
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
        Progress[] avances = new Progress[length];
        for( int i = 0 ; i < length ; i++ ){
            c.moveToNext();
            Progress a = new Progress();
            //a.id = c.getInt(6);
            a.lessonId = c.getInt(2);
            a.memberId = c.getInt(0);
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
