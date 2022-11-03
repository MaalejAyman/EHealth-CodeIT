/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.service;

import Database.Database;
import app.entity.Laboratoire;
import app.entity.Medecin;
import app.entity.Patient;
import app.entity.Service;
import app.entity.Specialite;
import app.entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author meria
 */
public class MedecinService {

    Connection cnx;
    PreparedStatement ste;
    ServiceUser su = new ServiceUser();

    public MedecinService() {
        cnx = Database.getInstance().getConn();
    }

    public List<Medecin> getMedecins() throws SQLException {
        List<Medecin> ls = new ArrayList<Medecin>();
        Statement st = cnx.createStatement();
        String req = "SELECT * FROM medecin";
        ResultSet rs = st.executeQuery(req);
        while (rs.next()) {
            ServiceUser us = new ServiceUser();
            ServiceSpecialite ss = new ServiceSpecialite();
            User u = us.getUserById(rs.getInt(1));
            Specialite s = ss.getSpecialiteById(rs.getInt("specialite_id"));

            ls.add(new Medecin(rs.getInt(1), s, u, rs.getString(2), rs.getString(3), rs.getString(4), rs.getTime(5), rs.getTime(6), rs.getInt(7)));
        }
        return ls;
    }

    /* public List<Medecin> afficherListeMedecin() throws SQLException {
        List<Medecin> listMedecin = new ArrayList<Medecin>();
        String sql = " select * from `medecin`";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        ServiceUser su =new ServiceUser();
        while(rs.next()){
            User u = su.getUserById(rs.getInt(1));
            Medecin m = new Medecin(rs.getInt(1),u, rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5), rs.getBoolean(6));
            listMedecin.add(m);
            
        }
        return listMedecin;
    }*/
    public void ajouterMedecin(Medecin m) {
        try {
            PreparedStatement ste1;
            DateFormat format3 = new SimpleDateFormat("h:mm", Locale.ENGLISH);
            String heureDebut = format3.format(m.getHoraireDebut());
            String heureFin = format3.format(m.getHoraireFin());
            int i = su.ajouterUser(m.getUser());
            String sql = "INSERT INTO `medecin` (`id`, `nom`, `prenom`, `sexe`, `horaire_debut`, `horaire_fin`, `specialite_id`, `num_service`, `note`) VALUES (?, '" + m.getNom() + "', '" + m.getPrenom() + "',  '" + m.getSexe() + "','" + heureDebut + "', '" + heureFin + "', '" + m.getSpecialite().getId() + "', '" + m.getNumService() + "', 0)";
            if (i != -1) {
                ste1 = cnx.prepareStatement(sql);
                ste1.setInt(1, i);
                ste1.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void DeleteMedecin(int id) throws SQLException {
        String SQL = "DELETE FROM medecin WHERE `id` = " + id;
        ServiceUser su = new ServiceUser();
        su.DeleteUser(id);
        PreparedStatement ste = cnx.prepareStatement(SQL);
        ste.executeUpdate();
    }

    public Medecin getMedecinById(int id) throws SQLException {
        Medecin medecin = new Medecin();
        String sql = " select * from medecin where id=" + id;
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        ServiceUser su = new ServiceUser();
        ServiceSpecialite ss = new ServiceSpecialite();

        if (rs.next()) {
            User u = su.getUserById(rs.getInt(1));
            Specialite s = ss.getSpecialiteById(rs.getInt("specialite_id"));

            medecin = new Medecin(rs.getInt(1), s, u, rs.getString(2), rs.getString(3), rs.getString(4), rs.getTime(5), rs.getTime(6), rs.getInt(7));
            medecin.setNote(rs.getInt("note"));
        }
        return medecin;
    }

    public void updateMedecin(Medecin m) {
        try {
            int i = su.updateUser(m.getUser());
            DateFormat format3 = new SimpleDateFormat("h:mm", Locale.ENGLISH);
            String heureDebut = format3.format(m.getHoraireDebut());
            String heureFin = format3.format(m.getHoraireFin());
            String sql = "UPDATE medecin SET nom = '" + m.getNom() + "', prenom = '" + m.getPrenom() + "', horaire_debut = '" + heureDebut + "', horaire_fin = '" + heureFin + "', num_service = '" + m.getNumService() + "', sexe = '" + m.getSexe() + "' where id = " + m.getId();
            if (i != -1) {
                ste = cnx.prepareStatement(sql);
                ste.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    //Anaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaass----------------------------------
    public void calculNote(int id_medecin, int evaluation) throws SQLException {
        System.out.println("Medecin id =" + id_medecin);

        ServiceRendezVous SRDV = new ServiceRendezVous();

        int nb_eval = SRDV.calculerNombreEvaluation(id_medecin);

        Medecin m = getMedecinById(id_medecin);
        float notef = evaluation + m.getNote() / nb_eval;
        int note = Math.round(notef);
        System.out.println("NOTE = " + note);

        String query = "UPDATE  Medecin set note=?  Where id ='" + id_medecin + "'";
        PreparedStatement ste = cnx.prepareStatement(query);

        ste.setInt(1, note);
        ste.executeUpdate();
    }

    public List<Medecin> afficherTopMedecin() throws SQLException {
        List<Medecin> ls = new ArrayList<Medecin>();
        Statement st = cnx.createStatement();
        String req = "SELECT *  FROM medecin  where note >3 ORDER BY note DESC";
        ResultSet rs = st.executeQuery(req);
        while (rs.next()) {
            ServiceUser us = new ServiceUser();
            ServiceSpecialite ss = new ServiceSpecialite();
            User u = us.getUserById(rs.getInt(1));
            Specialite s = ss.getSpecialiteById(rs.getInt("specialite_id"));

            ls.add(new Medecin(rs.getInt(1), s, u, rs.getString(2), rs.getString(3), rs.getString(4), rs.getTime(5), rs.getTime(6), rs.getInt(7)));
        }
        return ls;
    }

}
