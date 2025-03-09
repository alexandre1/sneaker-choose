package org.vaadin.example.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.mail.MessagingException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.vaadin.example.entity.Address;
import org.vaadin.example.entity.Shoe;
import org.vaadin.example.entity.Shop;
import org.vaadin.example.service.MailWithAttachmentService;
import org.vaadin.example.sneaker.service.ShoeService;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.Lumo;
import org.vaadin.example.utils.VaadinI18NProvider;

public class ViewChangeStateShoes extends Dialog {

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
	
	private static final String LABEL_QUIT = "lbl_quit";
	
	private static final String LABEL_RESERVED = "lbl_reserved";
	
	private static final String LABEL_DEFINE_AS_RESERVED = "lbl_define_as_sold";
	
	private static final String LABEL_EMAIL_SENDED= "lbl_email_sended";
	
	private static final String LABEL_COME_IN_SHOP = "lbl_come_in_shop";
	
	private static final String LABEL_PHONE_CONTACT = "lbl_phone_contact";
	
	private static final String LABEL_DELIVER_ME = "lbl_deliver_me";
	
	private static final String LABEL_RESERVATION = "lbl_reservation";
	
	private static final VaadinI18NProvider provider = new VaadinI18NProvider();
	
	public ViewChangeStateShoes() {
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


		content = new VerticalLayout(marque, size,category, labelReservation, price);
		content.addClassName("dialog-content");
		content.setAlignItems(Alignment.STRETCH);
		add(content);
		// Button theming
		for (Button button : new Button[] { min, max, close }) {
			button.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
		}
		Button save = new Button (provider.getTranslation(LABEL_QUIT, getLocale()));
		save.addClickListener(event -> close());

		add(save);

		Button resever = new Button (provider.getTranslation(LABEL_RESERVED, getLocale()));
		add(resever);

	}

	public ViewChangeStateShoes(Shoe shoes, @Autowired ShoeService shoeService, String path) {
		setDraggable(true);
		setModal(false);
		setResizable(true);
		this.path = path;
        HorizontalLayout layout = new HorizontalLayout();
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

		content = new VerticalLayout();
        
        //add(layout); 

		content.addClassName("dialog-content");
		content.setAlignItems(Alignment.STRETCH);
		add(content);
		// Button theming
		for (Button button : new Button[] { min, max, close }) {
			button.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
		}
		Button save = new Button (provider.getTranslation(LABEL_QUIT, getLocale()));
		save.addClickListener(event -> close());

		

		Button resever = new Button (provider.getTranslation(LABEL_DEFINE_AS_RESERVED, getLocale()),	        
                e ->  {
                	mettreDeCote(shoes, shoeService, targetFile);
                	Notification.show(provider.getTranslation(LABEL_EMAIL_SENDED, getLocale()));
                }
        	);


		
		this.marque.setValue(shoes.getMarque());
		if (shoes.getSizeUE() != null && shoes.getSizeUE().length() > 0) {
			this.size.setValue("UE : " + shoes.getSizeUE());
		} else if (shoes.getSizeUS() != null && shoes.getSizeUS().length() > 0) {
			this.size.setValue("US : " + shoes.getSizeUS());
		}else if (shoes.getSizeCM() != null && shoes.getSizeCM().length() > 0) {
			this.size.setValue("CM : " + shoes.getSizeCM());
		}
		if (shoes.getCategory() != null && shoes.getCategory().length() > 0 ) {
			this.category.setValue(provider.getTranslation(shoes.getCategory(), getLocale()));
		}

		labelReservation.setItems(provider.getTranslation(LABEL_COME_IN_SHOP, getLocale()), provider.getTranslation(LABEL_PHONE_CONTACT, getLocale()), provider.getTranslation(LABEL_DELIVER_ME, getLocale()));
		labelReservation.setLabel(provider.getTranslation(LABEL_RESERVATION, getLocale()));
	    //add(labelReservation);
		add(save);
		add(resever);
	}
	
