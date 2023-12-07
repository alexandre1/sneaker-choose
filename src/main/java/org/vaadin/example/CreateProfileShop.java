package org.vaadin.example;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.mail.MessagingException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.vaadin.example.sneaker.service.AdressService;
import org.vaadin.example.sneaker.service.CountryService;
import org.vaadin.example.sneaker.service.PersonService;
import org.vaadin.example.sneaker.service.RegisterService;
import org.vaadin.example.sneaker.service.ShopService;

import com.vaadin.componentfactory.Autocomplete;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

@Route("createProfileShop")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class CreateProfileShop  extends VerticalLayout{

	private static final VaadinI18NProvider provider = new VaadinI18NProvider();
	
	private final static String LABEL_EMAIL = "lbl_email";
	
	private final static String LABEL_EMAIL_ERROR = "lbl_email_error";	
	
	private final static String LABEL_SIZE_ERROR = "lbl_size_error";
	
	private EmailField email; 
	
	private final static String LABEL_PASSWORD = "lbl_password";
	
	private PasswordField password;
	
	private final static String LABEL_NAME = "lbl_name";
	
	
	private ValidTextField  name;
	
	private ValidTextField  nameShop;
	
	private final static String LABEL_FIRSTNAME = "lbl_firstname";
	
	private ValidTextField  firstName = new ValidTextField ();
	
	private final static String LABEL_ADRESS = "lbl_adress";
	
	private ValidTextField  adresse = new ValidTextField ();
	
	private final static String LABEL_ADRESS2 = "lbl_adress2";
	
	private ValidTextField  adresse2 = new ValidTextField ();
	
	private final static String LABEL_CITY = "lbl_city";
	
	private ValidTextField  city = new ValidTextField ();
	
	private final static String LABEL_NPA = "lbl_npa";
	
	private ValidTextField  npa = new ValidTextField ();
	
	private static final String LABEL_BIRTHDATE = "lbl_birthdate";
	
	private DatePicker labelDatePicker;
	
	private Autocomplete autocomplete;
	
	private static final String LABEL_PHONE_NUMBER = "lbl_phone_number";
	
	private ValidTextField  phoneNumber = new ValidTextField ();
	
	private static final String LABEL_NAME_SHOP = "lbl_name_shop";
	
	private static final String LABEL_CANCEL = "lbl_cancel";
	
	private Button cancel = new Button ();
	private static final String LABEL_TAKE_PICTURE = "lbl_take_picture";
	
	private static final String LABEL_ACCEPT = "lbl_accept";
	
	private static final String LABEL_SAVE = "lbl_save";

	private RegisterService registerService;
	
	private AdressService adressService;
	
	private Select<String> labelSelect;
	
	private CountryService countryService;
	
	private Label errorEmail = new Label();
	
	private Label errorName = new Label();
	
	private Label errorFirstName = new Label();
	
	private Label errorAddress = new Label();
	
	private Label errorCity = new Label();
	
	private Label errorPhoneNumber = new Label();

	private Label errorNpa = new Label();
	
	private Label errorPassword = new Label();
	
	private Label errorDate = new Label();
	
	private Label errorCountry = new Label();
	
	private Label errorNameShop = new Label();
	
	private PersonService personService;

	private ShopService shopService;
	
    private Component previousPhoto;
    
    final Image image = new Image();

    private Paragraph photoName;
  
    public Image newShoes;
    
    private MemoryBuffer buffer = new MemoryBuffer();
	  
	private static final long serialVersionUID = 4L;
	  
	private File targetFile;
	
	private static final String LABEL_ERROR_EMAIL_ALREADY_EXIST = "lbl_error_email_already_exist";
	
	private static final String LABEL_COUNTRY = "lbl_country";
	
	private static final String LABEL_ERROR_COUNTRY_REQUIRED = "lbl_error_country_required";
	
	private static final String LABEL_ERROR_NAME_REQUIRED = "lbl_error_name_required";
	
	private static final String LABEL_ERROR_DATE_OF_BIRTH_REQUIRED = "lbl_error_birth_date_required";
	
	private static final String LABEL_ERROR_PASSWORD_REQUIRED = "lbl_error_password_required";
	
	private static final String LABEL_ERROR_FIRSTNAME_REQUIRED = "lbl_error_firstname_required";
	
	private static final String LABEL_ADRESS_REQUIRED = "lbl_error_adress_required";
	
	private static final String LABEL_CITY_REQUIRED = "lbl_error_city_required";
	
	private static final String LABEL_NPA_REQUIRED = "lbl_error_npa_required";
	
	private static final String LABEL_PNONE_REQUIRED = "lbl_error_phone_required";
	
	private static final String LABEL_MENU_ABOUT = "lbl_about";
	
	private static final String LABEL_MENU_ACCOUNT = "lbl_account";
	
	private static final String LABEL_MENU_CREATE_ACCOUNT = "lbl_create_account";
	
	private static final String LABEL_MENU_EDIT_ACCOUNT = "lbl_edit_account";
	
	private static final String LABEL_MENU_PRIVACY = "lbl_privacy";

	private Shop shop;
	
	@Autowired
	private Environment env;

	public String getAppPath() {
		return env.getProperty("spring.servlet.multipart.location");
	}

	
	public CreateProfileShop (@Autowired RegisterService registerService,@Autowired AdressService adressService,@Autowired CountryService countryService,@Autowired PersonService personService, @Autowired ShopService shopService) {
		this.registerService = registerService;
		this.adressService = adressService;
		this.countryService = countryService;
		this.personService = personService;
		this.shopService = shopService;
		this.setClassName("background-image2");
		email = new EmailField(provider.getTranslation(LABEL_EMAIL, getLocale()));
		password = new PasswordField(provider.getTranslation(LABEL_PASSWORD, getLocale()));
		name = new ValidTextField (provider.getTranslation(LABEL_NAME, getLocale()));
		firstName = new ValidTextField(provider.getTranslation(LABEL_FIRSTNAME, getLocale()));
		adresse = new ValidTextField(provider.getTranslation(LABEL_ADRESS, getLocale()));
		adresse2 = new ValidTextField (provider.getTranslation(LABEL_ADRESS2, getLocale()));
		npa = new ValidTextField (provider.getTranslation(LABEL_NPA, getLocale()));
		city = new ValidTextField (provider.getTranslation(LABEL_CITY, getLocale()));
		phoneNumber = new ValidTextField(provider.getTranslation(LABEL_PHONE_NUMBER, getLocale()));
		labelDatePicker= new DatePicker(provider.getTranslation(LABEL_BIRTHDATE, getLocale()));
		cancel.setText(provider.getTranslation(LABEL_CANCEL, getLocale()));
		nameShop = new ValidTextField(provider.getTranslation(LABEL_NAME_SHOP, getLocale()));
		String content = "<div><br/><br/><br/><br/><br/><br/><br/></div>"; // wrapping <div> tags are required here
	    Html html = new Html(content);			    
	    add(html);		
		buildMenu();			
		Upload upload = new Upload(buffer);
        upload.setHeight("160px");
        upload.setAcceptedFileTypes("image/*");
        // You can use the capture html5 attribute
        // https://caniuse.com/html-media-capture
        upload.getElement().setAttribute("capture", "environment");
        
        InputStream in = buffer.getInputStream();
        
        Div output = new Div();
        Label label = new Label(provider.getTranslation(LABEL_TAKE_PICTURE, getLocale()));
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
        
        VerticalLayout l = new VerticalLayout(label, upload, output);
        add(l);

		email.setClearButtonVisible(true);		
		email.setErrorMessage(provider.getTranslation(LABEL_EMAIL_ERROR, getLocale()));		
		name.addValidator(new StringLengthValidator(provider.getTranslation(LABEL_SIZE_ERROR, getLocale()), 2, 100));		
		firstName.addValidator(new StringLengthValidator(provider.getTranslation(LABEL_SIZE_ERROR, getLocale()), 2, 400));		
		adresse.addValidator(new StringLengthValidator(provider.getTranslation(LABEL_SIZE_ERROR, getLocale()), 2, 400));
		
		city.addValidator(new StringLengthValidator(provider.getTranslation(LABEL_SIZE_ERROR, getLocale()), 2, 500));
		
		npa.addValidator(new StringLengthValidator(provider.getTranslation(LABEL_SIZE_ERROR, getLocale()), 2, 10));
		
		phoneNumber.addValidator(new StringLengthValidator(provider.getTranslation(LABEL_SIZE_ERROR, getLocale()), 2,20));
	
		HorizontalLayout shopNameLayout = new HorizontalLayout();
		shopNameLayout.add(nameShop, errorNameShop);
		add(shopNameLayout);
		
		HorizontalLayout nameLayout = new HorizontalLayout();
		
		
		nameLayout.add(name,errorName, firstName, errorFirstName);
		add(nameLayout);
		HorizontalLayout emailLayout = new HorizontalLayout();
		emailLayout.add(email, errorEmail);
		add(emailLayout);
		HorizontalLayout passwordLayout = new HorizontalLayout();
		passwordLayout.add(password, errorPassword);
		add(passwordLayout);
		HorizontalLayout dateLayout = new HorizontalLayout();
		dateLayout.add(labelDatePicker, errorDate);
		add(dateLayout);
		VerticalLayout adressLayout = new VerticalLayout();
		autocomplete = new Autocomplete(5);

	        autocomplete.addChangeListener(event -> {
	            String text = event.getValue();
	            List<Country> result = findCountry(text);
	            List<String> countryNames = new ArrayList<String>();
	            for (int i = 0; i < result.size();i++) {
	            	if (result.get(i).getLibelle().startsWith(text)) {
	            		countryNames.add(result.get(i).getLibelle());
	            		if (result.get(i).getLibelle().equals(text)) {
	            			phoneNumber.setValue(result.get(i).getIndicatif());
	            		}
	            	}
	            	autocomplete.setOptions(countryNames);
	            	
	            	
	            }
	            
	        });

	        autocomplete.addValueChangeListener(event -> {
	            
	        });

	        autocomplete.addValueClearListener(event -> {
	            
	        });

	        autocomplete.setLabel("Pays");
	        autocomplete.setWidth("300px");
	        autocomplete.setThemeName("my-autocomplete");
		
			HorizontalLayout adresseLayout = new HorizontalLayout();
			adresseLayout.add(adresse, errorAddress);
			add(adresseLayout);
			add(adresse2);
			HorizontalLayout cityLayout = new HorizontalLayout();
			cityLayout.add(city, errorCity);
			add(cityLayout);
			HorizontalLayout npaLayout = new HorizontalLayout();
			npaLayout.add(npa, errorNpa);
			add(npaLayout);
			HorizontalLayout countryLayout = new HorizontalLayout();
			countryLayout.add(autocomplete, errorCountry);
			add(countryLayout);
			HorizontalLayout phoneNumberLayout = new HorizontalLayout();
			phoneNumberLayout.add(phoneNumber, errorPhoneNumber);
			add(phoneNumberLayout);
			Checkbox accepted = new Checkbox(provider.getTranslation(LABEL_ACCEPT, getLocale()));
			add(accepted);
			
		
		    Button save = new Button (provider.getTranslation(LABEL_SAVE, getLocale()),
                e ->  {   
                	if (accepted.getValue() == true)	{
                		Person p = savePerson();
                		PaymentView view = new PaymentView(p, this.personService, shop, env);
                		view.open();
            			view.setVisible(true);            			
                		
                	}
                }
        	);
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

	private boolean validate(String email) {
		boolean result = true;
		Person existingPerson = this.personService.findByEmail(email);
		if (existingPerson != null ) {
			this.errorEmail.setClassName("label_error");
			this.errorEmail.setText(provider.getTranslation(LABEL_ERROR_EMAIL_ALREADY_EXIST, getLocale()));
			result = false;
		}
		if (this.labelDatePicker.getValue() == null ) {
			this.errorDate.setClassName("label_error");
			this.errorDate.setText(provider.getTranslation(LABEL_ERROR_DATE_OF_BIRTH_REQUIRED, getLocale()));
			result = false;
		} else {
			this.errorDate.setText("");
		}
		if (this.password.getValue() == null || this.password.getValue().length() == 0 ) {
			this.errorPassword.setClassName("label_error");
			this.errorPassword.setText(provider.getTranslation(LABEL_ERROR_PASSWORD_REQUIRED, getLocale()));
			result = false;
		} else {
			this.errorPassword.setText("");
		}
		if (this.name.getValue() == null || this.name.getValue().length() == 0 ) {
			this.errorName.setClassName("label_error");
			this.errorName.setText(provider.getTranslation(LABEL_ERROR_NAME_REQUIRED, getLocale()));
			result = false;
		} else {
			this.errorName.setText("");
		}
		if (this.firstName.getValue() == null || this.firstName.getValue().length() == 0 ) {
			this.errorFirstName.setClassName("label_error");
			this.errorFirstName.setText(provider.getTranslation(LABEL_ERROR_FIRSTNAME_REQUIRED, getLocale()));
			result = false;
		}else {
			this.errorFirstName.setText("");
		}
		if (this.email.getValue() == null || this.email.getValue().length() == 0 ) {
			this.errorEmail.setClassName("label_error");
			this.errorEmail.setText(provider.getTranslation(LABEL_ERROR_NAME_REQUIRED, getLocale()));
			result = false;
		}else {
			this.errorFirstName.setText("");
		}
		if (this.adresse.getValue() == null || this.adresse.getValue().length() == 0 ) {
			this.errorAddress.setClassName("label_error");
			this.errorAddress.setText(provider.getTranslation(LABEL_ADRESS_REQUIRED, getLocale()));
			result = false;
		}else {
			this.errorAddress.setText("");
		}
		if (this.city.getValue() == null || this.city.getValue().length() == 0 ) {
			this.errorCity.setClassName("label_error");
			this.errorCity.setText(provider.getTranslation(LABEL_CITY_REQUIRED, getLocale()));
			result = false;
		}else {
			this.errorCity.setText("");
		}
		if (this.npa.getValue() == null || this.npa.getValue().length() == 0 ) {
			this.errorNpa.setClassName("label_error");
			this.errorNpa.setText(provider.getTranslation(LABEL_NPA_REQUIRED, getLocale()));
			result = false;
		}else {
			this.errorNpa.setText("");
		}
		if (this.phoneNumber.getValue() == null || this.phoneNumber.getValue().length() == 0 ) {
			this.errorPhoneNumber.setClassName("label_error");
			this.errorPhoneNumber.setText(provider.getTranslation(LABEL_PNONE_REQUIRED, getLocale()));
			result = false;
		}else {
			this.errorPhoneNumber.setText("");
		}
		return result;
		
	}
	
	private Person savePerson() {
		Person p = new Person ();
		if (validate(this.email.getValue())) {
			Shop shop = new Shop ();
			shop.setNom(this.name.getValue());
			
			ZoneId defaultZoneId = ZoneId.systemDefault();
			LocalDate localDate = this.labelDatePicker.getValue();
			Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
			p.setDateOfBirth(date);
			
			p.setEmail(this.email.getValue());
			p.setFirstName(this.firstName.getValue());
			p.setLastName(this.name.getValue());
			p.setGroupOfUser("SHOP");
			p.setPhoneNumber(this.phoneNumber.getValue());
			p.setPassword(getPassword(this.email.getValue(),this.password.getValue()));
			Address adress = new Address() ;//this.adressService.findById(p.getAddress().getId());
			adress.setAdress(this.adresse.getValue());
			adress.setCity(this.city.getValue());
			adress.setCountry(this.autocomplete.getValue());
			adress.setNpa(this.npa.getValue());
			this.adressService.save(adress);
			p.setAddress(adress);
			shop.setAdresse(adress);
			this.registerService.save(p);
			shop.setGerant(p);
			shop.setEmail(this.email.getValue().trim());
			try {
				shop.setImage(ImageToByte(targetFile));
			}catch (FileNotFoundException ex) {
				
			}
			
			this.shopService.save(shop);
			this.shop = shop;
			//sendEmailForShoes(p);
			
			//UI.getCurrent().navigate("shopMainView");
		}
		return p;
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
	
	public String getConfirmUrl() {
		return env.getProperty("sneaker.confirmUrl");
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
		return encryptedString;
	}

    @SuppressWarnings("deprecation")
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
    	MenuItem about = menuBar.addItem(provider.getTranslation(LABEL_MENU_ABOUT, getLocale()));
    	MenuItem account = menuBar.addItem(provider.getTranslation(LABEL_MENU_ACCOUNT, getLocale()));
    	menuBar.addItem("Sign Out", e -> selected.setText("Sign Out"));
    	SubMenu projectSubMenu = about.getSubMenu();
    	account.getSubMenu().addItem(provider.getTranslation(LABEL_MENU_EDIT_ACCOUNT, getLocale()),
    			e ->  {
    				selected.setText(provider.getTranslation(LABEL_MENU_EDIT_ACCOUNT, getLocale()));
    				UI.getCurrent().navigate("editShop");
    			}
    	);
    	account.getSubMenu().addItem(provider.getTranslation(LABEL_MENU_CREATE_ACCOUNT, getLocale()),
    			e ->  {
    				selected.setText(provider.getTranslation(LABEL_MENU_CREATE_ACCOUNT, getLocale()));
    				UI.getCurrent().navigate("createProfile");
    			}
    	);

    	account.getSubMenu().addItem(provider.getTranslation(LABEL_MENU_PRIVACY, getLocale()),
    	        e -> selected.setText(provider.getTranslation(LABEL_MENU_PRIVACY, getLocale())));
    	add(menuBar, message);
    	
    }

    private List<Country> findCountry (String text) {
    	List<Country> countries = new ArrayList<Country>();
    	countries = this.countryService.findAll();
    	List<Country> result = new ArrayList<Country>();
    	for (int i = 0; i < countries.size();i++) {
    		if (countries.get(i).getLibelle().startsWith(text)) {
    			result.add(countries.get(i));
    		}
    	}
    	return result;
    }
    
    public String getIndicatif() {
    	return "";
    }

}

