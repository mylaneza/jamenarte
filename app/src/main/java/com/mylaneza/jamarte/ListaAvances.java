package com.mylaneza.jamarte;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Avance;
import com.mylaneza.jamarte.entities.Leccion;
import com.mylaneza.jamarte.entities.Miembro;

import java.util.Hashtable;

public class ListaAvances extends AppCompatActivity {


    WebView wv;

    private static final String WHITE = "#FFFFFF";
    private static final String RED = "#FF3333";
    private static final String BLUE = "#0080FF";
    private static final String PURPLE = "#9933FF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_avances);
        wv = (WebView)findViewById(R.id.wbList);

        DBHelper db = new DBHelper(this);
        Miembro miembros[] = db.getMiembros();
        Leccion lecciones[] = db.getLeccionesDeEscuela("Jam en Arte");
        String pageHeader = "<html>";
        pageHeader+="<head>";
       pageHeader+="<style>";
       pageHeader+="         table, th, td {";
       pageHeader+="     border: 1px solid black;";
       pageHeader+="     border-collapse: collapse;";
        pageHeader+="     white-space: nowrap;";
        pageHeader+="}";
        pageHeader+="</style>";
                pageHeader+="</head>";
        pageHeader+="<body>";
        pageHeader+="<table >";
        String table = "<tr><th>Miembro</th>";
        //Log.i("Avances",""+lecciones.length);
        for(int j = 0; j < lecciones.length; j++)
            table+="<th>"+lecciones[j].nivel+"-"+lecciones[j].nombre+"</th>";
        table+="</tr>";

        for(int i = 0 ; i < miembros.length; i++){
            table+="<tr><td>"+miembros[i].nickname+"</td>";
            Hashtable<Long,Avance> avances = setAvances(db.getAvances(miembros[i].id));
            for(int j = 0; j < lecciones.length; j++){
                Avance avance = avances.get(lecciones[j].id);
                if(avance == null)
                    table+="<td bgcolor=\""+WHITE+"\">&nbsp;</td>";
                else{
                    switch(avance.rol){
                        case 0:
                            table+="<td bgcolor=\""+WHITE+"\">&nbsp;</td>";
                            break;
                        case 1:
                            table+="<td bgcolor=\""+RED+"\">&nbsp;</td>";
                            break;
                        case 2:
                            table+="<td bgcolor=\""+BLUE+"\">&nbsp;</td>";
                            break;
                        case 3:
                            table+="<td bgcolor=\""+PURPLE+"\">&nbsp;</td>";
                            break;
                        default:
                            table+="<td bgcolor=\""+WHITE+"\">&nbsp;</td>";
                            break;
                    }
                }
            }
            table+="</tr>";
            //Avance avances[] = db.getAvances(miembros[i].id);
        }

        String pageFooter = "</table>";
        pageFooter+="</body>";
        pageFooter+="</html>";
        //Log.i("Loading",""+miembros.length);
        wv.loadData(pageHeader+table+pageFooter,"text/html","UTF-8");

    }

    public Hashtable<Long,Avance> setAvances(Avance[] avances){
        Hashtable<Long,Avance> avn = new Hashtable<Long,Avance>();
        for(int i = 0; i < avances.length;i++)
            avn.put(avances[i].leccion,avances[i]);
        return avn;
    }
}
