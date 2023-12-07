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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.vaadin.example.sneaker.service.AdressService;
import org.vaadin.example.sneaker.service.BrandService;
import org.vaadin.example.sneaker.service.CountryService;
import org.vaadin.example.sneaker.service.LoginService;
import org.vaadin.example.sneaker.service.PersonService;
import org.vaadin.example.sneaker.service.RegisterService;
import org.vaadin.example.sneaker.service.ShopService;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.SubscriptionSchedule;
import com.stripe.model.checkout.Session;
import com.stripe.model.checkout.SessionCollection;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.vaadin.componentfactory.Autocomplete;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
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
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

@Route("editShop")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")

public class EditShopView extends VerticalLayout {

	public String key = "sk_test_51KYtlEJQxBwCBmV4jPk64zZcdNX15kZEQcfwoe8BAvbwYrWoHfYtlKWFma8ZRhU0fkWoPGL5nRSUgBQW16jtl0rd00oXLGJHrz";
	
	private static final VaadinI18NProvider provider = new VaadinI18NProvider();
	
	private final static String LABEL_SHOP_NAME = "lbl_name_shop";
	
	private final static String LABEL_EMAIL = "lbl_email";
	
	private final static String LABEL_EMAIL_ERROR = "lbl_email_error";	
	
	private final static String LABEL_SIZE_ERROR = "lbl_size_error";
	
	private final static String LABEL_COUNTRY = "lbl_country";
	
	private final static String LABEL_NAME_SHOP_REQUIRED = "lbl_nane_shop_required";
	
	private final static String LABEL_COUNTRY_REQUIRED = "lbl_country_required";
	
	private EmailField email = new EmailField(); 
	
	private final static String LABEL_PASSWORD = "lbl_password";
	
	private PasswordField password = new PasswordField();
	
	private final static String LABEL_NAME = "lbl_name";
	
	
	private ValidTextField  name; 
	
	private ValidTextField  nameShop = new ValidTextField ();;
	
	private Autocomplete  country = new Autocomplete();
	
	private final static String LABEL_FIRSTNAME_MANAGER = "lbl_firstname_manager";
	
	private final static String LABEL_NAME_MANAGER = "lbl_name_manager";
	
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
	
	private DatePicker labelDatePicker = new DatePicker ();
	
	private Autocomplete autocomplete;
	
	private static final String LABEL_PHONE_NUMBER = "lbl_phone_number";
	
	private ValidTextField  phoneNumber = new ValidTextField ();
	
	private static final String LABEL_NAME_SHOP = "lbl_name_shop";
	
	private static final String LABEL_CANCEL = "lbl_cancel";
	
	private Button cancel = new Button ();
	
	private static final String LABEL_TAKE_PICTURE = "lbl_take_picture";
	
	private static final String LABEL_ACCEPT = "lbl_accept";
	
	private static final String LABEL_SAVE = "lbl_save";
	
	private static final String LABEL_ERROR_COUNTRY_REQUIRED = "lbl_error_country_required";
	
	private static final String LABEL_ERROR_NAME_REQUIRED = "lbl_error_name_required";
	
	private static final String LABEL_ERROR_DATE_OF_BIRTH_REQUIRED = "lbl_error_birth_date_required";
	
	private static final String LABEL_ERROR_PASSWORD_REQUIRED = "lbl_error_password_required";
	
	private static final String LABEL_ERROR_FIRSTNAME_REQUIRED = "lbl_error_firstname_required";
	
	private static final String LABEL_ADRESS_REQUIRED = "lbl_error_adress_required";
	
	private static final String LABEL_CITY_REQUIRED = "lbl_error_city_required";
	
	private static final String LABEL_NPA_REQUIRED = "lbl_error_npa_required";
	
	private static final String LABEL_PNONE_REQUIRED = "lbl_error_phone_required";
	
	private static final String LABEL_MENU_ABOUT = "lbl_menu_about";
	
	private static final String LABEL_MENU_CONSULT_ACTUAL_QUERIES = "lbl_menu_view_actual_queries";
	
	private static final String LABEL_MENU_CONSULT_REALIZED_QUERIES = "lbl_menu_view_realized_queries";
	
	private static final String LABEL_MENU_EDIT_ACCOUNT = "lbl_edit_account";
	
	private static final String LABEL_MENU_CREATE_ACCOUNT = "lbl_create_shop_account";
	
	private static final String LABEL_MENU_PRIVACY = "lbl_privacy";
	
	private static final String LABEL_MENU_NEW_SEARCH = "lbl_new_search";
	
	private static final String LABEL_MENU_SEARCH = "lbl_search";

