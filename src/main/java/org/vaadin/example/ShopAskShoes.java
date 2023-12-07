package org.vaadin.example;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.vaadin.example.sneaker.service.PersonService;
import org.vaadin.example.sneaker.service.ShoeService;
import org.vaadin.example.sneaker.service.ShopService;

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
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

@Route("ShopAskShoes")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@SuppressWarnings("serial")


public class ShopAskShoes extends VerticalLayout {

	
		  private Component previousPhoto;
	  
		  final Image image = new Image();

		  private Paragraph photoName;
		  
		  public Image newShoes;
		  
		  private Grid<Shoe> grid = new Grid<>(Shoe.class);
		  
		  private ShoeService shoeService;
		  
		  private static final String LABEL_SIZE = "lbl_size";
		  
		  private static final String LABEL_SIZE_SHOES_ASKED = "lbl_number_of_shoes";
		  
		  private Label labelSize;
		  
		  static Set<PosixFilePermission> defaultPosixPermissions = null;
		  
		  private MemoryBuffer buffer = new MemoryBuffer();
		  
		  private static final long serialVersionUID = 4L;
		  
		  private File targetFile;
		  
		  private ShopService shopService;
		  
		  private PersonService personService;

		  private static final String LABEL_MENU_ABOUT = "lbl_menu_about";
		  
		  private static final String LABEL_MENU_ACCOUNT = "lbl_menu_account";
		  
		  private static final String LABEL_MENU_CONSULT_ACTUAL_QUERIES = "lbl_menu_view_actual_queries";
			  
		  private static final String LABEL_MENU_CONSULT_REALIZED_QUERIES = "lbl_menu_view_realized_queries";
		  
		  private static final String LABEL_MENU_CREATE_ACCOUNT = "lbl_create_shop_account";
		  
		  private static final String LABEL_MENU_EDIT_ACCOUNT = "lbl_edit_account";
		  
		  private static final String LABEL_MENU_PRIVACY = "lbl_privacy";
		  
