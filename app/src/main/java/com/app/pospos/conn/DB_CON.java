package com.app.pospos.conn;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class  DB_CON {
    private static final String LOG = "DEBUG";
    private static String ip = "141.164.96.15";
    private static String port = "1455";
    private static String classs = "net.sourceforge.jtds.jdbc.Driver";
    private static String db = "NAMPAPA_VTE";
    private static String un = "npp_vte";
    private static String password = "nppvte@2022";
    private static Context context;


    public static Connection CONN(){
        Connection conn = null;
        String ConnURL = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://" + ip +":"+port+";"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    + password + ";";
            conn = DriverManager.getConnection(ConnURL);

        } catch (SQLException e) {
            Log.d(LOG, e.getMessage());
        } catch (ClassNotFoundException e) {

            Log.d(LOG, e.getMessage());
        }
        return conn;
    }
}