	private static final String LABEL_MENU_ACCCOUNT = "lbl_menu_account";

	private Binder<Person> binder = new Binder<>(Person.class);
	
	private Grid<Brand> grid = new Grid<>(Brand.class);
	  	
	private RegisterService registerService;
	
	private AdressService adressService;
	
	private Select<String> labelSelect;
	
	private PersonService personService;
	
	private LoginService loginService;
	
	private CountryService countryService;
	
	private ShopService shopService;
	
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
	
	private Label errorNamePerson = new Label();

	private Label errorNameShop = new Label();

	private BrandService brandService;
	
    private Component previousPhoto;
    
    private Div output = new Div();
    
    private Label label = new Label("Prendre une photo");

    private Paragraph photoName;
  
    public Image newShoes;
    
    private MemoryBuffer buffer = new MemoryBuffer();
	  
    private Upload upload;
    
	private static final long serialVersionUID = 4L;
	  
	private File targetFile;

	private Image image = new Image();
	
	@Autowired
	private Environment env;

	private static final String LABEL_STATUS = "lbl_view_status";
	
	private Person person;
	
	public String getAppPath() {
		return env.getProperty("spring.servlet.multipart.location");
	}

	
	private void savePerson() {
		if (validate()) {
			Person p = this.personService.findByEmail(VaadinSession.getCurrent().getSession().getAttribute("email").toString());
			ZoneId defaultZoneId = ZoneId.systemDefault();
			LocalDate localDate = this.labelDatePicker.getValue();
			Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
			p.setDateOfBirth(date);
			p.setEmail(this.email.getValue());
			p.setPhoneNumber(this.country.getValue());
			p.setFirstName(this.firstName.getValue());
			p.setLastName(this.name.getValue());
			p.setGroupOfUser("SHOP");
			p.setPhoneNumber(phoneNumber.getValue());
			p.setPassword(getPassword(this.email.getValue(),this.password.getValue()));
			Address adress = p.getAddress();
			Shop shop = this.shopService.findByEmailAndAdress(p.getEmail(), adress);
			shop.setNom(this.nameShop.getValue());			
			adress.setAdress(this.adresse.getValue());
			adress.setCity(this.city.getValue());
			adress.setCountry(this.country.getValue());
			adress.setNpa(this.npa.getValue());
			this.adressService.update(adress);
			p.setAddress(adress);
			this.personService.update(p);
			shop.setGerant(p);
			try {
				if (targetFile != null) {
					shop.setImage(ImageToByte(targetFile));
				}
			}catch (FileNotFoundException ex) {
				
			}
			this.shopService.update(this.nameShop.getValue().toString(), this.email.getValue().toString(),this.phoneNumber.getValue().toString(),p, p.getAddress(), shop.getIdShop(), shop.getImage());
			Notification.show("Mise à jour effectuer");
			UI.getCurrent().navigate("mainShopView");
		}
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
	private boolean validate() {
		boolean result = true;
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
		if (this.nameShop.getValue() == null || this.nameShop.getValue().length() == 0 ) {
			this.errorNameShop.setClassName("label_error");
			this.errorNameShop.setText(provider.getTranslation(LABEL_NAME_SHOP_REQUIRED, getLocale()));
			result = false;
		}else {
			this.errorPhoneNumber.setText("");
		}
		if (this.country.getValue() == null || this.country.getValue().length() == 0 ) {
			this.errorCountry.setClassName("label_error");
			this.errorCountry.setText(provider.getTranslation(LABEL_COUNTRY_REQUIRED, getLocale()));
			result = false;
		}else {
			this.errorPhoneNumber.setText("");
		}

		return result;
	}


	private void fillField() {
		System.out.println("test");
		if (this.loginService.login(VaadinSession.getCurrent().getSession().getAttribute("email").toString(), VaadinSession.getCurrent().getSession().getAttribute("password").toString()).equals("mainShopView")) {
		//
			System.out.println("FillField will occurs");
			if (VaadinSession.getCurrent().getSession().getAttribute("email") != null) {
			person = this.personService.findByEmail(VaadinSession.getCurrent().getSession().getAttribute("email").toString());
			if (person != null) {
				
				System.out.println("person : " + person.getEmail());
				this.adresse.setValue(person.getAddress().getAdress());
				Shop shop = this.shopService.findByEmailAndAdress((VaadinSession.getCurrent().getSession().getAttribute("email").toString()), person.getAddress());
				System.out.println("shop : " + shop.getEmail());
				if (person.getAddress().getAdress2() != null) {
						this.adresse2.setValue(person.getAddress().getAdress2());
				}						
				this.city.setValue(person.getAddress().getCity());
				this.firstName.setValue(person.getFirstName());
				this.name.setValue(person.getLastName());
				if (shop != null && shop.getNom() != null && shop.getNom().length() > 0) {
					this.nameShop.setValue(shop.getNom());
					this.email.setValue(shop.getEmail());
					this.password.setValue(getPasswordToSave(person.getEmail(), person.getPassword()));
					this.labelDatePicker.setValue(convertToLocalDateViaInstant(person.getDateOfBirth()));			
					phoneNumber.setValue(person.getPhoneNumber());
	
					if (person.getAddress().getNpa() != null) {
						this.npa.setValue(person.getAddress().getNpa());
						this.country.setValue(person.getAddress().getCountry());
					}
					if (shop.getImage() != null) {
						byte[] imageBytes  = shop.getImage();
						StreamResource resource = new StreamResource("dummyImageName.jpg", () -> new ByteArrayInputStream(imageBytes));
						this.image = new Image(resource, "dummy image");
			        
					}
				    VerticalLayout l = new VerticalLayout(image, label, upload, output);
		            add(l);
	
					this.country.setValue(person.getAddress().getCountry());
	//				this.phoneNumber.setValue(p.getPhoneNumber());
				}
				System.out.println("FILL FIELD TERMINATED : ");
			}
			}
		}else {
			UI.getCurrent().navigate("login");
		}
	}
	
	public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
	    return dateToConvert.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDate();
	}
	private String getPasswordToSave (String email, String password) {
		final String secretKey = "aslkdfjlkj)/d89723897bc";
	    Person user = this.personService.findByEmail(email) ;
	    String passwordFromDb = user.getPassword();
	    String originalString = passwordFromDb;
	    String encryptedString = AES.encrypt(originalString, secretKey) ;
	    String decryptedString = AES.decrypt(passwordFromDb, secretKey) ;	    			
	    
		return decryptedString;
	}
	private void initField() {
		nameShop = new ValidTextField(provider.getTranslation(LABEL_SHOP_NAME, getLocale()));
		email = new EmailField(provider.getTranslation(LABEL_EMAIL, getLocale()));
		password = new PasswordField(provider.getTranslation(LABEL_PASSWORD, getLocale()));
		name = new ValidTextField (provider.getTranslation(LABEL_NAME_MANAGER, getLocale()));
		firstName = new ValidTextField(provider.getTranslation(LABEL_FIRSTNAME_MANAGER, getLocale()));
		adresse = new ValidTextField(provider.getTranslation(LABEL_ADRESS, getLocale()));
		adresse2 = new ValidTextField (provider.getTranslation(LABEL_ADRESS2, getLocale()));
		npa = new ValidTextField (provider.getTranslation(LABEL_NPA, getLocale()));
		city = new ValidTextField (provider.getTranslation(LABEL_CITY, getLocale()));
		phoneNumber = new ValidTextField(provider.getTranslation(LABEL_PHONE_NUMBER, getLocale()));
		labelDatePicker= new DatePicker(provider.getTranslation(LABEL_BIRTHDATE, getLocale()));
		cancel.setText(provider.getTranslation(LABEL_CANCEL, getLocale()));

	}
	private boolean auth (LoginService loginService) {
		final String secretKey = "aslkdfjlkj)/d89723897bc";
	    
		if (VaadinSession.getCurrent().getSession().getAttribute("password") != null) {
			String originalString = VaadinSession.getCurrent().getSession().getAttribute("password").toString();
			String encryptedString = AES.encrypt(originalString, secretKey) ;
			String decryptedString = AES.decrypt(encryptedString, secretKey) ;	     				
			String page = loginService.login(VaadinSession.getCurrent().getSession().getAttribute("email").toString(), encryptedString);	    
			return true;
		} else {
			UI.getCurrent().navigate("login");
	    	com.vaadin.flow.component.page.Page page = UI.getCurrent().getPage();
	    	page.executeJavaScript("window.location.reload();");
	    	return false;
		}
	}