		  @Autowired
		  private Environment env;

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
	    public ShopAskShoes(@Autowired GreetService service, @Autowired ShoeService shoeService, @Autowired ShopService shopService, @Autowired PersonService personService) {
			//HorizontalLayout gmap = buildGoogleMaps();
	    	this.setClassName("background-image2");
	    	this.shoeService = shoeService;
	    	this.shopService = shopService;
	    	this.personService = personService;
			String content = "<div><br/><br/><br/><br/><br/><br/><br/></div>"; // wrapping <div> tags are required here
		    Html html = new Html(content);			    
		    add(html);	    	
	    	buildMenu();	
	        addClassName("v-margin-left");
	        HorizontalLayout horizontalLyout = new HorizontalLayout();
	    	buildGrid();
	        add(horizontalLyout);
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
	    private static String TRANSPARENT_GIF_1PX =
	            "data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACwAAAAAAQABAAACAkQBADs=";

	    private String getImageAsBase64(byte[] string) {
	            String mimeType = "image/png";
	            String htmlValue = null;
	            if (string == null) htmlValue = TRANSPARENT_GIF_1PX; else htmlValue =
	                "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(string);
	            return htmlValue;
	        }

	    private void buildGrid () {
	    	VaadinI18NProvider provider = new VaadinI18NProvider();
	    	
	    	String email = VaadinSession.getCurrent().getSession().getAttribute("email").toString();
	    	Shop shop = this.shopService.findByEmail(email);
	    	Set<Shoe> shoesList = shoeService.findAllByShop(shop.getIdShop());
	    	System.out.println("ID SHOP  " + shop.getIdShop());
	    	List<Shoe> result = new ArrayList<Shoe>();
	    	Iterator<Shoe> namesIterator = shoesList.iterator();
	    	while (namesIterator.hasNext()) {
	    		Shoe shoe = namesIterator.next();
	    		if (shoe.getState() == null || (shoe.getState() != null && !(shoe.getState().equals("Vendue")))) {
	      			result.add(shoe);
	    		}
	    	}
	    	labelSize = new Label(provider.getTranslation(LABEL_SIZE_SHOES_ASKED, getLocale()) + result.size());
	    	add(labelSize);
	    	grid.setMinWidth("1200px");    	
	    	grid.setHeight("2000px");
	    	
	    	grid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
	    	        GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
	    	grid.setItems(result);
	    	List<Column<Shoe>> columns = grid.getColumns();
	    	for (int i = 0; i < columns.size();i++) {
	    		if (    columns.get(i).getKey().equals("idShoe")  || columns.get(i).getKey().equals("idShoe") || 
	    				columns.get(i).getKey().equals("prix") ||// columns.get(i).getKey().equals("shoes_by_shops_associations") ||
	    				columns.get(i).getKey().equals("shoesByShopAsked") || 
	    				columns.get(i).getKey().equals("reservation") ||
	    				columns.get(i).getKey().equals("size") ||
	    				columns.get(i).getKey().equals("image") ||
	    				columns.get(i).getKey().equals("vendeur") ||
	    				columns.get(i).getKey().equals("imageFounded") ||
	    				columns.get(i).getKey().equals("demandeur")
	    				) {
	    			columns.get(i).setVisible(false);
	    		}
	    		System.out.println("KEY : " + columns.get(i).getKey());
	    		if (columns.get(i).getKey().equals("marque")) {
	    			columns.get(i).setHeader(provider.getTranslation("lbl_brand", getLocale()));	    			
	    		}
	    		if (columns.get(i).getKey().equals("state")) {
	    			columns.get(i).setHeader(provider.getTranslation("lbl_state", getLocale()));	    			
	    		}

	    		if (columns.get(i).getKey().equals("demandeur")) {
	    			System.out.println("DEMANDEUR KEX FOUNDED");
	    			columns.get(i).setHeader(provider.getTranslation("lbl_shopper_ask", getLocale()));
	    		}
	    		if (columns.get(i).getKey().equals("sizeCM")) {
	    			columns.get(i).setHeader(provider.getTranslation("lbl_size_cm", getLocale()));
	    		}
	    		if (columns.get(i).getKey().equals("sizeUS")) {
	    			columns.get(i).setHeader(provider.getTranslation("lbl_size_us", getLocale()));
	    		}
	    		if (columns.get(i).getKey().equals("sizeUE")) {
	    			columns.get(i).setHeader(provider.getTranslation("lbl_size_ue", getLocale()));
	    		}
	    		if (columns.get(i).getKey().equals("category")) {
	    			columns.get(i).setHeader(provider.getTranslation("lbl_category", getLocale()));
	    		}	    		
//columns.get(i).getKey().equals("category") 
	    		//columns.set(i, columns.get(i)).setHeader(provider.getTranslation(columns.get(i).getKey(), getLocale()));
	    	}
	        grid.addColumn(shoe -> { 
	            Person person = shoe.getDemandeur(); 
	            return person == null ? "-" : person.getLastName() + " " + person.getFirstName();
	        }).setHeader(provider.getTranslation("lbl_shopper_ask", getLocale()));
	    	add(grid);
	    	
	        grid.addColumn(
	    			            TemplateRenderer
	    			                .<Shoe>of(
	    			                    "<div><img style='height: 180px; width: 180px;' src='[[item.imagedata]]' alt='[[item.name]]'/></div>"
	    			                )
	    			                .withProperty("imagedata", item -> getImageAsBase64(item.getImage())));
	       grid.addComponentColumn(item -> createRemoveButton(grid, item)).setHeader("Actions");       	        
	       grid.addComponentColumn(item -> createConsulterButton(grid, item)).setHeader("Actions");
	       grid.addComponentColumn(item -> createChangeStateButton(grid, item)).setHeader("Actions");
	       
	       add(grid);			                
	    }
	        

	    private Button createConsulterButton(Grid<Shoe> grid, Shoe item) {
	        @SuppressWarnings("unchecked")
	        Button button = new Button("Consulter", clickEvent -> {
	        	VaadinSession.getCurrent().getSession().setAttribute("idShoe",item.getIdShoe());
	        	UI.getCurrent().navigate("ShoesShopView");	            
	        });
	        return button;
	    }
		public String getAppPath() {
			return env.getProperty("spring.servlet.multipart.location");
		}

	    private Button createRemoveButton(Grid<Shoe> grid, Shoe item) {
	        @SuppressWarnings("unchecked")
	        Button button = new Button("Gérer", clickEvent -> {
	        	ViewRequestShoes editor = new ViewRequestShoes (item, shoeService, getAppPath());
	            editor.open();
	            editor.setVisible(true);
	            
	        });
	        return button;
	    }
	    
	    private Button createChangeStateButton(Grid<Shoe> grid, Shoe item) {
	        @SuppressWarnings("unchecked")
	        Button button = new Button("Définir comme vendue", clickEvent -> {
	        	ViewChangeStateShoes editor = new ViewChangeStateShoes (item, shoeService, getAppPath());
	            editor.open();
	            editor.setVisible(true);
	            
	        });
	        return button;
	    }
	 

	    private void removeOutput() {
	        this.remove(grid);
	        this.remove(labelSize);
	    }
	    
	    @SuppressWarnings("deprecation")
		private void buildMenu() {
	    	VaadinI18NProvider provider = new VaadinI18NProvider();
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
	    	menuBar.addItem("Sign Out", 
	    			e -> {
	    				VaadinSession.getCurrent().getSession().invalidate();
	    				UI.getCurrent().getPage().executeJavaScript("window.location.href=''");
	    				UI.getCurrent().navigate("");
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
	    				UI.getCurrent().navigate("ShopShoesPaidHistoric");
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

	    	account.getSubMenu().addItem(provider.getTranslation(LABEL_MENU_PRIVACY,getLocale()),
	    	        e -> {
	    	        	selected.setText(provider.getTranslation(LABEL_MENU_PRIVACY,getLocale()));
	    	        	UI.getCurrent().navigate( "map");
	    	        }				
	    			);
	    	
	    	add(menuBar, message);
	    	
	    }
	 
	 
}