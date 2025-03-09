package org.vaadin.example.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.mail.MessagingException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.vaadin.example.entity.Person;
import org.vaadin.example.entity.Shop;
import org.vaadin.example.service.GreetService;
import org.vaadin.example.service.MailWithAttachmentService;

@Route("shopView")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@SuppressWarnings("serial")
@StyleSheet("context://frontend/styles/google-maps/demo-styles.css")

public class ShopView extends VerticalLayout {

		  private Component previousPhoto;
	  
		  final Image image = new Image();

		  private Paragraph photoName;
		  
		  public Image newShoes;
		  
		  private Grid<Shop> grid = new Grid<>(Shop.class);
		  
		  private Label labelSize = new Label ("Nombre de boutiques touché");
		  
		  static Set<PosixFilePermission> defaultPosixPermissions = null;
		  
		  private MemoryBuffer buffer = new MemoryBuffer();
		  
		  private static final long serialVersionUID = 4L;
		  
		  private File targetFile;
		  
		  private TextField email = new TextField("Email");
		
	 	  private PasswordField password = new PasswordField ("Not de passe");
		
		  private TextField name  = new TextField("Nom");;;
		
		  private TextField firstName = new TextField("Prénom");;
		
		  private TextField adresse = new TextField("Adresse");
		
		  private TextField city = new TextField("Ville");
		
		  private TextField npa = new TextField("Npa");
		
		  private TextField phoneNumber = new TextField("Téléphone");
		
 		  private Button save = new Button ("Sauvegarder");
 		
