package org.vaadin.example.view;

import javax.servlet.http.Cookie;

import org.vaadin.example.utils.VaadinI18NProvider;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinService;

@Route("")
@PWA(name = "ValetShopping",
        shortName = "ValetShopping",
        description = "This is a prototype2.",
        enableInstallPrompt = true)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@SuppressWarnings("serial")



public class IndexView extends VerticalLayout implements BeforeEnterObserver{ 
	
	
	private final static String LABEL_INDEX = "lbl_index";
	
	private final static String LABEL_BEEN_SHOP = "lbl_been_shop";
	
	private final static String LABEL_BEEN_SHOPER = "lbl_been_shoper";
	
	public IndexView () {
		this.setClassName("background-image2");
		String content = "<div><br/><br/><br/><br/><br/><br/><br/></div>"; // wrapping <div> tags are required here
	    Html html = new Html(content);		
	    HorizontalLayout layout = new HorizontalLayout();
	    add(html);
		layout.setClassName("centered-content2");
	    VaadinI18NProvider provider = new VaadinI18NProvider();
		Label label = new Label(provider.getTranslation(LABEL_INDEX, getLocale()));
		label.setClassName("centered-content2");
		add(label);
		Button viewShoper = new Button(provider.getTranslation(LABEL_BEEN_SHOP, getLocale()),
				e ->  {
					UI.getCurrent().navigate("mainShopView");
        }
	);

		viewShoper.setWidth("50%");
		Button viewShop = new Button(provider.getTranslation(LABEL_BEEN_SHOPER, getLocale()),
				e ->  {
					UI.getCurrent().navigate("mainShopperView");
        }
	);
		viewShop.setWidth("50%");
		layout.add(viewShop, viewShoper);
		add(layout);
		
		
	}


	
	 @Override
	 public void beforeEnter(BeforeEnterEvent event) {
		Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
		String page = "login";
		if (cookies != null) {
			 for (int i = 0; i < cookies.length; i++) {
					Cookie cookie = cookies[i];
					if (cookie.getName().equals("page_to_redirect")) {						
						UI.getCurrent().navigate(cookie.getValue());
					} 	   
			 }
			 if (cookies.length == 0) {
					UI.getCurrent().navigate(page);
			    	com.vaadin.flow.component.page.Page pageToReload = UI.getCurrent().getPage();
			    	pageToReload.executeJavaScript("window.location.reload();");
			 }
		}else {
			UI.getCurrent().navigate(page);
		}
	 }
}