package org.vaadin.example.view;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.entity.Shoe;
import org.vaadin.example.sneaker.service.BrandService;
import org.vaadin.example.sneaker.service.LoginService;
import org.vaadin.example.sneaker.service.PersonService;
import org.vaadin.example.sneaker.service.ShoeService;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import org.vaadin.example.utils.AES;
import org.vaadin.example.utils.VaadinI18NProvider;

@Route("ShoesShopView")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")

public class ShoesShopView extends VerticalLayout {
	 
	
	private BrandService brandService;

    private LoginService loginService;
	
	private ShoeService shoeService;
	
	private static final VaadinI18NProvider provider = new VaadinI18NProvider();

	private static final String LABEL_BRAND = "lbl_brand";
	
	private static final String LABEL_PRICE = "lbl_price";
	
	private static final String LABEL_SIZE_UE = "lbl_size_ue";
	
	private static final String LABEL_SIZE_US = "lbl_size_us";
	
	private static final String LABEL_SIZE_CM = "lbl_size_cm";
	
	private static final String LABEL_RESERVATION = "lbl_reservation";
	
	private TextField textFieldBrand = new TextField(provider.getTranslation(LABEL_BRAND, getLocale()));
	  
	private TextField textFieldPrice = new TextField(provider.getTranslation(LABEL_PRICE, getLocale()));
	  
	private TextField textFieldSizeEU = new TextField(provider.getTranslation(LABEL_SIZE_UE, getLocale()));
	
	private TextField textFieldSizeES = new TextField(provider.getTranslation(LABEL_SIZE_US, getLocale()));
	
	private TextField textFieldSizeCM = new TextField(provider.getTranslation(LABEL_SIZE_CM, getLocale()));
	
	private TextField textFieldReservation = new TextField(provider.getTranslation(LABEL_RESERVATION, getLocale()));

	private static final String LABEL_VIEW_PROFIL = "lbl_view_profile";
	
	private static final String LABEL_IMAGE_DESIRED = "lbl_image_desired";
	
	private static final String LABEL_IMAGE_FOUNDED = "lbl_image_founded";
	
	private static final String LABEL_BACK= "lbl_back";
	
	private static final String LABEL_MENU_ABOUT = "lbl_menu_about";
	
	private static final String LABEL_MENU_CONSULT_ACTUAL_QUERIES = "lbl_menu_view_actual_queries";
		
	private static final String LABEL_MENU_CONSULT_REALIZED_QUERIES = "lbl_menu_view_realized_queries";
		
	private static final String LABEL_MENU_EDIT_ACCOUNT = "lbl_edit_account";
		
	private static final String LABEL_MENU_CREATE_ACCOUNT = "lbl_create_shop_account";
		
	private static final String LABEL_MENU_PRIVACY = "lbl_privacy";

	public ShoesShopView (@Autowired LoginService loginService, @Autowired PersonService personService, @Autowired ShoeService shoeService) {
    	if (authenticate(loginService) != null && authenticate(loginService).equals("mainShopView")) {
    		this.loginService = loginService;
    		this.shoeService = shoeService;
    		this.setClassName("background-image2");
			String content = "<div><br/><br/><br/><br/><br/><br/><br/></div>"; // wrapping <div> tags are required here
		    Html html = new Html(content);		
		    HorizontalLayout layout = new HorizontalLayout();
		    add(html);
			layout.setClassName("centered-content2");
			buildMenu();
			buildMask();
	
	}else {
		UI.getCurrent().navigate("login");
    	com.vaadin.flow.component.page.Page page = UI.getCurrent().getPage();
    	page.executeJavaScript("window.location.reload();");

	}
	}
	
	private void buildMask() {
		String idShoe = VaadinSession.getCurrent().getSession().getAttribute("idShoe").toString();
		Shoe shoes = shoeService.findByIdShoe(Integer.valueOf(idShoe));
		VerticalLayout layout = new VerticalLayout ();
		this.textFieldBrand.setValue(shoes.getMarque());
		if (shoes.getPrix() != null && shoes.getPrix().length() > 0) {
			this.textFieldPrice.setValue(shoes.getPrix());
		}
		if (shoes.getReservation() != null && shoes.getReservation().length() > 0) {
			this.textFieldReservation.setValue(shoes.getReservation());
		}
		if (shoes.getSizeCM() != null && shoes.getSizeCM().length() > 0) {
			this.textFieldSizeCM.setValue(shoes.getSizeCM());
		}
		if (shoes.getSizeUE() != null && shoes.getSizeUE().length() > 0) {
			this.textFieldSizeEU.setValue(shoes.getSizeUE());
		}
		if (shoes.getSizeUS() != null && shoes.getSizeUS().length() > 0) {
			this.textFieldSizeES.setValue(shoes.getSizeUS());
		}
		
        Button buttonViewProfil = new Button(provider.getTranslation(LABEL_VIEW_PROFIL, getLocale()),
                e ->  {
        		
                	ViewShoperProfil viewProfil = new ViewShoperProfil (shoes.getDemandeur());
                	viewProfil.open();
                }
        	);

		layout.add(textFieldBrand,textFieldSizeEU,textFieldSizeES,textFieldSizeCM, textFieldPrice,textFieldReservation,buttonViewProfil);
		this.add(layout);
		Label labelImageFounded = new Label (provider.getTranslation(LABEL_IMAGE_FOUNDED, getLocale()));
		Label labelImage = new Label (provider.getTranslation(LABEL_IMAGE_DESIRED, getLocale()));
		HorizontalLayout hLayout= new HorizontalLayout(); 
		byte[] imageBytes  = shoes.getImageFounded();
		StreamResource resource = new StreamResource("dummyImageName.jpg", () -> new ByteArrayInputStream(imageBytes));
		Image image = new Image(resource, "dummy image");
		image.setWidth("30%");
		byte[] imageBytes2  = shoes.getImage();
		StreamResource resource2 = new StreamResource("dummyImageName.jpg", () -> new ByteArrayInputStream(imageBytes2));
		Image image2 = new Image(resource2, "dummy image");
		image2.setWidth("30%");
		hLayout.add(labelImageFounded, image,labelImage ,image2);		
		add(hLayout);
        Button buttonReturn = new Button(provider.getTranslation(LABEL_BACK, getLocale()),
                e ->  {        		
                	UI.getCurrent().navigate("ShopAskShoes");
                }
        	);
        add(buttonReturn);

	}
	
    private String getImageAsBase64(byte[] string) {
        String mimeType = "image/png";
        String htmlValue = null;
        if (string == null) htmlValue = TRANSPARENT_GIF_1PX; else htmlValue =
            "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(string);
        return htmlValue;
    }

    private static String TRANSPARENT_GIF_1PX =
            "data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACwAAAAAAQABAAACAkQBADs=";



	private String authenticate (LoginService loginService) {
		final String secretKey = "aslkdfjlkj)/d89723897bc";
	    
		if (VaadinSession.getCurrent().getSession().getAttribute("password") != null) {
			String originalString = VaadinSession.getCurrent().getSession().getAttribute("password").toString();
			String encryptedString = AES.encrypt(originalString, secretKey) ;
			String c = AES.decrypt(encryptedString, secretKey) ;	     				
			String page = loginService.loginNew(VaadinSession.getCurrent().getSession().getAttribute("email").toString(), originalString);	    
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
 
}
