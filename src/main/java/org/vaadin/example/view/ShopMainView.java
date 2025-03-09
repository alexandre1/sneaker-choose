package org.vaadin.example.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.springframework.core.env.Environment;
import org.vaadin.example.entity.Brand;
import org.vaadin.example.entity.Person;
import org.vaadin.example.entity.Shoe;
import org.vaadin.example.entity.Shop;
import org.vaadin.example.service.GreetService;
import org.vaadin.example.service.MailWithAttachmentService;
import org.vaadin.example.sneaker.service.BrandService;
import org.vaadin.example.sneaker.service.LoginService;
import org.vaadin.example.sneaker.service.PersonService;
import org.vaadin.example.sneaker.service.ShoeService;
import org.vaadin.example.sneaker.service.ShopService;

import com.vaadin.componentfactory.Autocomplete;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import org.vaadin.example.utils.AES;
import org.vaadin.example.utils.VaadinI18NProvider;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and
 * use @Route annotation to announce it in a URL as a Spring managed
 * bean.
 * Use the @PWA annotation make the application installable on phones,
 * tablets and some desktop browsers.
 * <p>
 * A new instance of this class is created for every new user and every
 * browser tab/window.
 */
@Route("shopMainView")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@SuppressWarnings("serial")
 
public class ShopMainView extends VerticalLayout {

	    private Label error = new Label();
	  
	    private Component previousPhoto;
  
	    private Select<String> labelSelect = new Select<>();
	  
	    final Image image = new Image();

	    private Paragraph photoName;
	  
	    public Image newShoes;
	  
	    private Grid<Shop> grid = new Grid<>(Shop.class);
	  
	    private Label labelSize;
	  
	    private static final String LABEL_PRICE = "lbl_price";
	  
	    private static Set<PosixFilePermission> defaultPosixPermissions = null;
	  
	    private MemoryBuffer buffer = new MemoryBuffer();
	  
	    private static final long serialVersionUID = 4L;
	  
	    private File targetFile;
	  
	    private LoginService loginService;
	  
	    private PersonService personService;
	  
	    private Autocomplete textField;
	  
	    private TextField textFieldPrice;
	  
	    private BrandService brandService;
	  
	    private ShopService shopService;
	  
        private ShoeService shoeService;
	  		
		private static final String LABEL_MENU_NEW_SEARCH = "lbl_new_search";
		
		private static final String LABEL_MENU_SEARCH = "lbl_search";

		private static final String LABEL_MENU_ACCCOUNT = "lbl_menu_account";
	  
		private static final String LABEL_TAKE_A_PICTURE = "lbl_take_a_picture";
		
		private static final String LABEL_DOWNLOAD_A_PICTURE = "lbl_download_a_picture";

		private static final String LABEL_DOWNLOAD_A_PICTURE_FROM_URL = "lbl_take_a_picture_from_url";
		
		private static final String LABEL_WOMAN = "lbl_woman";
		
		private static final String LABEL_MAN = "lbl_man";
		
		private static final String LABEL_CHILD = "lbl_child";
		
		private static final String LABEL_SIZE = "lbl_size";
		
		private static final String LABEL_TYPE_OF_CLIENT = "lbl_type_of_client";
		
		private static final String LABEL_BRAND = "lbl_brand";
		
		private static final String LABEL_SIZE_CM = "lbl_size_cm";
		
		private static final String LABEL_SIZE_US = "lbl_size_us";
		
		private static final String LABEL_SIZE_UE = "lbl_size_ue";
		
		private static final String LABEL_SEARCH_SHOES = "lbl_search_shoes";
		
		private static final String LABEL_NBR_SHOPS = "lbl_number_of_shoes";
		
		private static final String LABEL_ERROR_REQUIRED_FIELDS = "lbl_error_fill_all_fields";
		
		private static final String LABEL_MENU_ABOUT = "lbl_menu_about";
		
		private static final String LABEL_MENU_CONSULT_ACTUAL_QUERIES = "lbl_menu_view_actual_queries";
		
		private static final String LABEL_MENU_CONSULT_REALIZED_QUERIES = "lbl_menu_view_realized_queries";
		
		private static final String LABEL_MENU_EDIT_ACCOUNT = "lbl_edit_account";
		
		private static final String LABEL_MENU_CREATE_ACCOUNT = "lbl_create_shop_account";
		
