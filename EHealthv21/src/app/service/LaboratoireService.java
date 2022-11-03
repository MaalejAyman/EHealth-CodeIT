/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.service;

import Database.Database;
import app.entity.Laboratoire;
import app.entity.Service;
import app.entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author meria
 */
public class LaboratoireService {

    Connection cnx;
    PreparedStatement ste;
    ServiceUser su = new ServiceUser();

    public LaboratoireService() {
        cnx = Database.getInstance().getConn();
    }

    public List<Laboratoire> getLaboratoires() throws SQLException {
        List<Laboratoire> ls = new ArrayList<Laboratoire>();
        Statement st = cnx.createStatement();
        String req = "SELECT * FROM Laboratoire";
        ResultSet rs = st.executeQuery(req);
        while (rs.next()) {
            ServiceUser us = new ServiceUser();
            ServiceService ss = new ServiceService();
            User u = us.getUserById(rs.getInt(1));
            List<Service> s = ss.getServicesByIdLab(rs.getInt(1));
            ls.add(new Laboratoire(rs.getInt(1), u, rs.getInt(2), rs.getString(3), rs.getTime(4), rs.getTime(5), new HashSet<Service>(s)));
        }
        return ls;
    }
   public Laboratoire getLaboratoireById(int id) throws SQLException {
        Laboratoire laboratoire = new Laboratoire();
        String sql = " select * from laboratoire where id=" + id;
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        ServiceUser su = new ServiceUser();
        if (rs.next()) {
            User u = su.getUserById(rs.getInt(1));
             laboratoire = new Laboratoire(rs.getInt(1), u, rs.getInt(2), rs.getString(3), rs.getTime(4), rs.getTime(5));
        }
        return laboratoire;
    }
    public void ajouterLaboratoire(Laboratoire l) {
        try {
            PreparedStatement ste1;
            DateFormat format3 = new SimpleDateFormat("h:mm", Locale.ENGLISH);
            String heureDebut = format3.format(l.getHoraireDebut());
            String heureFin = format3.format(l.getHoraireFin());
            int i = su.ajouterUser(l.getUser());
            String sql = "INSERT INTO `laboratoire` (`id`, `num_service`, `nom`, `horaire_debut`, `horaire_fin`, `note`) VALUES (?, ?, ?, '" + heureDebut + "', '" + heureFin + "', 0)";
            if (i != -1) {
                ste1 = cnx.prepareStatement(sql);
                ste1.setInt(1, i);
                ste1.setInt(2, l.getNumService());
                ste1.setString(3, l.getNom());
                ste1.executeUpdate();
                for (Object s : l.getServices()) {
                    ajouterLabService(new Laboratoire(i), (Service) s);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void ajouterLabService(Laboratoire l, Service s) {
        try {
            PreparedStatement ste2;
            String sql = "INSERT INTO `laboratoire_service` (`laboratoire_id`, `service_id`) VALUES ('"+l.getId()+"', '"+s.getId()+"') ";
            ste2 = cnx.prepareStatement(sql);
            System.out.println(l.getId());
            ste2.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }
    public void DeleteLaboratoire(int id) throws SQLException{
        String SQL="DELETE FROM laboratoire WHERE `id` = "+id;
        ServiceUser su = new ServiceUser();
        su.DeleteUser(id);
        PreparedStatement ste = cnx.prepareStatement(SQL);
        ste.executeUpdate();
    }
    
    public void updateLaboratoire(Laboratoire l) {
        try {
            int i = su.updateUser(l.getUser());
            DateFormat format3 = new SimpleDateFormat("h:mm", Locale.ENGLISH);
            String heureDebut = format3.format(l.getHoraireDebut());
            String heureFin = format3.format(l.getHoraireFin());
            String sql = "UPDATE laboratoire SET nom = '"+l.getNom()+"', horaire_debut = '" + heureDebut + "', horaire_fin = '" + heureFin + "', num_service = '"+l.getNumService()+"' where id = "+l.getId();
            if (i != -1) {
                ste = cnx.prepareStatement(sql);
                ste.executeUpdate();
                 for (Object s : l.getServices()) {
                    ajouterLabService(new Laboratoire(i), (Service) s);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
}
