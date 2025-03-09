package org.vaadin.example.view;

import java.io.File;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.vaadin.example.entity.Address;
import org.vaadin.example.entity.Person;
import org.vaadin.example.entity.Shop;
import org.vaadin.example.sneaker.service.PersonService;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import com.vaadin.annotations.JavaScript;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.theme.lumo.Lumo;
import org.vaadin.example.utils.VaadinI18NProvider;

@JavaScript("https://js.stripe.com/v3/")

public class PaymentStatus extends Dialog {

	//public String key = "sk_test_51KYtlEJQxBwCBmV4jPk64zZcdNX15kZEQcfwoe8BAvbwYrWoHfYtlKWFma8ZRhU0fkWoPGL5nRSUgBQW16jtl0rd00oXLGJHrz";
	public String key = "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";
	
	public static final String CREATION_DATE = "lbl_creation_date";
	
	public static final String ENDING_DATE = "lbl_ending_date";
	
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
	
	private static final String LABEL_STATUS = "lbl_view_status";
	
	private Person person;

	private PersonService personService;
	
	private Shop shop;
	
	public PaymentStatus (@Autowired PersonService personServic, Person person) {
		setDraggable(true);
		setModal(false);
		setResizable(true);

		this.personService = personService;		
		this.person = person;
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
		showStatus();
		// Button theming
		for (Button button : new Button[] { min, max, close }) {
			button.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
		}
		String content = "<div><br/></div>"; // wrapping <div> tags are required here
	    Html html = new Html(content);
	    add(html);

		Button save = new Button (provider.getTranslation(LABEL_QUIT, getLocale()));        		
        save.addClickListener(e -> 
        	close()
        );
        
	
		add(save);


	}
	

	private void showStatus () {
		try {
			Stripe.apiKey = key;
			Subscription subscription =
					  Subscription.retrieve(
					    person.getSubscriptionId()
					  );			
			Date d = new Date(subscription.getCurrentPeriodEnd() * 1000);
			add (new Label (provider.getTranslation(ENDING_DATE, getLocale()) + d.toLocaleString().toString()));
		}catch (StripeException ex) {
			System.out.println(ex.getMessage());
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
	
}
