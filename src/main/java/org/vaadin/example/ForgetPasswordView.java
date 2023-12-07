package org.vaadin.example;

import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.sneaker.service.PersonService;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;


@Route("forgetPasswordView")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@SuppressWarnings("serial")

public class ForgetPasswordView extends VerticalLayout {
	
	private TextField email = new TextField("Email");
	
	private Button send = new Button ("Envoyer");
	
	private PersonService personService;
	
	public ForgetPasswordView (@Autowired PersonService personService) {
		this.personService = personService;
		this.setClassName("background-image2");
		String content = "<div><br/><br/><br/><br/><br/><br/><br/></div>"; // wrapping <div> tags are required here
	    Html html = new Html(content);		
	    HorizontalLayout layout = new HorizontalLayout();
	    add(html);

		add(email);
		add(send);
		send.addClickListener(e-> {
			Person p = this.personService.findByEmail(this.email.getValue());
			if (p != null) {								
				sendEmailForShoes(p);
				UI.getCurrent().navigate("login");
				
			}
		});
	}
	
	private void sendEmailForShoes (Person p) {
        MailWithAttachmentService mail = new MailWithAttachmentService();      
        try {                                      	
	        try {
	        	mail.sendPassword(mail.getSession(),p.getEmail(),p);
	        } catch (IOException ex) {
	        	System.out.println(ex.getMessage());
	        } catch (MessagingException ex) {
	        	System.out.println(ex.getMessage());
	        }	           
    }finally {
    	System.out.println("Email sended succefully for " + p.getEmail());
    	} 
 }

	private String getPassword (String email, String password) {
		final String secretKey = "aslkdfjlkj)/d89723897bc";
	     
	    String originalString = password;
	    String encryptedString = AES.encrypt(originalString, secretKey) ;
	    String decryptedString = AES.decrypt(encryptedString, secretKey) ;
	     
			
		VaadinSession.getCurrent().getSession().setAttribute("email", email);
		VaadinSession.getCurrent().getSession().setAttribute("passsword", encryptedString);		
		String result = "";
		
	    System.out.println("PASSWORD TO REDIRECT :  " + encryptedString);
		return decryptedString;
	}


}