	public EditShopView (@Autowired RegisterService registerService,@Autowired AdressService adressService, @Autowired PersonService personService, @Autowired LoginService loginService,  @Autowired CountryService countryService, @Autowired ShopService shopService, @Autowired BrandService brandService) {
    	if (auth(loginService)) {
    		
    		System.out.println("EditShopView authenticateds : ");
    	
    		this.registerService = registerService;
    		this.adressService = adressService;
    		this.countryService = countryService;
    		this.loginService = loginService;
    		this.personService = personService;
    		this.shopService = shopService;
    		this.brandService = brandService;
    		this.setClassName("background-image2");
    		String content = "<div><br/><br/><br/><br/><br/><br/><br/></div>"; // wrapping <div> tags are required here
    	    Html html = new Html(content);			    
    	    add(html);		
    		buildMenu();			
    		initField();
    	
    		upload = new Upload(buffer);
            upload.setHeight("160px");
            upload.setAcceptedFileTypes("image/*");
//            // You can use the capture html5 attribute
//            // https://caniuse.com/html-media-capture
            upload.getElement().setAttribute("capture", "environment");
//            
            InputStream in = buffer.getInputStream();
//            
            output = new Div();
            label = new Label("Prendre une photo");
            upload.addSucceededListener(e -> {
                Component component = createComponent(e.getMIMEType(),
                        e.getFileName(), buffer.getInputStream());
                showOutput(e.getFileName(), component, output);
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
            
            fillField();
    	
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
    		
    		this.email.setEnabled(false);
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
   	        country.addChangeListener(event -> {
	    	            String text = event.getValue();
	    	            List<Country> result = findCountry(text);
	    	            List<String> countryNames = new ArrayList<String>();
	    	            for (int i = 0; i < result.size();i++) {
	    	            	if (result.get(i).getLibelle().startsWith(text)) {
	    	            		countryNames.add(result.get(i).getLibelle());
	    	            		if (result.get(i).getLibelle().equals(text)) {
	    	            			phoneNumber.setValue(phoneNumber.getValue());
    	            		}
	    	            	}
	    	            	country.setOptions(countryNames);
    	            }
   	        }
   	        );
    
	    	            	
//	    	            	
//	    	            }
//	    	            
//	    	        });
//    	//s	}
//	
//
    	        country.setLabel(provider.getTranslation(LABEL_COUNTRY, getLocale()));
    	        country.setWidth("300px");
    	        country.setThemeName("my-autocomplete");
    		
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
    			countryLayout.add(country, errorCountry);
    			add(countryLayout);
    			HorizontalLayout phoneNumberLayout = new HorizontalLayout();
    			phoneNumberLayout.add(phoneNumber, errorPhoneNumber);
    			add(phoneNumberLayout);



    		//add(email,password,name, firstName,adresse,city,npa, phoneNumber);
    		
    		Button save = new Button ("Sauvegarder",
                    e ->  {        		
                    	savePerson();
                    }
            	);
    		save.addThemeVariants(ButtonVariant.LUMO_SMALL,
    		        ButtonVariant.LUMO_PRIMARY);
    		HorizontalLayout horizontalLayout = new HorizontalLayout();
    		Button addBrand = new Button("Ajouter une marque");
    		addBrand.addThemeVariants(ButtonVariant.LUMO_SMALL,
    		        ButtonVariant.LUMO_PRIMARY);
    		addBrand.addClickListener(e -> {
    				createAddBrand(grid);
    		}
    		);
	
    		cancel.addThemeVariants(ButtonVariant.LUMO_SMALL,
    		        ButtonVariant.LUMO_PRIMARY);
    		PaymentStatus statusView = new PaymentStatus (personService, person) ;
    		Button status = new Button(provider.getTranslation(LABEL_STATUS, getLocale()),
    				e -> {
    					statusView.open();
    					statusView.setVisible(true);
    				}
    			);
    		status.addThemeVariants(ButtonVariant.LUMO_SMALL,
    		        ButtonVariant.LUMO_PRIMARY);    		
    		horizontalLayout.add(save,cancel, addBrand, status);
    		add(horizontalLayout);    				
    	
		}
		else {
			redirectToLogin();
		}
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


	private void buildGrid () {
		List<Brand> items = new ArrayList<Brand>();
		
		grid.setItems(items);
		grid.setMinWidth("1200px");    	
    	
    	grid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
    	        GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        //grid.addComponentColumn(item -> createRemoveButton(grid, item)).setHeader("Actions");
        
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setClassName("v-margin-right");
        grid.setHeight("900px");
        grid.setMinHeight("650px");
        add(grid);

	}
    private void redirectToLogin( ) {
    	UI.getCurrent().navigate("login");
    }
    
	private String authenticate (LoginService loginService) {
		final String secretKey = "aslkdfjlkj)/d89723897bc";
	    
		if (VaadinSession.getCurrent().getSession().getAttribute("passsword") != null) {
			String originalString = VaadinSession.getCurrent().getSession().getAttribute("passsword").toString();
			System.out.println("PASSWORD AUTHENTH" + originalString);
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
    	
    	MenuItem account = menuBar.addItem("Compte");
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
    				UI.getCurrent().navigate("ShopAskShoes");
    			}
    	);
    	account.getSubMenu().addItem(provider.getTranslation(LABEL_MENU_CONSULT_REALIZED_QUERIES, getLocale()),
    			e ->  {
    				selected.setText(provider.getTranslation(LABEL_MENU_CONSULT_REALIZED_QUERIES, getLocale()));
    				UI.getCurrent().navigate("shoperHistoricMainView");
    			}
    	);

    	account.getSubMenu().addItem(provider.getTranslation(LABEL_MENU_EDIT_ACCOUNT, getLocale()),
    			e ->  {
    				selected.setText(provider.getTranslation(LABEL_MENU_EDIT_ACCOUNT, getLocale()));
    				UI.getCurrent().navigate("editShop");
    			}
    	);
    	account.getSubMenu().addItem(provider.getTranslation(LABEL_MENU_CREATE_ACCOUNT, getLocale()),
    			e ->  {
    				selected.setText(provider.getTranslation(LABEL_MENU_CREATE_ACCOUNT, getLocale()));
    				UI.getCurrent().navigate("createProfileShop");
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
 
    private void createAddBrand(Grid<Brand> grid) {
        @SuppressWarnings("unchecked")
    	Shoe shoes = new Shoe();
       	AddBrandView editor = new AddBrandView (brandService, shoes, shopService);
        editor.open();
        editor.setVisible(true);
    }

	public Customer createCustomer() {
		Stripe.apiKey = key;
		Person person = this.personService.findByEmail(VaadinSession.getCurrent().getSession().getAttribute("email").toString());
		System.out.println("API KEY : " + Stripe.apiKey);
		Customer customer = null;
		CustomerCreateParams params =
				  CustomerCreateParams
				    .builder()
				    .setEmail(person.getEmail())
				    .setPaymentMethod("pm_card_visa")
				    .setInvoiceSettings(
				      CustomerCreateParams.InvoiceSettings
				        .builder()
				        .setDefaultPaymentMethod("pm_card_visa")
				        .build()
				    ).build();
				try {
					customer = Customer.create(params);
					person.setStripeId(customer.getId());
					Session session =createStripSession();
					person.setSessionId(session.getId());
					//this.personService.update(customer.getId(), person);
					subscribe(customer, session);					
					
				}catch (StripeException ex) {
					System.out.println(ex.getMessage());
				}
				return customer;	
	}
	
	public Session createStripSession() {
		Stripe.apiKey = key;
		
		System.out.println("API KEY : " + Stripe.apiKey);
		//String priceId = "price_1KYX8OCLVCGN5jA7JNtDRaAx";
		
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
				.setSuccessUrl("https://example.com/success.html?session_id=" + last.getId())
				.setCancelUrl("https://example.com/canceled.html")
				.setMode(SessionCreateParams.Mode.SUBSCRIPTION)
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

	public void subscribe(Customer customer, Session session) throws StripeException {
		Stripe.apiKey = key;
		
		System.out.println("API KEY : " + Stripe.apiKey);
		List<Object> phases = new ArrayList<>();
		List<Object> items = new ArrayList<>();
		Map<String, Object> item1 = new HashMap<>();
		item1.put(
		  "price",
		  "price_1KYtmOJQxBwCBmV4UQ1aTJRU"
		);
		item1.put("quantity", 1);
		items.add(item1);
		Map<String, Object> phase1 = new HashMap<>();
		phase1.put("items", items);
		phase1.put("iterations", 12);
	//   String priceId = request.queryParams("priceId");
		//String priceId = "price_1KYX8OCLVCGN5jA7JNtDRaAx";

		SessionCreateParams params = new SessionCreateParams.Builder()
		  .setSuccessUrl("http://localhost:8080/success?session_id=" + session.getId())
		  .setCancelUrl("https://example.com/canceled.html")
		  .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
		  .addLineItem(new SessionCreateParams.LineItem.Builder()
		    // For metered billing, do not pass quantity
		    .setQuantity(1L)
		    .setPrice("price_1KYtmOJQxBwCBmV4UQ1aTJRU") 
		    .build()
		  )
		  .build();

		session = Session.create(params);
		phases.add(phase1);
		Map<String, Object> params2 = new HashMap<>();
		params2.put("customer", customer.getId());
		params2.put("start_date", 1646485214);
		params2.put("end_behavior", "release");
		params2.put("phases", phases);
		SubscriptionSchedule subscriptionSchedule = SubscriptionSchedule.create(params2);
		getUI().get().getPage().executeJavaScript("window.location.href = " + "'" + session.getUrl() + "'");

	}

}
