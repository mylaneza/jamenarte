package com.mylaneza.jamarte;



import android.os.Bundle;
import android.content.SharedPreferences;
import android.util.Base64;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.view.Menu;
import androidx.appcompat.app.AppCompatActivity;

import com.mylaneza.jamarte.database.DBHelper;
import com.mylaneza.jamarte.entities.Progress;
import com.mylaneza.jamarte.entities.Lesson;
import com.mylaneza.jamarte.entities.Member;

import java.util.Hashtable;



public class ProgressList extends AppCompatActivity  {


    WebView wv;

    public static final String WHITE = "#FFFFFF";
    public static final String RED = "#FF3333";
    public static final String BLUE = "#0080FF";
    public static final String PURPLE = "#9933FF";

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
        Member[] members = db.getMiembros();
        Lesson[] lessons = db.getLeccionesDeEscuela(school);
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
        StringBuilder table = new StringBuilder("<tr><th>Member</th>");

        for (Lesson lesson : lessons) {
            table.append("<th>");
            table.append( lesson.level);
            table.append("-");
            table.append(lesson.name);
            table.append("</th>");
        }
        table.append("</tr>");

        for (Member member : members) {

            Progress[] arr1 = db.getListaAvances(school,member.id);
            boolean hasBeenPresent = false;

            for(Progress a : arr1 ){
                if(a != null && a.rol != 0){
                    hasBeenPresent = true;
                    break;
                }
            }
            if( !hasBeenPresent ){
                continue;
            }

            StringBuilder row = new StringBuilder();
            row.append("<tr><td>");
            row.append(member.nickname);
            row.append("</td>");


            Hashtable<Long, Progress> progressMap = setProgressMap(arr1);
            for (Lesson lesson : lessons) {
                Progress progress = progressMap.get(lesson.id);
                if (progress == null) {
                    /*row.append("<td bgcolor=\"");
                    row.append(WHITE);
                    row.append("\">&nbsp;</td>");*/
                    row.append("<td class=\"none\">&nbsp;</td>");
                }else {
                    switch (progress.rol) {
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
            table.append(row);


        }

        String pageFooter = "</table>";
        pageFooter+="</body>";
        pageFooter+="</html>";
        String fullHtml = pageHeader.toString()+table.toString()+pageFooter.toString();
        String encodedHtml = Base64.encodeToString(fullHtml.getBytes(),
                Base64.NO_PADDING);
        wv.loadData(encodedHtml,"text/html","base64");

    }

    public Hashtable<Long, Progress> setProgressMap(Progress[] progressList){
        Hashtable<Long, Progress> avn = new Hashtable<>();
        for (Progress progress : progressList) {
            avn.put(progress.lessonId, progress);
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
