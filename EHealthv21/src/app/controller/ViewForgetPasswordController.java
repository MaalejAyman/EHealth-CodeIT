/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controller;

import app.entity.Patient;
import app.entity.User;
import app.service.ServicePatient;
import app.service.ServiceUser;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * FXML Controller class
 *
 * @author meria
 */
public class ViewForgetPasswordController implements Initializable {

    @FXML
    private Button btn_code;
    @FXML
    private TextField input_email_forgetPass;
    @FXML
    private TextField input_code;
    @FXML
    private Button btn_verif_code;
    private int randomCode;
    @FXML
    private Label label_erreur_code;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private int SendCode(ActionEvent event) throws Exception {

        try {
            Random random = new Random();
            randomCode = random.nextInt(999999);
            String host = "smtp.gmail.com";
            String user = "ehealthcodeit@gmail.com";
            String pass = "EHealth2021";
            String to = input_email_forgetPass.getText();
            String subject = "réinitialiser votre mot de passe";
            String message = "votre code de réinitialisation est :" + randomCode;
            boolean sessionDebug = false;
            Properties properties = System.getProperties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.starttls.required", "true");
            java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            Session mailSession = Session.getDefaultInstance(properties, null);
            mailSession.setDebug(sessionDebug);
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(user));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setSubject(subject);
            msg.setText(message);
            Transport transport = mailSession.getTransport("smtp");
            transport.connect(host, user, pass);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("code envoyé");
            alert.setHeaderText("votre code de verification a eté envoyé ");
            alert.showAndWait();
        } catch (AddressException ex) {
            Logger.getLogger(ViewForgetPasswordController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return randomCode;
    }

    @FXML
    private void VerifyCode(ActionEvent event) throws Exception {
        if (Integer.valueOf(input_code.getText()) == randomCode) {

            String email = input_email_forgetPass.getText();
            User u = new User();
            u.setEmail(email);
            ServiceUser su = new ServiceUser();
            User uu = su.loginUser(u);
            Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2i);
            String ch = "";
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
            ServiceUser sp = new ServiceUser();
            User p = sp.getUserById(uu.getId());
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/views/ViewNewPassword.fxml"));
                Parent root;
                ViewNewPasswordController controller = new ViewNewPasswordController(p);
                loader.setController(controller);
                root = loader.load();
                stage.setTitle("E-Health");
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(ViewLoginController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            label_erreur_code.setText("Votre code de verification est incorrect");
        }

    }

}
