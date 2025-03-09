package org.vaadin.example.service;

import org.vaadin.example.entity.Person;
import org.vaadin.example.utils.AES;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class    MailWithAttachmentService {

    private String username = "";
    private String password = "";
    private String host = "";
    private String port = "";

    public MailWithAttachmentService() {
    }

    MailWithAttachmentService(String username, String password, String host, String port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    public Session getSession() {
    	final String username = "alexjaquet@gmail.com";
        final String password = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.socketFactory.port", "587");    
        props.put("mail.smtp.socketFactory.class",    
                  "javax.net.ssl.SSLSocketFactory");    
        props.put("mail.smtp.auth", "true");    
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
         new javax.mail.Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
          }
         });

        return session;
    }

    public Message createMail(Session session, String fileName, String textField, String sizeSelectUE, String sizeSelectUs ,String  sizeCm, String email) throws AddressException, MessagingException, IOException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("alexjaquet@gmail.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("Nouvelle recherche de chaussures");

        BodyPart messageBodyPart = new MimeBodyPart();
        
        messageBodyPart.setText("Bonjour un client recherche actuellement cette paire de chaussures ! + " + " De la marque : " + textField + " de la taille UE " + sizeSelectUE  + " ou de taille US : " + sizeSelectUs + " ou en cm : " + sizeCm  + " Veuillez s'il vous plaît la mettre de côté, Si vous l'avez en stock connectez-vous à votre compte");
        
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        MimeBodyPart attachmentPart = new MimeBodyPart();
        MimeBodyPart attachmentPart2 = new MimeBodyPart();

        attachmentPart.attachFile(new File(fileName));
        multipart.addBodyPart(attachmentPart);
        message.setContent(multipart);

        return message;
    }

    public Message createMailForShoper(Session session, String fileName, String textField, String sizeSelectUE, String sizeSelectUs ,String  sizeCm, String email, String boutique, String cas, String price) throws AddressException, MessagingException, IOException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("alexjaquet@gmail.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("Nouvelle paire de chaussures trouvé");

        BodyPart messageBodyPart = new MimeBodyPart();

        if (cas.equals("Livrer moi")) {
        	messageBodyPart.setText("Bonjour une boutique " + boutique + " à mis de côté pour vous cette paire de chaussure de la marque : " + textField + " de la taille UE " + sizeSelectUE  + " ou de taille US : " + sizeSelectUs + " ou en cm : " + sizeCm  + " Pour un prix de " + price + " Veuillez s'il vous plaît vous connectez à votre compte et définir l'adresse de livraison");
        }else if (cas.equals("Venir chercher en boutique")) {
        	messageBodyPart.setText("Bonjour une boutique " + boutique + " à mis de côté pour vous cette paire de chaussure de la marque : " + textField + " de la taille UE " + sizeSelectUE  + " ou de taille US : " + sizeSelectUs + " ou en cm : " + sizeCm  + " Pour un prix de " + price + " Veuillez s'il vous plaît vous connectez à votre compte et passer en boutique");        	
        }else if (cas.equals("Contact téléphonique")) {
        	messageBodyPart.setText("Bonjour une boutique " + boutique + " à mis de côté pour vous cette paire de chaussure de la marque : " + textField + " de la taille UE " + sizeSelectUE  + " ou de taille US : " + sizeSelectUs + " ou en cm : " + sizeCm  + " Pour un prix de " + price + " Veuillez s'il vous plaît vous connectez à votre compte et téléphoner à la boutique");        	
        }
        
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        MimeBodyPart attachmentPart = new MimeBodyPart();
        MimeBodyPart attachmentPart2 = new MimeBodyPart();

        attachmentPart.attachFile(new File(fileName));
        multipart.addBodyPart(attachmentPart);
        message.setContent(multipart);

        return message;
    }
    public Message createMailForShoperSell(Session session, String fileName, String textField, String sizeSelectUE, String sizeSelectUs ,String  sizeCm, String email, String boutique, String cas, String prix) throws AddressException, MessagingException, IOException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("alexjaquet@gmail.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("Nouvelle paire de chaussures trouvé");

        BodyPart messageBodyPart = new MimeBodyPart();
       	messageBodyPart.setText("Bonjour une boutique " + boutique + " vous a vendu une paire de chaussures de la marque : " + textField + " de la taille UE " + sizeSelectUE  + " ou de taille US : " + sizeSelectUs + " ou en cm : " + sizeCm  +  " pour le prix de :"  + prix +  " Veuillez s'il vous plaît vous connectez à votre compte et définir l'adresse de livraison");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        MimeBodyPart attachmentPart = new MimeBodyPart();
        MimeBodyPart attachmentPart2 = new MimeBodyPart();

        attachmentPart.attachFile(new File(fileName));
        multipart.addBodyPart(attachmentPart);
        message.setContent(multipart);

        return message;
    }


    public Message createMailRegistration(Session session, String email, Person p, String url) throws AddressException, MessagingException, IOException {
    	String encodedEmail = new String(Base64.getEncoder().encode(
                email.getBytes(StandardCharsets.UTF_8)));
    	
    	String aHref = "<a href=" + "\"" + url + encodedEmail + "\"" + ">" + "Veuillez confirmer votre inscription à Choose !"+ "</a>";
        String html = "Bonjour " + p.getFirstName()  +
        		", <br/> Votre inscription vous permet de trouver la chaussure de vos rêves en boutique ! <br/> Afin de confirmer votre inscription, nous avons besoin d'être sûrs que vous êtes" +
        		" bien l'auteur et que votre email est bien valide, pour cela merci de cliquez sur le lien ci-dessous <br/>" + 
        		aHref +
        		"Si vous n'êtes pas à l'origine de l'inscription sur le site Choose !, c'est qu'il s'agit d'une erreur. Ignorez <br/>  " +
        		"simplement ce message et ne cliquez pas sur le lien." + 
        		"<br/><br/><br/><br/>" + 
        		"Nous nous réjouissons de partager le plaisir du shopping avec vous." +
        		"<br/><br/>Choose !";

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("alexjaquet@gmail.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(p.getEmail()));
        message.setSubject("Confirmation de votre inscription à Choose !");

        Multipart multipart = new MimeMultipart( "alternative" );

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText( aHref, "utf-8" );

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent( html, "text/html; charset=utf-8" );

        multipart.addBodyPart( textPart );
        multipart.addBodyPart( htmlPart );
        message.setContent( multipart );


        // Required magic (violates principle of least astonishment).
        message.saveChanges();
        return message;
    }
    
	private String getPassword (String email) {
		final String secretKey = "aslkdfjlkj)/d89723897bc";
	     
	    String originalString = email;
	    String encryptedString = AES.encrypt(originalString, secretKey) ;
	    String decryptedString = AES.decrypt(encryptedString, secretKey) ;
	     
			
		return encryptedString;
	}

	private String getDecryptedPassword (String email) {
		final String secretKey = "aslkdfjlkj)/d89723897bc";
	     
	    String originalString = email;
	    String encryptedString = AES.encrypt(originalString, secretKey) ;
	    String decryptedString = AES.decrypt(email, secretKey) ;
	     
			
		return decryptedString;
	}



    public void sendRegistrationMail (Session session, String email, Person p, String url) throws AddressException, MessagingException, IOException {
    	System.out.println(" Sending "  + email);
    	
        Message message = createMailRegistration(session,email,p, url);
        Transport.send(message);
    }
    
    public void sendPassword (Session session, String email, Person p) throws AddressException, MessagingException, IOException {
    	System.out.println(" Sending "  + email);
    	
        Message message = createMailLostPassword(session,email,p);
        Transport.send(message);
    }

    public void sendMail(Session session,String fileName,String textField, String sizeSelectUE, String sizeSelectUs ,String  sizeCm, String email) throws MessagingException, IOException {    	
    	System.out.println(" Sending "  + fileName);
        Message message = createMail(session,fileName,textField, sizeSelectUE, sizeSelectUs ,  sizeCm, email);
        Transport.send(message);
    }
    
    public void sendMailForShoper(Session session,String fileName,String textField, String sizeSelectUE, String sizeSelectUs ,String  sizeCm, String email, String boutique, String cas, String price) throws MessagingException, IOException {    	
    	System.out.println(" Sending "  + fileName);
    	System.out.println(" TextField "  + textField);
    	System.out.println(" sizeSelectUE "  + sizeSelectUE);
    	System.out.println(" sizeSelectUs "  + sizeSelectUs);
    	System.out.println(" sizeSelectUs "  + sizeCm);
// 		String email, String boutique, String cas, String price
    	System.out.println(" email "  + email);
    	System.out.println(" Boutique "  + boutique);
    	System.out.println(" Price "  + price);
        Message message = createMailForShoper(session,fileName,textField, sizeSelectUE, sizeSelectUs ,  sizeCm, email, boutique, cas, price);
        Transport.send(message);
    }
    

    public void     sendMailForShoperSell(Session session,String fileName,String textField, String sizeSelectUE, String sizeSelectUs ,String  sizeCm, String email, String boutique, String cas, String prix) throws MessagingException, IOException {    	
    	System.out.println(" Sending "  + fileName);
        Message message = createMailForShoperSell(session,fileName,textField, sizeSelectUE, sizeSelectUs ,  sizeCm, email, boutique, cas, prix);
        Transport.send(message);
    }

    public Message createMailLostPassword(Session session, String email,Person p) throws AddressException, MessagingException, IOException {
    	String aHref = "<a href=" + "\"" + "http://localhost:8084/confirmRegistration?encryptedEmail="+ getPassword(email) + "\"" + ">" + "Veuillez confirmer votre inscription à Choose !"+ "</a>";
        String html = "Bonjour " + p.getFirstName()  +
        		", <br/> Votre mot de passe pour l'application et le site Choose ! est le suivant " +getDecryptedPassword( p.getPassword());

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("alexjaquet@gmail.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(p.getEmail()));
        message.setSubject("Votre mot de passe à Choose !");

        Multipart multipart = new MimeMultipart( "alternative" );

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText( aHref, "utf-8" );

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent( html, "text/html; charset=utf-8" );

        multipart.addBodyPart( textPart );
        multipart.addBodyPart( htmlPart );
        message.setContent( multipart );


        // Required magic (violates principle of least astonishment).
        message.saveChanges();
        return message;
    }


    
}
