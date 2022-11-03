/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author meria
 */
public class SendMail {


    public static void EnvoyerMail(String recepient,String recepientPass) throws Exception{
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
         
         Message message = prepareMessage(session, myAccountEmail ,recepient,recepientPass);
         Transport.send(message);

    }
    private static Message prepareMessage(Session session, String myAccountEmail , String recepient,String recepientPass){
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("Nouveau Compte E-Health");
            message.setText("Votre compte a eté creer avec succes \n Bienvenu parmis nous \n Voici votre email : "+recepient+"\n et votre mot de passe : "+recepientPass );
            return message;
        } catch (MessagingException ex) {
            Logger.getLogger(SendMail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    } 

}
