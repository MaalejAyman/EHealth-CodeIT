/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controller;

import app.entity.Medecin;
import app.entity.Specialite;
import app.entity.User;
import app.mail.SendMail;
import app.service.MedecinService;
import app.service.ServiceSpecialite;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author meria
 */
public class ViewAjoutMedecinController implements Initializable {

    @FXML
    private TextField inputEmailMed;
    @FXML
    private TextField inputNomMed;
    @FXML
    private TextField inputPrenomMed;
    @FXML
    private TextField inputAdresseMed;
    @FXML
    private TextField inputTelMed;
    @FXML
    private ChoiceBox<String> inputSexeMed;
    @FXML
    private Button btn_ajoutMed;
    @FXML
    private PasswordField inputPasswordMed;
    @FXML
    private TextField inputHoraireOMed;
    @FXML
    private TextField inputHoraireFMed;
    @FXML
    private TextField inputNumeroServiceMed;
    @FXML
    private ChoiceBox<Specialite> inputSpecialiteMed;
    @FXML
    private ChoiceBox<String> inputVilleMed;
    @FXML
    private Label label_erreur_email_med;
    @FXML
    private Label label_erreur_numserv_med;
    @FXML
    private Label lable_erreur_tel_med;
    @FXML
    private Label label_blank_med;
    private int id = -1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            List<String> listVille = new ArrayList<String>();
            listVille.add("Ariana");
            listVille.add("Tunis");
            listVille.add("Ben Arous");
            listVille.add("Manouba");
            listVille.add("Nabeul");
            listVille.add("Zaghouan");
            listVille.add("Bizerte");
            listVille.add("Béja");
            listVille.add("Jendouba");
            listVille.add("Kef");
            listVille.add("Siliana");
            listVille.add("Sousse");
            listVille.add("Monastir");
            listVille.add("Mahdia");
            listVille.add("Sfax");
            listVille.add("Kairouan");
            listVille.add("Kasserine");
            listVille.add("Sidi Bouzid");
            listVille.add("Kasserine");
            listVille.add("Gabès");
            listVille.add("Mednine");
            listVille.add("Tataouine");
            listVille.add("Gafsa");
            listVille.add("Tozeur");
            listVille.add("Kebili");
            List<String> list = new ArrayList<String>();
            list.add("Homme");
            list.add("Femme");
            ObservableList<String> sexe = FXCollections.observableArrayList(list);
            inputSexeMed.setItems(sexe);
            ObservableList<String> listVilles = FXCollections.observableArrayList(listVille);
            inputVilleMed.setItems(listVilles);

            ServiceSpecialite ssp = new ServiceSpecialite();
            List<Specialite> listSpecialite = ssp.getSpecialites();
            ObservableList<Specialite> listSpecialites = FXCollections.observableArrayList(listSpecialite);
            inputSpecialiteMed.setItems(listSpecialites);
        } catch (SQLException ex) {
            Logger.getLogger(ViewAjoutMedecinController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
// controle saisi

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."
                + "[a-zA-Z0-9_+&*-]+)*@"
                + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                + "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pat.matcher(email).matches();
    }

    public boolean isValidCin(String cin) {
        String emailRegex = "[0-9]{8}";

        Pattern pat = Pattern.compile(emailRegex);
        if (cin == null) {
            return false;
        }
        return pat.matcher(cin).matches();
    }

    public boolean isBlank(String cin) {
        boolean test = true;
        if (cin == null) {
            test = false;
        }
        return test;
    }

    public boolean isValidnumService(String cin) {
        String emailRegex = "[0-9]{5}";

        Pattern pat = Pattern.compile(emailRegex);
        if (cin == null) {
            return false;
        }
        return pat.matcher(cin).matches();
    }

    @FXML
    public void AjoutMedecin() throws SQLException {
        try {
            boolean test = true;
            if (!isValidEmail(inputEmailMed.getText())) {
                label_erreur_email_med.setText("verifier votre mail");
                test = false;
            }
            if (!isValidnumService(inputNumeroServiceMed.getText())) {
                label_erreur_numserv_med.setText("verifier votre numero  de service");
                test = false;
            }
            if (!isValidCin(inputTelMed.getText())) {
                lable_erreur_tel_med.setText("verifier votre numero de telephone");
                test = false;
            }
            if (test && (isBlank(inputEmailMed.getText())) && (isBlank(inputPasswordMed.getText()))
                    && (isBlank(inputNomMed.getText())) && (isBlank(inputPrenomMed.getText()))
                    && (isBlank(inputAdresseMed.getText()))
                    && (isBlank(inputVilleMed.getSelectionModel().getSelectedItem()))
                    && (isBlank(inputSexeMed.getSelectionModel().getSelectedItem()))
                    && (isBlank(inputTelMed.getText()))
                    && (isBlank(inputNumeroServiceMed.getText()))) {
                String email = inputEmailMed.getText();
                String password = inputPasswordMed.getText();
                String nom = inputNomMed.getText();
                String prenom = inputPrenomMed.getText();
                String adresse = inputAdresseMed.getText();
                String ville = inputVilleMed.getSelectionModel().getSelectedItem();
                int tel = Integer.parseInt(inputTelMed.getText());
                Specialite specialite = inputSpecialiteMed.getValue();
                User us = new User();
                us.setPassword(password);
                int numeroService = Integer.parseInt(inputNumeroServiceMed.getText());
                DateFormat format = new SimpleDateFormat("h:mm", Locale.ENGLISH);
                Date horaireDebut = format.parse(inputHoraireOMed.getText());
                Date horaireFin = format.parse(inputHoraireFMed.getText());
                String sexeP = inputSexeMed.getSelectionModel().getSelectedItem();
                User u = new User(email, "[\"ROLE_MEDECIN\"]", us.hashPassword(), adresse, ville, tel, true, "medecin");
                if (sexeP == "Homme") {
                    sexeP = "0";
                } else {
                    sexeP = "1";
                }
                Medecin m = new Medecin(id, specialite, u, nom, prenom, sexeP, horaireDebut, horaireFin, numeroService);
                MedecinService sm = new MedecinService();
                sm.ajouterMedecin(m);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ajout de compte");
                alert.setHeaderText("ce compte a eté ajouté avec succées");
                alert.showAndWait();
                Stage stage = (Stage) btn_ajoutMed.getScene().getWindow();
                stage.close();
                try {
                
                SendMail.EnvoyerMail(email,password);
            } catch (Exception ex) {
                Logger.getLogger(ViewAjoutPatientController.class.getName()).log(Level.SEVERE, null, ex);
            }
            } else {
                label_blank_med.setText("verifier les champs");
            }

        } catch (ParseException ex) {
            Logger.getLogger(ViewPatientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