 		  private Button cancel = new Button ("Annuler");
 		

		  
		  static {
		      defaultPosixPermissions = new HashSet<>();
		      defaultPosixPermissions.add(PosixFilePermission.OWNER_READ);
		      defaultPosixPermissions.add(PosixFilePermission.OWNER_WRITE);
		      defaultPosixPermissions.add(PosixFilePermission.OWNER_EXECUTE);
		      defaultPosixPermissions.add(PosixFilePermission.GROUP_READ);
		      defaultPosixPermissions.add(PosixFilePermission.GROUP_WRITE);
		      //Others have read permission so that ftp user who doesn't belong to the group can fetch the file
		      defaultPosixPermissions.add(PosixFilePermission.OTHERS_READ);
		      defaultPosixPermissions.add(PosixFilePermission.OTHERS_WRITE);
		  }
	    /**
	     * Construct a new Vaadin view.
	     * <p>
	     * Build the initial UI state for the user accessing the application.
	     *
	     * @param service The message service. Automatically injected Spring managed bean.
	     */
	    public ShopView(@Autowired GreetService service) {
	    	this.setClassName("background-image2");
			String content = "<div><br/><br/><br/><br/><br/><br/><br/></div>"; // wrapping <div> tags are required here
		    Html html = new Html(content);			    
		    add(html);	    	
	    	buildMenuForShop();	
	        
	        removeOutput();	
	        
	        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
	        addClassName("v-margin-left");



	        
	        Upload upload = new Upload(buffer);
	        upload.setHeight("160px");
	        upload.setAcceptedFileTypes("image/*");
	        // You can use the capture html5 attribute
	        // https://caniuse.com/html-media-capture
	        upload.getElement().setAttribute("capture", "environment");
	        
	        InputStream in = buffer.getInputStream();
	        
	        Div output = new Div();
	        upload.addSucceededListener(e -> {
	            Component component = createComponent(e.getMIMEType(),
	                    e.getFileName(), buffer.getInputStream());
	            showOutput(e.getFileName(), component, output);
	            InputStream inputStream = buffer.getInputStream();
	            targetFile = new File("D:\\tmp\\" + e.getFileName());
	            try {
	                FileUtils.copyInputStreamToFile(inputStream, targetFile);
	            } catch (IOException e1) {
	                e1.printStackTrace();
	                Notification.show("Error");
	            }            
	            System.out.println("PATH : " +targetFile.getPath() );
	        });
	        
	        HorizontalLayout layout = new HorizontalLayout();
	        layout.add(upload);//gmap);
	        add(layout);
			add(output, email,password,adresse,city,npa, phoneNumber);
			Select<String> labelSelect = new Select<>();
			labelSelect.setItems("France", "Suisse");
			labelSelect.setLabel("Pays");
			add(labelSelect);
			save.addThemeVariants(ButtonVariant.LUMO_SMALL,
			        ButtonVariant.LUMO_PRIMARY);
			HorizontalLayout horizontalLayout = new HorizontalLayout();

			cancel.addThemeVariants(ButtonVariant.LUMO_SMALL,
			        ButtonVariant.LUMO_PRIMARY);
			horizontalLayout.add(save,cancel);
			add(horizontalLayout);

	        
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



	    private Button createRemoveButton(Grid<Shop> grid, Shop item) {
	        @SuppressWarnings("unchecked")
	        Button button = new Button("Voir", clickEvent -> {
	            ViewShoes editor = new ViewShoes ();
	            editor.open();
	            editor.setVisible(true);
	            
	        });
	        return button;
	    }
	    
	    private static String randomProfilePictureUrl(Object context) {
	    	int tmp = (1 + (int) (Math.random() * 100));
	    	if (tmp < 1) {
	    		System.out.println("Result : test 1");
	    		return "http://localhost:8084/icons/test1.png";
	    	} else {
	    		System.out.println("Result : test 2");
	    		return "http://localhost:8084/icons/test2.png";
	    	}
	    	//String result = "http://localhost:8081/icons/test"        + (1 + (int) (Math.random() * 100) + ".png");
	    	//System.out.println("Result : " + result);
	        //return result;    
	    }

	    private static String randomProfilePictureUrl1(Object context) {
	    	String result = "http://localhost:8081/icons/test1.png";
	    	System.out.println("Result : " + result);
	        return result;    }

	    private static String randomProfilePictureUrl0(Object context) {
	    	String result = "http://localhost:8081/icons/test0.png";
	    	System.out.println("Result : " + result);
	        return result;    }


	    /**
	     * Resizes an image to a absolute width and height (the image may not be
	     * proportional)
	     * @param inputImagePath Path of the original image
	     * @param outputImagePath Path to save the resized image
	     * @param scaledWidth absolute width in pixels
	     * @param scaledHeight absolute height in pixels
	     * @throws IOException
	     */
	    public static void resize(String inputImagePath,
	            String outputImagePath, int scaledWidth, int scaledHeight)
	            throws IOException {
	        // reads input image
	        File inputFile = new File(inputImagePath);
	        BufferedImage inputImage = ImageIO.read(inputFile);
	 
	        // creates output image
	        BufferedImage outputImage = new BufferedImage(scaledWidth,
	                scaledHeight, inputImage.getType());
	 
	        // scales the input image to the output image
	        Graphics2D g2d = outputImage.createGraphics();
	        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
	        g2d.dispose();
	 
	        // extracts extension of output file
	        String formatName = outputImagePath.substring(outputImagePath
	                .lastIndexOf(".") + 1);
	 
	        // writes to output file
	        ImageIO.write(outputImage, formatName, new File(outputImagePath));
	    }
	 
	    /**
	     * Resizes an image by a percentage of original size (proportional).
	     * @param inputImagePath Path of the original image
	     * @param outputImagePath Path to save the resized image
	     * @param percent a double number specifies percentage of the output image
	     * over the input image.
	     * @throws IOException
	     */
	    public static void resize(String inputImagePath,
	        String outputImagePath, double percent) throws IOException {
	        File inputFile = new File(inputImagePath);
	        BufferedImage inputImage = ImageIO.read(inputFile);
	        int scaledWidth = (int) (inputImage.getWidth() * percent);
	        int scaledHeight = (int) (inputImage.getHeight() * percent);
	        resize(inputImagePath, outputImagePath, scaledWidth, scaledHeight);
	    } 

	    private void removeOutput() {
	        this.remove(grid);
	        this.remove(labelSize);
	    }
	    
	    private void buildMenu() {
	    	MenuBar menuBar = new MenuBar();
	    	Text selected = new Text("");
	    	Div message = new Div(new Text("Selected: "), selected);
	    	MenuItem home = menuBar.addItem("Home",
	    			e ->  {
	    				selected.setText("Home");
	    				UI.getCurrent().navigate("");
	    			}
	    	);;
	    	MenuItem about = menuBar.addItem("A propos");
	    	MenuItem account = menuBar.addItem("Compte");
	    	menuBar.addItem("Sign Out", e -> selected.setText("Sign Out"));
	    	SubMenu projectSubMenu = about.getSubMenu();
	    	account.getSubMenu().addItem("Editer son profile",
	    			e ->  {
	    				selected.setText("Login");
	    				UI.getCurrent().navigate("");
	    			}
	    	);
	    	account.getSubMenu().addItem("Créer un compte",
	    			e ->  {
	    				selected.setText("Créer un compte");
	    				UI.getCurrent().navigate("createProfile");
	    			}
	    	);

	    	account.getSubMenu().addItem("Privacy Settings",
	    	        e -> {
	    	        	selected.setText("Privacy Settings");
	    	        	UI.getCurrent().navigate( "map");
	    	        }				
	    			);
	    	
	    	add(menuBar, message);
	    	
	    }
	    
	    private void buildMenuForShop () {
	    	MenuBar menuBar = new MenuBar();
	    	Text selected = new Text("");
	    	Div message = new Div(new Text("Selected: "), selected);
	    	MenuItem home = menuBar.addItem("Home",
	    			e ->  {
	    				selected.setText("Home");
	    				UI.getCurrent().navigate("");
	    			}
	    	);;
	    	MenuItem about = menuBar.addItem("A propos");
	    	MenuItem account = menuBar.addItem("Compte");
	    	menuBar.addItem("Sign Out", e -> selected.setText("Sign Out"));
	    	SubMenu projectSubMenu = about.getSubMenu();
	    	account.getSubMenu().addItem("Consulter ses opportunitées de ventes",
	    			e ->  {
	    				selected.setText("");
	    				UI.getCurrent().navigate("");
	    			}
	    	);
	    	account.getSubMenu().addItem("Editer son compte",
	    			e ->  {
	    				selected.setText("Editer son compte");
	    				UI.getCurrent().navigate("createProfile");
	    			}
	    	);

	    	account.getSubMenu().addItem("Privacy Settings",
	    	        e -> {
	    	        	selected.setText("Privacy Settings");
	    	        	UI.getCurrent().navigate( "map");
	    	        }				
	    			);
	    	
	    	add(menuBar, message);
	    	
	    	
	    }
	    private List<String> getListOfShopForBrand(String brand) {
	    	List<String> listOfShop = new ArrayList<String>();
	    	listOfShop.add("alexjaquet@gmail.com");
	    	//listOfShop = service.getShopList(brand);
	    	return listOfShop;
	    }
	    
	    private void sendEmailsForShoes (List<Shop> shops, String fileName, String textField,String sizeSelectUE, String sizeSelectUs ,String  sizeCm, Person p) {
	    	MailWithAttachmentService mailService = new MailWithAttachmentService();
	        try {
	            File file = new File(fileName);                          	
		        try {
		        	for (int i = 0; i < shops.size();i++) {
		        		mailService.sendMail(mailService.getSession(), file.getAbsolutePath().toString(), textField, sizeSelectUE, sizeSelectUs ,  sizeCm, shops.get(i).getEmail());	
		        	}	        	
		        } catch (IOException ex) {
		        	System.out.println(ex.getMessage());
		        } catch (MessagingException ex) {
		        	System.out.println(ex.getMessage());
		        }	           
	    }finally {
	    	System.out.println("Email sended succefully");
	    	} 
	    	
	    }

	    private void sendEmailForShoes (String fileName,String textField,String checkboxGroup, String sizeSelectUE, String sizeSelectUs ,String  sizeCm) {
	        MailWithAttachmentService mail = new MailWithAttachmentService();      
	    }

}

