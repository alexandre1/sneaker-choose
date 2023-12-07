package org.vaadin.example;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {

    private String host = "";
    private int port = 0;
    private String username = "";
    private String password = "";


    public EmailService(String host, int port, String username, String password) {

        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;

        sendMail();
    }

    private void sendMail() {

    	final String username = "alexjaquet@gmail.com";
        final String password = "bxwpypgecjwvvthp";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "587");    
        props.put("mail.smtp.socketFactory.class",    
                  "javax.net.ssl.SSLSocketFactory");    
        props.put("mail.smtp.auth", "true");    
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(props,
         new javax.mail.Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
          }
         });

        try {

          Message message = new MimeMessage(session);
          message.setFrom(new InternetAddress("alexjaquet@gmail.com"));
          message.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse("alexjaquet@gmail.com"));
          message.setSubject("Testing Subject");
          message.setText("Dear Mail Crawler,"
            + "\n\n No spam to my email, please!");

          Transport.send(message);

          System.out.println("Done");

        } catch (MessagingException e) {
          throw new RuntimeException(e);
        }
      }
    
    public static void main(String ... args) {
        new EmailService("smtp.gmail.com", 587, "alexjaquet@gmail.com", "bxwpypgecjwvvthp");
    }

}