		private static final String LABEL_MENU_PRIVACY = "lbl_privacy";
		
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
	  
		
		@Autowired
		private Environment env;

		
		public String getAppPath() {
			return env.getProperty("spring.servlet.multipart.location");
		}

    /**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     * @param service The message service. Automatically injected Spring managed bean.
     */
    public ShopMainView(@Autowired GreetService service, @Autowired LoginService loginService, @Autowired PersonService personService, @Autowired BrandService brandService, @Autowired ShopService shopService, @Autowired ShoeService shoeService) {
    	if (authenticate(loginService) != null) {
    		this.personService = personService;
    		this.brandService = brandService;
    		labelSize = new Label("");
    		VaadinI18NProvider provider = new VaadinI18NProvider();
    		this.shopService = shopService;
    		this.shoeService = shoeService;
	    	this.setClassName("background-image2");
			String content = "<div><br/><br/><br/><br/><br/><br/><br/></div>"; // wrapping <div> tags are required here
		    Html html = new Html(content);			    
		    add(html);
	    	//HorizontalLayout gmap = buildGoogleMaps();	    	
	    	buildMenu();	
	        // Use TextField for standard text input
	        textField = new Autocomplete(5);
	        textField.setLabel(provider.getTranslation(LABEL_BRAND, getLocale()));
	        textField.addThemeName("bordered");
	        List<String> brandNames = new ArrayList<String>();
	        textField.addChangeListener(event -> {
	            String text = event.getValue();
	            List<Brand> result = findBrand(text);
	            List<String> resultToAdd = new ArrayList<String>();
	            for (int i = 0; i < result.size();i++) {
	            	Brand brand = result.get(i);
	            	System.out.println("BRAND IN LIST : " + brand.getName());
	            	if (brand.getName().toString().trim().startsWith(text.trim())) {
	            		if (!resultToAdd.contains(brand.getName())) {
	            			resultToAdd.add(brand.getName());
            		}
	            	}
	            
	            	
	            	
	            }
	        	textField.setOptions(resultToAdd);
	        });

	        textFieldPrice = new TextField(provider.getTranslation(LABEL_PRICE, getLocale()));
	        textFieldPrice.addThemeName("bordered");
	        
	        removeOutput();	
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
	            //imgUpload = (Image) component;
	            InputStream inputStream = buffer.getInputStream();
	            //targetFile = new File("D:\\tmp\\" + e.getFileName());
	            targetFile = new File(getAppPath() + e.getFileName());
	            try {
	                FileUtils.copyInputStreamToFile(inputStream, targetFile);
	            } catch (IOException e1) {
	                e1.printStackTrace();
	                Notification.show("Error");
	            }            
	            System.out.println("PATH : " +targetFile.getPath() );
	        });
	        Label label = new Label(provider.getTranslation(LABEL_TAKE_A_PICTURE, getLocale()));
	        HorizontalLayout layout = new HorizontalLayout();
	        layout.add(label, upload, output);//gmap);
	        Upload uploadhooseFile = new Upload(buffer);
	        uploadhooseFile.setHeight("160px");
	        uploadhooseFile.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
	        // You can use the capture html5 attribute
	        // https://caniuse.com/html-media-capture
	        //uploadhooseFile.getElement().setAttribute("capture", "environment");
	        
	        InputStream inuploadhooseFile = buffer.getInputStream();
	        
	        Div outputuploadhooseFile = new Div();
	        uploadhooseFile.addSucceededListener(e -> {
	            Component component = createComponent(e.getMIMEType(),
	                    e.getFileName(), buffer.getInputStream());
	            showOutput(e.getFileName(), component, output);
	            //imgUpload = (Image) component;
	            InputStream inputStream = buffer.getInputStream();
	            targetFile = new File(getAppPath()+ e.getFileName());
	            try {
	                FileUtils.copyInputStreamToFile(inputStream, targetFile);
	            } catch (IOException e1) {
	                e1.printStackTrace();
	                Notification.show("Error");
	            }            
	            System.out.println("PATH : " +targetFile.getPath() );
	        });
	        Label labeluploadhooseFile = new Label(provider.getTranslation(LABEL_DOWNLOAD_A_PICTURE, getLocale()));
	        HorizontalLayout layoutuploadhooseFile = new HorizontalLayout();
	
	        layout.add(labeluploadhooseFile, uploadhooseFile, outputuploadhooseFile);//gmap);
	        //layoutuploadhooseFile.add(labeluploadhooseFile);
	        