    private Component createComponent(String mimeType, String fileName, InputStream stream) {
    	 if (mimeType.startsWith("image")) {
             Image image = new Image();
             try {
                 byte[] bytes = IOUtils.toByteArray(stream);
                 URL url = this.getClass().getResource("/upload/");
                 image
                     .getElement()
                     .setAttribute("src", new StreamResource(fileName, () -> new ByteArrayInputStream(bytes)));                
                 try (ImageInputStream in = ImageIO.createImageInputStream(new ByteArrayInputStream(bytes))) {

                     final Iterator readers = ImageIO.getImageReaders(in);
                     if (readers.hasNext()) {
                         ImageReader reader = (ImageReader) readers.next();                                                
                             reader.setInput(in);
                             image.setMaxWidth("100%");
                             image.setSrc(new StreamResource("upload", () -> new ByteArrayInputStream(bytes)));
                             image.setVisible(true);                             
                         	 reader.dispose();
                         }
                     }                
             } catch (IOException e) {
                 e.printStackTrace();
             }                         
             return image;
         }
         Div content = new Div();
         String text = String.format(
             "Mime type: '%s'\nSHA-256 hash: '%s'",
             mimeType,
             Arrays.toString(MessageDigestUtil.sha256(stream.toString()))
         );
         content.setText(text);
         return content;
     }
    

    private void showOutput(String text, Component content, HasComponents outputContainer) {
        if (photoName != null) {
            outputContainer.remove(photoName);
        }
        if (previousPhoto != null) {
            outputContainer.remove(previousPhoto);
        }
        photoName = new Paragraph(text);
        outputContainer.add(photoName);
        previousPhoto = content;
        outputContainer.add(previousPhoto);
    }

    public static byte [] ImageToByte(File file) throws FileNotFoundException{    	
        FileInputStream fis = new FileInputStream(file);    	
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	        byte[] buf = new byte[1024];    	
        try {    	
            for (int readNum; (readNum = fis.read(buf)) != -1;) {    	
                bos.write(buf, 0, readNum);         	
                System.out.println("read " + readNum + " bytes,");
            }    	
        } catch (IOException ex) {    	
        }    	
        byte[] bytes = bos.toByteArray();
     return bytes;
}

	public String getAppPath() {
		return path;
	}

    private void mettreDeCote(Shoe shoes, ShoeService shoeService, File destinationFile) {
		if (shoes != null) {
			shoes.setState("Vendue");
			byte[] imageBytes;
			if (shoes.getImageFounded() != null) {
				imageBytes  = shoes.getImageFounded();
			} else {
				imageBytes  = shoes.getImage();
			}
			File file = new File(getAppPath() + shoes.getIdShoe() + ".jpg");
			try { 
				FileUtils.writeByteArrayToFile (file, imageBytes);
			}catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
			shoeService.updateShoeSellerSold(shoes.getReservation(), shoes.getPrix(),shoes);
			sendEmailToShoper(shoes,file.getAbsolutePath());
		}
	}
	
    private void sendEmailToShoper (Shoe shoes, String fileName) {
    	MailWithAttachmentService mailService = new MailWithAttachmentService();
        try {
            File file = new File(fileName);                          	
	        try {
	        	mailService.sendMailForShoperSell(mailService.getSession(), file.getAbsolutePath().toString(), shoes.getMarque(), shoes.getSizeUE(), shoes.getSizeUS() ,  shoes.getSizeCM(), shoes.getDemandeur().getEmail(),shoes.getVendeur().getNom(), this.labelReservation.getValue(), shoes.getPrix());
	        	System.out.println("SENDING TO SHOP : " + shoes.getDemandeur().getEmail() + " " + shoes.getVendeur().getNom() + " FILE + " + fileName);		        		        	
	        } catch (IOException ex) {
	        	System.out.println(ex.getMessage());
	        } catch (MessagingException ex) {
	        	System.out.println(ex.getMessage());
	        }	           
    }finally {
    	System.out.println("Email sended succefully");
    	} 
    	
    }

	public ViewChangeStateShoes(Shop shoe) {
		setDraggable(true);
		setModal(false);
		setResizable(true);

		// Dialog theming
		getElement().getThemeList().add("my-dialog");
		setWidth("600px");

		// Accessibility
		getElement().setAttribute("aria-labelledby", "dialog-title");

		// Header
		H2 title = new H2("Personne");
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


		content = new VerticalLayout(this.marque,this.category,this.size);
		content.addClassName("dialog-content");
		content.setAlignItems(Alignment.STRETCH);
		add(content);

		// Button theming
		for (Button button : new Button[] { min, max, close }) {
			button.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
		}
		Button save = new Button ("Sauvegarder");
		add(save);

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
