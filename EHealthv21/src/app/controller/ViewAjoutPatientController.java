/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controller;

import app.entity.Patient;
import app.entity.User;
import app.mail.SendMail;
import app.service.ServicePatient;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author meria
 */
public class ViewAjoutPatientController implements Initializable {

    @FXML
    private TextField inputEmail;
    @FXML
    private TextField inputNom;
    @FXML
    private TextField inputPrenom;
    @FXML
    private TextField inputAdresse;
    @FXML
    private TextField inputTel;
    @FXML
    private TextField inputCin;
    @FXML
    private ChoiceBox<String> inputSexe;
    @FXML
    private Button btn_ajoutPatient;
    @FXML
    private PasswordField inputPassword;
    @FXML
    private ChoiceBox<String> inputVille;
    @FXML
    private Label label_erreur_cin;
    @FXML
    private Label lable_erreur_tel;
    @FXML
    private Label label_erreur_email;
    @FXML
    private Label label_blank_patient;
    @FXML
    private Text label;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
        ObservableList<String> listVilles = FXCollections.observableArrayList(listVille);
        inputVille.setItems(listVilles);
        inputSexe.setItems(sexe);

        // TODO
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

    @FXML
    public void AjoutPatient() throws SQLException {
        boolean test = true;
        if (!isValidEmail(inputEmail.getText())) {
            label_erreur_email.setText("verifier votre mail");
            test = false;
        }
        if (!isValidCin(inputCin.getText())) {
            label_erreur_cin.setText("verifier votre CIN");
            test = false;
        }
        if (!isValidCin(inputTel.getText())) {
            lable_erreur_tel.setText("verifier votre numero de telephone");
            test = false;
        }
        if (test && (isBlank(inputEmail.getText())) && (isBlank(inputPassword.getText()))
                && (isBlank(inputNom.getText())) && (isBlank(inputPrenom.getText()))
                && (isBlank(inputAdresse.getText()))
                && (isBlank(inputVille.getSelectionModel().getSelectedItem()))
                && (isBlank(inputSexe.getSelectionModel().getSelectedItem()))
                && (isBlank(inputTel.getText()))
                && (isBlank(inputCin.getText()))) {
            String email = inputEmail.getText();
            String password = inputPassword.getText();
            String nom = inputNom.getText();
            String prenom = inputPrenom.getText();
            String adresse = inputAdresse.getText();
            String ville = inputVille.getSelectionModel().getSelectedItem();
            int tel = Integer.parseInt(inputTel.getText());
            int cin = Integer.parseInt(inputCin.getText());
            String sexeP = inputSexe.getSelectionModel().getSelectedItem();
            User us = new User();
            us.setPassword(password);
            User u = new User(email, "[\"ROLE_USER\"]", us.hashPassword(), adresse, ville, tel, true, "patient");
            if (sexeP == "Homme") {
                sexeP = "0";
            } else {
                sexeP = "1";
            }
            Patient p = new Patient(u, nom, prenom, cin, sexeP, true);
            ServicePatient sp = new ServicePatient();
            sp.ajouterPatient(p);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ajout de compte");
            alert.setHeaderText("ce compte a eté ajouté avec succées");
            alert.showAndWait();
            Stage stage = (Stage) btn_ajoutPatient.getScene().getWindow();
            stage.close();
            try {
                
                SendMail.EnvoyerMail(email,password);
            } catch (Exception ex) {
                Logger.getLogger(ViewAjoutPatientController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            label_blank_patient.setText("verifier les champs");
        }

    }

}
