/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.service;

import Database.Database;
import app.entity.Patient;
import app.entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.chart.PieChart;

/**
 *
 * @author meria
 */
public class ServiceUser {

    Connection cnx;
    PreparedStatement ste;

    public ServiceUser() {
        cnx = Database.getInstance().getConn();
    }

    public int ajouterUser(User u) {

        try {
            String sql = "INSERT INTO `user` (`email`, `roles`, `password`, `adresse`, `ville`, `tel`, `is_verified`, `Type`) VALUES ('" + u.getEmail() + "','" + u.getRoles() + "', ?, ?, ?, ?, true,'" + u.getType() + "')";
            ste = cnx.prepareStatement(sql);
            ste.setString(2, u.getAdresse());
            // ste.setString(1, u.getEmail());
            ste.setString(1, u.getPassword());
            ste.setInt(4, u.getTel());
            ste.setString(3, u.getVille());
            ste.executeUpdate();
            Statement st = cnx.createStatement();
            String sql2 = "Select id from user where email= '" + u.getEmail() + "'";
            ResultSet rs = st.executeQuery(sql2);
            int id = -1;
            if (rs.next()) {
                id = rs.getInt(1);
            }
            return id;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return -1;
    }

    public List<User> afficherListeUsers() throws SQLException {
        List<User> listUser = new ArrayList<User>();
        String sql = " select * from `user`  ";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            User p = new User(rs.getInt("id"), rs.getString("email"), rs.getString("adresse"), rs.getString("ville"), rs.getInt("tel"));
            listUser.add(p);
        }
        return listUser;
    }

    public User getUserById(int id) throws SQLException {
        User ls = new User();
        Statement st = cnx.createStatement();
        String req = "SELECT * FROM user where id=" + id;
        ResultSet rs = st.executeQuery(req);
        if (rs.next()) {
            ls = new User(rs.getInt(1), rs.getString(2), null, rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7), rs.getBoolean(8), rs.getString(9));
        }
        return ls;
    }

    public void DeleteUser(int id) throws SQLException {
        String SQL = "DELETE FROM user WHERE `id` = " + id;
        PreparedStatement ste = cnx.prepareStatement(SQL);
        ste.executeUpdate();
    }
    
    public int updateUser(User u) {
        try {
            //  adiscuter
            String sql = "UPDATE user SET email = ?,  adresse = ?, ville = ? , tel = ? where id = " + u.getId();
            ste = cnx.prepareStatement(sql);
            ste.setString(2, u.getAdresse());
            ste.setString(1, u.getEmail());
            ste.setInt(4, u.getTel());
            ste.setString(3, u.getVille());
            ste.executeUpdate();
            Statement st = cnx.createStatement();
            String sql2 = "Select id from user where email= '" + u.getEmail() + "'";
            ResultSet rs = st.executeQuery(sql2);
            int id = -1;
            if (rs.next()) {
                id = rs.getInt(1);
                System.out.println(id);
            }
            return id;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return -1; 

    }
     public int updateUserPassword(User u) {
        try {
            //  adiscuter
            String sql = "UPDATE user SET password =? where email = '" + u.getEmail() + "'";
            ste = cnx.prepareStatement(sql);
            ste.setString(1, u.getPassword());
            ste.executeUpdate();
            Statement st = cnx.createStatement();
            String sql2 = "Select id from user where email= '" + u.getEmail() + "'";
            ResultSet rs = st.executeQuery(sql2);
            int id = -1;
            if (rs.next()) {
                id = rs.getInt(1);
                System.out.println(id);
            }
            return id;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return -1;

    }

    public User loginUser(User u) {
        try {

            String sql = " select * from user where email ='" + u.getEmail() + "'";
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            User us = new User();
            if (rs.next()) {
                us.setId(rs.getInt("id"));
                us.setEmail(rs.getString("email"));
                us.setPassword(rs.getString("password"));
                us.setType(rs.getString("type"));

            }
            return us;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
    }

    public List<PieChart.Data> pieChartStat() {
        String sql = "select type from user";
        List<PieChart.Data> ls = new ArrayList<PieChart.Data>();
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int numMed = 0;
            int numLab = 0;
            int numPatient = 0;
            while (rs.next()) {
                if (rs.getString("type").equals("medecin")) {
                    numMed++;
                }
                if (rs.getString("type").equals("laboratoire")) {
                    numLab++;
                }
                if (rs.getString("type").equals("patient")) {
                    numPatient++;
                }

            }
            PieChart.Data slice1 = new PieChart.Data("Patient", numPatient);
            PieChart.Data slice2 = new PieChart.Data("Medecin", numMed);
            PieChart.Data slice3 = new PieChart.Data("Laboratoire", numLab);
            ls.add(slice3);
            ls.add(slice2);
            ls.add(slice1);

        } catch (Exception ex) {
            Logger.getLogger(ServiceRendezVous.class.getName()).log(Level.SEVERE, null, ex);

        }
        return ls;
    }
}
