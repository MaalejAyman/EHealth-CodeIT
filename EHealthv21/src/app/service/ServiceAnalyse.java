/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.service;

import Database.Database;
import app.entity.Analyse;
import app.entity.DossierLab;
import app.entity.Medecin;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Amine Chelly
 */
public class ServiceAnalyse {

    Connection cnx;
    PreparedStatement ste;
    ServiceUser su = new ServiceUser();

    public ServiceAnalyse() {
        cnx = Database.getInstance().getConn();
    }

    public List<Analyse> getAnalyseByLab(int id_lab) throws SQLException {
        List<Analyse> la = new ArrayList<Analyse>();
        Statement st = cnx.createStatement();
        String req = "SELECT * FROM analyse where dossier_lab_id ='" + id_lab + "'";
        ResultSet rs = st.executeQuery(req);
        ServiceDossierLab dls = new ServiceDossierLab();
        while (rs.next()) {
            DossierLab dl = dls.getDossierLabById(id_lab);
            la.add(new Analyse(rs.getInt(1), dl, rs.getString("description"), rs.getDate("date")));
        }
        return la;
    }

    public void ajouterAnalyse(Analyse a) {
        try {
            DateFormat formatd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String date = formatd.format(a.getDate());
            String sql = "INSERT INTO `analyse` (`dossier_lab_id`, `description`, `date`) VALUES (?, ?, '" + date + "')";
            ste = cnx.prepareStatement(sql);
            ste.setString(2, a.getDescription());
            ste.setInt(1, a.getDossierLab().getId());
            ste.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void majAnalyse(Analyse a) {
        try {
            System.out.println(a.getId());
            DateFormat formatd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String date = formatd.format(a.getDate());
            String sql = "UPDATE analyse SET  description = ?, date = '" + date + "' where id = " + a.getId();
            ste = cnx.prepareStatement(sql);
            ste.setString(1, a.getDescription());
            ste.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void DeleteAnalyse(int id) throws SQLException {
        String SQL = "DELETE FROM analyse WHERE `id` = " + id;
        PreparedStatement ste = cnx.prepareStatement(SQL);
        ste.executeUpdate();
    }
}
