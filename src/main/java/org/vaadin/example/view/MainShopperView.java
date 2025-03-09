package org.vaadin.example.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.utils.VaadinI18NProvider;
import org.vaadin.example.sneaker.service.LoginService;
import org.vaadin.example.sneaker.service.PersonService;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.vaadin.example.utils.AES;

@Route("mainShopperView")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@SuppressWarnings("serial")


public class MainShopperView  extends VerticalLayout {
	
	private PersonService personService;
	
	private LoginService loginService;

	private static final String LABEL_MENU_ABOUT = "lbl_menu_about";
	
	private static final String LABEL_MENU_CONSULT_ACTUAL_QUERIES = "lbl_menu_view_actual_queries";
	
	private static final String LABEL_MENU_CONSULT_REALIZED_QUERIES = "lbl_menu_view_realized_queries";
	
	private static final String LABEL_MENU_EDIT_ACCOUNT = "lbl_edit_account";
	
	private static final String LABEL_MENU_CREATE_ACCOUNT = "lbl_create_shop_account";
	
	private static final String LABEL_MENU_PRIVACY = "lbl_privacy";
	
	private static final String LABEL_MENU_NEW_SEARCH = "lbl_new_search";
	
	private static final String LABEL_MENU_SEARCH = "lbl_search";

	private static final String LABEL_MENU_ACCCOUNT = "lbl_menu_account";
	
	public MainShopperView (@Autowired LoginService loginService, @Autowired PersonService personService) {
    	if (authenticate(loginService) != null && authenticate(loginService).equals("mainShopperView")) {
    		this.personService = personService;

			this.setClassName("background-image2");
			String content = "<div><br/><br/><br/><br/><br/><br/><br/></div>"; // wrapping <div> tags are required here
		    Html html = new Html(content);		
		    HorizontalLayout layout = new HorizontalLayout();
		    add(html);
			layout.setClassName("centered-content2");
			buildMenu();
	
	}else {
		UI.getCurrent().navigate("login");
    	com.vaadin.flow.component.page.Page page = UI.getCurrent().getPage();
    	page.executeJavaScript("window.location.reload();");

	}
	}

	private String authenticate (LoginService loginService) {
		final String secretKey = "aslkdfjlkj)/d89723897bc";
	    
		if (VaadinSession.getCurrent().getSession().getAttribute("password") != null) {
			String originalString = VaadinSession.getCurrent().getSession().getAttribute("password").toString();
			String encryptedString = AES.encrypt(originalString, secretKey) ;
			String decryptedString = AES.decrypt(encryptedString, secretKey) ;	     				
			String page = loginService.login(VaadinSession.getCurrent().getSession().getAttribute("email").toString(), getPassword(VaadinSession.getCurrent().getSession().getAttribute("password").toString()));	    
			return page;
		} else {
			UI.getCurrent().navigate("login");
	    	com.vaadin.flow.component.page.Page page = UI.getCurrent().getPage();
	    	page.executeJavaScript("window.location.reload();");
	    	return "";
		}
	}
	
	private String getPassword (String password) {
		final String secretKey = "aslkdfjlkj)/d89723897bc";
	     
	    String originalString = password;
	    String encryptedString = AES.encrypt(originalString, secretKey) ;
	    String decryptedString = AES.decrypt(encryptedString, secretKey) ;	  			  
		
	    
		return decryptedString;
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

}
