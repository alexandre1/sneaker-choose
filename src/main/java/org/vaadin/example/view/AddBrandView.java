package org.vaadin.example.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.entity.Address;
import org.vaadin.example.entity.Brand;
import org.vaadin.example.entity.Shoe;
import org.vaadin.example.entity.Shop;
import org.vaadin.example.sneaker.service.BrandService;
import org.vaadin.example.sneaker.service.ShopService;

import com.vaadin.componentfactory.Autocomplete;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.Lumo;

public class AddBrandView extends Dialog {

	public String DOCK = "dock";
	
	public String FULLSCREEN = "fullscreen";

	private boolean isDocked = false;
	
	private boolean isFullScreen = false;

	private Header header;
	
	private Button min;
	
	private Button max;

	private VerticalLayout content;
				
	private Label marque = new Label ("Marque");;
	
	private Autocomplete brands = new Autocomplete(5);
	
	private Address address = new Address ();
	
	private Grid<Brand> grid = new Grid<>(Brand.class);
	
	private BrandService brandService;
	
	private ShopService shopService;
	
	public AddBrandView(@Autowired BrandService brandService, @Autowired ShopService shopService) {
		this.brandService = brandService;
		this.shopService = shopService;
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


		
		// Button theming
		for (Button button : new Button[] { min, max, close }) {
			button.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
		}
		Button save = new Button ("Quitter");
		add(save);

		Button resever = new Button ("Mettre de côté");
		add(resever);
		add(marque);
		brands = new Autocomplete(5);
		add(brands);
        brands.addChangeListener(event -> {
            String text = event.getValue();
            List<Brand> result = findBrand(text);
            List<String> brandNames = new ArrayList<String>();
            for (int i = 0; i < result.size();i++) {
            	if (result.get(i).getName().startsWith(text)) {
            		brandNames.add(result.get(i).getName());
            		if (result.get(i).getName().equals(text)) {
            			//phoneNumber.setValue(result.get(i).getIndicatif());
            		}
            	}
            	//country.setOptions(countryNames);
            	
            	
            }
            
        });


	}

