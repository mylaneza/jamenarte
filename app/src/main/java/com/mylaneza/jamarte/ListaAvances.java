package com.mylaneza.jamarte;



import android.os.Bundle;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.view.Menu;
import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Avance;
import com.mylaneza.jamarte.entities.Leccion;
import com.mylaneza.jamarte.entities.Miembro;

import java.util.Hashtable;



public class ListaAvances extends AppCompatActivity  {


    WebView wv;

    private static final String WHITE = "#FFFFFF";
    private static final String RED = "#FF3333";
    private static final String BLUE = "#0080FF";
    private static final String PURPLE = "#9933FF";

    private static final String SP_JAMARTE = "com.mylaneza.jamarte.SP";
    private static final String SP_SCHOOL = "com.mylaneza.jamarte.school";

    private static final String DEF_SCHOOL = "Calavera Swing";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getSharedPreferences(SP_JAMARTE,MODE_PRIVATE);
        String school = sp.getString(SP_SCHOOL, DEF_SCHOOL);
        setTitle(school);
        setContentView(R.layout.activity_lista_avances);
        wv = (WebView)findViewById(R.id.wbList);
        WebSettings webSettings = wv.getSettings();

        loadData(school);

    }

    /**
     *
     *
     * @param school name of school
     */
    private void loadData(String school){
        DBHelper db = new DBHelper(this);
        Miembro[] miembros = db.getMiembros();
        Leccion[] lecciones = db.getLeccionesDeEscuela(school);
        db.close();
        StringBuilder pageHeader = new StringBuilder();
        pageHeader.append("<html>");
        pageHeader.append("<head>");
        pageHeader.append("<style>");
        //pageHeader.append("    html{");
        //pageHeader.append("         height: 100%;");
        //pageHeader.append("    }");
        //pageHeader.append("    body{");
        //pageHeader.append("         min-height: 100vh;");
        //pageHeader.append("    }");
        pageHeader.append("         table, th{");
        pageHeader.append("     border: 1px solid black;");
        pageHeader.append("     border-collapse: collapse;");
        pageHeader.append("     white-space: nowrap;");
        pageHeader.append("}");
        pageHeader.append("td.follow{");
        pageHeader.append("    border: 1px solid black;");
        pageHeader.append("    border-collapse: collapse;");
        pageHeader.append("    background-color: red;");
        pageHeader.append("     white-space: nowrap;");
        pageHeader.append("}");
        pageHeader.append("td.none{");
        pageHeader.append("    background-color: white;");
        pageHeader.append("}");
        pageHeader.append("td.lead{");
        pageHeader.append("    background-color: blue;");
        pageHeader.append("}");
        pageHeader.append("td.both{");
        pageHeader.append("    background-color: purple;");
        pageHeader.append("}");
        pageHeader.append("</style>");
        pageHeader.append("</head>");
        pageHeader.append("<body>");
        pageHeader.append("<table border=\"1\">");
        StringBuilder table = new StringBuilder("<tr><th>Miembro</th>");
        //Log.i("Avances",""+lecciones.length);
        for (Leccion leccione : lecciones) {
            table.append("<th>");
            table.append( leccione.nivel);
            table.append("-");
            table.append(leccione.nombre);
            table.append("</th>");
        }
        table.append("</tr>");
        int count = 1;
        for (Miembro miembro : miembros) {
            //Avance[] arr1 = db.getAvances(miembro.id);
            Avance[] arr1 = db.getListaAvances(school,miembro.id);
            boolean hasBeenPresent = false;
            //Log.i("Miembro",miembro.nickname);
            for(Avance a : arr1 ){

                if(a != null && a.rol != 0){
                    //Log.i("Leccion "+a.leccion,"Rol"+a.rol);
                    hasBeenPresent = true;
                    break;
                }
            }
            if( !hasBeenPresent ){
                continue;
            }

            StringBuilder row = new StringBuilder();
            row.append("<tr><td>");
            row.append(miembro.nickname);
            row.append("</td>");


            Hashtable<Long, Avance> avances = setAvances(arr1);
            for (Leccion leccione : lecciones) {
                Avance avance = avances.get(leccione.id);
                if (avance == null) {
                    /*row.append("<td bgcolor=\"");
                    row.append(WHITE);
                    row.append("\">&nbsp;</td>");*/
                    row.append("<td class=\"none\">&nbsp;</td>");
                }else {
                    switch (avance.rol) {
                        case 0:
                           /* row.append( "<td bgcolor=\"");
                            row.append(WHITE);
                            row.append("\">&nbsp;</td>");*/
                            row.append("<td class=\"none\">&nbsp;</td>");
                            break;
                        case 1:
                            row.append("<td class=\"follow\">&nbsp;</td>");
                            break;
                        case 2:
                            /*row.append("<td bgcolor=\"");
                            row.append(BLUE);
                            row.append("\">&nbsp;</td>");*/
                            row.append("<td class=\"lead\">&nbsp;</td>");
                            break;
                        case 3:
                            /*row.append("<td bgcolor=\"");
                            row.append(PURPLE);
                            row.append("\">&nbsp;</td>");*/
                            row.append("<td class=\"both\">&nbsp;</td>");
                            break;
                        default:
                            row.append("<td class=\"none\">&nbsp;</td>");
                            break;
                    }
                }
            }
            row.append("</tr>");
            table.append(row.toString());


        }

        String pageFooter = "</table>";
        pageFooter+="</body>";
        pageFooter+="</html>";
        String fullHtml = pageHeader.toString()+table.toString()+pageFooter.toString();
        String encodedHtml = Base64.encodeToString(fullHtml.getBytes(),
                Base64.NO_PADDING);
        wv.loadData(encodedHtml,"text/html","base64");

    }

    public Hashtable<Long,Avance> setAvances(Avance[] avances){
        Hashtable<Long,Avance> avn = new Hashtable<Long,Avance>();
        for (Avance avance : avances) {
            avn.put(avance.leccion, avance);
        }
        return avn;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuescuela, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int menuItemId = item.getItemId();
        if( menuItemId == R.id.escCalavera || menuItemId == R.id.escJamArte ){
            String school = item.getTitle().toString();
            SharedPreferences sp = getSharedPreferences(SP_JAMARTE,MODE_PRIVATE);
            sp.edit().putString(SP_SCHOOL,school).apply();
            setTitle(school);
            loadData(school);
            return true;
        }
        return false;
    }


}
