/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.service;

import Database.Database;
import app.entity.Specialite;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author meria
 */
public class ServiceSpecialite {
      
    Connection cnx;
    PreparedStatement ste;
    
    public ServiceSpecialite() {
        cnx = Database.getInstance().getConn();
    }
    public Specialite getSpecialiteById(int id) throws SQLException {
        Specialite ls = new Specialite();
        Statement st = cnx.createStatement();
        String req = "SELECT * FROM specialite where id="+id;
        ResultSet rs = st.executeQuery(req);
          if(rs.next()){
              ls = new Specialite(rs.getInt(1),rs.getString(2));
          }
        return ls;
   }
   public List<Specialite> getSpecialites() throws SQLException {
        List<Specialite> ls = new ArrayList<Specialite>();
        Statement st = cnx.createStatement();
        String req = "SELECT * FROM specialite";
        ResultSet rs = st.executeQuery(req);
          while(rs.next()){
              ls.add(new Specialite(rs.getInt(1),rs.getString(2)));
          }
        return ls;
   }
   
    public List<String> loadSpecialite() throws SQLException {

        List<String> ls = new ArrayList<String>();
        Statement st = cnx.createStatement();
        String req = "SELECT * FROM Specialite";
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Specialite s = new Specialite(rs.getString(2));
          
            ls.add(s.getNomSpec());
        }
        return ls;
    }

    public List<Specialite> chargerSpecialiteParNom(String nom_spec) throws SQLException {
        List<Specialite> ls = new ArrayList<Specialite>();
        Statement st = cnx.createStatement();
        String req = "SELECT * FROM Specialite WHERE nom_spec = " + nom_spec + ")";
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Specialite s = new Specialite(rs.getString(2));
            ls.add(s);

        }
        return ls;

    }
       public Specialite getSpecialiteByNom(String nom) throws SQLException {

        Specialite s = new Specialite();
       // System.out.println("J'ai entré dans le service"+s);
         // System.out.println(nom);
               
             
        String req = "SELECT * FROM specialite where nom_spec like'"+nom+"'"  ;
        Statement st = cnx.createStatement();
        //ste.setString(1,nom);

        ResultSet rs = st.executeQuery(req);
        if (rs.next()) {
            s = new Specialite(rs.getInt(1), rs.getString(2));
      //  System.out.println("Apres la requete"+s);
        }
        return s;
    }
}
