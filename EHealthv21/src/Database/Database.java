/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author meria
 */
public class Database {

    private static String url = "jdbc:mysql://localhost:3306/ehealthv2";
    private static String user = "root";
    private static String pwd = "";
    private static Connection conn;
    static Database instance;

    private Database() {
        try {
            conn = DriverManager.getConnection(url, user, pwd);
            System.out.println(" Connected to Database");

        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }

    public static Connection getConn() {
        return conn;
    }
}