	public AddBrandView(@Autowired BrandService brandService, Shoe shoe, @Autowired ShopService shopService) {
		setDraggable(true);
		setModal(false);
		setResizable(true);
		this.brandService = brandService;
		this.shopService = shopService;
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

		add(marque);
		brands = new Autocomplete(5);
		add(brands);
        brands.addChangeListener(event -> {
            String text = event.getValue();
            List<Brand> result = findBrand(text);
            List<String> brandNames = new ArrayList<String>();
            for (int i = 0; i < result.size();i++) {
            	Brand brand = result.get(i);
            	if (brand.getName().toString().startsWith(text)) {
            		if (!brandNames.contains(brand.getName())) {
            			brandNames.add(result.get(i).getName());
            			if (result.get(i).getName().equals(text)) {
            				//	phoneNumber.setValue(result.get(i).getIndicatif());
            		}
            	}
            	}
            	brands.setOptions(brandNames);
            	
            	
            }
            
        });

		// Button theming
		for (Button button : new Button[] { min, max, close }) {
			button.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
		}

		Button saveBrand = new Button ("Sauvegarder");
		add(saveBrand);
		Button refresh = new Button ("Rafraichir");
		add(refresh);

		buildGrid();
		String email = VaadinSession.getCurrent().getSession().getAttribute("email").toString();		
		List<Brand> existingBrandByShopUpdated = this.brandService.findAllByUsername(email);			
		List<Brand> notDuplicatedToDisplay = new ArrayList<Brand>();
		for (int i = 0; i < existingBrandByShopUpdated.size();i++) {
			Brand brand = existingBrandByShopUpdated.get(i);
			if (!notDuplicatedToDisplay.contains(brand)) {
				notDuplicatedToDisplay.add(existingBrandByShopUpdated.get(i));
			}
			
		}
		this.grid.setItems(notDuplicatedToDisplay);
		this.grid.getDataProvider().refreshAll();
		
		Set<Brand> existingBrand = this.brandService.findByName(this.brands.getValue());
		Shop shop = this.shopService.findByEmail(email);
		
		saveBrand.addClickListener(e -> {

			
			Brand brand = new Brand();
			brand.setName(this.brands.getValue());
			
			System.out.println ("Size : " + existingBrand.size());
			Iterator<Brand> itr = existingBrand.iterator();
			if (existingBrand == null || existingBrand.size() != 0) {
			while (itr.hasNext()) {
				Brand b = itr.next();
				System.out.println("Brand id : " + b.getIdBrand());
				System.out.println("Brand name : " + b.getName());
				System.out.println("Brand id2 : " + brand.getIdBrand());
				System.out.println("Brand name2 : " + brand.getName());

				if (b.getIdBrand() != brand.getIdBrand() || !(b.getName().toString().equals(brand.getName().toString()))) {
					brand.setIdBrand(b.getIdBrand());
					this.brandService.save(brand);
					shop.addBrand(brand);
					this.shopService.update(shop.getNom(), shop.getEmail(), shop.getTelephone(), shop.getGerant(), shop.getAdresse(), shop.getIdShop(), shop.getImage());
					System.out.println("NEW BRAND ID : " + brand.getIdBrand());
					
				}
			}
			}else {				
				shop.addBrand(brand);
				this.brandService.save(brand);
				shop.getBrandsByShop().add(brand);
				this.shopService.update(shop.getNom(), shop.getEmail(), shop.getTelephone(), shop.getGerant(), shop.getAdresse(), shop.getIdShop(), shop.getImage());						
			
			}
		});
		refresh.addClickListener(e-> {						
			Set<Brand> existingBrand2 = shop.getBrandsByShop();
					
			
			
			List<Brand> notDuplicatedToDisplay2 = new ArrayList<Brand>();
			if (existingBrand2 == null || existingBrand2.size() != 0) {
					Iterator<Brand> itr = existingBrand2.iterator();
					
					while (itr.hasNext()) {
						Brand b = itr.next();
						System.out.println("Brand id : " + b.getIdBrand());
						System.out.println("Brand name : " + b.getName());
						if (!notDuplicatedToDisplay2.contains(b)) {
							notDuplicatedToDisplay2.add(b);
						}
					}
				
			}
			this.grid.setItems(notDuplicatedToDisplay2);
			this.grid.getDataProvider().refreshAll();
		});
		

	}
    
	public AddBrandView(@Autowired BrandService brandService, Shop shoe,@Autowired ShopService shopService) {
		setDraggable(true);
		setModal(false);
		setResizable(true);
		this.brandService = brandService;
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

		add(marque);
		brands = new Autocomplete(5);
		add(brands);
        brands.addChangeListener(event -> {
            String text = event.getValue();
            List<Brand> result = findBrand(text);
            List<String> brandNames = new ArrayList<String>();
            for (int i = 0; i < result.size();i++) {
            	if (result.get(i).getName().startsWith(text)) {
            		brandNames.add(result.get(i).getName());
            		if (result.get(i).getName().equals(text)) {
            			//phoneNumber.setValue(result.get(i).getIndicatif());
            		}
            	}
            	brands.setOptions(brandNames);
            	
            	
            }
            
        });


		// Button theming
		for (Button button : new Button[] { min, max, close }) {
			button.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
		}
		Button save = new Button ("Sauvegarder");
		save.addClickListener(e -> {
			Brand brand = new Brand();
			brand.setName(this.brands.getValue());
			Set<Brand> existingBrand = this.brandService.findByName(this.brands.getValue());
			if (!existingBrand.contains(brand)) {
				this.brandService.save(brand);
			}
		});
		add(save);
		buildGrid();
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


    private List<Brand> findBrand (String text) {
    	List<Brand> brands = new ArrayList<Brand>();
    	brands = this.brandService.findAll();
    	List<Long> toTestList = new ArrayList<Long>();    	
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
    
	private void buildGrid () {
		
		grid.setMinWidth("1200px");    	
    	
    	grid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
        GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
    	List<Column<Brand>> columns = grid.getColumns();
    	for (int i = 0; i < columns.size();i++) {
    		if (columns.get(i).getKey().equals("shopByBrand") || columns.get(i).getKey().equals("idBrand")) {
    			columns.get(i).setVisible(false);
    		}
    	}
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setClassName("v-margin-right");
        grid.setHeight("900px");
        grid.setMinHeight("650px");
        add(grid);

	}

}
