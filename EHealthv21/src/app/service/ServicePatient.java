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

/**
 *
 * @author meria
 */
public class ServicePatient {
    
    Connection cnx;
    PreparedStatement ste;
    ServiceUser su = new ServiceUser();
    
    public ServicePatient() {
        cnx = Database.getInstance().getConn();
    }
    
    public void ajouterPatient(Patient p) {
        try {
            int i = su.ajouterUser(p.getUser());
            String sql = "INSERT INTO `patient` (`id`, `nom`, `prenom`, `cin`, `sexe`, `blocked`, `nb_alert`, `blocked_rdv`) VALUES (?, ?, ?, ?, ?, 0, 0, 0)";
            if (i != -1) {
                ste = cnx.prepareStatement(sql);
                ste.setInt(1, i);
                ste.setString(2, p.getNom());
                ste.setString(3, p.getPrenom());
                ste.setString(5, p.getSexe());
                ste.setInt(4, p.getCin());
                ste.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
    public Patient getPatientById(int id) throws SQLException {
        Patient Patient = new Patient();
        String sql = " select * from patient where id=" + id;
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        ServiceUser su = new ServiceUser();
        if (rs.next()) {
            User u = su.getUserById(rs.getInt(1));
            Patient = new Patient(rs.getInt(1), u, rs.getString("nom"), rs.getString("prenom"), rs.getInt("cin"), rs.getString("sexe"), rs.getBoolean(6));
            Patient.setBlocked(rs.getBoolean("blocked"));
        }
        return Patient;
    }
    
    public void updatePatient(Patient p) {
        try {
            int i = su.updateUser(p.getUser());
            String sql = "UPDATE patient SET nom = ?, prenom = ?, cin = ?, sexe = ? where id = " + p.getId();
            if (i != -1) {
                ste = cnx.prepareStatement(sql);
                
                ste.setString(1, p.getNom());
                ste.setString(2, p.getPrenom());
                ste.setString(4, p.getSexe());
                ste.setInt(3, p.getCin());
                ste.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
    public List<Patient> afficherListePatients() throws SQLException {
        List<Patient> listPatient = new ArrayList<Patient>();
        String sql = " select * from `patient`";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        ServiceUser su = new ServiceUser();
        while (rs.next()) {
            User u = su.getUserById(rs.getInt(1));
            Patient p = new Patient(rs.getInt(1), u, rs.getString("nom"), rs.getString("prenom"), rs.getInt("cin"), rs.getString("sexe"), rs.getBoolean(6));
            p.setBlocked(rs.getBoolean("blocked"));
            listPatient.add(p);
            
        }
        return listPatient;
    }
    
    public void DeletePatient(int id) throws SQLException {
        String SQL = "DELETE FROM patient WHERE `id` = " + id;
        ServiceUser su = new ServiceUser();
        su.DeleteUser(id);
        PreparedStatement ste = cnx.prepareStatement(SQL);
        ste.executeUpdate();
    }
    // +-------------------------------Anas work -------------------------------------------------------------------------

    public void blockPatient(int id) throws SQLException {
        String queryblock = "UPDATE  Patient set blocked=1 where id=" + id;
        PreparedStatement ste = cnx.prepareStatement(queryblock);
        ste.executeUpdate();
        System.out.println("Patient bloquée avec succé ");
        
    }
    
    public void deblockPatient(int id) throws SQLException {
        String queryblock = "UPDATE  Patient set blocked=0 where id=" + id;
        PreparedStatement ste = cnx.prepareStatement(queryblock);
        ste.executeUpdate();
        System.out.println("Patient débloquée avec succé ");
        
    }
    
}
