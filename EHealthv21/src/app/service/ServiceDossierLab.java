/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.service;

import Database.Database;
import app.entity.Analyse;
import app.entity.DossierLab;
import app.entity.Laboratoire;
import app.entity.Patient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Amine Chelly
 */
public class ServiceDossierLab {

    Connection cnx;
    PreparedStatement ste;
    ServiceUser su = new ServiceUser();

    public ServiceDossierLab() {
        cnx = Database.getInstance().getConn();
    }

    public List<DossierLab> getDossierLabByLab(int id_lab) throws SQLException {
        List<DossierLab> la = new ArrayList<DossierLab>();
        Statement st = cnx.createStatement();
        String req = "SELECT * FROM dossier_lab where laboratoire_id ='" + id_lab + "'";
        ResultSet rs = st.executeQuery(req);
        LaboratoireService ls = new LaboratoireService();
        ServicePatient ps = new ServicePatient();
        while (rs.next()) {
            Patient p = ps.getPatientById(rs.getInt("patient_id"));
            Laboratoire l = ls.getLaboratoireById(id_lab);
            la.add(new DossierLab(rs.getInt(1), l, p));
        }
        return la;
    }
    
    public DossierLab getDossierLabById(int id_dsl) throws SQLException {
        DossierLab la = new DossierLab();
        Statement st = cnx.createStatement();
        String req = "SELECT * FROM dossier_lab where id ='" + id_dsl + "'";
        ResultSet rs = st.executeQuery(req);
        LaboratoireService ls = new LaboratoireService();
        ServicePatient ps = new ServicePatient();
        if (rs.next()) {
            Patient p = ps.getPatientById(rs.getInt("patient_id"));
            Laboratoire l = ls.getLaboratoireById(rs.getInt("laboratoire_id"));
            la = new DossierLab(rs.getInt(1), l, p);
        }
        return la;
    }
    
    public void ajouterDossierLab(DossierLab a) {
        try {
            String sql = "INSERT INTO `dossier_lab` (`laboratoire_id`, `patient_id`) VALUES (?, ?)";
            ste = cnx.prepareStatement(sql);
            ste.setInt(1, a.getLaboratoire().getId());
            ste.setInt(2, a.getPatient().getId());
            ste.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}
