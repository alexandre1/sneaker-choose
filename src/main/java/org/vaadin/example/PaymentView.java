package org.vaadin.example;		

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.vaadin.example.sneaker.service.PersonService;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.PaymentMethodCollection;
import com.stripe.model.SetupIntent;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.model.checkout.SessionCollection;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SubscriptionCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.vaadin.annotations.JavaScript;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;

@JavaScript("https://js.stripe.com/v3/")
@Route(value = "payment")

public class PaymentView extends Dialog  {
	
	//public String key = "sk_test_51KYtlEJQxBwCBmV4jPk64zZcdNX15kZEQcfwoe8BAvbwYrWoHfYtlKWFma8ZRhU0fkWoPGL5nRSUgBQW16jtl0rd00oXLGJHrz";
	public String key = "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";
	
	public String DOCK = "dock";

	private Paragraph photoName;
	
	public String FULLSCREEN = "fullscreen";

	private boolean isDocked = false;
	
	private boolean isFullScreen = false;

	private Header header;
	
	private Button min;
	
	private Button max;

	private VerticalLayout content;

	private Image image = new Image();
	
	private TextField marque = new TextField ("Marque");;
	
	private Address address = new Address ();		 
	
	private TextField size = new TextField ("Taille");
	
	private TextField category = new TextField ("Sexe");

	private Select<String> labelReservation = new Select<String>();

	private TextField price = new TextField ("Prix");
	
	private MemoryBuffer buffer = new MemoryBuffer();
	
	private File targetFile;
	
	private Component previousPhoto;
	
	@Autowired
	private Environment env;

	private String path;
	
	private static final VaadinI18NProvider provider = new VaadinI18NProvider();
	
	private static final String LABEL_QUIT = "lbl_quit";
	
	private static final String LABEL_DEFINE_AS_SOLD = "lbl_define_as_sold";
	
	private static final String LABEL_TAKE_PICTURE = "lbl_take_picture";
	
	private static final String LABEL_RESERVED = "lbl_reserved";
	
	private static final String LABEL_EMAIL_SENDED= "lbl_email_sended";
	
	private static final String LABEL_COME_IN_SHOP = "lbl_come_in_shop";
	
	private static final String LABEL_PHONE_CONTACT = "lbl_phone_contact";
	
	private static final String LABEL_DELIVER_ME = "lbl_deliver_me";
	
	private static final String LABEL_RESERVATION = "lbl_reservation";
	
	private static final String LABEL_DEFINE_AS_RESERVED = "lbl_define_as_reserved";
	
	private static final String LABEL_PAY_WITH_STRIPE = "lbl_pay_stripe";
	
	private Person person;

	private PersonService personService;
	
	private Shop shop;
	
	public PaymentView (Person person, @Autowired PersonService personService, Shop shop, Environment env) {
		setDraggable(true);
		setModal(false);
		setResizable(true);
		this.env = env;
		this.personService = personService;
		this.shop = shop;
		// Dialog theming
		getElement().getThemeList().add("my-dialog");
		setWidth("600px");

		// Accessibility
		getElement().setAttribute("aria-labelledby", "dialog-title");

		// Header
		H2 title = new H2("Détails");
		title.addClassName("dialog-title");

		min = new Button(VaadinIcon.DOWNLOAD_ALT.create());
		min.addClickListener(event -> minimise());

		max = new Button(VaadinIcon.EXPAND_SQUARE.create());
		max.addClickListener(event -> maximise());

		Button close = new Button(VaadinIcon.CLOSE_SMALL.create());
		close.addClickListener(event -> close());

		header = new Header(title, min, max, close);
		header.getElement().getThemeList().add(Lumo.DARK);
		add(header);

		this.person = person;

		content = new VerticalLayout();
		content.addClassName("dialog-content");
		content.setAlignItems(Alignment.STRETCH);
		add(content);
		// Button theming
		for (Button button : new Button[] { min, max, close }) {
			button.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
		}
		Button save = new Button (provider.getTranslation(LABEL_QUIT, getLocale()));        		
        save.addClickListener(e -> 
        	close()
        );
        
	
		add(save);

		Button resever = new Button (provider.getTranslation(LABEL_PAY_WITH_STRIPE, getLocale()));
		resever.addClickListener(e -> 
			createCustomer()
			
		);
		add(resever);
		sendEmailForShoes(person);
	}
	
	public String getConfirmUrl() {
		return env.getProperty("sneaker.confirmUrl");
	}
	
	private void sendEmailForShoes (Person p) {
        MailWithAttachmentService mail = new MailWithAttachmentService();      
        try {                                      	
	        try {
	        	mail.sendRegistrationMail(mail.getSession(),p.getEmail(),p, getConfirmUrl());
	        } catch (IOException ex) {
	        	System.out.println(ex.getMessage());
	        } catch (MessagingException ex) {
	        	System.out.println(ex.getMessage());
	        }	           
    }finally {
    	System.out.println("Email sended succefully for " + p.getEmail());
    	} 
	}


	
	private void minimise() {
		if (isDocked) {
			initialSize();
		} else {
			if (isFullScreen) {
				initialSize();
			}
			min.setIcon(VaadinIcon.UPLOAD_ALT.create());
			getElement().getThemeList().add(DOCK);
			setWidth("320px");
		}
		isDocked = !isDocked;
		isFullScreen = false;
		content.setVisible(!isDocked);		
	}

