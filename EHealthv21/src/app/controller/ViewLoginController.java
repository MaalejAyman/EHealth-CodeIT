/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controller;

import app.entity.Laboratoire;
import app.entity.Medecin;
import app.entity.Patient;
import app.entity.User;
import app.service.LaboratoireService;
import app.service.MedecinService;
import app.service.ServicePatient;
import app.service.ServiceUser;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author meria
 */
public class ViewLoginController implements Initializable {

    @FXML
    private Label label_erreur;
    @FXML
    private Button btn_cnx;
    @FXML
    private TextField input_email_login;
    @FXML
    private PasswordField input_password_login;
    @FXML
    private ComboBox<String> btn_register;
    @FXML
    private Hyperlink btn_forget_password;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<String> registerList = new ArrayList<String>();
        registerList.add("Patient");
        registerList.add("Medecin");
        registerList.add("Laboratoire");
        ObservableList<String> registerListe = FXCollections.observableArrayList(registerList);
        btn_register.setItems(registerListe);

    }

    @FXML
    public void login(ActionEvent event) throws SQLException {
        String login = input_email_login.getText();
        String password = input_password_login.getText();
        User u = new User(login, password);
        ServiceUser su = new ServiceUser();
        User uu = su.loginUser(u);
        System.out.println(uu);
        Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2i);
        String ch = "";
        if (uu.getType() != null) {
            if (argon2.verify(uu.getPassword(), password)) {
                System.out.println("Anas");
                if (uu.getType().equals("user")) {
                    try {
                        ((Node) (event.getSource())).getScene().getWindow().hide();
                        Parent parent = FXMLLoader.load(getClass().getResource("/app/views/ViewPatient.fxml"));
                        Scene scene = new Scene(parent);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.setTitle("E-Health");
                        stage.setResizable(false);
                        stage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(ViewLoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (uu.getType().equals("patient")) {
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    stage.close();
                    ServicePatient sp = new ServicePatient();
                    Patient p = sp.getPatientById(uu.getId());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/views/ViewClient.fxml"));
                        Parent root;
                        ViewClientController controller = new ViewClientController(p);
                        loader.setController(controller);
                        root = loader.load();
                        stage.setTitle("E-Health");
                        stage.setScene(new Scene(root));
                        stage.setResizable(false);
                        stage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(ViewLoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (uu.getType().equals("medecin")) {
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    stage.close();
                    MedecinService sp = new MedecinService();
                    Medecin m = sp.getMedecinById(uu.getId());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/views/ViewMedecin.fxml"));
                        Parent root;
                        ViewMedecinController controller = new ViewMedecinController(m);
                        loader.setController(controller);
                        root = loader.load();
                        stage.setScene(new Scene(root));
                        stage.setResizable(false);
                        stage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(ViewLoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                if (uu.getType().equals("laboratoire")) {
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    stage.close();
                    LaboratoireService sp = new LaboratoireService();
                    Laboratoire l = sp.getLaboratoireById(uu.getId());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/views/ViewLaboratoire.fxml"));
                        Parent root;
                        ViewLaboratoireController controller = new ViewLaboratoireController(l);
                        loader.setController(controller);
                        root = loader.load();
                        stage.setScene(new Scene(root));
                        stage.setResizable(false);
                        stage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(ViewLoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                ch += "Mot de Passe Incorrect";

            }

        } else {
            ch += " Adresse mail n'existe pas";
        }

        label_erreur.setText(ch);

    }

    @FXML
    private void CompteSelectionne(ActionEvent event) throws IOException {
        String selectedType = btn_register.getSelectionModel().getSelectedItem();
        if (selectedType.equals("Patient")) {
            Parent parent3 = FXMLLoader.load(getClass().getResource("/app/views/ViewAjoutPatient.fxml"));
            Scene scene = new Scene(parent3);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
        if (selectedType.equals("Medecin")) {
            Parent parent3 = FXMLLoader.load(getClass().getResource("/app/views/ViewAjoutMedecin.fxml"));
            Scene scene = new Scene(parent3);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
        if (selectedType.equals("Laboratoire")) {
            Parent parent3 = FXMLLoader.load(getClass().getResource("/app/views/ViewAjoutLab.fxml"));
            Scene scene = new Scene(parent3);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }

    }

    @FXML
    private void forgetPasswordRedirect(ActionEvent event) throws IOException {
        Parent parent3 = FXMLLoader.load(getClass().getResource("/app/views/ViewForgetPassword.fxml"));
        Scene scene = new Scene(parent3);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
