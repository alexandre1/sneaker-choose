package org.vaadin.example;

import java.util.Locale;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.vaadin.example.sneaker.service.LoginService;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;

@Route("login")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@SuppressWarnings("serial")

public class LoginIView  extends VerticalLayout {

	private LoginService loginService;
	
	private final static String LABEL_CREATE_SHOPPER_ACCOUNT = "lbl_create_shopper_account";
	
	private final static String LABEL_CREATE_SHOP_ACCOUNT = "lbl_create_shop";
	
	private final static String LABEL_WRONG = "lbl_wrong_email_or_password";
	
	private final static String LABEL_LANGUAGE = "lbl_language";
	
	private static final Locale FR_CH = new Locale("fr", "CH");
	
	private static final Locale EN_US = new Locale("en", "US");

	public LoginIView (@Autowired LoginService loginService) {
	    VaadinI18NProvider provider = new VaadinI18NProvider();
		this.loginService = loginService;
		this.setClassName("background-image2");
		String content = "<div><br/><br/><br/><br/><br/><br/><br/></div>"; // wrapping <div> tags are required here
	    Html html = new Html(content);		
	    VerticalLayout layout = new VerticalLayout();
	    add(html);
	    LoginForm component = new LoginForm();
	    Label lblUserName = new Label("Username");
	    TextField txtFieldUserName = new TextField();
	    Label lblPassword = new Label("Password");
	    PasswordField passwordField = new PasswordField();
	    Anchor anchor = new Anchor();
	    anchor.setText("Forgot password");
	    
	    anchor.getElement().addEventListener("click", e -> { 
	    	String page = "";
	    	if (page != null) {
	    		navigateToMainPage("forgetPasswordView");
	    	}}
        
	    );
	    Label lblLangues = new Label(provider.getTranslation(LABEL_LANGUAGE, getLocale()));
	    Select<String> langues = new Select<String> (new String ("Français"), new String ("English"));
	    Button login = new Button("Login");
	    login.addClickListener(e-> {
	    	String lng = langues.getValue();
	    	if (lng.equals("Français")) {
	    		UI.getCurrent().getSession().setLocale(FR_CH);
	    		
	    	}else if (lng.equals("English")) {
	    		UI.getCurrent().getSession().setLocale(EN_US);
	    	}
	        String page = authenticate(txtFieldUserName.getValue(), passwordField.getValue());
	        if (page != null && page.length() > 0) {
	        	navigateToMainPage(page);
	        }if (page.equals("login")) {
	        	Label error = new Label("Invalid username or password");
	        	error.setClassName("label_error");
	        	add(error);
	        }
	    });
	    layout.add(lblUserName,txtFieldUserName,lblPassword,passwordField,lblLangues,  langues, login, anchor);
	    add(layout);
	    Button register = new Button(provider.getTranslation(LABEL_CREATE_SHOPPER_ACCOUNT, getLocale()));
	    register.addClickListener(e -> {
	        String page = "createProfile";
	        if (page != null) {
	        	navigateToMainPage(page);
	        }
	    });
	    HorizontalLayout hz = new HorizontalLayout();
	    hz.add(register);
	    Button registerShop = new Button(provider.getTranslation(LABEL_CREATE_SHOP_ACCOUNT, getLocale()));
	    registerShop.addClickListener(e -> {
	        String page = "createProfileShop";
	        if (page != null) {
	        	navigateToMainPage(page);
	        }
	    });
	    hz.add(registerShop);
	    add(hz);

	}

	private String authenticate (String email, String password) {
		final String secretKey = "aslkdfjlkj)/d89723897bc";
	     
	    String originalString = password;
	    String encryptedString = AES.encrypt(originalString, secretKey) ;
	    String decryptedString = AES.decrypt(encryptedString, secretKey) ;
	     
			
		VaadinSession.getCurrent().getSession().setAttribute("email", email);
		VaadinSession.getCurrent().getSession().setAttribute("password", encryptedString);		
		VaadinSession.getCurrent().getSession().setAttribute("auth", true);

		

		// Set the cookie path.
				//VaadinSession.getCurrent().getSession().setMaxInactiveInterval(1);
		String result = "";
		String page = loginService.login(email, encryptedString);
	    System.out.println("PAGE TO REDIRECT :  " + page);
	    Cookie myCookie = new Cookie("user_email", email);
	    Cookie myCookiePage = new Cookie("page_to_redirect", page);
	    myCookie.setPath(VaadinService.getCurrentRequest().getContextPath());

		// Save cookie
		VaadinService.getCurrentResponse().addCookie(myCookie);
	    myCookiePage.setPath(VaadinService.getCurrentRequest().getContextPath());

		// Save cookie
		VaadinService.getCurrentResponse().addCookie(myCookiePage);

	    return page;
	}
	
	private void navigateToMainPage(String page) {
		UI.getCurrent().navigate(page);
	}
	

}
