package org.vaadin.example;

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
import org.vaadin.example.sneaker.service.ShoeService;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.Lumo;

public class ViewShopForShoper extends Dialog {

	public String DOCK = "dock";

	private Paragraph photoName;
	
	public String FULLSCREEN = "fullscreen";

	private boolean isDocked = false;
	
	private boolean isFullScreen = false;

	private Header header;
	
	private Button min;
	
	private Button max;

	private VerticalLayout content;
				
	private TextField nomDeLaBoutique = new TextField ("Nom de la boutique");
	
	private TextField adress = new TextField ("Adresse");
	
	private TextField city = new TextField ("Ville");
	
	private TextField npa = new TextField ("Npa");
	
	private Anchor web = new Anchor ();
		
	private MemoryBuffer buffer = new MemoryBuffer();
	
	private File targetFile;
	
	private Component previousPhoto;
	
	public ViewShopForShoper() {
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


		content = new VerticalLayout(nomDeLaBoutique);
		content.addClassName("dialog-content");
		content.setAlignItems(Alignment.STRETCH);
		add(content);
		// Button theming
		for (Button button : new Button[] { min, max, close }) {
			button.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
		}
		Button save = new Button ("Quitter");
		add(save);

		Button resever = new Button ("Mettre de côté");
		add(resever);

	}

	public ViewShopForShoper(Shop shoes, @Autowired ShoeService shoeService) {
		setDraggable(true);
		setModal(false);
		setResizable(true);
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
		nomDeLaBoutique.setValue(shoes.getNom());
		this.adress.setValue(shoes.getAdresse().getAdress());
		this.city.setValue(shoes.getAdresse().getCity());
		this.npa.setValue(shoes.getAdresse().getNpa());
		this.web.setHref("https://www.google.ch");
		this.web.setText("Voir le site web de la boutique");
		Label labelImage = new Label ("Image désirée");
		HorizontalLayout hLayout= new HorizontalLayout(); 
		byte[] imageBytes  = shoes.getImage();
		StreamResource resource = new StreamResource("dummyImageName.jpg", () -> new ByteArrayInputStream(imageBytes));
		Image image = new Image(resource, "dummy image");

		content = new VerticalLayout(nomDeLaBoutique, this.city,this.adress,this.npa, this.web, image);
        
        //add(layout); 

		content.addClassName("dialog-content");
		content.setAlignItems(Alignment.STRETCH);
		add(content);
		// Button theming
		for (Button button : new Button[] { min, max, close }) {
			button.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
		}
		Button save = new Button ("Quitter");        		
        save.addClickListener(e -> 
        	close()
        );

		add(save);

	}
	
    private Component createComponent(String mimeType, String fileName, InputStream stream, Shop shop) {
    	 if (mimeType.startsWith("image")) {
             Image image = new Image();
             if (shop.getImage() != null) {
         		byte[] imageBytes  = shop.getImage();
        		StreamResource resource = new StreamResource("dummyImageName.jpg", () -> new ByteArrayInputStream(imageBytes));
        		image = new Image(resource, "dummy image");
            	 
             }
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

	private void mettreDeCote(Shoe shoes, ShoeService shoeService, File destinationFile) {
		if (shoes != null) {
			try {
				shoes.setImageFounded(ImageToByte(destinationFile));
			}catch (FileNotFoundException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
	
   	public ViewShopForShoper(Shop shoe) {
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

		Label labelImageFounded = new Label ("Image trouvée");
		Label labelImage = new Label ("Image désirée");
		HorizontalLayout hLayout= new HorizontalLayout(); 
		byte[] imageBytes  = shoe.getImage();
		StreamResource resource = new StreamResource("dummyImageName.jpg", () -> new ByteArrayInputStream(imageBytes));
		Image image = new Image(resource, "dummy image");
		content = new VerticalLayout(nomDeLaBoutique,labelImage,image);
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