	private void initialSize() {
		min.setIcon(VaadinIcon.DOWNLOAD_ALT.create());
		getElement().getThemeList().remove(DOCK);
		max.setIcon(VaadinIcon.EXPAND_SQUARE.create());
		getElement().getThemeList().remove(FULLSCREEN);
		setHeight("auto");
		setWidth("600px");
	}

	private void maximise() {
		if (isFullScreen) {
			initialSize();
		} else {
			if (isDocked) {
				initialSize();
			}
			max.setIcon(VaadinIcon.COMPRESS_SQUARE.create());
			getElement().getThemeList().add(FULLSCREEN);
			setSizeFull();
			content.setVisible(true);			
		}
		isFullScreen = !isFullScreen;
		isDocked = false;		
}
	public Session createStripeSessionOLD(String customerEmail) {
		Stripe.apiKey = key;
		
		Map<String, Object> params = new HashMap<>();
		params.put("limit", 3);
		Session last = null;
		try {
			SessionCollection sessions = Session.list(params);
			List<Session> list = sessions.getData();
			last = list.get(list.size() - 1);
			System.out.println(last.getId());
		}catch (StripeException ex) {
			System.out.println(ex.getMessage());
		}
			SessionCreateParams params2 = new SessionCreateParams.Builder()
				.setSuccessUrl("https://env-4428752.jcloud.ik-server.com/success?session_id=" + last.getId())
				.setCancelUrl("https://env-4428752.jcloud.ik-server.com/cancel")
				.setMode(SessionCreateParams.Mode.SUBSCRIPTION)
				.setCustomerEmail(customerEmail)
				.addLineItem(new SessionCreateParams.LineItem.Builder()
	    // For metered billing, do not pass quantity
				.setQuantity(1L)
				.setPrice("price_1KYtmOJQxBwCBmV4UQ1aTJRU")
				.build()
				)
				.build();
		Session session = null;
		try {
			session = Session.create(params2);
//			UI ui = getUI().get();
		    //ui.getPage().executeJs("window.location.href='success.html'");
		}catch (StripeException ex) {
			System.out.println(ex.getMessage());
		}
		return session;
	}
	
	public Session createStripeSession(String customerEmail) {
		Stripe.apiKey = key;
		String priceId = "price_1KsPySEJYxYFb5Gp8UgQtChT";
		SessionCreateParams params = new SessionCreateParams.Builder()
				  .setSuccessUrl("http://env-4428752.jcloud.ik-server.com/success?session_id={CHECKOUT_SESSION_ID}")
				  .setCancelUrl("http://env-4428752.jcloud.ik-server.com/canceled")
				  .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
				  .addLineItem(new SessionCreateParams.LineItem.Builder()
				    // For metered billing, do not pass quantity
				    .setQuantity(1L)
				    .setPrice(priceId)
				    .build()
				  )
				  .build();
				Session session = null;
				try { 
					session = Session.create(params);
				}catch (StripeException ex) {
					System.out.println(ex.getMessage());
				}
				return session;

	}

	public Customer createCustomer() {
		Stripe.apiKey = key;
		Customer customer = null;
		CustomerCreateParams params =
				  CustomerCreateParams
				    .builder()
				    .setEmail(person.getEmail())				    
				    //.setPaymentMethod("pm_card_visa")				  
				    .setInvoiceSettings(
				      CustomerCreateParams.InvoiceSettings
				        .builder()
				      //  .setDefaultPaymentMethod("pm_card_visa")
				        .build()
				    )
				    .build();
				try {
					customer = Customer.create(params);
					person.setStripeId(customer.getId());
					Session session = createStripeSession(customer.getEmail());
					person.setSessionId(session.getId());				
					System.out.println(customer.getEmail());
					this.personService.update(customer.getId(), person);
					subscribe(customer, session);					
					
				}catch (StripeException ex) {
					System.out.println(ex.getMessage());
				}
				return customer;	
	}
	public void subscribe(Customer customer, Session session) throws StripeException {
		Stripe.apiKey = key;
		List<Object> phases = new ArrayList<>();
		List<Object> items = new ArrayList<>();
		Map<String, Object> item1 = new HashMap<>();
		item1.put(
		  "price",
		  //"price_1KYtmOJQxBwCBmV4UQ1aTJRU"
		  "price_1KsPySEJYxYFb5Gp8UgQtChT"
		);
		item1.put("quantity", 1);
		items.add(item1);
		Map<String, Object> phase1 = new HashMap<>();
		phase1.put("items", items);
		phase1.put("iterations", 12);
		SessionCreateParams params = new SessionCreateParams.Builder()
		  .setSuccessUrl("https://env-4428752.jcloud.ik-server.com/success?session_id=" + session.getId())
		  .setCancelUrl("https://env-4428752.jcloud.ik-server.com/canceled")
		  
		  .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
		  .addLineItem(new SessionCreateParams.LineItem.Builder()
		    // For metered billing, do not pass quantity
		    .setQuantity(1L)
		    //.setPrice("price_1KYtmOJQxBwCBmV4UQ1aTJRU") 
		    .setPrice("price_1KsPySEJYxYFb5Gp8UgQtChT")		    
		    .build()
		  )
		  .build();

		session = Session.create(params);
		person.setSubscriptionId(session.getId());
		this.personService.update(customer.getId(), person, person.getSubscriptionId());		
		getUI().get().getPage().executeJavaScript("window.location.href = " + "'" + session.getUrl() + "'");
	}

}
