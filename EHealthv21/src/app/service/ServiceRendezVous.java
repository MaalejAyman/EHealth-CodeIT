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
import app.entity.Rendezvous;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

/**
 *
 * @author meria
 */
public class ServiceRendezVous {

    Connection cnx;
    PreparedStatement ste;
    ServiceUser su = new ServiceUser();

    public ServiceRendezVous() {
        cnx = Database.getInstance().getConn();
    }

    public List<Rendezvous> getRendezVousByUser(int id) {
        List<Rendezvous> ls = new ArrayList<Rendezvous>();
        try {
            ServicePatient ps = new ServicePatient();
            Patient p = ps.getPatientById(id);
            Statement st = cnx.createStatement();
            String req = "SELECT * FROM Rendezvous Rdv where Rdv.patient_id=" + id;
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                if (rs.getInt("medecin_id") != 0) {
                    MedecinService ms = new MedecinService();
                    Medecin m = ms.getMedecinById(rs.getInt("medecin_id"));
                    //System.out.println(rs.getInt("medecin_id"));
                    Rendezvous rdv = new Rendezvous(rs.getInt("id"), null, m, p, rs.getDate("date"), rs.getTime("heure"), rs.getString("etat"), rs.getInt("evaluation"));
                    ls.add(rdv);
                }
                if (rs.getInt("laboratoire_id") != 0) {
                    LaboratoireService ms = new LaboratoireService();
                    Laboratoire L = ms.getLaboratoireById(rs.getInt("laboratoire_id"));
                    Rendezvous rdv = new Rendezvous(rs.getInt("id"), L, null, p, rs.getDate("date"), rs.getTime("heure"), rs.getString("etat"), rs.getInt("evaluation"));
                    ls.add(rdv);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceRendezVous.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ls;
    }

    public XYChart.Series<Integer, String> loadChart() {
        String sql = "select etat from rendezvous order by patient_id";
        XYChart.Series<Integer, String> series = new XYChart.Series<>();
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int etatNumberConfirme = 0;
            int etatNumberEffectue = 0;
            while (rs.next()) {
                if (rs.getString("etat").equals("effectué")) {
                    etatNumberEffectue++;
                }
                if (rs.getString("etat").equals("confirmé")) {
                    etatNumberConfirme++;
                }
                series.getData().add(new XYChart.Data("confirmé", etatNumberConfirme));
                series.getData().add(new XYChart.Data("effectué", etatNumberEffectue));

            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceRendezVous.class.getName()).log(Level.SEVERE, null, ex);

        }
        return series;
    }

    // Anaaaaaaaaaaaaaaaaas ******************************* 
    public void evaluerService(int id_rdv, int eval) {

        String query = "UPDATE  rendezvous set evaluation=? Where id = '" + id_rdv + "'";
        try {
            PreparedStatement ste = cnx.prepareStatement(query);
            ste.setInt(1, eval);
            ste.executeUpdate();
            System.out.println("Evaluation ajouté à la base ");
            //read();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public int calculerNombreEvaluation(int id_medecin) throws SQLException {
        int nomnbreEvaluation = 0;
        Statement st = cnx.createStatement();
        String req = "SELECT * FROM Rendezvous where medecin_id =" + id_medecin;

        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            nomnbreEvaluation++;
        }
        return nomnbreEvaluation;
    }

    public int getEvaluationById(int id) throws SQLException {
        Statement st = cnx.createStatement();
        String req = "SELECT evaluation FROM rendezvous where id = " + id;
        ResultSet rs = st.executeQuery(req);

        if (rs.next()) {
            return (rs.getInt("evaluation"));
        }
        return (rs.getInt(0));

    }

}
