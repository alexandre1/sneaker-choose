package org.vaadin.example.view;

import com.flowingcode.vaadin.addons.googlemaps.GoogleMap;
import com.flowingcode.vaadin.addons.googlemaps.GoogleMap.MapType;
import com.flowingcode.vaadin.addons.googlemaps.LatLon;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.Lumo;
import org.vaadin.example.entity.Address;
import org.vaadin.example.entity.Shop;

public class ViewShoes extends Dialog {

		public String DOCK = "dock";
		
		public String FULLSCREEN = "fullscreen";

		private boolean isDocked = false;
		
		private boolean isFullScreen = false;

		private Header header;
		
		private Button min;
		
		private Button max;

		private VerticalLayout content;
		
		private TextField id = new TextField ("Id");;
		
		private TextField marque = new TextField ("Marque");;
		
		private Address address = new Address ();
		 
		private TextField link = new TextField ();;
		
		private TextField adress = new TextField ("Addresse");
		
		private TextField telephone = new TextField ("Telephone");
		
		public ViewShoes() {
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


			content = new VerticalLayout(id, marque, adress,telephone);
			content.addClassName("dialog-content");
			content.setAlignItems(Alignment.STRETCH);
			add(content);
			//HorizontalLayout gmap = buildGoogleMaps();
			//add(gmap);
			// Button theming
			for (Button button : new Button[] { min, max, close }) {
				button.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
			}
			Button save = new Button ("Quitter");
			add(save);

			Button resever = new Button ("Réserver");
			add(resever);

		}

		public ViewShoes(Shop shoe) {
//			HorizontalLayout gmap = buildGoogleMaps();
//			add(gmap);
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


			content = new VerticalLayout(this.id, this.marque,this.link);
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
		
		private HorizontalLayout buildGoogleMaps () {
	        String apiKey = "AIzaSyBS2Jj2YIic9jARosQQdRsdXyAhj3jbJqw";
	        this.setSizeFull();
	    	if (apiKey==null) {
	    		add(new H2("Api key is needded to run the demo, pass it using the following system property: '-Dgoogle.maps.api=<your-api-key>'"));
	    	} else {
	        GoogleMap gmaps = new GoogleMap(apiKey,null,null);
	        gmaps.setMapType(MapType.SATELLITE);
	        gmaps.setSizeFull();
	        gmaps.setCenter(new LatLon(48.858774,2.2069771));
	        HorizontalLayout layout = new HorizontalLayout();
	        layout.setWidth("900px");
	        layout.setHeight("500px");
	        layout.add(gmaps);
	        return layout;  
	        }        	
	    return null;
	    }
}