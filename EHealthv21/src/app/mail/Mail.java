/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.mail;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author anasz
 */
public class Mail {

    public static void sendMail(String recepient, String objet, String contenu) {

    }

    public static void sendMail(String recepient, int code, String text) throws MessagingException {

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String myAccountEmail = "ehealthcodeit@gmail.com";
        String myAccountPassword = "EHealth2021";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, myAccountPassword);
            }
        });
//
        if (code == 1) { //Si le code=1 envoyé un mail de blocage
            Message message = prepareMessageBlock(session, myAccountEmail, recepient);
            Transport.send(message);

        }
        if (code == 2) {// si le code=2 envoyé un mail de débloblocage
            Message message = prepareMessageDeblock(session, myAccountEmail, recepient);
            Transport.send(message);
        }
        if (code == 3) { // si le code=3 envoyé une réclamation
            Message message = prepareMessageReclamation(session, myAccountEmail, recepient, text);
            Transport.send(message);
        }
        if (code == 4) { // si le code=3 envoyé une réclamation
            Message message = prepareMessageNouvelleReponseAjoute(session, myAccountEmail, recepient);
            Transport.send(message);
        }

    }


    /*public static void main(String[] args) {
        try {
            sendMail("anas.zitoun@esprit.tn");
        } catch (MessagingException ex) {
            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
        }

    } */
    private static Message prepareMessageBlock(Session session, String myAccountEmail, String recepient) {

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("Utilisateur bloqué");
            message.setText("Vous êtes bloqué à cause de vos question qui présente des termes de violence.\n Merci de les supprimer ou les modifier pour pouvoir poser des questions\n Cordialement");
            return message;

        } catch (Exception ex) {
            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    private static Message prepareMessageDeblock(Session session, String myAccountEmail, String recepient) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("Demande de déblocage");
            message.setText("Certain utilisateur demande un déblocage.\n Verifier la liste des patients pour pouvoir les débloquer\n Cordialement");
            return message;

        } catch (Exception ex) {
            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static Message prepareMessageReclamation(Session session, String myAccountEmail, String recepient, String text) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("Reclamation");
            message.setText("Bonjour, l'un des medecins à reclamer à cette question qui présente des termes de violence \n * " + text + " * \n  Merci de bloqué cet utilisateur si c'est nécessaire \n Cordialement");
            return message;

        } catch (Exception ex) {
            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static Message prepareMessageNouvelleReponseAjoute(Session session, String myAccountEmail, String recepient) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("Reponse ajouté");
            message.setText("Bonjour,  une réponse à votre question est ajouté,   \n  Vérifier votre compte \n Cordialement");
            return message;

        } catch (Exception ex) {
            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
