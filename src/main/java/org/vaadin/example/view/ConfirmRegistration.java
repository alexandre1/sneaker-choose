package org.vaadin.example.view;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.service.MailWithAttachmentService;
import org.vaadin.example.entity.Person;
import org.vaadin.example.entity.Shop;
import org.vaadin.example.sneaker.service.LoginService;
import org.vaadin.example.sneaker.service.PersonService;
import org.vaadin.example.sneaker.service.ShopService;
import org.vaadin.example.utils.AES;
import org.vaadin.flow.helper.HasUrlParameterMapping;
import org.vaadin.flow.helper.UrlParameter;
import org.vaadin.flow.helper.UrlParameterMapping;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("confirmRegistration")
@UrlParameterMapping(":encryptedEmail")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@SuppressWarnings("serial")

public class ConfirmRegistration extends VerticalLayout implements HasUrlParameterMapping, BeforeEnterObserver { 
	
	private PersonService personService;
	
	private ShopService shopService;
	
	private String email;
	
	private String moveToPage;
	
	private Person person;
	
	private LoginService loginService;
	
	@UrlParameter
	public String encryptedEmail;
	
	private TextField login;
	
	private PasswordField password;
	
	private Button loginButton;
	
	public ConfirmRegistration (@Autowired PersonService personService, @Autowired LoginService loginService, @Autowired ShopService shopService) {
		this.personService = personService;
		this.loginService = loginService;
		this.shopService = shopService;
		this.setClassName("background-image2");
		String content = "<div><br/><br/><br/><br/><br/><br/><br/></div>"; // wrapping <div> tags are required here
	    Html html = new Html(content);		
	    HorizontalLayout layout = new HorizontalLayout();
	    add(html);
	    login = new TextField("Email");
	    password = new PasswordField("Mot de passe");	    	    
	    add(login,password);
	    loginButton = new Button("Login",
				e ->  {
			        String page = authenticate(login.getValue(), password.getValue());
					UI.getCurrent().navigate(page);
        }
	);
	    if (this.email != null) {
	    	String group =  person.getGroupOfUser();
	    	if (group.equals("SHOPER")) {
	    		moveToPage = "shopMainView";
	    	}else if (group.equals("SHOP")) {
	    		moveToPage = "shoperMainView";
    
	    }
		

	    }
		add(loginButton);
	}
	


	
	private String authenticate (String email, String password) {
		final String secretKey = "aslkdfjlkj)/d89723897bc";
	     
	    String originalString = password;
	    String encryptedString = AES.encrypt(originalString, secretKey) ;
	    String decryptedString = AES.decrypt(encryptedString, secretKey) ;
	     
			
		VaadinSession.getCurrent().getSession().setAttribute("email", email);
		VaadinSession.getCurrent().getSession().setAttribute("passsword", encryptedString);		
		//VaadinSession.getCurrent().getSession().setMaxInactiveInterval(1);
		String result = "";
		String page = loginService.loginNew(email, encryptedString);	    
		return page;
	}


	@Override
	public void beforeEnter(BeforeEnterEvent event) {			
		this.encryptedEmail = encryptedEmail;

	}
	
	public void setEncryptedEmail(String encryptedEmail) {
		this.encryptedEmail = encryptedEmail;
	}

	private String getEmail (String email) {
		final String secretKey = "aslkdfjlkj)/d89723897bc";
	     
	    String originalString = email;
	    String encryptedString = AES.encrypt(originalString, secretKey) ;
	    String decryptedString = AES.decrypt(email, secretKey) ;
	     
			
		return decryptedString;
	}

	public String getEncryptedEmail() {
		return encryptedEmail;
	}

	@Override
	public void setParameter(BeforeEvent event,
	        @OptionalParameter String parameter) {

	    Location location = event.getLocation();
	    QueryParameters queryParameters = location.getQueryParameters();
	    String encryptedEmail = queryParameters.getParameters().get("encryptedEmail").get(0);
		//String email = this.getEmail(encryptedEmail);
    	String email = new String(Base64.getDecoder().decode(encryptedEmail.getBytes(StandardCharsets.UTF_8)));

    	Person person = this.personService.findByEmail(email);	
    	Shop shop = this.shopService.findByEmail(email);
    	if (person != null) {
    		this.email = person.getEmail();
    		this.personService.confirmRegistation(person);
    		this.person = person;
    		System.out.println("PERSON  " + person.getEmail());
    		if (person.isActive() != null && person.isActive() == true) {
    			this.remove(login,password,loginButton);
    			Label label = new Label ("Ce compte est déjà activé dans la base de données veuillez vous identifiez avec votre email et password où si vous avez perdu votre mot de passe veuillez clicker sur réinitialiser");
    			add(label);
    			String content = "<div><br/><br/><br/><br/><br/><br/><br/></div>"; // wrapping <div> tags are required here
    		    Html html = new Html(content);		
    		    HorizontalLayout layout = new HorizontalLayout();
    		    add(html);
    		    login = new TextField("Email");
    		    password = new PasswordField("Mot de passe");	    	    
    		    add(login,password);
    		    loginButton = new Button("Login",
    					e ->  {
    				        String page = authenticate(login.getValue(), password.getValue());
    						UI.getCurrent().navigate(page);
    	        }
    		);
    		    if (this.email != null) {
    		    	String group =  person.getGroupOfUser();
    		    	if (group.equals("SHOPER")) {
    		    		moveToPage = "shopMainView";
    		    	}else if (group.equals("SHOP")) {
    		    		moveToPage = "shoperMainView";
    	    
    		    }
    			

    		    }

    			Button reinitializeButton = new Button("Récupérer votre mot de passe",
     					e ->  {
     						sendEmailForShoes(person);     
     						}
     		);	
    			layout.add(loginButton,reinitializeButton);
    			add(layout);
    		}else {
    			this.personService.confirmRegistation(person);
    			Label label = new Label ("Ce compte est maintenant validé dans la base de données veuillez fournir votre email et mot de passe pour vous connectez");
    			add(label);
    			String content = "<div><br/><br/><br/><br/></div>"; // wrapping <div> tags are required here
    		    Html html = new Html(content);		
    		    HorizontalLayout layout = new HorizontalLayout();
    		    add(html);
    	}
	    
    		} else {
    			if (shop != null) {
    				this.shopService.confirmRegistration(shop);
        			Label label = new Label ("Ce compte est maintenant validé dans la base de données veuillez fournir votre email et mot de passe pour vous connectez");
        			add(label);
        			String content = "<div><br/><br/><br/><br/></div>"; // wrapping <div> tags are required here
        		    Html html = new Html(content);		
        		    HorizontalLayout layout = new HorizontalLayout();
        		    add(html);
        				
    			}
    		}
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

}



