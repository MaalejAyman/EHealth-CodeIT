/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controller;

import app.entity.User;
import app.service.ServiceUser;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author meria
 */
public class ViewNewPasswordController implements Initializable {

    @FXML
    private Label label_erreur;
    @FXML
    private PasswordField input_new_pass2;
    @FXML
    private Button btn_change_pass;
    @FXML
    private PasswordField input_new_pass;
    private User userEmail;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public ViewNewPasswordController(User value) {
        this.userEmail = value;

    }

    public ViewNewPasswordController() {
        this(new User());
    }

    public void setP(User value) {
        userEmail = value;

    }

    public void setNewPassword(User u) {
        this.userEmail = u;
        System.out.println(this.userEmail.getEmail());
    }

    @FXML
    private void resetPassword(ActionEvent event) throws Exception {
        System.out.println(userEmail);
        if (input_new_pass.getText().equals(input_new_pass2.getText())) {
            String pass = input_new_pass.getText();
            ServiceUser su = new ServiceUser();
            System.out.println(pass);
            ServiceUser us = new ServiceUser();

            userEmail.setPassword(pass);
            userEmail.hashPassword();
            su.updateUserPassword(userEmail);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reinistialisation de mot de passe");
            alert.setHeaderText("Votre mot de passe a eté modifié avec succées ");
            alert.showAndWait();
            ((Node) (event.getSource())).getScene().getWindow().hide();
            Parent parent3 = FXMLLoader.load(getClass().getResource("/app/views/ViewLogin.fxml"));
            Scene scene = new Scene(parent3);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }
    }

}
