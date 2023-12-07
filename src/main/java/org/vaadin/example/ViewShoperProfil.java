package org.vaadin.example;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.mail.MessagingException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.sneaker.service.AdressService;
import org.vaadin.example.sneaker.service.CountryService;
import org.vaadin.example.sneaker.service.LoginService;
import org.vaadin.example.sneaker.service.PersonService;
import org.vaadin.example.sneaker.service.RegisterService;
import org.vaadin.example.sneaker.service.ShoeService;

import com.vaadin.componentfactory.Autocomplete;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.Lumo;

public class ViewShoperProfil extends Dialog {

	public String DOCK = "dock";

	private Paragraph photoName;
	
	public String FULLSCREEN = "fullscreen";

	private boolean isDocked = false;
	
	private boolean isFullScreen = false;

	private Header header;
	
	private Button min;
	
	private Button max;

	private VerticalLayout content;
					
	private ValidTextField  name  = new ValidTextField ("Nom");;;
	
	private ValidTextField  firstName = new ValidTextField ("Prénom");;
	
	private ValidTextField  city = new ValidTextField ("Ville");
		
	private Button cancel = new Button ("Annuler");
		
	private Binder<Person> binder = new Binder<>(Person.class);
	
	private RegisterService registerService;
	
	private AdressService adressService;
	
	private Select<String> labelSelect;
	
	private PersonService personService;
	
	private LoginService loginService;
	
	private CountryService countryService;
	
    public ViewShoperProfil() {
		setDraggable(true);
		setModal(false);
		setResizable(true);

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


		// Button theming
		for (Button button : new Button[] { min, max, close }) {
			button.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
		}
		Button save = new Button ("Quitter");
		save.addClickListener(event -> close());

		add(save);

		Button resever = new Button ("Mettre de côté");
		add(resever);

	}

	public ViewShoperProfil(Person person) {
		setDraggable(true);
		setModal(false);
		setResizable(true);
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
		for (Button button : new Button[] { min, max, close }) {
			button.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
		}
		
		add(name);
		VerticalLayout verticalLayout =  new VerticalLayout();
		verticalLayout.add(name,firstName,city);
		byte[] imageBytes  = person.getImageProfil();
		StreamResource resource = new StreamResource("dummyImageName.jpg", () -> new ByteArrayInputStream(imageBytes));
		Image image = new Image(resource, "dummy image");
		add(image);

		add(verticalLayout);
		fillField(person);
		Button save = new Button ("Quitter");        		
        save.addClickListener(e -> 
        	close()
        );

		add(save);
	
	}
	
	private void fillField(Person p) {
		if (p != null) {
			this.city.setValue(p.getAddress().getCity());
			this.firstName.setValue(p.getFirstName());
			this.name.setValue(p.getLastName());
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

