/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.service;

import Database.Database;
import app.entity.Question;
import app.entity.Reponse;
import app.entity.Specialite;
import app.service.ServiceQuestion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author anasz
 */
public class ServiceReponse {

    Connection cnx;

    PreparedStatement ste;

    Statement s;

    public ServiceReponse() {
        cnx = Database.getInstance().getConn();
    }

    ;
    
    public List<Reponse> getReponsesByIdQuestion(int question_id) throws SQLException {
        List<Reponse> lr = new ArrayList<>();
        Reponse rp = new Reponse();
        Statement st = cnx.createStatement();
        String req = "SELECT * FROM Reponse where question_id=" + question_id;
        ResultSet rs = st.executeQuery(req);
        while (rs.next()) {
            Question q = new Question();
            ServiceQuestion sq = new ServiceQuestion();
            q = sq.getQuestionById(question_id);
            rp = new Reponse(rs.getInt("id"), q, rs.getString("text"));
            lr.add(rp);
            //System.out.println(rp.getText());
        }
        return lr;

    }

    public Reponse getReponseById(int id) throws SQLException {
        Reponse rep = new Reponse();
        Statement st = cnx.createStatement();
        String req = "SELECT * FROM Reponse where id=" + id;
        ResultSet rs = st.executeQuery(req);
        if (rs.next()) {
            rep.setId(rs.getInt("id"));
            rep.setText(rs.getString("text"));

        }
        return rep;
    }

    public void repondreQuestion(Reponse r, int id_question,int id_medecin) {
        String query = "insert into reponse(medecin_id,question_id,text) values (?,?,?)";

        try {
            ste = cnx.prepareStatement(query);
            ste.setInt(1, id_medecin);

            ste.setInt(2, id_question);
            ste.setString(3, r.getText());

            ste.executeUpdate();
            System.out.println("Reponse ajouté à la base ");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    //Med
    public List<Reponse> afficherMesReponses(int id_medecin, int question_id) throws SQLException {

        List<Reponse> lr = new ArrayList<>();
        Statement st = cnx.createStatement();
        String req = "SELECT * FROM Reponse WHERE medecin_id='" + id_medecin + "' AND  question_id=" + question_id;

        ResultSet rs = st.executeQuery(req);
        //System.out.println(3);
        while (rs.next()) {
              System.out.println(rs.getString("text"));
            Reponse r = new Reponse(rs.getString("text"));
            r.setId(rs.getInt("id"));
            lr.add(r);
        }

        return lr;

    }

    //Afficher les question destiné  à ma specialite
  /*  public List<Reponse> readSelonSpecialite(int id_specialite) throws SQLException {
        List<Reponse> lr = new ArrayList<>();
        Statement st = cnx.createStatement();
        String req = "SELECT * FROM Reponse where";
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            // System.out.println(rs.getString("text"));
            Reponse r = new Reponse(rs.getString("text"));
            r.setId(rs.getInt("id"));
            lr.add(r);
        }

        return lr;
    }
*/
    public List<Reponse> read() throws SQLException {
        List<Reponse> lr = new ArrayList<>();
        Statement st = cnx.createStatement();
        String req = "SELECT * FROM Reponse";
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            // System.out.println(rs.getString("text"));
            Reponse r = new Reponse(rs.getString("text"));
            r.setId(rs.getInt("id"));
            lr.add(r);
        }

        return lr;
    }

    public void modifierReponse(Reponse r) {
        System.out.println("id fel modif =" + r.getId());
        String query = "UPDATE  Reponse set text=? Where id ='" + r.getId() + "'";
        try {
            ste = cnx.prepareStatement(query);
            ste.setString(1, r.getText());
            ste.executeUpdate();
            System.out.println("Reponse modifié  ");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void supprimerReponse(int id) {
        String query = "DELETE from Reponse where id = " + id;  // "'" + id + "'";
        try {
            s = cnx.createStatement();
            s.executeUpdate(query);
            System.out.println("Reponse Supprimée de la base ");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public Reponse getReponseByText(String text) throws SQLException {

        Reponse r = new Reponse();
        //System.out.println("J'ai entré dans la service"+s);
        //System.out.println(nom);

        String req = "SELECT * FROM Reponse where text like'" + text + "'";
        Statement st = cnx.createStatement();
        //ste.setString(1,nom);

        ResultSet rs = st.executeQuery(req);
        if (rs.next()) {

            r = new Reponse(rs.getInt("id"), rs.getString("text"));

            // System.out.println("Apres la requete" + r);
        }
        return r;
    }

}