	        add(layout);
	        TextField textFieldFileUrl = new TextField(provider.getTranslation(LABEL_DOWNLOAD_A_PICTURE_FROM_URL, getLocale()));
	        textFieldFileUrl.setWidth("450px");
	        add(textFieldFileUrl);
	        add(textField);
	        
	        
	        labelSelect.setItems(provider.getTranslation(LABEL_WOMAN, getLocale()), provider.getTranslation(LABEL_MAN, getLocale()), provider.getTranslation(LABEL_CHILD, getLocale()));
	        labelSelect.setLabel(provider.getTranslation(LABEL_TYPE_OF_CLIENT, getLocale()));
	        add(labelSelect);
	        HorizontalLayout horiz = new HorizontalLayout();
	        Select<String> sizeSelectUE = new Select<>();
	        sizeSelectUE.setItems("", "33", "34", "36", "37", "38", "39", "40", "41","42","42","43","44","45");
	        sizeSelectUE.setLabel(provider.getTranslation(LABEL_SIZE_UE, getLocale()));
	        horiz.add(sizeSelectUE);
	        add(horiz);
	
	
	        Select<String> sizeSelectUs = new Select<>();
	        sizeSelectUs.setItems("","10", "11", "12", "13", "14", "15", "16");
	        sizeSelectUs.setLabel(provider.getTranslation(LABEL_SIZE_US, getLocale()));
	        horiz.add(sizeSelectUs);
	        
	        TextField sizeCm = new TextField(provider.getTranslation(LABEL_SIZE_CM, getLocale()));
	        horiz.add(sizeCm);
	        //Select<String> timeSelect = new Select<>();
	        //timeSelect.setItems("Une demi-journée", "Une journée","Me faire livrer");
	        //timeSelect.setLabel("Réponse");
	        //add(timeSelect);
	        HorizontalLayout horizontalLyout = new HorizontalLayout();
	        Button buttonPost = new Button(provider.getTranslation(LABEL_SEARCH_SHOES, getLocale()),
	                e ->  {	                	     		
	                	//buildGrid();
	                	if (validate (sizeSelectUE, sizeSelectUs,sizeCm)) { 
		                	if ((textFieldFileUrl.getValue() != null && textFieldFileUrl.getValue().length()  > 0) && textField.getValue() != null && textField.getValue().length() > 0 && labelSelect.getValue() != null && labelSelect.getValue().length() > 0)  {
		                		String imageUrl = (textFieldFileUrl.getValue());
		                		
		                	    String destinationFile = getAppPath() + "url_image"+ ".jpg";     
		                	    List<Shop> shops = this.getListOfShopForBrand(textField.getValue());
		                	    try { 
		                	    	
		                	    	Image image = new Image(textFieldFileUrl.getValue(), "DummyImage");                	    	
		                	    	showOutput(textFieldFileUrl.getValue(), image, output);
		                	    	saveImage(imageUrl, destinationFile);
		                	    	sendEmailsForShoes(shops,destinationFile,textField.getValue(), sizeSelectUE.getValue(), sizeSelectUs.getValue(), sizeCm.getValue(), getUser());
		                	    	File f = new File (destinationFile);
		                	    	saveShoes(f, textField.getValue(), sizeSelectUE.getValue(), sizeSelectUs.getValue(), sizeCm.getValue());//, //timeSelect.getValue());
		                		} catch (IOException ex) {
		                			System.out.println(ex.getMessage());
		                		}
		
		                	}
		                	else if (textField.getValue() != null && textField.getValue().length() > 0 && (sizeSelectUE.getValue() != null && sizeSelectUE.getValue().length() > 0 || sizeSelectUs.getValue() != null &&  sizeSelectUs.getValue().length() > 0  || sizeCm.getValue() != null && sizeCm.getValue().length() > 0 )) {
		                		List<Shop> shops = this.getListOfShopForBrand(textField.getValue());
		                		sendEmailsForShoes(shops,targetFile.getPath(),textField.getValue(), sizeSelectUE.getValue(), sizeSelectUs.getValue(), sizeCm.getValue(), getUser());
		                		saveShoes(targetFile, textField.getValue(), sizeSelectUE.getValue(), sizeSelectUs.getValue(), sizeCm.getValue());//, timeSelect.getValue());
		                	} 
		                	List<String> result = new ArrayList<String>();
		                	for (int i = 0; i < brandNames.size();i++) {
		                		String brand = brandNames.get(i);    			                		
		                			System.out.println("Brands : " + brand);
		                			if (!result.contains(brand )) {
		                					System.out.println("Brand to add : " + brand);	
		                					result.add(brand);	                			
		                		}
		                	}
		                	if (textField.getValue() != null && textField.getValue().length() > 0 ) {
		                	List<Shop> shops = this.getListOfShopForBrand(textField.getValue());
		                	List<String> resultShoes = new ArrayList<String>();
		                	for (int i = 0; i < shops.size();i++) {
		                		Shop shop = shops.get(i);   			                		
		                			System.out.println("shop : " + shop);
		                			if (!resultShoes.contains(shop)) {
		                					System.out.println("Shop to add : " +shop);	
		                					resultShoes.add(shop.getNom());	                			
		                		}
		                	}
	
		                	add(new Label(provider.getTranslation(LABEL_SIZE, getLocale()) + resultShoes.size()));
		                	}
		                	removeOutput();

		                }
	                else {
	                	error = new Label();
	                	error.setText(provider.getTranslation(LABEL_ERROR_REQUIRED_FIELDS, getLocale()));
	                	error.setClassName("label_error");
	                	add(error);
	                }
	                }

	        	);
	
	    
	
