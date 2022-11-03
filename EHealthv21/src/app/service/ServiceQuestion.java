/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.service;

import Database.Database;
import app.entity.Patient;
import app.entity.Question;
import app.entity.Specialite;
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
 * @author anasz
 */
public class ServiceQuestion {

    Connection cnx;

    PreparedStatement ste;

    Statement s;

    public ServiceQuestion() {
        cnx = Database.getInstance().getConn();
    }

    ;
    
      public Question getQuestionByText(String text) throws SQLException {

        Question q = new Question();
        //System.out.println("J'ai entré dans la service"+s);
        //System.out.println(nom);

        String req = "SELECT * FROM Question where text like'" + text + "'";
        Statement st = cnx.createStatement();
        //ste.setString(1,nom);

        ResultSet rs = st.executeQuery(req);
        if (rs.next()) {

            q = new Question(rs.getInt("id"), rs.getString("text"));
            //  System.out.println("Apres la requete" + q);
        }
        return q;
    }

    public Question getQuestionById(int id) throws SQLException {
        Statement st = cnx.createStatement();
        String req = "SELECT * FROM Question where id = " + id;
        ResultSet rs = st.executeQuery(req);

        if (rs.next()) {

        }
        ServiceSpecialite ss = new ServiceSpecialite();

        Specialite s = ss.getSpecialiteById(rs.getInt("specialite_id"));
        ServicePatient ps = new ServicePatient();
        Patient p = ps.getPatientById(rs.getInt("patient_id"));
        Question q = new Question(p, s, rs.getString("text"));
        q.setId(rs.getInt("id"));

        return q;

    }

    public List<Question> read() throws SQLException {

        List<Question> ls = new ArrayList<Question>();
        Statement st = cnx.createStatement();
        String req = "SELECT * FROM Question";
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            //Recuperer Liste des Specialite 
            ServiceSpecialite ms = new ServiceSpecialite();
            Specialite sp = ms.getSpecialiteById(rs.getInt("specialite_id"));
            //RECUPERER le patient
            ServicePatient ps = new ServicePatient();
            Patient p = ps.getPatientById(rs.getInt("patient_id"));

            Question q = new Question(sp, rs.getString("text"));
            q.setId(rs.getInt("id"));
            ls.add(q);
        }
        //   System.out.println(ls);
        return ls;
    }

    public List<Question> getQuestionByIdPatient(int id) throws SQLException {

        List<Question> ls = new ArrayList<Question>();
        Statement st = cnx.createStatement();
        String req = "SELECT * FROM Question where patient_id=" + id;
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            ServiceSpecialite ms = new ServiceSpecialite();
            Specialite sp = ms.getSpecialiteById(rs.getInt("specialite_id"));
            //RECUPERER le patient
            ServicePatient ps = new ServicePatient();
            Patient p = ps.getPatientById(rs.getInt("patient_id"));

            Question q = new Question() ;  //sp, rs.getString("text"));
            q.setId(rs.getInt("id"));
            q.setPatient(p);
            q.setSpecialite(sp);
            q.setText(rs.getString("text"));
            ls.add(q);
        }
        //   System.out.println(ls);
        return ls;
    }

    public void poserQuestion(Patient p, Question q, Specialite s) {

        String query = "insert into question(patient_id,specialite_id,text) values (?,?,?)";

        try {
            ste = cnx.prepareStatement(query);
            ste.setInt(1, p.getId());
            ste.setInt(2, s.getId());
            ste.setString(3, q.getText());

            ste.executeUpdate();
            System.out.println("Question ajouté à la base ");
            //read();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void modifierQuestion(Patient p, Question q, Specialite s) {
        //  System.out.println("Specialite dans modifier Service " + s);
        // System.out.println("Question dans modifier Service " + q);
        // System.out.println("Patient dans modifier Service " + p);

        String query = "UPDATE  Question set text=?,specialite_id=?  Where id ='" + q.getId() + "'";

        try {
            ste = cnx.prepareStatement(query);
            ste.setString(1, q.getText());
            ste.setInt(2, s.getId());
            ste.executeUpdate();
            System.out.println("Question modifiée ");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void supprimerQuestion(String text) {
        String query = "DELETE from QUESTION where text like " + "'" + text + "'";
        try {
            s = cnx.createStatement();
            s.executeUpdate(query);
            System.out.println("Question Supprimée de la base ");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public List<Question> readSelonSpecialite(int specialite_id) throws SQLException {
        List<Question> ls = new ArrayList<Question>();
        Statement st = cnx.createStatement();
        String req = "SELECT * FROM Question where specialite_id=" + specialite_id;
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            //Recuperer Liste des Specialite 
            ServiceSpecialite ms = new ServiceSpecialite();
            Specialite sp = ms.getSpecialiteById(rs.getInt("specialite_id"));
            //RECUPERER le patient

            Question q = new Question(sp, rs.getString("text"));
            q.setId(rs.getInt("id"));
            ls.add(q);
        }
        //   System.out.println(ls);
        return ls;
    }

}
