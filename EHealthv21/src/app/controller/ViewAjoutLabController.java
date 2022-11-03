/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controller;

import app.entity.Laboratoire;
import app.entity.Service;
import app.entity.User;
import app.mail.SendMail;
import app.service.LaboratoireService;
import app.service.ServiceService;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
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
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author meria
 */
public class ViewAjoutLabController implements Initializable {

    @FXML
    private TextField inputEmailLab;
    @FXML
    private TextField inputNomLab;
    @FXML
    private TextField inputNumServiceLab;
    @FXML
    private TextField inputAdresseLab;
    @FXML
    private TextField inputTelLab;
    @FXML
    private TextField inputHoraireOLab;
    @FXML
    private Button btn_ajoutLab;
    @FXML
    private PasswordField inputPasswordLab;
    @FXML
    private TextField inputHoraireFLab;
    @FXML
    private ChoiceBox<String> inputVilleLab;
    @FXML
    private ListView<Service> inputServiceLab;
    @FXML
    private Label label_erreur_email_lab;
    @FXML
    private Label lable_erreur_tel_lab;
    @FXML
    private Label label_erreur_numserv_lab;
    @FXML
    private Label label_blank_lab;

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
            ObservableList<String> listVilles = FXCollections.observableArrayList(listVille);
            ServiceService ss = new ServiceService();
            List<Service> listService = ss.getServices();
            inputVilleLab.setItems(listVilles);
            ObservableList<Service> listServices = FXCollections.observableArrayList(listService);

            inputServiceLab.setItems(listServices);
            inputServiceLab.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            // TODO
        } catch (SQLException ex) {
            Logger.getLogger(ViewAjoutLabController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
    public void AjoutLaboratoire() throws SQLException {
        try {
            boolean test = true;
            if (!isValidEmail(inputEmailLab.getText())) {
                label_erreur_email_lab.setText("verifier votre mail");
                test = false;
            }
            if (!isValidnumService(inputNumServiceLab.getText())) {
                label_erreur_numserv_lab.setText("verifier votre numero  de service");
                test = false;
            }
            if (!isValidCin(inputTelLab.getText())) {
                lable_erreur_tel_lab.setText("verifier votre numero de telephone");
                test = false;
            }
            if (test && (isBlank(inputPasswordLab.getText()))
                    && (isBlank(inputNomLab.getText()))
                    && (isBlank(inputAdresseLab.getText()))
                    && (isBlank(inputVilleLab.getSelectionModel().getSelectedItem()))
                    && (isBlank(inputHoraireOLab.getText()))
                    && (isBlank(inputHoraireFLab.getText()))) {
                String email = inputEmailLab.getText();
                ObservableList selectedIndicesLab = inputServiceLab.getSelectionModel().getSelectedIndices();
                ServiceService ss = new ServiceService();
                List<Service> listService = ss.getServices();
                ObservableList<Service> listServices = FXCollections.observableArrayList(listService);
                String password = inputPasswordLab.getText();
                String nom = inputNomLab.getText();
                int numService = Integer.parseInt(inputNumServiceLab.getText());
                String adresse = inputAdresseLab.getText();
                String ville = inputVilleLab.getSelectionModel().getSelectedItem();
                int tel = Integer.parseInt(inputTelLab.getText());
                User us = new User();
                us.setPassword(password);
                User u = new User(email, "[\"ROLE_LABORATOIRE\"]", us.hashPassword(), adresse, ville, tel, true, "laboratoire");
                DateFormat format = new SimpleDateFormat("h:mm", Locale.ENGLISH);
                Date horaireDebut = format.parse(inputHoraireOLab.getText());
                Date horaireFin = format.parse(inputHoraireFLab.getText());
                Laboratoire l = new Laboratoire(u, numService, nom, horaireDebut, horaireFin);
                LaboratoireService ls = new LaboratoireService();
                Set<Service> h = new HashSet<Service>();
                for (Object i : selectedIndicesLab) {
                    h.add(listServices.get((int) i));
                }
                l.setServices(h);
                ls.ajouterLaboratoire(l);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ajout de compte");
                alert.setHeaderText("ce compte a eté ajouté avec succées");
                alert.showAndWait();
                Stage stage = (Stage) btn_ajoutLab.getScene().getWindow();
                stage.close();
                try {
                
                SendMail.EnvoyerMail(email,password);
            } catch (Exception ex) {
                Logger.getLogger(ViewAjoutPatientController.class.getName()).log(Level.SEVERE, null, ex);
            }
            } else {
                label_blank_lab.setText("verifier les champs");
            }
        } catch (ParseException ex) {
            Logger.getLogger(ViewPatientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
