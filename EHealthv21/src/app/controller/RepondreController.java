/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controller;

import app.entity.Medecin;
import app.entity.Patient;
import app.entity.Question;
import app.entity.Reponse;
import app.entity.Service;
import app.entity.Specialite;
import static app.mail.Mail.sendMail;
import app.service.ServiceQuestion;
import app.service.ServiceReponse;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javax.mail.MessagingException;
import org.hibernate.Session;
import org.jboss.logging.Message;

/**
 * FXML Controller class
 *
 * @author anasz
 */
public class RepondreController implements Initializable {

    public static int idQuestion = -1;
    public static int idReponse = -1;
    @FXML
    private TextField tfreponse;
    @FXML
    private Label label;
    @FXML
    private TableView<Question> tvbox;
    @FXML
    private TableColumn<Question, String> colquestion;

    @FXML
    private Label labelquestion;
    @FXML
    private TableView<Reponse> tvboxrep;
    @FXML
    private TableColumn<?, ?> coltouslesreponses;
    @FXML
    private TableColumn<?, ?> colmesreponses;
    @FXML
    private TableView<Reponse> tvboxmesrep;
    private Label labelreponseamodifier;
    @FXML
    private Button btnvider;
    @FXML
    private Label labelquestion1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {

            ServiceQuestion SQ = new ServiceQuestion();
            List<Question> ql = SQ.readSelonSpecialite(2);  //1 Génraliste 2 cardiologue
            ObservableList<Question> data = FXCollections.observableArrayList(ql);
            colquestion.setCellValueFactory(new PropertyValueFactory<>("text"));

            tvbox.setItems(data);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    @FXML
    private void selectionnerReponse(MouseEvent event) {
        Reponse rep = tvboxmesrep.getSelectionModel().getSelectedItem();
        tfreponse.setText(rep.getText());
        idReponse = rep.getId();

        //   int id = quest.getId() ;
    }

    @FXML
    private void selectionnerQuestion(MouseEvent event) throws SQLException {
        Question quest = tvbox.getSelectionModel().getSelectedItem();
        RepondreController.idQuestion= quest.getId();
        System.out.println(idQuestion);
        label.setText(quest.getText());

        //Afficher tousles reponses de cette question
        ServiceReponse SR = new ServiceReponse();
        List<Reponse> rl = SR.read();                     //SelonSpecialite(2);  //Recuperation depuis login
        ObservableList<Reponse> datarep = FXCollections.observableArrayList(rl);
        coltouslesreponses.setCellValueFactory(new PropertyValueFactory<>("text"));

        //id medecin en attendant nekhouh m login
        List<Reponse> rl1 = SR.afficherMesReponses(24, quest.getId());
        ObservableList<Reponse> datamesreponses = FXCollections.observableArrayList(rl1);
        colmesreponses.setCellValueFactory(new PropertyValueFactory<>("text"));

        tvboxrep.setItems(datarep);
        tvboxmesrep.setItems(datamesreponses);

    }

    @FXML
    private void clear(ActionEvent event) {
        tfreponse.clear();
        label.setText("");
        label.setText("");
        tvboxmesrep.setItems(null);
        tvboxrep.setItems(null);
        idQuestion = -1;
        idReponse = -1;

    }

    @FXML
    private void supprimerReponse(MouseEvent event) {
        if (idReponse == -1) {
            label.setText("Merci de selectionner une de vos réponses pour pouvoir la supprimer");
            return;
        }
        try {
            ServiceReponse sq;
            String text = tfreponse.getText();
            Question q = new Question(new Patient(), new Specialite(), text);

            sq = new ServiceReponse();
            sq.supprimerReponse(idReponse);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/views/Repondre.fxml"));
            Parent root = loader.load();
            tfreponse.getScene().setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(RepondreController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void RepondreQuestion(MouseEvent event) throws SQLException {
        if (idQuestion == -1) {
            label.setText("Merci de selectionner une question avant de poser votre réponse");
            return;
        }
        if (tfreponse.getText().equals("")) {
            label.setText("Réponse invalide");
            return;
        }
        try {
            ServiceReponse sr;
            String text = tfreponse.getText();
            String question = label.getText();
            Reponse r = new Reponse(new Medecin(), new Question(), text);
            sr = new ServiceReponse();
            ServiceQuestion sq = new ServiceQuestion();
            Question q = new Question();
            q = sq.getQuestionByText(question);

            sr.repondreQuestion(r, q.getId(),34);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/views/Repondre.fxml"));
            Parent root = loader.load();
            tfreponse.getScene().setRoot(root);
        } catch (IOException ex) {
            System.out.print(ex.getMessage());
        }
    }

    @FXML
    private void modifierReponse(MouseEvent event) throws SQLException, IOException {
        if (idReponse == -1) {
            label.setText("Merci de selectionner une de vos question pour pouvoir la modifié");
            return;
        }
        ServiceReponse sr = new ServiceReponse();
        String text = tfreponse.getText();
        System.out.println("id rep = " + idReponse);
        Reponse r = sr.getReponseById(idReponse);
        r.setText(text);
        r.setId(idReponse);
        sr.modifierReponse(r);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/views/Repondre.fxml"));
        Parent root = loader.load();
        //tekhou ay variable m scene le9dima
        tfreponse.getScene().setRoot(root);
    }

    @FXML
    private void reclamer(MouseEvent event
    ) {
        //Controle de saisie
        if (idQuestion == -1) {
            label.setText("Merci de selectionner une question pour reclamer");
            return;
        }
        // Envoyer un mail au admin
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reclamation");
        alert.setHeaderText("Une réclamation a été envoyer aux administrateurs .\n Merci pour votre coopération ");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.FINISH) {

            try {
                sendMail("ehealthcodeit@gmail.com", 3, label.getText());
            } catch (MessagingException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

}