	        Button buttonClean = new Button("Réinitialiser",
	                e ->  {
	                	
	                	removeOutput();
	                }
	        	);
	
	        horizontalLyout.add(buttonPost, buttonClean);
	        add(horizontalLyout);
    	
    	} else {
    		redirectToLogin();		
    
    	}
    }

    private boolean validate (Select<String> sizeSelectUE, Select<String> sizeSelectUs,TextField sizeCm) {
    	boolean result = false;
    	if (sizeSelectUE.getValue() != null && sizeSelectUE.getValue().length() > 0) {
    		result = true;
    	}    		 
    	if (sizeSelectUs.getValue() != null && sizeSelectUs.getValue().length() > 0) {
    		result = true;
    	}    		 
    	if (sizeCm.getValue() != null && sizeCm.getValue().length() > 0) {
    		result = true;
    	}    		 

    	return result;
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
    	        	System.out.println("Exception while saving image : " + ex.getMessage());
    	        }    	
    	        byte[] bytes = bos.toByteArray();
    	     return bytes;
    }

    
    private void saveShoes(File destinationFile,String textField, String sizeSelectUE,String sizeSelectUs,String  sizeCm) { //, String timeSelect) {
    	
    	try {
	    	Person p = getUser();
	    	Set<Brand> brands = this.brandService.findByName(textField);
	    	Iterator<Brand> iterator = brands.iterator();
	    	Brand brand = iterator.next();
	    	brand.setName(brand.getName());
	    	List<Shop> shops = getListOfShopForBrand(textField);
	    	System.out.println("SHOPS SIZE : " + shops.size());
	    	List<Shop> shopsExisting = new ArrayList<Shop>();
	    	Iterator<Shop> shopItr = shops.iterator();
	    	while(shopItr.hasNext()) {
	    		Shop shop = shopItr.next();
	    			if (!shopsExisting.contains(shop)) {
	    				shopsExisting.add(shop);
	    				System.out.println("SHOPS TO ADD : " + shop.getEmail());
	    				System.out.println("SHOPS TO ADD ID: " + shop.getIdShop());
	    				Shoe shoes = new Shoe();
	    				shoes.setVendeur(shop);
	    				shoes.addShop(shop);
	    				System.out.println("SHOPS TO ADD ID: " + shop.getIdShop() + "SHOES ID " + shoes.getIdShoe());;
	    				brand.addShop(shop);
	    		    	shoes.setMarque(textField);	    	
	    		    	shoes.setDemandeur(p);
	    		    	shoes.setCategory(labelSelect.getValue());
	    		    	shoes.setSizeUE(sizeSelectUE);
	    		    	shoes.setSizeUS(sizeSelectUs);
	    		    	shoes.setSizeCM(sizeCm);
	    		    	shoes.setImage(ImageToByte(destinationFile));
	    				this.shoeService.save(shoes);
	    				System.out.println("SAVED SHOES ID : " + shoes.getIdShoe());
	    				this.brandService.update(brand);	    					
	    				this.shopService.update(shop.getNom(), shop.getEmail(), shop.getTelephone(), shop.getGerant(), shop.getAdresse(), shop.getIdShop(), shop.getImage()	);
	    				System.out.println("SHOES SAVED ID  : " + shoes.getIdShoe() + " SHOP TO ADD ID : " + shop.getIdShop());
	    				//this.remove(this.e);
	    			}
	    			 
	    	}}
    	catch(FileNotFoundException ex) {
    		System.out.println("Exception : " + ex.getMessage());
    	}
    	
    }
    

    private List<Brand> findBrand (String text) {
    	List<Brand> brands = new ArrayList<Brand>();
    	brands = this.brandService.findAll();
    	List<Brand> result =  new ArrayList<Brand>();
    	for (int i = 0; i < brands.size();i++) {
    		Brand brand = brands.get(i);    		
    		if (brand.getName().toString().startsWith(text.toString())) {
    			System.out.println("Brands : " + brand.getName());
    			if (!result.contains(brand )) {
    					System.out.println("Brand to add 2: " + brand.getName());
    					System.out.println("Brand to add id 2 : " + brand.getIdBrand());	

    					result.add(brand);
    			}	
    		}
    	}
    	return result;
    }

    private Person getUser() {
    	Person p = this.personService.findByEmail(VaadinSession.getCurrent().getSession().getAttribute("email").toString());
    	return p;
    }
   
    private void redirectToLogin( ) {
    	UI.getCurrent().navigate("login");
    	com.vaadin.flow.component.page.Page page = UI.getCurrent().getPage();
    	page.executeJavaScript("window.location.reload();");
    }
    
	private String authenticate (LoginService loginService) {
		final String secretKey = "aslkdfjlkj)/d89723897bc";
	    
		if (VaadinSession.getCurrent().getSession().getAttribute("password") != null) {
			String originalString = VaadinSession.getCurrent().getSession().getAttribute("password").toString();
			String encryptedString = AES.encrypt(originalString, secretKey) ;
			String decryptedString = AES.decrypt(encryptedString, secretKey) ;	     				
			String page = loginService.login(VaadinSession.getCurrent().getSession().getAttribute("email").toString(), encryptedString);	    
			return page;
		} else {
			UI.getCurrent().navigate("login");
	    	com.vaadin.flow.component.page.Page page = UI.getCurrent().getPage();
	    	page.executeJavaScript("window.location.reload();");
	    	return "";
		}
	}
	

    private boolean isAutorized ()   {
    	boolean result = false;    	
    	return result;
    }
    
    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
        
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

    private void removeOutput() {
        this.remove(grid);
        this.remove(error);
        this.remove(labelSize);
    }
    
    @SuppressWarnings("deprecation")
   	private void buildMenu() {
       	MenuBar menuBar = new MenuBar();
       	VaadinI18NProvider provider = new VaadinI18NProvider();
       	Text selected = new Text("");
       	Div message = new Div(new Text("Selected: "), selected);
       	MenuItem home = menuBar.addItem("Home",
       			e ->  {
       				selected.setText("Home");
       				UI.getCurrent().navigate("");
       			}
       	);;
       	MenuItem about = menuBar.addItem(provider.getTranslation(LABEL_MENU_ABOUT, getLocale()));
       	MenuItem search = menuBar.addItem(provider.getTranslation(LABEL_MENU_SEARCH, getLocale()));
       	search.getSubMenu().addItem(provider.getTranslation(LABEL_MENU_NEW_SEARCH, getLocale()),
       			e ->  {
       				selected.setText(provider.getTranslation(LABEL_MENU_NEW_SEARCH, getLocale()));
       				UI.getCurrent().navigate("shopMainView");
       			}
       	);
       	MenuItem account = menuBar.addItem(provider.getTranslation(LABEL_MENU_ACCCOUNT, getLocale()));
       	menuBar.addItem("Sign Out", 
       			e -> {
       				VaadinSession.getCurrent().getSession().invalidate();
       				UI.getCurrent().getPage().executeJavaScript("window.location.href=''");
       			}
       	);
       	SubMenu projectSubMenu = about.getSubMenu();
       	account.getSubMenu().addItem(provider.getTranslation(LABEL_MENU_CONSULT_ACTUAL_QUERIES, getLocale()),
       			e ->  {
       				selected.setText(provider.getTranslation(LABEL_MENU_CONSULT_ACTUAL_QUERIES, getLocale()));
       				UI.getCurrent().navigate("shoperAskShoes");
       			}
       	);
       	account.getSubMenu().addItem(provider.getTranslation(LABEL_MENU_CONSULT_REALIZED_QUERIES, getLocale()),
       			e ->  {
       				selected.setText(provider.getTranslation(LABEL_MENU_CONSULT_REALIZED_QUERIES, getLocale()));
       				UI.getCurrent().navigate("ShoperMainViewHistoric");
       			}
       	);

       	account.getSubMenu().addItem(provider.getTranslation(LABEL_MENU_EDIT_ACCOUNT, getLocale()),
       			e ->  {
       				selected.setText(provider.getTranslation(LABEL_MENU_EDIT_ACCOUNT, getLocale()));
       				UI.getCurrent().navigate("editShoper");
       			}
       	);
       	account.getSubMenu().addItem(provider.getTranslation(LABEL_MENU_CREATE_ACCOUNT, getLocale()),
       			e ->  {
       				selected.setText(provider.getTranslation(LABEL_MENU_CREATE_ACCOUNT, getLocale()));
       				UI.getCurrent().navigate("createProfile");
       			}
       	);

       	account.getSubMenu().addItem(provider.getTranslation(LABEL_MENU_PRIVACY, getLocale()),
       	        e -> {
       	        	selected.setText(provider.getTranslation(LABEL_MENU_PRIVACY, getLocale()));
       	        	UI.getCurrent().navigate( "map");
       	        }				
       			);
       	
       	add(menuBar, message);
       	
       }
    
    private List<Shop> getListOfShopForBrand(String textField) {
    	List<Shop> shops = new ArrayList<Shop>();
    	
    	shops = this.shopService.findAll();
    	List<Shop> reduced = new ArrayList<Shop>();
    	for (int i = 0; i < shops.size();i++ ) {
    		Shop shop = shops.get(i);
    		Set<Brand> brands = shop.getBrandsByShop();
    		Iterator<Brand> itr = brands.iterator();
    		while (itr.hasNext()) {
    			if (itr.next().getName().equals(textField)) {
    				if (!reduced.contains(shop)) {
    					System.out.println("Shop to add  in collection : " + shop.getNom());
    					reduced.add(shop);
    				}
    			}
    			
    		}
    				
    	}
    	return reduced;
    }
    private List<String> getListOfShopForBrand(String fileName,String textField, String sizeSelectUE, String sizeSelectUs ,String  sizeCm, Person p) {
    	List<String> listOfShop = new ArrayList<String>();
    	listOfShop.add("alexjaquet@gmail.com");
    	Shoe shoes = new Shoe();
    	
    	Person demandeur = this.personService.findByEmail(VaadinSession.getCurrent().getSession().getAttribute("email").toString());  	
    	shoes.setDemandeur(demandeur);
    	shoes.setMarque(textField);
    	//shoes.setLink(fileName);
    	if (sizeSelectUE != null && sizeSelectUE.length() > 0) {
    		shoes.setSize(sizeSelectUE);
    	}
    	if (sizeSelectUs != null && sizeSelectUs.length() > 0) {
    		shoes.setSize(sizeSelectUs);
    	}
    	if (sizeCm != null && sizeCm.length() > 0) {
    		shoes.setSize(sizeCm);
    	}

    	//listOfShop = service.getShopList(textField fileName);
    	
    	return listOfShop;
    }
    
    private void sendEmailsForShoes (List<Shop> shops, String fileName, String textField,String sizeSelectUE, String sizeSelectUs ,String  sizeCm, Person p) {
    	MailWithAttachmentService mailService = new MailWithAttachmentService();
        try {
            File file = new File(fileName);                          	
	        try {
	        	for (int i = 0; i < shops.size();i++) {
	        		mailService.sendMail(mailService.getSession(), file.getAbsolutePath().toString(), textField, sizeSelectUE, sizeSelectUs ,  sizeCm, shops.get(i).getEmail());
	        		System.out.println("SENDING TO SHOP : " + shops.get(i).getEmail());
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
    private void sendEmailForShoes ( String fileName,String textField, String sizeSelectUE, String sizeSelectUs ,String  sizeCm, Person p) {
        MailWithAttachmentService mail = new MailWithAttachmentService();      
        List<String> listEmails = getListOfShopForBrand(fileName, textField, sizeSelectUE, sizeSelectUs ,sizeCm, p);
        try {
            File file = new File(fileName);                          	
	        try {
	        	mail.sendMail(mail.getSession(), file.getAbsolutePath().toString(), textField, sizeSelectUE, sizeSelectUs ,  sizeCm, p.getEmail());
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