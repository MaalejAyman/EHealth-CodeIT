/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controller;

import static app.controller.RepondreController.idQuestion;
import static app.controller.RepondreController.idReponse;
import app.entity.Medecin;
import app.entity.Patient;
import app.entity.Question;
import app.entity.Reponse;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.mail.MessagingException;

/**
 * FXML Controller class
 *
 * @author meria
 */
public class ViewMedecinController implements Initializable {

    private Medecin ConnectedMedecin;
    //Anaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaas

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

    //Anas End
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Anaaaaaaaaaaaaas
        try {

            ServiceQuestion SQ = new ServiceQuestion();
            int idSpecialite = ConnectedMedecin.getSpecialite().getId();
            List<Question> ql = SQ.readSelonSpecialite(idSpecialite);  //1 Génraliste 2 cardiologue
            ObservableList<Question> data = FXCollections.observableArrayList(ql);
            colquestion.setCellValueFactory(new PropertyValueFactory<>("text"));

            tvbox.setItems(data);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        //End Anas
    }

    public ViewMedecinController(Medecin value) {
        this.ConnectedMedecin = value;

    }

    public ViewMedecinController() {
        this(new Medecin());
    }

    public void setP(Medecin value) {
        ConnectedMedecin = value;

    }

    public void setConnectedMedecin(Medecin p) {
        this.ConnectedMedecin = p;
        System.out.println(this.ConnectedMedecin.getUser().getType());
    }

    // ---------------------------------------------Anaaaaaaaaaaaaaas -----------------------------------------
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
        ViewMedecinController.idQuestion = quest.getId();
        label.setText(quest.getText());

        //Afficher tousles reponses de cette question
        afficherTousLesReponse();
          //Afficher mes reponses à cette question 
          afficherMesReponse() ;
    }

    
    @FXML
    private void supprimerReponse(MouseEvent event) {
        if (idReponse == -1) {
            label.setText("Merci de selectionner une de vos réponses pour pouvoir la supprimer");
            return;
        }

        ServiceReponse sq;
        String text = tfreponse.getText();
        Question q = new Question(new Patient(), new Specialite(), text);

        sq = new ServiceReponse();
        sq.supprimerReponse(idReponse);

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

        ServiceReponse sr;
        String text = tfreponse.getText();
        String question = label.getText();
        Reponse r = new Reponse(new Medecin(), new Question(), text);
        sr = new ServiceReponse();
        ServiceQuestion sq = new ServiceQuestion();
        Question q = new Question();
        q = sq.getQuestionByText(question);

        sr.repondreQuestion(r, q.getId(), ConnectedMedecin.getId());

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

    @FXML
    void clearchamps(MouseEvent event) {
        tfreponse.clear();
        label.setText("");
        label.setText("");
        tvboxmesrep.setItems(null);
        tvboxrep.setItems(null);
        idQuestion = -1;
        idReponse = -1;

    }

    private void afficherTousLesReponse() throws SQLException {
        Question quest = tvbox.getSelectionModel().getSelectedItem();
        idQuestion = quest.getId();

        ServiceReponse SR = new ServiceReponse();
        List<Reponse> rl = SR.getReponsesByIdQuestion(idQuestion);                     //SelonSpecialite(2);  //Recuperation depuis login
        ObservableList<Reponse> datarep = FXCollections.observableArrayList(rl);
        coltouslesreponses.setCellValueFactory(new PropertyValueFactory<>("text"));

        tvboxrep.setItems(datarep);

    }

    private void afficherMesReponse() throws SQLException {
        Question quest = tvbox.getSelectionModel().getSelectedItem();
        idQuestion = quest.getId();
        ServiceReponse SR = new ServiceReponse();

        List<Reponse> rl1 = SR.afficherMesReponses(ConnectedMedecin.getId(), quest.getId());
        ObservableList<Reponse> datamesreponses = FXCollections.observableArrayList(rl1);
        colmesreponses.setCellValueFactory(new PropertyValueFactory<>("text"));

        tvboxmesrep.setItems(datamesreponses);
    }
@FXML
    private void logoutMed(ActionEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        Parent parent3 = FXMLLoader.load(getClass().getResource("/app/views/ViewLogin.fxml"));
        Scene scene = new Scene(parent3);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
